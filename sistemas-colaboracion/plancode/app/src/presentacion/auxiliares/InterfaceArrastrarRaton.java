package presentacion.auxiliares;

import com.sun.media.jsdt.ConnectionException;
import com.sun.media.jsdt.InvalidClientException;
import com.sun.media.jsdt.NoSuchChannelException;
import com.sun.media.jsdt.NoSuchClientException;
import com.sun.media.jsdt.NoSuchSessionException;
import com.sun.media.jsdt.PermissionDeniedException;
import com.sun.media.jsdt.TimedOutException;

/**
 * REFERENCIA : http://www.chuidiang.com/java/codigo_descargable/appletpaint.php
 */

/**
 * Interface para las clases encargadas de hacer algo cuando se arrastre el
 * rat�n.
 */
public interface InterfaceArrastrarRaton
{
    /**
     * Crea un trazo nuevo y le pone como primer punto x,y.
     */ 
    public void comienzaDibujarTrazo(int x, int y) throws ConnectionException, InvalidClientException, NoSuchChannelException, NoSuchClientException, NoSuchSessionException, PermissionDeniedException, TimedOutException;

    /**
     * A�ade nuevos puntos al trazo mientras se arrastra el rat�n 
     */
    public void a�adirPuntosTrazo(int xAntigua, int yAntigua, int xNueva, int yNueva) throws ConnectionException, InvalidClientException, NoSuchChannelException, NoSuchClientException, NoSuchSessionException, PermissionDeniedException, TimedOutException;

    /**
     * Se llama a este m�todo cuando se termina de arrastrar el rat�n.
     * @throws TimedOutException 
     * @throws PermissionDeniedException 
     * @throws NoSuchSessionException 
     * @throws NoSuchClientException 
     * @throws NoSuchChannelException 
     * @throws InvalidClientException 
     * @throws ConnectionException 
     */
    public void finalizaArrastra(int x, int y) throws ConnectionException, InvalidClientException, NoSuchChannelException, NoSuchClientException, NoSuchSessionException, PermissionDeniedException, TimedOutException;
}
