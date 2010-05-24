package comunicaciones;

import java.io.StreamCorruptedException;

import javax.swing.event.EventListenerList;

import presentacion.MensajeListaUsuariosEvent;
import presentacion.MensajeListaUsuariosListener;

import com.sun.media.jsdt.ChannelConsumer;
import com.sun.media.jsdt.Data;

public class ConsumidorCanalGestionListaUsuarios implements ChannelConsumer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3287333856928330930L;
	private EventListenerList listenerList;
	
	public ConsumidorCanalGestionListaUsuarios () {
		listenerList = new EventListenerList();
	}
	
	@Override
	public void dataReceived(Data d) {
		Object[] listeners;
		int i;
		
		// Notificamos que se ha recibido un mensaje de gestión para enviar la lista de usuarios
		listeners = listenerList.getListenerList();
		for(i = 0; i < listeners.length; i += 2) {	
			if(listeners[i] == MensajeListaUsuariosListener.class) {
					((MensajeListaUsuariosListener)listeners[i + 1]).MensajeListaUsuarios(new MensajeListaUsuariosEvent(this, d.getDataAsString()));


					// TODO: donde se ponen estas excepciones?

			}
		}
	}
	
	public void addMensajeListaUsuariosListener(MensajeListaUsuariosListener listener) {
		listenerList.add(MensajeListaUsuariosListener.class, listener);
	}


}
