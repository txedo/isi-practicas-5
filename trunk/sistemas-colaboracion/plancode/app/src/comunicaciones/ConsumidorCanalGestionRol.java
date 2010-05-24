package comunicaciones;

import javax.swing.event.EventListenerList;

import com.sun.media.jsdt.ChannelConsumer;
import com.sun.media.jsdt.Data;

import presentacion.MensajeRolEvent;
import presentacion.MensajeRolListener;

public class ConsumidorCanalGestionRol implements ChannelConsumer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2302586415061386684L;
	private EventListenerList listenerList;
	
	public ConsumidorCanalGestionRol () {
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
	
		}
	}
	
	public void addMensajeRolRecibidoListener(MensajeRolListener listener) {
		listenerList.add(MensajeRolListener.class, listener);
	}
	
}
