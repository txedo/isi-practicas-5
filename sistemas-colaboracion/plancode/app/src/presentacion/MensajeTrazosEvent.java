package presentacion;

import java.util.EventObject;
import java.util.LinkedList;

public class MensajeTrazosEvent  extends EventObject {

		private static final long serialVersionUID = -8922139367513391169L;
		private LinkedList<Trazo> listaTrazos;
		
		public MensajeTrazosEvent(Object obj, Object listaTrazos) {
			super(obj);
			this.listaTrazos = (LinkedList<Trazo>) listaTrazos;		
		}

		public LinkedList<Trazo> getListaTrazos() {
			return listaTrazos;
		}

}
