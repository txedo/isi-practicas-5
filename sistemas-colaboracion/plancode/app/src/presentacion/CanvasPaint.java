package presentacion;

/**
 * REFERENCIA : http://www.chuidiang.com/java/codigo_descargable/appletpaint.php
 */

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
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

    /** Objeto manejador de los arrastres de rat�n (para dibujar el trazo) */
    private ListenerArrastre listenerA = null;
    
    /** Objeto manejador para detectar donde se pincha con el rat�n (para eliminar el trazo) */
    private ListenerPinchar listenerP = null;

    /** Lista de trazos dibujados */
    private LinkedList<Trazo> trazos = new LinkedList<Trazo>();

    private boolean modoEliminar = false;
    
    /** 
     * Pone el modo de dibujo de trazos.
     */
    public void modoPintarTrazo()
    {
        listenerA.setAccion(gestorT);
        listenerP.setAccion(null);
    }
    
    /**
     * Pone el modo para eliminar trazos 
     */
    public void modoEliminarTrazo() {
    	listenerA.setAccion(null);
    	listenerP.setAccion(gestorT);
    	modoEliminar = true;
    }

    public CanvasPaint()
    {
        gestorT = new GestorTrazos(trazos, this);
        listenerA = new ListenerArrastre(gestorT);
        listenerP = new ListenerPinchar(gestorT);
        addMouseMotionListener(listenerA);
        addMouseListener(listenerP);
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
