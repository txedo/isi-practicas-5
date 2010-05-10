package presentacion;

/**
 * REFERENCIA : http://www.chuidiang.com/java/codigo_descargable/appletpaint.php
 */

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.LinkedList;

/**
 * Clase que hereda de Canvas y que permite dibujar trazos sobre el canvas
 * 
 */
public class CanvasPaint extends Canvas
{

    private static final long serialVersionUID = 3978706198935583032L;

    /** Objeto para gestionar lo relacionado con trazos (dibujar/eliminar) */
    private GestorTrazos gestorT = null;

    /** Objeto manejador de los arrastres de ratón (para dibujar el trazo) */
    private ListenerArrastre listenerA = null;
    
    /** Objeto manejador para detectar donde se pincha con el ratón (para eliminar el trazo) */
    private ListenerPinchar listenerP = null;

    /** Lista de trazos dibujados */
    private LinkedList<Trazo> trazos = new LinkedList<Trazo>();

    private boolean modoEliminar = false;
    
    
    // Se inicializa el gestor de trazos y los listener para arrastrar el ratón y pinchar con el ratón
    public CanvasPaint()
    {
        gestorT = new GestorTrazos(trazos, this);
        listenerA = new ListenerArrastre(gestorT);
        listenerP = new ListenerPinchar(gestorT);
        // Se pasa el propio canvas, para poder cambiar la imagen del cursor cuando éste entra en ese área
        listenerP.setComponent(this);
        // Se añaden los listeners al canvas
        addMouseMotionListener(listenerA);
        addMouseListener(listenerP);
    }
    
    /** 
     * Pone el modo de dibujo de trazos.
     * En este caso, el listener de "pinchar" no tiene ninguna acción. 
     */
    public void modoPintarTrazo()
    {
    	// El listener de arrastre recibe el gestor de trazos para ir dibujando los trazos
        listenerA.setAccion(gestorT);
        listenerP.setAccion(null);

    }
    
    /**
     * Pone el modo para eliminar trazos. En este caso, el listener de arrastre no tiene acción pero
     * sí tiene el listener de "pinchar", ya que se elimina un trazo al pinchar cerca de él. 
     */
    public void modoEliminarTrazo() {
    	listenerA.setAccion(null);
    	listenerP.setAccion(gestorT);
    	modoEliminar = true;
    }

    public void update(Graphics g)
    {
    	// Si es el modo eliminar trazos, se llama al padre para borrar el canvas completo.
    	// Luego se dibujan los trazos que sean necesarios
    	if (modoEliminar)
    		super.update(g);
    	paint(g);
    }

    /**
     * Dibuja los trazos en este componente
     */
    public void paint(Graphics g)
    {
        for (int i = 0; i < trazos.size(); i++)
        {
            dibujaTrazo(trazos.get(i), g);
        }
    }

    /**
     * Dibuja un trazo en este componente.
     */
    private void dibujaTrazo(Trazo trazo, Graphics g)
    {
        g.setColor(trazo.getColor());
        Point2D p0 = trazo.getPunto(0);
        for (int i = 0; i < trazo.getNumeroPuntos() - 1; i++)
        {
            Point2D p1 = trazo.getPunto(i + 1);
            g.drawLine((int) p0.getX(), (int) p0.getY(), (int) p1.getX(),
                    (int) p1.getY());
            p0 = p1;
        }
    }

    /**
     * Cambia el color de dibujo del trazo.
     */
    public void setColorActual(Color colorActual)
    {
        gestorT.setColorActual(colorActual);
    }
    
}
