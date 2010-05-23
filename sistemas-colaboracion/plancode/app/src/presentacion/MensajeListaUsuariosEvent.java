package presentacion;

import java.util.EventObject;
import java.util.Hashtable;

import dominio.conocimiento.Usuario;

public class MensajeListaUsuariosEvent extends EventObject {

	private static final long serialVersionUID = -8922139367513391169L;
	private Hashtable<String,Usuario> lista;
	
	public MensajeListaUsuariosEvent(Object obj, Object lista) {
		super(obj);
		this.lista = (Hashtable<String,Usuario>) lista;
	}

	public Hashtable<String, Usuario> getLista() {
		return lista;
	}
}
