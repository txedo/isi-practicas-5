package presentacion.auxiliares;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class panelConImagenFondo extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2643504940011956438L;
	private TexturePaint fondo;
	 
	public panelConImagenFondo() {
		super();
	}
		
	public void setMapaFondoRecibido(ImageIcon fondo) {
		// Se carga el mapa recibido
		this.fondo = handlerImagenFondoPanel.cargaImageIcon(fondo, this);
	}
	
	public void setMapaFondoLocal(URL urlMapa) {
		// Se crea el mapa a partir de la ruta local y se pone como fondo
		handlerImagenFondoPanel.creaImageURL(urlMapa, this);
		this.fondo = handlerImagenFondoPanel.cargaTextura(this);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		Dimension d = getSize();
		g2d.setPaint(fondo);
		g2d.fill(new Rectangle(0,0,d.width,d.height));
	}
	
	protected void finalize() throws Throwable {
		fondo = null;
		System.gc(); // lanza el colector de basura
		System.runFinalization(); // para ejecutar los "finalize" del resto de clases
		super.finalize();
	}
}

