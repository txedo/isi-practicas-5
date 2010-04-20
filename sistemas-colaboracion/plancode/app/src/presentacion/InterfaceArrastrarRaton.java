package presentacion;

/**
 * REFERENCIA : http://www.chuidiang.com/java/codigo_descargable/appletpaint.php
 */

/**
 * Interface para las clases encargadas de hacer algo cuando se arrastre el
 * rat�n.
 */
public interface InterfaceArrastrarRaton
{
    /**
     * Crea un trazo nuevo y le pone como primer punto x,y.
     */ 
    public void comienzaDibujarTrazo(int x, int y);

    /**
     * A�ade nuevos puntos al trazo mientras se arrastra el rat�n 
     */
    public void a�adirPuntosTrazo(int xAntigua, int yAntigua, int xNueva, int yNueva);

    /**
     * Se llama a este m�todo cuando se termina de arrastrar el rat�n.
     */
    public void finalizaArrastra(int x, int y);
}
