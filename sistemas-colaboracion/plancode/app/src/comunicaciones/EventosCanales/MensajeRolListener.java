package comunicaciones.EventosCanales;

import java.util.EventListener;

public interface MensajeRolListener extends EventListener {
	
	public void MensajeRolRecibido(MensajeRolEvent evt);

}
