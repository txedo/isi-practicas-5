package presentacion;

/**
 * REFERENCIA : http://www.chuidiang.com/java/codigo_descargable/appletpaint.php
 */

/**
 * Interface para las clases encargadas de hacer algo cuando se arrastre el
 * ratón.
 */
public interface InterfaceArrastrarRaton
{
    /**
     * Crea un trazo nuevo y le pone como primer punto x,y.
     */ 
    public void comienzaDibujarTrazo(int x, int y);

    /**
     * Añade nuevos puntos al trazo mientras se arrastra el ratón 
     */
    public void añadirPuntosTrazo(int xAntigua, int yAntigua, int xNueva, int yNueva);

    /**
     * Se llama a este método cuando se termina de arrastrar el ratón.
     */
    public void finalizaArrastra(int x, int y);
}
