package presentacion;

import java.util.EventObject;

import javax.swing.ImageIcon;

public class MensajeMapaEvent extends EventObject {

	private static final long serialVersionUID = -8922139367513391169L;
	private ImageIcon mapa;
	
	public MensajeMapaEvent(Object obj, Object mapa) {
		super(obj);
		this.mapa = (ImageIcon)mapa;		
	}

	public ImageIcon getMapa() {
		return mapa;
	}

}

