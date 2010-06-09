package dominio.conocimiento;

import java.io.Serializable;
import java.util.LinkedList;


public class InfoTrazo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1732709130048456235L;
	
	private Trazo trazo;
	/*
	private Trazo trazoAntiguo;
	private boolean dibujando;
	private boolean a�adiendo;
	private boolean terminado;*/
	private boolean dibujando;


	// Constructor para el caso de dibujar sin a�adir puntos (o para eliminar, si dibujando = false)
	public InfoTrazo(boolean dibujando, Trazo trazo) {
		this.trazo = trazo;
		this.dibujando = dibujando;
	}


	public Trazo getTrazo() {
		return trazo;
	}


	public boolean isDibujando() {
		return dibujando;
	}
	
	// Constructor para el caso de dibujar a�adiendo puntos
	/*public InfoTrazo(boolean dibujando, Trazo trazo, int x, int y) {
		this.a�adiendo = true;
		this.trazoNuevo = trazo;
		this.dibujando = dibujando;
		this.x = x;
		this.y = y;
	}*/
	
	// Constructor para el caso de dibujar a�adiendo puntos
	/*public InfoTrazo(boolean dibujando, Trazo trazoAntiguo, Trazo trazoNuevo) {
		this.a�adiendo = true;
		this.trazoNuevo = trazoNuevo;
		this.trazoAntiguo = trazoAntiguo;
		this.dibujando = dibujando;
		
	}
	
	// Constructor para el caso de haber terminado de dibujar un trazo
	public InfoTrazo(Trazo trazoTerminado) {
		this.terminado = true;
		this.dibujando = false;
		this.a�adiendo = false;
		this.trazoNuevo = trazoTerminado;
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

	public Trazo getTrazoAntiguo() {
		return trazoAntiguo;
	}

	public boolean isTerminado() {
		return terminado;
	}*/

	
}
