package dominio.conocimiento;

import java.awt.Color;
import java.io.Serializable;


public class Usuario implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -408343270653594450L;
	private Roles rol;
	private Color color;

	public Usuario(Roles rol, Color color) {
		this.rol = rol;
		this.color = color;
	}

	public Roles getRol() {
		return rol;
	}

	public Color getColor() {
		return color;
	}

}
