package presentacion;

import java.util.EventObject;

import dominio.conocimiento.Roles;

public class MensajeRolEvent extends EventObject  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6343783179343926816L;

	private String nombre;
	private Roles rol;

	public MensajeRolEvent(Object obj, String nombre, String rol) {
		super(obj);
		this.nombre = nombre;
		this.rol = Roles.valueOf(rol);
	}

	public String getNombre() {
		return nombre;
	}

	public Roles getRol() {
		return rol;
	}
}
