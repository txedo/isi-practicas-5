package dominio.control;

import java.awt.Color;
import java.util.Hashtable;
import presentacion.JFLogin;
import presentacion.JFPrincipal;
import presentacion.MensajeChatRecibidoEvent;
import presentacion.MensajeChatRecibidoListener;
import presentacion.MensajeRolEvent;
import presentacion.MensajeRolListener;

import com.sun.media.jsdt.Channel;
import com.sun.media.jsdt.ConnectionException;
import com.sun.media.jsdt.Data;
import com.sun.media.jsdt.InvalidClientException;
import com.sun.media.jsdt.InvalidURLException;
import com.sun.media.jsdt.NameInUseException;
import com.sun.media.jsdt.NoRegistryException;
import com.sun.media.jsdt.NoSuchChannelException;
import com.sun.media.jsdt.NoSuchClientException;
import com.sun.media.jsdt.NoSuchConsumerException;
import com.sun.media.jsdt.NoSuchHostException;
import com.sun.media.jsdt.NoSuchSessionException;
import com.sun.media.jsdt.PermissionDeniedException;
import com.sun.media.jsdt.PortInUseException;
import com.sun.media.jsdt.RegistryExistsException;
import com.sun.media.jsdt.RegistryFactory;
import com.sun.media.jsdt.Session;
import com.sun.media.jsdt.SessionFactory;
import com.sun.media.jsdt.TimedOutException;
import com.sun.media.jsdt.URLString;
import com.sun.media.jsdt.event.ChannelEvent;
import com.sun.media.jsdt.event.ChannelListener;

import comunicaciones.ClienteJSDT;
import comunicaciones.ConsumidorCanalChat;
import comunicaciones.ConsumidorCanalGestion;
import comunicaciones.DatosConexion;
import comunicaciones.ICanales;
import comunicaciones.ISesion;

import dominio.conocimiento.Roles;
import dominio.conocimiento.Usuario;

public class ControladorPrincipal implements ICanales, ISesion {
	private JFLogin ventanaLogin;
	private JFPrincipal ventanaPrincipal;
	
	private boolean esServidor = false;
	private Hashtable<String,Usuario> listaUsuarios;
	
	private DatosConexion con;
	private URLString url;
	private Session sesion;
	private ClienteJSDT cliente;
	private Channel canalChat;
	private Channel canalTelepuntero;
	private Channel canalDibujo;
	/* Este canal se utiliza para intercambiar datos entre clientes para gestionar sus colores y el panel de sesiones
	 * Se usa este canal para no mostrar estos datos por el chat ni mezclarlos con otros canales
	 */	
	private Channel canalGestion;
	private ConsumidorCanalChat consumidorChat;
	private ConsumidorCanalGestion consumidorGestion;
	
	public ControladorPrincipal () {
		ventanaLogin = new JFLogin(this);
		listaUsuarios = new Hashtable<String,Usuario>();
	}
	
	public void mostrarVentanaLogin() {
		ventanaLogin.mostrarVentana();
	}

	public void mostrarVentanaPrincipal() {
		ventanaPrincipal.mostrarVentana();
	}

	public void iniciarSesion(String host, int puerto, String nick, Roles rol, boolean sesionExistente) throws NoRegistryException, RegistryExistsException, ConnectionException, InvalidClientException, InvalidURLException, NameInUseException, NoSuchClientException, NoSuchHostException, NoSuchSessionException, PermissionDeniedException, PortInUseException, TimedOutException, NoSuchChannelException, NoSuchConsumerException {
		con = new DatosConexion (host, puerto);
		// 1. Si no está el Registry funcionando, ponerlo en funcionamiento
		if (RegistryFactory.registryExists(TIPO_SESION) == false) {	
			RegistryFactory.startRegistry(TIPO_SESION);
			esServidor = true;
		}
		// 2. Crear un cliente
		cliente = new ClienteJSDT(nick, rol);
		// Como este cliente actua como servidor, se auto-asigna un color, ademas de inicializar los colores disponibles
		if (esServidor) {
			GestorColores.inicializaColores();
			Color c = GestorColores.getColorLibre();
			// Se añade en la lista de usuarios conectados
			listaUsuarios.put(cliente.getName(), new Usuario(rol, c));
		}
		
		// 3. Crear la sesión
		crearSesion ();
		// 4. Crear los canales y poner el cliente como consumidor
		crearCanales ();
		ponerConsumidores();
		
		// Añadimos el evento para poder recibir el mensaje de rol de otros clientes que se conectan
		if (esServidor) {
			consumidorGestion.addMensajeRolRecibidoListener(new MensajeRolListener() {
				public void MensajeRolRecibido(MensajeRolEvent evt) {
					/* El mensaje del rol lo gestiona el servidor para saber que se ha conectado un
					 * nuevo cliente y enviar la lista de usuarios al resto de clientes conectados.
					 * Además, le asigna un color.
					 */
					Color c = GestorColores.getColorLibre();
					System.out.println(evt.getRol());
					listaUsuarios.put(evt.getNombre(), new Usuario(evt.getRol(), c));
					try {
						canalGestion.sendToOthers(cliente, new Data(listaUsuarios));
						// TODO: donde se tratan estas excepciones?
					} catch (ConnectionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidClientException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchChannelException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchClientException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchSessionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (PermissionDeniedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (TimedOutException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
		}
		
		/* Al conectarse a la aplicación, el cliente envía al resto su rol (si no es el servidor)
		 * El servidor tomará ese mensaje y lo incluirá en la lista de usuarios conectados
		 * Hay que enviar el mensaje a todos porque el cliente que se conecta no conoce el 
		 * nombre del servidor
		 */
		if (!esServidor) {
			canalGestion.sendToOthers(cliente, new Data(cliente.getRol()));
		}
		
		// Cerramos la ventana de login y abrimos la ventana principal
		ventanaLogin.cerrarVentana();
		ventanaPrincipal = new JFPrincipal (this);
		ventanaPrincipal.mostrarVentana();
		
	}
	
	private void crearSesion () throws ConnectionException, InvalidClientException, InvalidURLException, NameInUseException, NoRegistryException, NoSuchClientException, NoSuchHostException, NoSuchSessionException, PermissionDeniedException, PortInUseException, TimedOutException {
		url = URLString.createSessionURL(con.getIp(), con.getPuerto(), TIPO_SESION, SESION);
		sesion = SessionFactory.createSession(cliente, url, true);
	}
	
	private void crearCanales () throws ConnectionException, InvalidClientException, NameInUseException, NoSuchSessionException, NoSuchClientException, NoSuchHostException, PermissionDeniedException, TimedOutException, NoSuchChannelException {
		// El último parámetro indica un join implícito
		canalChat = sesion.createChannel(cliente, CANAL_CHAT, true, true, true);
		canalChat.addChannelListener(new ChannelListener() {
			// Pasamos a la interfaz gráfica el nick del cliente que se acaba de unir al canal del chat
			public void channelJoined(ChannelEvent e) {
				ventanaPrincipal.notificarLogin(e.getClientName());				
			}

			public void channelConsumerAdded(ChannelEvent arg0) {				
			}

			public void channelConsumerRemoved(ChannelEvent arg0) {				
			}

			public void channelExpelled(ChannelEvent arg0) {				
			}

			public void channelInvited(ChannelEvent arg0) {
			}
			
			// Pasamos a la interfaz gráfica el nick del cliente que acaba de dejar el canal del chat
			public void channelLeft(ChannelEvent e) {
				ventanaPrincipal.notificarLogout(e.getClientName());
			}
		});
		canalTelepuntero = sesion.createChannel(cliente, CANAL_TELEPUNTERO, true, true, true);
		canalDibujo = sesion.createChannel(cliente, CANAL_DIBUJO, true, true, true);
		canalGestion = sesion.createChannel(cliente, CANAL_GESTION, true, true, true);
	}
	
	private void ponerConsumidores () throws ConnectionException, InvalidClientException, NoSuchChannelException, NoSuchClientException, NoSuchConsumerException, NoSuchSessionException, PermissionDeniedException, TimedOutException {
		// Ponemos los consumidores del chat y del canal de gestión
		consumidorChat = new ConsumidorCanalChat ();
		canalChat.addConsumer(cliente, consumidorChat);		
		consumidorGestion = new ConsumidorCanalGestion ();
		canalGestion.addConsumer(cliente, consumidorGestion);
	}
	
	
	public void enviarMensajeChat (String mensaje) throws ConnectionException, InvalidClientException, NoSuchChannelException, NoSuchClientException, NoSuchSessionException, PermissionDeniedException, TimedOutException {
		canalChat.sendToAll(cliente, new Data (mensaje));
	}
	
	public Hashtable<String,Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public ConsumidorCanalChat getConsumidorCanalChat() {
		return consumidorChat;
	}
	
	public ConsumidorCanalGestion getConsumidorGestion() {
		return consumidorGestion;
	}
}
