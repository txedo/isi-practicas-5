package comunicaciones.EventosCanales;

import java.util.EventListener;

public interface MensajeMapaListener extends EventListener {
	
	public void MensajeMapa(MensajeMapaEvent evt);

}
