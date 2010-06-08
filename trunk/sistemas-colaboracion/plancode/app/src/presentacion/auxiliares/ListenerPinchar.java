package presentacion.auxiliares;

/**
 * REFERENCIA : http://www.chuidiang.com/java/codigo_descargable/appletpaint.php
 */

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.sun.media.jsdt.ConnectionException;
import com.sun.media.jsdt.InvalidClientException;
import com.sun.media.jsdt.NoSuchChannelException;
import com.sun.media.jsdt.NoSuchClientException;
import com.sun.media.jsdt.NoSuchSessionException;
import com.sun.media.jsdt.PermissionDeniedException;
import com.sun.media.jsdt.TimedOutException;


public class ListenerPinchar implements MouseListener {
	
	    /** Interfaz encargada de realizar acciones cuando se pincha el ratón */
	    private InterfacePincharRaton accion;
	    private Component canvas;
	    private Toolkit toolkit;

	    public ListenerPinchar(InterfacePincharRaton accion)
	    {
	        this.accion = accion;
	        toolkit = Toolkit.getDefaultToolkit();
	    }
	    
	    /**
	     * Este metodo se utiliza para poder cambiar el cursor cuando se selecciona 
	     * una herramienta en el canvas
	     */
	    public void setComponent(Component c) {
	    	canvas = c;
	    }
	    
	    public void setAccion(InterfacePincharRaton accion)
	    {
	        this.accion = accion;
	    }

		@Override
		public void mouseClicked(MouseEvent e) {
			if (accion!=null)
				try {
					accion.eliminarObjeto(e.getX(), e.getY());
				} catch (ConnectionException ex) {
	    			Dialogos.mostrarDialogoError(null, "Error", "No se puede establecer una conexión");
	    		} catch (InvalidClientException ex) {
	    			Dialogos.mostrarDialogoError(null, "Error", "Cliente de destino inválido");
	    		} catch (NoSuchChannelException ex) {
	    			Dialogos.mostrarDialogoError(null, "Error", "No existe el canal");
	    		} catch (NoSuchClientException ex) {
	    			Dialogos.mostrarDialogoError(null, "Error", "No se encuentra el cliente de destino");
	    		} catch (NoSuchSessionException ex) {
	    			Dialogos.mostrarDialogoError(null, "Error", "No se encuentra la sesión");
	    		} catch (PermissionDeniedException ex) {
	    			Dialogos.mostrarDialogoError(null, "Error", "Permiso denegado");
	    		} catch (TimedOutException ex) {
	    			Dialogos.mostrarDialogoError(null, "Error", "Tiempo de espera agotado");
	    		}
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {	
			// Si es la acción de eliminar (será distinto de null), se pone la imagen del borrador.
			// Si no, se pone la imagen del lápiz
			Image image = null;
			if (accion==null)
				image = toolkit.getImage("./resources/images/pencil.gif");
			else
				image = toolkit.getImage("./resources/images/eraser.png");
			// PROVISIONAL
			if (image!=null) {
				Point hotSpot = new Point(0,0);
				Cursor cursor = toolkit.createCustomCursor(image, hotSpot, "CustomCursor");
				canvas.setCursor(cursor);
			}
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
		}


		@Override
		public void mousePressed(MouseEvent e) {			
		}


		@Override
		public void mouseReleased(MouseEvent e) {			
		}

}
