package comunicaciones;

import java.io.StreamCorruptedException;

import javax.swing.event.EventListenerList;


import com.sun.media.jsdt.ChannelConsumer;
import com.sun.media.jsdt.Data;
import comunicaciones.EventosCanales.MensajeListaUsuariosEvent;
import comunicaciones.EventosCanales.MensajeListaUsuariosListener;
import comunicaciones.EventosCanales.MensajeMapaEvent;
import comunicaciones.EventosCanales.MensajeMapaListener;

public class ConsumidorCanalMapa implements ChannelConsumer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7604967864053861062L;
	private EventListenerList listenerList;
	
	public ConsumidorCanalMapa () {
		listenerList = new EventListenerList();
	}
	
	@Override
	public void dataReceived(Data d) {
		Object[] listeners;
		int i;
		
		// Notificamos que se ha recibido un mensaje para cargar el mapa que ha abierto otro cliente
		listeners = listenerList.getListenerList();
		for(i = 0; i < listeners.length; i += 2) {	
			if(listeners[i] == MensajeMapaListener.class) {
					// TODO: donde se ponen estas excepciones?
					try {
						((MensajeMapaListener)listeners[i + 1]).MensajeMapa(new MensajeMapaEvent(this, d.getDataAsObject()));
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
	
	public void addMensajeMapaListener(MensajeMapaListener listener) {
		listenerList.add(MensajeMapaListener.class, listener);
	}



}
