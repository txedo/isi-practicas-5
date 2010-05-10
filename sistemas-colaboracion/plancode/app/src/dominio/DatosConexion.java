package dominio;

import java.io.Serializable;

public class DatosConexion implements Serializable {

	private static final long serialVersionUID = 7427165557980228417L;
	private String host;
	private int puerto;
	
	public DatosConexion(String ip, int puerto) {
		this.host = ip;
		this.puerto = puerto;
	}

	public String getIp() {
		return host;
	}

	public void setIp(String ip) {
		this.host = ip;
	}

	public int getPuerto() {
		return puerto;
	}

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
	
}
