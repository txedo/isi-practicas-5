package dominio.conocimiento;

public class Usuario {
	private String nick;
	private String rol;

	public Usuario() {
		super();
	}

	public Usuario(String nick, String rol) {
		super();
		this.nick = nick;
		this.rol = rol;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}
}
