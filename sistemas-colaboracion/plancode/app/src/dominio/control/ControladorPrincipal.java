package dominio.control;


import presentacion.IVentana;
import presentacion.JFLogin;
import presentacion.JFPrincipal;
import presentacion.MensajeChatRecibidoEvent;
import presentacion.MensajeChatRecibidoListener;

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
import comunicaciones.ClienteJSDT;
import comunicaciones.ConsumidorCanalChat;
import comunicaciones.ICanales;

import dominio.DatosConexion;

public class ControladorPrincipal implements ICanales {
	private IVentana ventanaLogin;
	private IVentana ventanaPrincipal;
	
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

	public void iniciarSesion(String host, int puerto, String nick, String rol) throws NoRegistryException, RegistryExistsException, ConnectionException, InvalidClientException, InvalidURLException, NameInUseException, NoSuchClientException, NoSuchHostException, NoSuchSessionException, PermissionDeniedException, PortInUseException, TimedOutException, NoSuchChannelException, NoSuchConsumerException {
		con = new DatosConexion (host, puerto);
		// 1. Si no está el Registry funcionando, ponerlo en funcionamiento
		if (RegistryFactory.registryExists(con.getTipoSesion()) == false) {
			RegistryFactory.startRegistry(con.getTipoSesion());
		}
		// 2. Crear un cliente
		cliente = new ClienteJSDT(nick, rol);
		// 3. Crear la sesión
		crearSesion ();
		// 4. Crear los canales y poner el cliente como consumidor
		crearCanales ();
		ponerConsumidores();
		// Cerramos la ventana de login y abrimos la ventana principal
		ventanaLogin.cerrarVentana();
		ventanaPrincipal = new JFPrincipal (this);
		ventanaPrincipal.mostrarVentana();
	}
	
	private void crearSesion () throws ConnectionException, InvalidClientException, InvalidURLException, NameInUseException, NoRegistryException, NoSuchClientException, NoSuchHostException, NoSuchSessionException, PermissionDeniedException, PortInUseException, TimedOutException {
		url = URLString.createSessionURL(con.getIp(), con.getPuerto(), con.getTipoSesion(), con.getSesion());
		sesion = SessionFactory.createSession(cliente, url, true);
	}
	
	private void crearCanales () throws ConnectionException, InvalidClientException, NameInUseException, NoSuchSessionException, NoSuchClientException, NoSuchHostException, PermissionDeniedException, TimedOutException {
		// El último parámetro indica un join implícito
		canalChat = sesion.createChannel(cliente, CANAL_CHAT, true, true, true);
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
