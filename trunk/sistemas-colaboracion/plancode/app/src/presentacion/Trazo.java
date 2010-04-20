package presentacion;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.LinkedList;

/**
 * Trazo que se dibuja en el canvas. Esta compuesto de muchos puntos que se
 * unirán por una línea de color.
 *
 */
public class Trazo
{
	/** Lista de puntos que compone el trazo */
    private LinkedList<Point2D> puntos = new LinkedList<Point2D>();
    private Color color = Color.black;

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public void addPunto(Point2D punto)
    {
        puntos.add(punto);
    }

    public LinkedList<Point2D> getPuntos()
    {
        return puntos;
    }

    public int getNumeroPuntos()
    {
        return puntos.size();
    }

    public void addPunto(int x, int y)
    {
        Point2D p = new Point2D.Float(x, y);
        addPunto(p);
    }

    public Point2D getPunto(int posicion)
    {
        return puntos.get(posicion);
    }

}
