package org.riledev.se310steamlikeapp.services.launch;

import org.riledev.se310steamlikeapp.models.Game;

public class OSXLauncher implements Launcher {
    @Override
    public void launch(Game game) {
        System.out.println("Launching game on macOS: " + game.getTitle());
    }
}
