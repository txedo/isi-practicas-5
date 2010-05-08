package dominio;

import java.io.Serializable;

import com.sun.media.jsdt.URLString;
import comunicaciones.ISesion;


public class DatosConexion implements Serializable, ISesion {

	private static final long serialVersionUID = 7427165557980228417L;
	private String host;
	private int puerto;
	private String sesion;
	private String tipoSesion;
	
	public DatosConexion(String ip, int puerto) {
		this.host = ip;
		this.puerto = puerto;
		this.sesion = SESION;
		this.tipoSesion = SOCKET;
	}
	
	public String getTipoSesion() {
		return tipoSesion;
	}

	public void setTipoSesion(String tipo) {
		this.tipoSesion = tipo;
	}

	public String getSesion() {
		return sesion;
	}

	public void setSesion(String sesion) {
		this.sesion = sesion;
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
