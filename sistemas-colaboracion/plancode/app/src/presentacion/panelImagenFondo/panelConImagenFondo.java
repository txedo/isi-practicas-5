package presentacion.panelImagenFondo;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.net.URL;

import javax.swing.JPanel;

public class panelConImagenFondo extends JPanel{
	
	private TexturePaint fondo;
	 
	public panelConImagenFondo() {
		super();
	}
	
	public panelConImagenFondo(URL imgFondo)
	{
		fondo = handlerImagenFondoPanel.carga(imgFondo, this);
	}
	
	public void setImagen(URL fondo) {
		this.fondo = handlerImagenFondoPanel.carga(fondo, this);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		Dimension d = getSize();
		g2d.setPaint(fondo);
		g2d.fill(new Rectangle(0,0,d.width,d.height));
	}
}

