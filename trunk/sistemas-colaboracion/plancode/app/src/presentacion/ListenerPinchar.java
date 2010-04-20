package presentacion;

/**
 * REFERENCIA : http://www.chuidiang.com/java/codigo_descargable/appletpaint.php
 */

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ListenerPinchar implements MouseListener {
	
	    /** Interfaz encargada de realizar acciones cuando se pincha el ratón */
	    private InterfacePincharRaton accion;

	    public ListenerPinchar(InterfacePincharRaton accion)
	    {
	        this.accion = accion;
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
