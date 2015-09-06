package es.danirod.rectball.utils;

/**
 * This is the interface that gets notified when the Konami Code is pressed.
 * This interface should be passed to a KonamiCodeProcessor so that the
 * processor knows which interface notify when the code gets pressed.
 */
public interface KonamiCodeListener {

    void onKonamiCodePressed();

}
