package org.riledev.se310steamlikeapp.services.launch;

import org.riledev.se310steamlikeapp.models.Game;

/**
 * Launcher implementacija za Linux operativni sistem.
 *
 * @see Launcher
 * @see LauncherFactory
 */
public class LinuxLauncher implements Launcher {

    /** {@inheritDoc} */
    @Override
    public void launch(Game game) {
        System.out.println("Launching game on Linux: " + game.getTitle());
    }
}
