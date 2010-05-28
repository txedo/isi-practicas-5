package presentacion.auxiliares;

import java.awt.*;
import javax.swing.ImageIcon;
import java.awt.image.*;
import java.net.URL;

/**
 * 
 * REFERENCIA: http://bicosyes.com/capas-con-imagen-de-fondo-en-java/
 * 
 */
public class handlerImagenFondoPanel {
	
	// TexturePaint no implementa la interfaz serializable, ni la clase Image tampoco. Por ello, se debe crear
	// un objeto de tipo ImageIcon que si es serializable, encapsulando en él la imagen cargada.
	private static ImageIcon mapaCargado = null;
	
	// Este método carga una imagen desde una URL. Se usa para cargar el mapa desde el disco local al cliente
	public static void creaImageURL(URL imageFile, Component c) 	{
		Image img = (new ImageIcon(imageFile)).getImage();
		mapaCargado = new ImageIcon(img);
	}
	
	// Método para cargar un ImageIcon y crear su textura
	// Este método se usará cuando un cliente reciba el mapa de otro cliente
	public static TexturePaint cargaImageIcon(ImageIcon img, Component c) 	{
		mapaCargado = img;
		return cargaTextura(c);
	}
	
	// Método para cargar como textura un mapa, ya sea local o recibido de otro cliente
	public static TexturePaint cargaTextura(Component c) {
		TexturePaint imageDev;
		try
		{
			BufferedImage image = getBufferedImage(mapaCargado.getImage() , c);
			imageDev =  new TexturePaint(image,
					new Rectangle(0, 0, image.getWidth(), image.getHeight())
			);
		}catch(Exception e){
      	imageDev = null;
      }
      return imageDev;
		
	}
 
	private static BufferedImage getBufferedImage(Image image, Component c)
	{
		waitForImage(image, c);
		BufferedImage bufferedImage = new BufferedImage(
			image.getWidth(c), image.getHeight(c), BufferedImage.TYPE_INT_RGB
		);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.drawImage(image, 0, 0, c);
		return(bufferedImage);
	}
 
	private static boolean waitForImage(Image image, Component c)
	{
		MediaTracker tracker = new MediaTracker(c);
		tracker.addImage(image, 0);
		try{
			tracker.waitForAll();
		}catch(InterruptedException ie){}
		return(!tracker.isErrorAny());
	}

	// Se devuelve el mapa que se pone como fondo para pasarlo por el canal al resto de clientes conectados
	public static ImageIcon getMapaCargado() {
		return mapaCargado;
	}
}