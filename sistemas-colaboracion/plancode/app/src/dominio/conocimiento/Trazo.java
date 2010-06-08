package dominio.conocimiento;

/**
 * REFERENCIA : http://www.chuidiang.com/java/codigo_descargable/appletpaint.php
 */

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Trazo que se dibuja en el canvas. Esta compuesto de muchos puntos que se
 * unirán por una línea de color.
 *
 */
public class Trazo implements Serializable, Cloneable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5881732293220888123L;
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
    
    public double getDistanciaMinima(int x, int y)
    {
        double distancia = getDistanciaAbsoluta(x, y, 0);
        for (int i = 1; i < puntos.size(); i++)
        {
            distancia = Math.min(getDistanciaAbsoluta(x, y, i), distancia);
        }
        return distancia;
    }
    
    private double getDistanciaAbsoluta(int x, int y, int i)
    {
        return Math.abs(puntos.get(i).getX() - x)
                + Math.abs(puntos.get(i).getY() - y);
    }
    
    public void setPuntos(LinkedList<Point2D> puntos) {
		this.puntos = puntos;
	}
    
    public String toString(){
    	String res="";
    	for (int i=0; i<puntos.size(); i++){
    		res += puntos.get(i).toString() + " ";
    	}
    	return res;
    }
    
    public boolean equals(Object o){
    	boolean igual = false;
    	if (o instanceof Trazo) {
    		Trazo otro = (Trazo) o;
    		igual = ((this.puntos.equals(otro.getPuntos())) && this.color.equals(otro.getColor()));
    	}
    	return igual;
    }
    
    public Object clone(){
        Trazo t = new Trazo();
        t.setPuntos((LinkedList<Point2D>)this.puntos.clone());
        t.setColor(this.color);
        return t;
    }

	
}
