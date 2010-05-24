package presentacion;

import java.io.Serializable;
import java.util.EventObject;
import java.util.Hashtable;

import dominio.conocimiento.Usuario;

public class MensajeListaUsuariosEvent extends EventObject implements Serializable {

	private static final long serialVersionUID = -8922139367513391169L;
	//private Hashtable<String,Usuario> lista;
	private String lista;
	
	public MensajeListaUsuariosEvent(Object obj, String lista) {
		super(obj);
		//this.lista = (Hashtable<String,Usuario>)lista;
		this.lista = lista;
		System.out.println("Entra");
	}

	/*public Hashtable<String, Usuario> getLista() {
		return lista;
	}*/
	
	public String getLista() {
		return lista;
	}
}
