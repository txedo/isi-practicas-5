package dominio.conocimiento;

import java.io.Serializable;
import java.util.LinkedList;


public class InfoTrazo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1732709130048456235L;
	
	private Trazo trazoNuevo;
	private boolean dibujando;
	private boolean a�adiendo;
	private int x;
	private int y;
	
	// Constructor para el caso de dibujar sin a�adir puntos (o para eliminar, si dibujando = false)
	public InfoTrazo(boolean dibujando, Trazo trazo) {
		this.a�adiendo = false;
		this.trazoNuevo = trazo;
		this.dibujando = dibujando;
		this.x = -1;
		this.y = -1;
	}
	
	// Constructor para el caso de dibujar a�adiendo puntos
	public InfoTrazo(boolean dibujando, Trazo trazo, int x, int y) {
		this.a�adiendo = true;
		this.trazoNuevo = trazo;
		this.dibujando = dibujando;
		this.x = x;
		this.y = y;
	}	
	
	public Trazo getTrazoNuevo() {
		return trazoNuevo;
	}
	public boolean isDibujando() {
		return dibujando;
	}
	public boolean isA�adiendo() {
		return a�adiendo;
	}

	public void setTrazoNuevo(Trazo trazoNuevo) {
		this.trazoNuevo = trazoNuevo;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	
}
