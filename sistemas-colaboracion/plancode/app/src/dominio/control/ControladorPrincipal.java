package dominio.control;

import java.awt.Color;
import java.util.Hashtable;
import java.util.LinkedList;

import javax.swing.ImageIcon;

import presentacion.JFLogin;
import presentacion.JFPrincipal;

import com.sun.media.jsdt.Channel;
import com.sun.media.jsdt.ConnectionException;
import com.sun.media.jsdt.Data;
import com.sun.media.jsdt.InvalidClientException;
import com.sun.media.jsdt.InvalidURLException;
import com.sun.media.jsdt.NameInUseException;
import com.sun.media.jsdt.NoRegistryException;
import com.sun.media.jsdt.NoSuchByteArrayException;
import com.sun.media.jsdt.NoSuchChannelException;
import com.sun.media.jsdt.NoSuchClientException;
import com.sun.media.jsdt.NoSuchConsumerException;
import com.sun.media.jsdt.NoSuchHostException;
import com.sun.media.jsdt.NoSuchSessionException;
import com.sun.media.jsdt.NoSuchTokenException;
import com.sun.media.jsdt.NotBoundException;
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
import comunicaciones.ConsumidorCanalGestionListaUsuarios;
import comunicaciones.ConsumidorCanalGestionRol;
import comunicaciones.ConsumidorCanalMapa;
import comunicaciones.ConsumidorCanalTrazos;
import comunicaciones.DatosConexion;
import comunicaciones.ICanales;
import comunicaciones.ISesion;
import comunicaciones.EventosCanales.MensajeRolEvent;
import comunicaciones.EventosCanales.MensajeRolListener;

import dominio.conocimiento.InfoTrazo;
import dominio.conocimiento.Roles;
import dominio.conocimiento.Trazo;
import dominio.conocimiento.Usuario;
import excepciones.NoSlotsDisponiblesException;

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
	private Channel canalDibujo;
	/* Estos canales se utilizan para intercambiar datos entre clientes para gestionar sus roles y el panel de sesiones
	 * Se usan estos canales para no mostrar estos datos por el chat ni mezclarlos con otros canales
	 * Ademas, se necesitan usar dos, porque cada canl debe tener su listener, ya que si no, no se muestran bien los datos
	 */	
	private Channel canalGestionRol;
	private Channel canalGestionListaUsuarios;
	private Channel canalMapa;
	private ConsumidorCanalChat consumidorChat;
	private ConsumidorCanalGestionRol consumidorGestionRol;
	private ConsumidorCanalGestionListaUsuarios consumidorGestionListaUsuarios;
	private ConsumidorCanalTrazos consumidorTrazos;
	private ConsumidorCanalMapa consumidorMapa;
	
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

	public void iniciarSesion(String host, int puerto, String nick, Roles rol, boolean sesionExistente) throws NoRegistryException, RegistryExistsException, ConnectionException, InvalidClientException, InvalidURLException, NameInUseException, NoSuchClientException, NoSuchHostException, NoSuchSessionException, PermissionDeniedException, PortInUseException, TimedOutException, NoSuchChannelException, NoSuchConsumerException, NoSlotsDisponiblesException {
		con = new DatosConexion (host, puerto);
		// 1. Si no est� el Registry funcionando, ponerlo en funcionamiento
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
			// Se a�ade en la lista de usuarios conectados
			listaUsuarios.put(cliente.getName(), new Usuario(rol, c));
		}
		
		// 3. Crear la sesi�n
		crearSesion ();
		// 4. Crear los canales y poner el cliente como consumidor
		crearCanales ();
		ponerConsumidores();
		
		// Cerramos la ventana de login y abrimos la ventana principal
		ventanaLogin.cerrarVentana();
		ventanaPrincipal = new JFPrincipal (this);
		ventanaPrincipal.mostrarVentana();
		
		// Tras poner a los lcientes como consumidores, se a�aden los eventos al canal del chat
		canalChat.addChannelListener(new ChannelListener() {
			// Pasamos a la interfaz gr�fica el nick del cliente que se acaba de unir al canal del chat
			public void channelJoined(ChannelEvent e) {
				ventanaPrincipal.iniciarSesion(e.getClientName());
			}

			public void channelConsumerAdded(ChannelEvent arg0) {				
			}

			public void channelConsumerRemoved(ChannelEvent arg0) {				
			}

			public void channelExpelled(ChannelEvent arg0) {				
			}

			public void channelInvited(ChannelEvent arg0) {
			}
			
			// Pasamos a la interfaz gr�fica el nick del cliente que acaba de dejar el canal del chat
			// Liberamos tambi�n su color
			public void channelLeft(ChannelEvent e) {
				if (isServidor()) {
					GestorColores.liberarColor(listaUsuarios.get(e.getClientName()).getColor());
					listaUsuarios.remove(e.getClientName());
				}
				if (!isServidor() || (isServidor() && listaUsuarios.size() > 0 )) {
					ventanaPrincipal.notificarLogout(e.getClientName());
				}
			}
		});
		
		// A�adimos el evento para poder recibir el mensaje de rol de otros clientes que se conectan
		if (esServidor) {
			consumidorGestionRol.addMensajeRolRecibidoListener(new MensajeRolListener() {
				public void MensajeRolRecibido(MensajeRolEvent evt) throws ConnectionException, InvalidClientException, NoSuchChannelException, NoSuchClientException, NoSuchSessionException, PermissionDeniedException, TimedOutException, NoSlotsDisponiblesException, NoSuchConsumerException {
					/* El mensaje del rol lo gestiona el servidor para saber que se ha conectado un
					 * nuevo cliente y enviar la lista de usuarios al resto de clientes conectados.
					 * Adem�s, le asigna un color.
					 */
					Color c = GestorColores.getColorLibre();
					listaUsuarios.put(evt.getNombre(), new Usuario(evt.getRol(), c));
					canalGestionListaUsuarios.sendToAll(cliente, new Data(listaUsuarios));
					// Se env�a tambi�n el mapa cargado (si lo hay)
					if (ventanaPrincipal.getMapa()!=null) {
						enviarMapaRecienConectado(evt.getNombre(), ventanaPrincipal.getMapa());
					}
					// Se espera un peque�o tiempo para que reciba el mapa y sobre �l se carguen los trazos (si los hay)
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
					}
					// Env�a los trazos ya dibujados al nuevo cliente
					if (!ventanaPrincipal.getCanvas().getTrazos().isEmpty()) {
						enviarTrazosRecienConectado(evt.getNombre(), ventanaPrincipal.getCanvas().getTrazos());
					}
					
				}
			});
		}
		
		/* Al conectarse a la aplicaci�n, el cliente env�a al resto su rol (si no es el servidor)
		 * El servidor tomar� ese mensaje y lo incluir� en la lista de usuarios conectados
		 * Hay que enviar el mensaje a todos porque el cliente que se conecta no conoce el 
		 * nombre del servidor
		 */
		if (!esServidor) {
			canalGestionRol.sendToOthers(cliente, new Data(cliente.getRol().name()));
		}
		
	}
	
	private void crearSesion () throws ConnectionException, InvalidClientException, InvalidURLException, NameInUseException, NoRegistryException, NoSuchClientException, NoSuchHostException, NoSuchSessionException, PermissionDeniedException, PortInUseException, TimedOutException {
		url = URLString.createSessionURL(con.getIp(), con.getPuerto(), TIPO_SESION, SESION);
		sesion = SessionFactory.createSession(cliente, url, true);
	}
	
	private void crearCanales () throws ConnectionException, InvalidClientException, NameInUseException, NoSuchSessionException, NoSuchClientException, NoSuchHostException, PermissionDeniedException, TimedOutException, NoSuchChannelException {
		// El �ltimo par�metro indica un join impl�cito
		canalChat = sesion.createChannel(cliente, CANAL_CHAT, true, true, true);
		canalDibujo = sesion.createChannel(cliente, CANAL_DIBUJO, true, true, true);
		canalGestionRol = sesion.createChannel(cliente, CANAL_GESTION_ROL, true, true, true);
		canalGestionListaUsuarios = sesion.createChannel(cliente, CANAL_GESTION_LISTA, true, true, true);
		canalMapa = sesion.createChannel(cliente, CANAL_MAPA, true, true, true);
	}
	
	private void ponerConsumidores () throws ConnectionException, InvalidClientException, NoSuchChannelException, NoSuchClientException, NoSuchConsumerException, NoSuchSessionException, PermissionDeniedException, TimedOutException {			
		consumidorGestionRol = new ConsumidorCanalGestionRol ();
		canalGestionRol.addConsumer(cliente, consumidorGestionRol);
		consumidorGestionListaUsuarios = new ConsumidorCanalGestionListaUsuarios();
		canalGestionListaUsuarios.addConsumer(cliente, consumidorGestionListaUsuarios);
		consumidorTrazos = new ConsumidorCanalTrazos();
		canalDibujo.addConsumer(cliente, consumidorTrazos);
		consumidorMapa = new ConsumidorCanalMapa();
		canalMapa.addConsumer(cliente, consumidorMapa);
		consumidorChat = new ConsumidorCanalChat ();
		canalChat.addConsumer(cliente, consumidorChat);	
	}
	
	private void quitarConsumidoresYCanales () throws ConnectionException, InvalidClientException, NoSuchChannelException, NoSuchClientException, NoSuchConsumerException, NoSuchSessionException, PermissionDeniedException, TimedOutException, NoSuchByteArrayException, NoSuchTokenException {
		canalChat.removeConsumer(cliente, consumidorChat);
		canalChat.destroy(cliente);
		canalGestionRol.removeConsumer(cliente, consumidorGestionRol);
		canalGestionRol.destroy(cliente);
		canalGestionListaUsuarios.removeConsumer(cliente, consumidorGestionListaUsuarios);
		canalGestionListaUsuarios.destroy(cliente);
		canalDibujo.removeConsumer(cliente, consumidorTrazos);
		canalDibujo.destroy(cliente);
		canalMapa.removeConsumer(cliente, consumidorMapa);
		canalMapa.destroy(cliente);
	}
	
	
	public void enviarMensajeChat (String mensaje) throws ConnectionException, InvalidClientException, NoSuchChannelException, NoSuchClientException, NoSuchSessionException, PermissionDeniedException, TimedOutException {
		canalChat.sendToAll(cliente, new Data (mensaje));
	}
	
	public void enviarTrazo (InfoTrazo info) throws ConnectionException, InvalidClientException, NoSuchChannelException, NoSuchClientException, NoSuchSessionException, PermissionDeniedException, TimedOutException {
		canalDibujo.sendToOthers(cliente, new Data(info));
		
	}
	
	public void ponerMensajeLogLocal(InfoTrazo info) {
		this.ventanaPrincipal.ponerMensajeLog(cliente.getName(), info);
	}
	
	// Este m�todo es para que el servidor env�e los trazos ya dibujados al cliente que se acaba de conectar 
	public void enviarTrazosRecienConectado(String clienteDestino, LinkedList<Trazo> trazos) throws ConnectionException, InvalidClientException, NoSuchChannelException, NoSuchClientException, NoSuchConsumerException, NoSuchSessionException, PermissionDeniedException, TimedOutException {
		InfoTrazo info = new InfoTrazo((LinkedList<Trazo>)trazos.clone());
		canalDibujo.sendToClient(cliente, clienteDestino, new Data(info));
	}
	
	public void enviarTrazosClean() throws ConnectionException, InvalidClientException, NoSuchChannelException, NoSuchClientException, NoSuchSessionException, PermissionDeniedException, TimedOutException {
		InfoTrazo info = new InfoTrazo();
		canalDibujo.sendToOthers(cliente, new Data(info));
	}
	
	public void enviarMapa(ImageIcon mapa) throws ConnectionException, InvalidClientException, NoSuchChannelException, NoSuchClientException, NoSuchSessionException, PermissionDeniedException, TimedOutException {
		canalMapa.sendToOthers(cliente, new Data(mapa));
	}
	
	// Este m�todo es para que el servidor env�e el mapa al cliente que se acaba de conectar 
	public void enviarMapaRecienConectado(String clienteDestino, ImageIcon mapa) throws ConnectionException, InvalidClientException, NoSuchChannelException, NoSuchClientException, NoSuchConsumerException, NoSuchSessionException, PermissionDeniedException, TimedOutException {
		canalMapa.sendToClient(cliente, clienteDestino, new Data(mapa));
	}
	
	public Hashtable<String,Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public ConsumidorCanalChat getConsumidorCanalChat() {
		return consumidorChat;
	}
	
	public ConsumidorCanalGestionRol getConsumidorGestionRol() {
		return consumidorGestionRol;
	}
	
	public ConsumidorCanalGestionListaUsuarios getConsumidorGestionListaUsuarios() {
		return consumidorGestionListaUsuarios;
	}

	public boolean isServidor() {
		return esServidor;
	}
	
	public String getNombreCliente () {
		return cliente.getName();
	}

	public ConsumidorCanalTrazos getConsumidorTrazos() {
		return consumidorTrazos;
	}

	public ConsumidorCanalMapa getConsumidorMapa() {
		return consumidorMapa;
	}

	public Channel getCanalGestionListaUsuarios() {
		return canalGestionListaUsuarios;
	}

	public ClienteJSDT getCliente() {
		return cliente;
	}

	public void forzarCierre() throws NoRegistryException, ConnectionException, InvalidClientException, InvalidURLException, NoSuchClientException, NoSuchHostException, NoSuchSessionException, NotBoundException, PermissionDeniedException, TimedOutException, NoSuchChannelException, NoSuchConsumerException, NoSuchByteArrayException, NoSuchTokenException {
		quitarConsumidoresYCanales();
		SessionFactory.destroySession(cliente, url);
	}
}
