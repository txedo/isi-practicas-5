package comunicaciones;

import java.io.StreamCorruptedException;

import javax.swing.event.EventListenerList;

import presentacion.auxiliares.Dialogos;


import com.sun.media.jsdt.ChannelConsumer;
import com.sun.media.jsdt.ConnectionException;
import com.sun.media.jsdt.Data;
import com.sun.media.jsdt.InvalidClientException;
import com.sun.media.jsdt.NoSuchChannelException;
import com.sun.media.jsdt.NoSuchClientException;
import com.sun.media.jsdt.NoSuchConsumerException;
import com.sun.media.jsdt.NoSuchSessionException;
import com.sun.media.jsdt.PermissionDeniedException;
import com.sun.media.jsdt.TimedOutException;

import comunicaciones.EventosCanales.MensajeListaUsuariosEvent;
import comunicaciones.EventosCanales.MensajeListaUsuariosListener;

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
					try {
						((MensajeListaUsuariosListener)listeners[i + 1]).MensajeListaUsuarios(new MensajeListaUsuariosEvent(this, d.getDataAsObject()));
					} catch (StreamCorruptedException e) {
						Dialogos.mostrarDialogoError(null, "Error", e.getMessage());
					} catch (ClassNotFoundException e) {
						Dialogos.mostrarDialogoError(null, "Error", e.getMessage());
					}
			
			
			
			}
		}
	}
	
	public void addMensajeListaUsuariosListener(MensajeListaUsuariosListener listener) {
		listenerList.add(MensajeListaUsuariosListener.class, listener);
	}


}
