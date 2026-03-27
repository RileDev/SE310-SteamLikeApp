package org.riledev.se310steamlikeapp.services.launch;

import org.riledev.se310steamlikeapp.models.Game;

/**
 * Launcher implementacija za Windows operativni sistem.
 *
 * @see Launcher
 * @see LauncherFactory
 */
public class WindowsLauncher implements Launcher {

    /** {@inheritDoc} */
    @Override
    public void launch(Game game) {
        System.out.println("Launching game on Windows: " + game.getTitle());
    }
}
