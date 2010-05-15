package comunicaciones;
import com.sun.media.jsdt.*;

import dominio.conocimiento.Roles;

public class ClienteJSDT implements Client {

	private static final long serialVersionUID = 4706016223824672736L;
	private String nombre;
	private Roles rol;

	public ClienteJSDT(String nombre, Roles rol) {
		this.nombre = nombre;
		this.rol = rol;
	}

	public Object authenticate(AuthenticationInfo info) {
		return null;
	}

	public String getName() {
		return nombre;
	}
	
	public Roles getRol() {
		return rol;
	}
}