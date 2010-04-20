package dominio;

import java.util.ArrayList;

public class WSServidores {

	  private ArrayList<DatosConexion> servidores = new ArrayList<DatosConexion>();
	  private int nServidores = 0;
      
      public ArrayList<DatosConexion> getServidores() {
    	  return servidores;
      }
      
      public int anadirServidor(DatosConexion d) {
              servidores.add(d);
              nServidores++;
              return nServidores;
      }
      
      public void eliminarServidor(DatosConexion d) {
              servidores.remove(d);
      }

}
