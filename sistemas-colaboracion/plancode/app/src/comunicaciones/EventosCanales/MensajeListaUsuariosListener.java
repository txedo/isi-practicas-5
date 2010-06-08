package comunicaciones.EventosCanales;

import java.util.EventListener;

public interface MensajeListaUsuariosListener extends EventListener {
	
	public void MensajeListaUsuarios(MensajeListaUsuariosEvent evt);

}
