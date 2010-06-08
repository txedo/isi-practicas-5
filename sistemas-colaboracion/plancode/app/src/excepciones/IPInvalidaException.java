package excepciones;

/**
 * Excepci�n lanzada al introducir una direcci�n IP con un formato
 * inv�lido o fuera del rango 0.0.0.0 - 255.255.255.255.
 */
public class IPInvalidaException extends Exception {

	private static final long serialVersionUID = 4987479645945470008L;

	public IPInvalidaException () {
		super("El formato de la direcci�n IP no es v�lido.");
	}
	
	public IPInvalidaException (String message) {
		super(message);
	}
}
