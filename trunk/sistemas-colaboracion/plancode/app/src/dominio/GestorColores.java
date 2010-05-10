package dominio;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Vector;

import com.sun.media.jsdt.Session;

public class GestorColores {

	private static Vector<Color> coloresDisponibles = new Vector<Color>();
	// En cada sesión, se almacenan los colores ya ocupados, para luego buscar uno libre cuando se conecta un nuevo cliente a esa sesión
	private static Hashtable<Session, Vector<Color>> tablaColoresSesion = new Hashtable<Session, Vector<Color>>();
	
	public static void añadirSesion(Session s) {
		tablaColoresSesion.put(s, new Vector<Color>());	
	}
	
	public static void inicializaColores () {
		coloresDisponibles.add(Color.black);
		coloresDisponibles.add(Color.red);
		coloresDisponibles.add(Color.yellow);
		coloresDisponibles.add(Color.blue);
		coloresDisponibles.add(Color.green);
		coloresDisponibles.add(Color.magenta);
	}
	
	/**
	 * Se busca un color libre para esta sesion y se marca como ocupado
	 * 
	 */
	public static Color getColorLibre(Session s) {
		Color colorElegido = null;
		Vector<Color> coloresOcupados = null;
		coloresOcupados = (Vector<Color>) tablaColoresSesion.get(s);
		if (coloresOcupados!=null) {
			for (int i=0; colorElegido==null && i<coloresDisponibles.size(); i++) {
				if (!coloresOcupados.contains(coloresDisponibles.get(i))) {
					colorElegido = coloresDisponibles.get(i);
					coloresOcupados.add(colorElegido);
				}
			}
		}
		return colorElegido;
	}
}
