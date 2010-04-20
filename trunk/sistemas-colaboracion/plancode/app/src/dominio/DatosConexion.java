package dominio;

import java.io.Serializable;

public class DatosConexion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7427165557980228417L;
	private String ip;
	private int puerto;
	
	public DatosConexion() { }
	
	public DatosConexion(String ip, int puerto) {
		this.ip = ip;
		this.puerto = puerto;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPuerto() {
		return puerto;
	}

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
	
}
