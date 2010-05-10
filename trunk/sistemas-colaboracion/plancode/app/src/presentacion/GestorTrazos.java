package presentacion;

/**
 * REFERENCIA : http://www.chuidiang.com/java/codigo_descargable/appletpaint.php
 */

import java.awt.Color;
import java.awt.Component;
import java.util.LinkedList;

/**
 * Clase para gestionar lo relacionado con trazos (dibujar/eliminar)
 *
 */
public class GestorTrazos implements InterfaceArrastrarRaton, InterfacePincharRaton
{
    /** Lista de trazos */
    private LinkedList<Trazo> trazos;

    /** Trazo que se está construyendo actualmente */
    private Trazo trazoActual = null;

    /** Lienzo de dibujo (el canvas), necesario para llamar a repaint() si se constuye o elimina un trazo
     * 
     */
    private Component lienzo;

    /** Color del que se está dibujando el trazo actual */
    private Color colorActual = Color.black;

    public GestorTrazos(LinkedList<Trazo> trazos, Component lienzo)
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

    /** Añade un nuevo punto al trazo actual */
    public void añadirPuntosTrazo(int xAntigua, int yAntigua, int xNueva, int yNueva)
    {
        trazoActual.addPunto(xNueva, yNueva);
        lienzo.repaint();
    }

    /** Marca que ya no hay trazo actual */
    public void finalizaArrastra(int x, int y)
    {
        trazoActual = null;
    }

    /** Guarda el color para el próximo trazo que se dibuje */
    public void setColorActual(Color colorActual)
    {
        this.colorActual = colorActual;
    }

	/** Busca el trazo mas cercano a donde se ha pinchado con el ra´ton para eliminarlo **/
    // TODO: revisar el algoritmo de distancias, sobre todo para cuando hay 1 trazo
    
    
	public void eliminarObjeto(int x, int y) {
		int pos = -1;

		 double distancia = trazos.get(0).dameDistanciaMinima(x, y);
		 pos = 0;
	        for (int i = 1; i < trazos.size(); i++)
	        {
	            double distanciaAux = trazos.get(i).dameDistanciaMinima(x, y);
	            if (distanciaAux < distancia)
	            {
	                distancia = distanciaAux;
	                pos = i;
	            }
	        }
	       if (pos!=-1){
	    	   trazos.remove(trazos.get(pos));

	       }
    	   lienzo.repaint();
		
	}
}
