package comunicaciones;

import java.io.StreamCorruptedException;

import javax.swing.event.EventListenerList;

import presentacion.auxiliares.Dialogos;


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
					try {
						((MensajeMapaListener)listeners[i + 1]).MensajeMapa(new MensajeMapaEvent(this, d.getSenderName(), d.getDataAsObject()));
					} catch (StreamCorruptedException e) {
						Dialogos.mostrarDialogoError(null, "Error", e.getMessage());
					} catch (ClassNotFoundException e) {
						Dialogos.mostrarDialogoError(null, "Error", e.getMessage());
					}
			}
		}
	}
	
	public void addMensajeMapaListener(MensajeMapaListener listener) {
		listenerList.add(MensajeMapaListener.class, listener);
	}



}
