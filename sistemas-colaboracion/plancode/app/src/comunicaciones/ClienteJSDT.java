package comunicaciones;
import com.sun.media.jsdt.*;

public class ClienteJSDT implements Client {
	private String nombre;
	private String rol;

	public ClienteJSDT(String nombre, String rol) {
		this.nombre = nombre;
		this.rol = rol;
	}

	public Object authenticate(AuthenticationInfo info) {
		return null;
	}

	public String getName() {
		return nombre;
	}
	
	public String getRol() {
		return rol;
	}
}