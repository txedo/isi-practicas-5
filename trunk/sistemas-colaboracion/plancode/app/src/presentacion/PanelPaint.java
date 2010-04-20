package presentacion;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.LinkedList;

/**
 * Panel que hereda de un Canvas, que permite dibujar trazos 
 * arrastrando el ratón.
 */
public class PanelPaint extends Canvas
{

    private static final long serialVersionUID = 3978706198935583032L;

    /** Acción de pintar trazo mientras se arrastra el ratón */
    private PintaTrazo pintaTrazo = null;

    /** Clase manejadora de los arrastres de ratón */
    private ListenerArrastre listener = null;

    /** Lista de trazos dibujados */
    private LinkedList<Trazo> trazos = new LinkedList<Trazo>();

    /** 
     * Pone el modo de dibujo de trazos.
     */
    public void modoPintar()
    {
        listener.setAccion(pintaTrazo);
    }

    public PanelPaint()
    {
        pintaTrazo = new PintaTrazo(trazos, this);
        listener = new ListenerArrastre(pintaTrazo);
        addMouseMotionListener(listener);
    }

    public void update(Graphics g)
    {
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
        pintaTrazo.setColorActual(colorActual);
    }
}
