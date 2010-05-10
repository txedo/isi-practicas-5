package dominio.control;

import java.awt.Color;

import presentacion.JFLogin;
import presentacion.JFPrincipal;

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
import comunicaciones.ICanales;
import comunicaciones.ISesion;

import dominio.DatosConexion;
import dominio.GestorColores;
import dominio.Roles;

public class ControladorPrincipal implements ICanales, ISesion {
	private JFLogin ventanaLogin;
	private JFPrincipal ventanaPrincipal;
	
	private DatosConexion con;
	private URLString url;
	private Session sesion;
	private ClienteJSDT cliente;
	private Channel canalChat;
	private Channel canalTelepuntero;
	private Channel canalDibujo;
	private ConsumidorCanalChat consumidorChat;
	
	public ControladorPrincipal () {
		ventanaLogin = new JFLogin(this);
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
		}
		// 2. Crear un cliente
		cliente = new ClienteJSDT(nick, rol);
		// 3. Crear la sesión
		crearSesion ();
		// 4. Crear los canales y poner el cliente como consumidor
		crearCanales ();
		ponerConsumidores();
		// Buscar un color para este cliente de esta sesion
		Color c = GestorColores.getColorLibre(sesion);
		// Cerramos la ventana de login y abrimos la ventana principal
		ventanaLogin.cerrarVentana();
		ventanaPrincipal = new JFPrincipal (this);
		// Asignamos el color elegido al cliente (si quedan colores libres)
		if (c!=null) 
			ventanaPrincipal.setColorActual(c);
		// TODO: else: error
		ventanaPrincipal.mostrarVentana();
		
	}
	
	private void crearSesion () throws ConnectionException, InvalidClientException, InvalidURLException, NameInUseException, NoRegistryException, NoSuchClientException, NoSuchHostException, NoSuchSessionException, PermissionDeniedException, PortInUseException, TimedOutException {
		url = URLString.createSessionURL(con.getIp(), con.getPuerto(), TIPO_SESION, SESION);
		sesion = SessionFactory.createSession(cliente, url, true);
		// Marcamos una nueva sesión a la hora de elegir colores
		// TODO: revisar esto, porque aunque te unas a una misma sesión, el objeto es diferente
		GestorColores.añadirSesion(sesion);
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
	}
	
	private void ponerConsumidores () throws ConnectionException, InvalidClientException, NoSuchChannelException, NoSuchClientException, NoSuchConsumerException, NoSuchSessionException, PermissionDeniedException, TimedOutException {
		consumidorChat = new ConsumidorCanalChat ();
		canalChat.addConsumer(cliente, consumidorChat);
		
		
	}
	
	public ConsumidorCanalChat getConsumidorCanalChat() {
		return consumidorChat;
	}
	
	public void enviarMensajeChat (String mensaje) throws ConnectionException, InvalidClientException, NoSuchChannelException, NoSuchClientException, NoSuchSessionException, PermissionDeniedException, TimedOutException {
		canalChat.sendToAll(cliente, new Data (mensaje));
	}
}
