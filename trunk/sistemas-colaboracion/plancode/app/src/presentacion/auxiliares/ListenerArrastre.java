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
 * para conocer los movimientos de rat�n y almacenar las coordenadas por
 * las que se va arrastrando, avisando al InterfaceArrastrarRaton.
 */
public class ListenerArrastre implements MouseMotionListener
{
    /** Interfaz encargada de realizar acciones cuando se arrastra el rat�n */
    private InterfaceArrastrarRaton accion;
    
    /** Indica si se est� arrastrando el rat�n */
    boolean arrastrando = false;

    public ListenerArrastre(InterfaceArrastrarRaton accion)
    {
        this.accion = accion;
    }
    

    public void setAccion(InterfaceArrastrarRaton accion)
    {
        this.accion = accion;
    }

    /** x del rat�n antes de producirse el �ltimo arrastre */
    int xAntigua;

    /** y del rat�n antes de producirse el �ltimo arrastre */
    int yAntigua;

    /**
     * Se mueve el rat�n a un nuevo punto. Cuando se deja de arrastrar el rat�n, 
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
     * Se est� arrastrando el rat�n. Se avisa a la accion correspondiente y
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
	    			Dialogos.mostrarDialogoError(null, "Error", "No se puede establecer una conexi�n");
	    		} catch (InvalidClientException ex) {
	    			Dialogos.mostrarDialogoError(null, "Error", "Cliente de destino inv�lido");
	    		} catch (NoSuchChannelException ex) {
	    			Dialogos.mostrarDialogoError(null, "Error", "No existe el canal");
	    		} catch (NoSuchClientException ex) {
	    			Dialogos.mostrarDialogoError(null, "Error", "No se encuentra el cliente de destino");
	    		} catch (NoSuchSessionException ex) {
	    			Dialogos.mostrarDialogoError(null, "Error", "No se encuentra la sesi�n");
	    		} catch (PermissionDeniedException ex) {
	    			Dialogos.mostrarDialogoError(null, "Error", "Permiso denegado");
	    		} catch (TimedOutException ex) {
	    			Dialogos.mostrarDialogoError(null, "Error", "Tiempo de espera agotado");
	    		}
	            arrastrando = true;
	        }
	        /* Si ya se estaba arrastrando, se a�aden m�s puntos al trazo */
	        try {
				accion.a�adirPuntosTrazo(xAntigua, yAntigua, e.getX(), e.getY());
	        } catch (ConnectionException ex) {
    			Dialogos.mostrarDialogoError(null, "Error", "No se puede establecer una conexi�n");
    		} catch (InvalidClientException ex) {
    			Dialogos.mostrarDialogoError(null, "Error", "Cliente de destino inv�lido");
    		} catch (NoSuchChannelException ex) {
    			Dialogos.mostrarDialogoError(null, "Error", "No existe el canal");
    		} catch (NoSuchClientException ex) {
    			Dialogos.mostrarDialogoError(null, "Error", "No se encuentra el cliente de destino");
    		} catch (NoSuchSessionException ex) {
    			Dialogos.mostrarDialogoError(null, "Error", "No se encuentra la sesi�n");
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
