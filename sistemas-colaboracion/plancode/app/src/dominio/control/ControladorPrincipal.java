package dominio.control;

import dominio.DatosConexion;
import dominio.conocimiento.Usuario;
import presentacion.IVentana;
import presentacion.JFLogin;
import presentacion.JFPrincipal;

public class ControladorPrincipal {
	private IVentana ventanaLogin;
	private IVentana ventanaPrincipal;
	
	public ControladorPrincipal () {
		ventanaLogin = new JFLogin(this);
		//ventanaPrincipal = new JFPrincipal(this);
	}
	
	public void mostrarVentanaLogin() {
		ventanaLogin.mostrarVentana();
	}

	public void mostrarVentanaPrincipal() {
		ventanaPrincipal.mostrarVentana();
	}

	public void iniciarSesion(DatosConexion con, Usuario usuario) {
		if (con == null) {
			// el cliente hace de servidor
		}
		else {
			// el cliente se conecta a un servidor
		}
	}
}
