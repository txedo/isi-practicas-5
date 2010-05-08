package comunicaciones;

import java.util.EventObject;

import javax.swing.event.EventListenerList;

import presentacion.MensajeChatRecibidoEvent;
import presentacion.MensajeChatRecibidoListener;

import com.sun.media.jsdt.ChannelConsumer;
import com.sun.media.jsdt.Data;

public class ConsumidorCanalChat implements ChannelConsumer {
	private EventListenerList listenerList;
	
	public ConsumidorCanalChat () {
	}
	
	@Override
	public void dataReceived(Data d) {
		Object[] listeners;
		int i;
		
		// Notificamos que se ha recibido un mensaje
		listeners = listenerList.getListenerList();
		for(i = 0; i < listeners.length; i += 2) {
			if(listeners[i] == MensajeChatRecibidoListener.class) {
				((MensajeChatRecibidoListener)listeners[i + 1]).MensajeChatRecibido(new MensajeChatRecibidoEvent(this, d.getSenderName(), d.getDataAsString()));
			}
		}
	}
	
	public void addMensajeChatRecibidoListener(MensajeChatRecibidoListener listener) {
		listenerList.add(MensajeChatRecibidoListener.class, listener);
	}

}
