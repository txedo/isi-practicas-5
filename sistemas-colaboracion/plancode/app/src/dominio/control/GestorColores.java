package dominio.control;

import java.awt.Color;
import java.util.Vector;

import excepciones.NoSlotsDisponiblesException;

public class GestorColores {

	private static Vector<Color> colores = new Vector<Color>();
	private static Vector<Color> coloresOcupados = new Vector<Color>();

	public static void inicializaColores() {
		colores.add(Color.darkGray);
		colores.add(Color.blue);
		colores.add(Color.magenta);		
		colores.add(Color.green);
		colores.add(Color.orange);
		
	}

	/*
	 * Se busca un color libre para esta sesion y se marca como ocupado
	 */
	public static Color getColorLibre() throws NoSlotsDisponiblesException {
		Color colorElegido = null;
		for (int i = 0; colorElegido == null && i < colores.size(); i++) {
			if (!coloresOcupados.contains(colores.get(i))) {
				colorElegido = colores.get(i);
				coloresOcupados.add(colorElegido);
			}
		}
		if (colorElegido == null) throw new NoSlotsDisponiblesException();
		return colorElegido;
	}
	
	public static void liberarColor(Color c) {
		coloresOcupados.remove(c);
	}

	public static Vector<Color> getColores() {
		return colores;
	}

	public static Vector<Color> getColoresOcupados() {
		return coloresOcupados;
	}
}
