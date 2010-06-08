package presentacion;

/**
 * REFERENCIA : http://www.chuidiang.com/java/codigo_descargable/appletpaint.php
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import presentacion.auxiliares.GestorTrazos;
import presentacion.auxiliares.ListenerArrastre;
import presentacion.auxiliares.ListenerPinchar;

import javax.swing.JPanel;

import dominio.conocimiento.InfoTrazo;
import dominio.conocimiento.Trazo;
import dominio.control.ControladorPrincipal;


/**
 * Clase que hereda de JPanel y que permite dibujar trazos, actuando como un canvas
 * 
 */
public class CanvasPaint extends JPanel
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
    private boolean listenersEstablecidos = false;
    
       
    // Se inicializa el gestor de trazos y los listener para arrastrar el ratón y pinchar con el ratón
    public CanvasPaint(ControladorPrincipal c)
    {
        gestorT = new GestorTrazos(trazos, this, c);
        listenerA = new ListenerArrastre(gestorT);
        listenerP = new ListenerPinchar(gestorT);
        // Se pasa el propio canvas, para poder cambiar la imagen del cursor cuando éste entra en ese área
        listenerP.setComponent(this);
    }
    
    public void noAction() {
    	// Se eliminan los listeners del canvas
        removeMouseMotionListener(listenerA);
        removeMouseListener(listenerP);
        listenersEstablecidos = false;
    }
    /** 
     * Pone el modo de dibujo de trazos.
     * En este caso, el listener de "pinchar" no tiene ninguna acción. 
     */
    public void modoPintarTrazo()
    {
    	if (!listenersEstablecidos) {
    		 // Se añaden los listeners al canvas
            addMouseMotionListener(listenerA);
            addMouseListener(listenerP);
            listenersEstablecidos = true;
    	}
    	// El listener de arrastre recibe el gestor de trazos para ir dibujando los trazos
        listenerA.setAccion(gestorT);
        listenerP.setAccion(null);

    }
    
    /**
     * Pone el modo para eliminar trazos. En este caso, el listener de arrastre no tiene acción pero
     * sí tiene el listener de "pinchar", ya que se elimina un trazo al pinchar cerca de él. 
     */
    public void modoEliminarTrazo() {
    	if (!listenersEstablecidos) {
   		 // Se añaden los listeners al canvas
           addMouseMotionListener(listenerA);
           addMouseListener(listenerP);
           listenersEstablecidos = true;
    	}
    	listenerA.setAccion(null);
    	listenerP.setAccion(gestorT);
    	modoEliminar = true;
    }

    public void update(Graphics g)
    {
    	// Si es el modo eliminar trazos, se llama al padre para borrar el canvas completo.
    	// Luego se dibujan los trazos que sean necesarios
    	if (modoEliminar) {    	
    		super.update(g);
    	}
    	paint(g);
    }

    /**
     * Dibuja los trazos en este componente
     */
    public void paint(Graphics g)
    {
    	super.paint(g);
        for (int i = 0; i < trazos.size(); i++)
        {
            dibujaTrazo(trazos.get(i), g);
        }
    }

    public void clear() {
    	trazos.clear();
    	this.repaint();
    }
    
    /**
     * Dibuja un trazo en este componente.
     */
    private void dibujaTrazo(Trazo trazo, Graphics g)
    {    	
        g.setColor(trazo.getColor());
        // Aumentamos el grosor del trazo, para que se vea mejor sobre el mapa
        ((Graphics2D)g).setStroke( new BasicStroke(4) ); 
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

	public LinkedList<Trazo> getTrazos() {
		return trazos;
	}

	// Se comrpueba si se está en el modo de dibujo o de eliminacion.
	// Además, en el modo de dibujo, si se están añadiendo puntos, se elimina el trazo viejo y se incluye el nuevo
	public void setTrazos(InfoTrazo info) {
		if (info.isDibujando()){
			this.modoEliminar = false;
			// Si no se están añadiendo puntos a algún trazo, se inserta el nuevo trazo
			if (!info.isAñadiendo()) {
				this.trazos.add(info.getTrazoNuevo());
			// Se añaden puntos a un trazo
			} else {
				// Se recupera el índice de ese trazo
				int index = -1;
				for (int i=0; index==-1 && i<this.trazos.size(); i++) {
					
					if (this.trazos.get(i).equals(info.getTrazoNuevo())) {
						index = i;
					}
				}
				System.out.println(index);
				if (index!=-1)
					this.trazos.get(index).addPunto(info.getX(), info.getY());
			}
		}
		else {
			this.modoEliminar = true;
			LinkedList<Trazo> aux = new LinkedList<Trazo>();
			// Se elimina el trazo
			for (int i=0; i<this.trazos.size(); i++) {
				if (!this.trazos.get(i).equals(info.getTrazoNuevo())) {
					aux.add(this.trazos.get(i));
				}					
			}
			this.trazos = aux;
		}
		
	}
    
}
