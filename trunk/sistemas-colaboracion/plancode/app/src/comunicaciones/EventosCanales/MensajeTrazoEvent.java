package comunicaciones.EventosCanales;

import java.util.EventObject;
import java.util.LinkedList;

import dominio.conocimiento.InfoTrazo;

public class MensajeTrazoEvent  extends EventObject {

		private static final long serialVersionUID = -8922139367513391169L;
		private InfoTrazo info;
		
		public MensajeTrazoEvent(Object obj, Object info) {
			super(obj);
			this.info = (InfoTrazo)info;
		}

		public InfoTrazo getInfo() {
			return info;
		}	

}
