package comunicaciones.EventosCanales;

import java.util.EventObject;

public class MensajeChatRecibidoEvent extends EventObject {

	private static final long serialVersionUID = -8922139367513391169L;
	private String nombre;
	private String mensaje;

	public MensajeChatRecibidoEvent(Object obj, String nombre, String mensaje) {
		super(obj);
		this.nombre = nombre;
		this.mensaje = mensaje;
	}

	public String getNombre() {
		return nombre;
	}

	public String getMensaje() {
		return mensaje;
	}

}
