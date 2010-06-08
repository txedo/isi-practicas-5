package comunicaciones.EventosCanales;

import java.util.EventListener;

import com.sun.media.jsdt.ConnectionException;
import com.sun.media.jsdt.InvalidClientException;
import com.sun.media.jsdt.NoSuchChannelException;
import com.sun.media.jsdt.NoSuchClientException;
import com.sun.media.jsdt.NoSuchSessionException;
import com.sun.media.jsdt.PermissionDeniedException;
import com.sun.media.jsdt.TimedOutException;

public interface MensajeRolListener extends EventListener {
	
	public void MensajeRolRecibido(MensajeRolEvent evt) throws ConnectionException, InvalidClientException, NoSuchChannelException, NoSuchClientException, NoSuchSessionException, PermissionDeniedException, TimedOutException;

}
