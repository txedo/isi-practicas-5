package presentacion;

import java.awt.Color;
import java.awt.Component;
import java.util.LinkedList;

/**
 * Construye trazos seg�n se va arrastrando el rat�n
 *
 */
public class PintaTrazo implements InterfaceArrastrarRaton
{
    /** Lista de trazos */
    private LinkedList<Trazo> trazos;

    /** Trazo que se est� construyendo actualmente */
    private Trazo trazoActual = null;

    /** Lienzo de dibujo (el canvas), necesario para llamar a repaint() seg�n se va
     * construyendo un nuevo trazo.
     */
    private Component lienzo;

    /** Color del que se est� dibujando el trazo actual */
    private Color colorActual = Color.black;

    public PintaTrazo(LinkedList<Trazo> trazos, Component lienzo)
    {
        this.trazos = trazos;
        this.lienzo = lienzo;
    }

    /**
     * Crea un trazo nuevo y le pone como primer punto x,y.
     */
    public void comienzaDibujarTrazo(int x, int y)
    {
        trazoActual = new Trazo();
        trazoActual.setColor(colorActual);
        trazoActual.addPunto(x, y);
        trazos.add(trazoActual);
        lienzo.repaint();
    }

    /** A�ade un nuevo punto al trazo actual */
    public void a�adirPuntosTrazo(int xAntigua, int yAntigua, int xNueva, int yNueva)
    {
        trazoActual.addPunto(xNueva, yNueva);
        lienzo.repaint();
    }

    /** Marca que ya no hay trazo actual */
    public void finalizaArrastra(int x, int y)
    {
        trazoActual = null;
    }

    /** Guarda el color para el pr�ximo trazo que se dibuje */
    public void setColorActual(Color colorActual)
    {
        this.colorActual = colorActual;
    }
}
