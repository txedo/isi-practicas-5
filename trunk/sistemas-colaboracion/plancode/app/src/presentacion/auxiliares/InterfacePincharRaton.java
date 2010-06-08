package presentacion.auxiliares;

import com.sun.media.jsdt.ConnectionException;
import com.sun.media.jsdt.InvalidClientException;
import com.sun.media.jsdt.NoSuchChannelException;
import com.sun.media.jsdt.NoSuchClientException;
import com.sun.media.jsdt.NoSuchSessionException;
import com.sun.media.jsdt.PermissionDeniedException;
import com.sun.media.jsdt.TimedOutException;

/**
 * Interfaz para implementar operaciones cuando se pincha con el ratón
 *
 */
public interface InterfacePincharRaton {

	public void eliminarObjeto (int x, int y) throws ConnectionException, InvalidClientException, NoSuchChannelException, NoSuchClientException, NoSuchSessionException, PermissionDeniedException, TimedOutException;

}
