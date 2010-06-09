package comunicaciones.EventosCanales;

import java.util.EventObject;

import dominio.conocimiento.InfoTrazo;

public class MensajeTrazoEvent  extends EventObject {

		private static final long serialVersionUID = -8922139367513391169L;
		private String nombreCliente;
		private InfoTrazo info;
		
		public MensajeTrazoEvent(Object obj, String cliente, Object info) {
			super(obj);
			this.nombreCliente = cliente;
			this.info = (InfoTrazo)info;
		}

		public InfoTrazo getInfo() {
			return info;
		}

		public String getNombreCliente() {
			return nombreCliente;
		}	

}
