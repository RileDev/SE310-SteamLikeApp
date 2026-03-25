package org.riledev.se310steamlikeapp.services.launch;

public class LauncherFactory {

    public static Launcher getLauncher(){
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            return new WindowsLauncher();
        } else if (os.contains("mac")) {
            return new OSXLauncher();
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            return new LinuxLauncher();
        } else {
            System.err.println("Unsupported Operating System: " + os);
            return null;
        }
    }
}
