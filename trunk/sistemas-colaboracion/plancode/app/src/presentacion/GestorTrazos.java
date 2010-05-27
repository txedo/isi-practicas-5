package presentacion;

/**
 * REFERENCIA : http://www.chuidiang.com/java/codigo_descargable/appletpaint.php
 */

import java.awt.Color;
import java.awt.Component;
import java.util.LinkedList;

import dominio.control.ControladorPrincipal;

/**
 * Clase para gestionar lo relacionado con trazos (dibujar/eliminar)
 *
 */
public class GestorTrazos implements InterfaceArrastrarRaton, InterfacePincharRaton
{
	private ControladorPrincipal controlador; 
	
    /** Lista de trazos */
    private LinkedList<Trazo> trazos;

    /** Trazo que se est� construyendo actualmente */
    private Trazo trazoActual = null;

    /** Lienzo de dibujo (el canvas), necesario para llamar a repaint() si se constuye o elimina un trazo
     * 
     */
    private Component lienzo;

    /** Color del que se est� dibujando el trazo actual */
    private Color colorActual = Color.black;

    private final double DISTANCIA_MARGEN = 15.0;
    
    public GestorTrazos(LinkedList<Trazo> trazos, Component lienzo, ControladorPrincipal c)
    {
        this.trazos = trazos;
        this.lienzo = lienzo;
        controlador = c;
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
        controlador.enviarTrazoDibujado(trazos);
        lienzo.repaint();
    }

    /** A�ade un nuevo punto al trazo actual */
    public void a�adirPuntosTrazo(int xAntigua, int yAntigua, int xNueva, int yNueva)
    {
        trazoActual.addPunto(xNueva, yNueva);
        controlador.enviarTrazoDibujado(trazos);
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

	/** Busca el trazo mas cercano a donde se ha pinchado con el rat�n (con un cierto margen) para eliminarlo **/    
	public void eliminarObjeto(int x, int y) {
		int pos = -1;
		double distancia, distanciaAux;
		// Se comprueba si existe alg�n trazo
		if (trazos.size()>0) {
			distancia = trazos.get(0).getDistanciaMinima(x, y);
			if (trazos.size()==1) {
				// Si la distancia es mayor que la distancia de margen, no es v�lido
				pos = distancia > DISTANCIA_MARGEN ? -1 : 0;
			}
			else {
				for (int i = 0; i < trazos.size(); i++) {
		            distanciaAux = trazos.get(i).getDistanciaMinima(x, y);
		            // Este trazo se aproxima m�s al punto donde se ha pinchado
		            if (distanciaAux < distancia) {
		                distancia = distanciaAux;
		                // Si la nueva distancia est� dentro del margen dado, se toma ese trazo
		                pos = distancia > DISTANCIA_MARGEN ? pos : i;
		            }
		        }
			}
		}	
		// se elimina el trazo encontrado y se repinta el canvas
		if (pos!=-1) {
			trazos.remove(trazos.get(pos));
			lienzo.repaint();
		}
		
	}

}
