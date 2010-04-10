package dominio;

public class DatosConexion {

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
