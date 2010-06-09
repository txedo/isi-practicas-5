package dominio.conocimiento;

import java.io.Serializable;
import java.util.LinkedList;


public class InfoTrazo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1732709130048456235L;
	
	private Trazo trazo;
	private LinkedList<Trazo> trazos;
	private boolean dibujando;
	private boolean clear;
	private boolean recienConectado;


	// Constructor para el caso de dibujar sin añadir puntos (o para eliminar, si dibujando = false)
	public InfoTrazo(boolean dibujando, Trazo trazo) {
		this.trazo = trazo;
		this.dibujando = dibujando;
		clear = false;
		recienConectado = false;
	}
	
	// Constructor para pasar todos los trazos al conectarse
	public InfoTrazo (LinkedList<Trazo> trazos) {
		this.trazos = trazos;
		clear = false;
		recienConectado = true;
	}
	
	// El construcotr por defecto significa limpiar el canvas completo
	public InfoTrazo() {
		this.clear = true;
		recienConectado = false;
		dibujando = false;
	}


	public Trazo getTrazo() {
		return trazo;
	}


	public boolean isDibujando() {
		return dibujando;
	}
	
	public boolean isTerminado() {
		return trazo.isTerminado();
	}

	public LinkedList<Trazo> getTrazos() {
		return trazos;
	}

	public boolean isClear() {
		return clear;
	}

	public boolean isRecienConectado() {
		return recienConectado;
	}
	
}
