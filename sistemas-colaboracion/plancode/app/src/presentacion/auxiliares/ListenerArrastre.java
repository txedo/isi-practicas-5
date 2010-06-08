package presentacion.auxiliares;

/**
 * REFERENCIA : http://www.chuidiang.com/java/codigo_descargable/appletpaint.php
 */

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import com.sun.media.jsdt.ConnectionException;
import com.sun.media.jsdt.InvalidClientException;
import com.sun.media.jsdt.NoSuchChannelException;
import com.sun.media.jsdt.NoSuchClientException;
import com.sun.media.jsdt.NoSuchSessionException;
import com.sun.media.jsdt.PermissionDeniedException;
import com.sun.media.jsdt.TimedOutException;


/**
 * Clase que implementa la interfaz MouseMotionListener
 * para conocer los movimientos de ratón y almacenar las coordenadas por
 * las que se va arrastrando, avisando al InterfaceArrastrarRaton.
 */
public class ListenerArrastre implements MouseMotionListener
{
    /** Interfaz encargada de realizar acciones cuando se arrastra el ratón */
    private InterfaceArrastrarRaton accion;
    
    /** Indica si se está arrastrando el ratón */
    boolean arrastrando = false;

    public ListenerArrastre(InterfaceArrastrarRaton accion)
    {
        this.accion = accion;
    }
    

    public void setAccion(InterfaceArrastrarRaton accion)
    {
        this.accion = accion;
    }

    /** x del ratón antes de producirse el último arrastre */
    int xAntigua;

    /** y del ratón antes de producirse el último arrastre */
    int yAntigua;

    /**
     * Se mueve el ratón a un nuevo punto. Cuando se deja de arrastrar el ratón, 
     * se finaliza el trazo
     */
    public void mouseMoved(MouseEvent e)
    {
    	if (accion!=null) {
	        if (arrastrando == true)
	            accion.finalizaArrastra(xAntigua, yAntigua);
	        arrastrando = false;
	        xAntigua = e.getX();
	        yAntigua = e.getY();
    	}
    }

    /**
     * Se está arrastrando el ratón. Se avisa a la accion correspondiente y
     * se actualizan todas las coordenadas.
     * Si no se estaba arrastrando previamente, comienza un nuevo trazo
     * 
     */
    public void mouseDragged(MouseEvent e) 
    {
    	if (accion!=null){
	        if (arrastrando == false)
	        {
	            try {
					accion.comienzaDibujarTrazo(e.getX(), e.getY());
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
	            arrastrando = true;
	        }
	        /* Si ya se estaba arrastrando, se añaden más puntos al trazo */
	        try {
				accion.añadirPuntosTrazo(xAntigua, yAntigua, e.getX(), e.getY());
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
	        xAntigua = e.getX();
	        yAntigua = e.getY();
    	}
    }
}
