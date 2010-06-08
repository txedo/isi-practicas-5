package presentacion.auxiliares;

import java.util.regex.Pattern;


import excepciones.CadenaIncorrectaException;
import excepciones.CadenaVaciaException;
import excepciones.IPInvalidaException;
import excepciones.PuertoInvalidoException;


/**
 * Clase estática que contiene métodos para comprobar la validez
 * de los campos de las ventanas.
 */
public class Validacion {

	public static final int MAX_LONGITUD_CAMPOS = 255;
	public static final int PUERTO_MINIMO = 1025;
	public static final int PUERTO_MAXIMO = 65535;
	

	// Una cadena es válida si todos sus caracteres son alfabéticos, espacios o guiones
	public static void comprobarCadena(String cadena) throws CadenaIncorrectaException, CadenaVaciaException {
		boolean bCorrecto = false;
		boolean bAux = true;

		if(cadena.length() > MAX_LONGITUD_CAMPOS) {
			throw new CadenaIncorrectaException();
		}
		
		// El primer caracter debe ser una letra
		if(cadena.length() > 0) {
			if(Character.isLetter(cadena.charAt(0))) {
				bAux = !cadena.contains("--");
				// El resto de caracteres pueden ser letra, espacio o guion (no se permiten guiones juntos, como --)
				for(int i = 1; i < cadena.length() && bAux; i++) {
					bAux = Character.isLetter(cadena.charAt(i)) || Character.isWhitespace(cadena.charAt(i)) || cadena.charAt(i) == '-';
				}
				bCorrecto = bAux;
				
				// No se puede terminar la cadena con un guión ni con espacios
				if ((bCorrecto && cadena.charAt(cadena.length()-1) == '-') || bCorrecto && Character.isWhitespace(cadena.charAt(cadena.length()-1)))
					bCorrecto = false;
				
			}
			if(!bCorrecto) {
				throw new CadenaIncorrectaException();
			}
		} else {
			throw new CadenaVaciaException();
		}
	}
	
	public static void comprobarDireccionIP(String ip) throws IPInvalidaException {
		Pattern patronIP;
		
		// Creamos un patrón que define las IPs válidas
		patronIP = Pattern.compile("\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." + 
				"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b");
		
		if(ip.equals("")) {
			throw new IPInvalidaException("La dirección IP no puede ser nula.");
		} else if(!patronIP.matcher(ip).matches()) {
			throw new IPInvalidaException();
		}
	}
	
	public static void comprobarPuerto(String puerto) throws PuertoInvalidoException {
		int numPuerto;
	
		if(puerto.equals("")) {
			throw new PuertoInvalidoException("El puerto no puede ser nulo.");
		} else {
			try {
				numPuerto = Integer.parseInt(puerto);
				if(numPuerto < PUERTO_MINIMO || numPuerto > PUERTO_MAXIMO) {
					throw new PuertoInvalidoException("El puerto debe ser un número comprendido entre " + PUERTO_MINIMO + " y " + PUERTO_MAXIMO + ".");
				}
			} catch(NumberFormatException ex) {
				throw new PuertoInvalidoException();
			}
		}
	}
}
