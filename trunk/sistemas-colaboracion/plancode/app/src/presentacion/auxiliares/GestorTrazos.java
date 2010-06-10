package presentacion.auxiliares;

/**
 * REFERENCIA : http://www.chuidiang.com/java/codigo_descargable/appletpaint.php
 */

import java.awt.Color;
import java.awt.Component;
import java.util.LinkedList;

import com.sun.media.jsdt.ConnectionException;
import com.sun.media.jsdt.InvalidClientException;
import com.sun.media.jsdt.NoSuchChannelException;
import com.sun.media.jsdt.NoSuchClientException;
import com.sun.media.jsdt.NoSuchSessionException;
import com.sun.media.jsdt.PermissionDeniedException;
import com.sun.media.jsdt.TimedOutException;


import dominio.conocimiento.InfoTrazo;
import dominio.conocimiento.Trazo;
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

    /** Trazo que se está construyendo actualmente */
    private Trazo trazoActual = null;

    /** Lienzo de dibujo (el canvas), necesario para llamar a repaint() si se constuye o elimina un trazo
     * 
     */
    private Component lienzo;

    /** Color del que se está dibujando el trazo actual */
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
     * 
     */
    public void comienzaDibujarTrazo(int x, int y) throws ConnectionException, InvalidClientException, NoSuchChannelException, NoSuchClientException, NoSuchSessionException, PermissionDeniedException, TimedOutException
    {
        trazoActual = new Trazo();
        trazoActual.setColor(colorActual);
        trazoActual.addPunto(x, y);
        trazos.add(trazoActual);
        // Se envía al resto de clientes la información del trazo
        InfoTrazo info = new InfoTrazo(true, trazoActual);
        controlador.enviarTrazo(info);
        // Se actualiza el log local
        controlador.ponerMensajeLogLocal(info);
        lienzo.repaint();
    }

    /** Añade un nuevo punto al trazo actual 
     * 
     */
    public void añadirPuntosTrazo(int xAntigua, int yAntigua, int xNueva, int yNueva) throws ConnectionException, InvalidClientException, NoSuchChannelException, NoSuchClientException, NoSuchSessionException, PermissionDeniedException, TimedOutException
    {
        trazoActual.addPunto(xNueva, yNueva);
        trazoActual.setTerminado(false);
        // Se envía al resto de clientes la información del trazo al que se le añaden puntos
        InfoTrazo info = new InfoTrazo(true, trazoActual);
        controlador.enviarTrazo(info);
        lienzo.repaint();
    }

    /** Marca que ya no hay trazo actual 
     * 
     *  */
    public void finalizaArrastra(int x, int y) throws ConnectionException, InvalidClientException, NoSuchChannelException, NoSuchClientException, NoSuchSessionException, PermissionDeniedException, TimedOutException
    {
    	trazoActual.setTerminado(true);
    	Trazo t = (Trazo)trazoActual.clone();
    	// Se envia el trazo terminado a los clientes
    	InfoTrazo info = new InfoTrazo(true, t);
    	controlador.enviarTrazo(info);
        trazoActual = null;
        // Se actualiza el log local
        controlador.ponerMensajeLogLocal(info);

    }

    /** Guarda el color para el próximo trazo que se dibuje */
    public void setColorActual(Color colorActual)
    {
        this.colorActual = colorActual;
    }

	/** Busca el trazo mas cercano a donde se ha pinchado con el ratón (con un cierto margen) para eliminarlo *
	 *  */    
	public void eliminarObjeto(int x, int y) throws ConnectionException, InvalidClientException, NoSuchChannelException, NoSuchClientException, NoSuchSessionException, PermissionDeniedException, TimedOutException {
		int pos = -1;
		double distancia, distanciaAux;
		// Se comprueba si existe algún trazo
		if (trazos.size()>0) {
			distancia = trazos.get(0).getDistanciaMinima(x, y);
			if (trazos.size()==1) {
				// Si la distancia es mayor que la distancia de margen, no es válido
				pos = distancia > DISTANCIA_MARGEN ? -1 : 0;
			}
			else {
				for (int i = 0; i < trazos.size(); i++) {
		            distanciaAux = trazos.get(i).getDistanciaMinima(x, y);
		            // Este trazo se aproxima más al punto donde se ha pinchado
		            if (distanciaAux < distancia) {
		                distancia = distanciaAux;
		                // Si la nueva distancia está dentro del margen dado, se toma ese trazo
		                pos = distancia > DISTANCIA_MARGEN ? pos : i;
		            }
		        }
			}
		}	
		// Se elimina el trazo encontrado y se repinta el canvas
		if (pos!=-1) {
			Trazo aux = (Trazo)trazos.get(pos).clone();
			// Se elimina el trazo de la lista
			trazos.remove(trazos.get(pos));
			// Se envía al resto de clientes la información del trazo que se elimina	
			InfoTrazo info = new InfoTrazo(false, aux);
			controlador.enviarTrazo(info);
			// Se actualiza el log local
	        controlador.ponerMensajeLogLocal(info);
			lienzo.repaint();
		}
		
	}

	public LinkedList<Trazo> getTrazos() {
		return trazos;
	}

	public void setTrazos(LinkedList<Trazo> trazos) {
		this.trazos = trazos;
	}

}
