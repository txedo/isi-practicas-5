package dominio.control;

import java.awt.Color;
import java.util.Vector;

public class GestorColores {

	private static Vector<Color> colores = new Vector<Color>();
	private static Vector<Color> coloresOcupados = new Vector<Color>();

	public static void inicializaColores() {
		colores.add(Color.black);
		colores.add(Color.red);
		colores.add(Color.yellow);
		colores.add(Color.blue);
		colores.add(Color.green);
		colores.add(Color.magenta);
	}

	/*
	 * Se busca un color libre para esta sesion y se marca como ocupado
	 */
	public static Color getColorLibre() {
		Color colorElegido = null;
		for (int i = 0; colorElegido == null && i < colores.size(); i++) {
			if (!coloresOcupados.contains(colores.get(i))) {
				colorElegido = colores.get(i);
				coloresOcupados.add(colorElegido);
			}
		}
		return colorElegido;
	}
	
	public static void liberarColor(Color c) {
		coloresOcupados.remove(c);
	}
}
