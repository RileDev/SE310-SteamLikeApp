package org.riledev.se310steamlikeapp.services.launch;

import org.riledev.se310steamlikeapp.models.Game;

/**
 * Launcher implementacija za macOS operativni sistem.
 *
 * @see Launcher
 * @see LauncherFactory
 */
public class OSXLauncher implements Launcher {

    /** {@inheritDoc} */
    @Override
    public void launch(Game game) {
        System.out.println("Launching game on macOS: " + game.getTitle());
    }
}
