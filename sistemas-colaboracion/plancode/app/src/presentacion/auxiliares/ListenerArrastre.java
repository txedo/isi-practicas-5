package presentacion.auxiliares;

/**
 * REFERENCIA : http://www.chuidiang.com/java/codigo_descargable/appletpaint.php
 */

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


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
     */
    public void mouseDragged(MouseEvent e)
    {
    	if (accion!=null){
	        if (arrastrando == false)
	        {
	            accion.comienzaDibujarTrazo(e.getX(), e.getY());
	            arrastrando = true;
	        }
	        /* Si ya se estaba arrastrando, se añaden más puntos al trazo */
	        accion.añadirPuntosTrazo(xAntigua, yAntigua, e.getX(), e.getY());
	        xAntigua = e.getX();
	        yAntigua = e.getY();
    	}
    }
}
