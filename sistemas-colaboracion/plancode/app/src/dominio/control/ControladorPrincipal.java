package dominio.control;

import presentacion.IVentana;
import presentacion.JFPrincipal;

public class ControladorPrincipal {
	private IVentana ventanaPrincipal;
	
	public ControladorPrincipal () {
		ventanaPrincipal = new JFPrincipal(this);
	}

	public void mostrarVentanaPrincipal() {
		ventanaPrincipal.mostrarVentana();
	}
}
