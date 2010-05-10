package presentacion;

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

public class ListenerPinchar implements MouseListener {
	
	    /** Interfaz encargada de realizar acciones cuando se pincha el ratón */
	    private InterfacePincharRaton accion;
	    private Component canvas;

	    public ListenerPinchar(InterfacePincharRaton accion)
	    {
	        this.accion = accion;
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
				accion.eliminarObjeto(e.getX(), e.getY());
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {	
			Toolkit toolkit = Toolkit.getDefaultToolkit();  
			Image image = toolkit.getImage("./resources/images/pencil.gif");
			Point hotSpot = new Point(0,0);
			Cursor cursor = toolkit.createCustomCursor(image, hotSpot, "Pencil");
			canvas.setCursor(cursor);  
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
