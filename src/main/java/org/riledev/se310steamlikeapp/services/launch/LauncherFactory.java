package org.riledev.se310steamlikeapp.services.launch;

/**
 * Fabrika za kreiranje odgovarajuceg Launcher-a u zavisnosti od operativnog sistema.
 * Implementira Factory Method obrazac dizajna - izoluje pozivajuci kod od
 * direktnog instanciranja konkretnih Launcher implementacija.
 *
 * @see Launcher
 */
public class LauncherFactory {

    /**
     * Odredjuje operativni sistem i vraca odgovarajuci Launcher.
     *
     * @return instanca platformski-specificnog Launcher-a, ili null za nepodrzane sisteme
     */
    public static Launcher getLauncher() {
        String os = System.getProperty("os.name").toLowerCase();

        // Detekcija OS-a na osnovu naziva sistema
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
