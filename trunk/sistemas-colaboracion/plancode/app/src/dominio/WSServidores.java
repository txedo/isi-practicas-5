package dominio;

import java.util.ArrayList;

public class WSServidores {

	  private ArrayList<DatosConexion> servidores = new ArrayList<DatosConexion>() ;
      
      public ArrayList<DatosConexion> getServidores() {
              return servidores;
      }
      
      public void anadirServidor(DatosConexion d) {
              servidores.add(d);
      }
      
      public void eliminarServidor(DatosConexion d) {
              servidores.remove(d);
      }

}
