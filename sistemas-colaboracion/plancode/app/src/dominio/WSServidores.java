package dominio;

import java.util.ArrayList;
import java.util.List;

public class WSServidores {

	  private List<DatosConexion> servidores = new ArrayList<DatosConexion>();
	  private int nServidores = 0;
      
      public DatosConexion getServidores() {
    	  return servidores.get(0);
      }
      
      public int anadirServidor(DatosConexion d) {
              servidores.add(d);
              nServidores++;
              return nServidores;
      }
      
      /*public void eliminarServidor(DatosConexion d) {
              servidores.remove(d);
      }*/

}
