package comunicaciones;

import java.io.StreamCorruptedException;

import javax.swing.event.EventListenerList;

import com.sun.media.jsdt.ChannelConsumer;
import com.sun.media.jsdt.Data;

import presentacion.MensajeListaUsuariosEvent;
import presentacion.MensajeListaUsuariosListener;
import presentacion.MensajeRolEvent;
import presentacion.MensajeRolListener;

public class ConsumidorCanalGestion implements ChannelConsumer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2302586415061386684L;
	private EventListenerList listenerList;
	
	public ConsumidorCanalGestion () {
		listenerList = new EventListenerList();
	}
	
	@Override
	public void dataReceived(Data d) {
		Object[] listeners;
		int i;
		
		// Notificamos que se ha recibido un mensaje de gestión
		listeners = listenerList.getListenerList();
		for(i = 0; i < listeners.length; i += 2) {
			if(listeners[i] == MensajeRolListener.class) {
				((MensajeRolListener)listeners[i + 1]).MensajeRolRecibido(new MensajeRolEvent(this, d.getSenderName(), d.getDataAsString()));
			}
	
			if(listeners[i] == MensajeListaUsuariosListener.class) {
				try {
					((MensajeListaUsuariosListener)listeners[i + 1]).MensajeListaUsuarios(new MensajeListaUsuariosEvent(this, d.getDataAsObject()));
					// TODO: donde se ponen estas excepciones?
				} catch (StreamCorruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void addMensajeRolRecibidoListener(MensajeRolListener listener) {
		listenerList.add(MensajeRolListener.class, listener);
	}
	
	public void addMensajeListaUsuariosListener(MensajeListaUsuariosListener listener) {
		listenerList.add(MensajeListaUsuariosListener.class, listener);
	}

}
