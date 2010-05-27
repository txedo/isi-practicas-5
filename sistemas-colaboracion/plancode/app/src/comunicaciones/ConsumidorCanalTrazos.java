package comunicaciones;

import java.io.StreamCorruptedException;

import javax.swing.event.EventListenerList;

import presentacion.MensajeChatRecibidoListener;
import presentacion.MensajeTrazosEvent;
import presentacion.MensajeTrazosListener;

import com.sun.media.jsdt.ChannelConsumer;
import com.sun.media.jsdt.Data;

public class ConsumidorCanalTrazos implements ChannelConsumer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3012373241632069873L;
	private EventListenerList listenerList;
	
	public ConsumidorCanalTrazos () {
		listenerList = new EventListenerList();
	}
	
	@Override
	public void dataReceived(Data d) {
		Object[] listeners;
		int i;
		
		// Notificamos que se ha recibido un mensaje para dibujar trazos
		listeners = listenerList.getListenerList();
		for(i = 0; i < listeners.length; i += 2) {
			if(listeners[i] == MensajeTrazosListener.class) {
				try {
					((MensajeTrazosListener)listeners[i + 1]).MensajeTrazo(new MensajeTrazosEvent(this, d.getDataAsObject()));
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
	
	public void addMensajeTrazoListener(MensajeTrazosListener listener) {
		listenerList.add(MensajeTrazosListener.class, listener);
	}

}
