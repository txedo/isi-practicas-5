package comunicaciones.EventosCanales;

import java.util.EventListener;

public interface MensajeChatRecibidoListener extends EventListener {
	
	public void MensajeChatRecibido(MensajeChatRecibidoEvent evt);

}
