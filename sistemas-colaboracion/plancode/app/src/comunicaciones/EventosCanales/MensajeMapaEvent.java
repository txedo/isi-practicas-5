package comunicaciones.EventosCanales;

import java.util.EventObject;

import javax.swing.ImageIcon;

import dominio.conocimiento.InfoMapa;

public class MensajeMapaEvent extends EventObject {

	private static final long serialVersionUID = -8922139367513391169L;
	private String nombreCliente;
	private ImageIcon mapa;
	
	public MensajeMapaEvent(Object obj, String cliente, Object mapa) {
		super(obj);
		this.nombreCliente = cliente;
		this.mapa = (ImageIcon)mapa;		
	}

	public ImageIcon getMapa() {
		return mapa;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

}

