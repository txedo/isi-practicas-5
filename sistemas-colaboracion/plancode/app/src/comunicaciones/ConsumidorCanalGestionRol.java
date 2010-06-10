package comunicaciones;

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

import comunicaciones.EventosCanales.MensajeRolEvent;
import comunicaciones.EventosCanales.MensajeRolListener;
import excepciones.NoSlotsDisponiblesException;


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
				try {
					((MensajeRolListener)listeners[i + 1]).MensajeRolRecibido(new MensajeRolEvent(this, d.getSenderName(), d.getDataAsString()));
				} catch (ConnectionException e) {
					Dialogos.mostrarDialogoError(null, "Error", "No se puede establecer una conexión");
				} catch (InvalidClientException e) {
					Dialogos.mostrarDialogoError(null, "Error", "Cliente de destino inválido");
				} catch (NoSuchChannelException e) {
					Dialogos.mostrarDialogoError(null, "Error", "No existe el canal");
				} catch (NoSuchClientException e) {
					Dialogos.mostrarDialogoError(null, "Error", "No se encuentra el cliente de destino");
				} catch (NoSuchSessionException e) {
					Dialogos.mostrarDialogoError(null, "Error", "No se encuentra la sesión");
				} catch (PermissionDeniedException e) {
					Dialogos.mostrarDialogoError(null, "Error", "Permiso denegado");
				} catch (TimedOutException e) {
					Dialogos.mostrarDialogoError(null, "Error", "Tiempo de espera agotado");
				} catch (NoSlotsDisponiblesException e) {
					Dialogos.mostrarDialogoError(null, "Error", "No se puede iniciar sesión porque el sistema ha alcanzado su capacidad máxima");
				} catch (NoSuchConsumerException e) {
					Dialogos.mostrarDialogoError(null, "Error", "No existe el consumidor");
				}
			}
		}
	}
	
	public void addMensajeRolRecibidoListener(MensajeRolListener listener) {
		listenerList.add(MensajeRolListener.class, listener);
	}
	
}
