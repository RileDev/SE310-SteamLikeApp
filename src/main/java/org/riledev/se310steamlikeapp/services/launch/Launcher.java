package org.riledev.se310steamlikeapp.services.launch;

import org.riledev.se310steamlikeapp.models.Game;

/**
 * Interfejs koji definise ugovor za pokretanje igara.
 * Svaka platforma (Windows, macOS, Linux) implementira ovaj interfejs
 * sa specificnom logikom pokretanja. Instanciranje se vrsi putem
 * {@link LauncherFactory} klase (Factory obrazac).
 *
 * @see LauncherFactory
 */
public interface Launcher {

    /**
     * Pokrece zadatu igru na odgovarajucoj platformi.
     *
     * @param game igra koja se pokrece
     */
    public void launch(Game game);
}
