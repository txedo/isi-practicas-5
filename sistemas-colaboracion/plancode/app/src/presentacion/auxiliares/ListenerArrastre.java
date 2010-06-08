package presentacion.auxiliares;

/**
 * REFERENCIA : http://www.chuidiang.com/java/codigo_descargable/appletpaint.php
 */

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


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
     */
    public void mouseDragged(MouseEvent e)
    {
    	if (accion!=null){
	        if (arrastrando == false)
	        {
	            accion.comienzaDibujarTrazo(e.getX(), e.getY());
	            arrastrando = true;
	        }
	        /* Si ya se estaba arrastrando, se a�aden m�s puntos al trazo */
	        accion.a�adirPuntosTrazo(xAntigua, yAntigua, e.getX(), e.getY());
	        xAntigua = e.getX();
	        yAntigua = e.getY();
    	}
    }
}
