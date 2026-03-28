package org.riledev.se310steamlikeapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.riledev.se310steamlikeapp.util.InputValidator;
import org.riledev.se310steamlikeapp.services.session.SessionManager;
import org.riledev.se310steamlikeapp.services.launch.LauncherFactory;
import org.riledev.se310steamlikeapp.services.launch.Launcher;
import org.riledev.se310steamlikeapp.services.launch.LinuxLauncher;

/**
 * Jedinicni (Unit) testovi u skladu sa poglavljem 7 dokumentacije.
 *
 * Svaki test proverava izolovanu funkcionalnost jedne klase bez zavisnosti
 * od baze podataka ili drugih modula sistema.
 *
 * Obuhvaceni test slucajevi:
 * - TS-01: Validacija praznih polja kod prijave (InputValidator)
 * - TS-02: Pripadnost singletona instanci sesije (SessionManager)
 * - TS-03: Fabrika OS klijenta na Linuxu (LauncherFactory)
 */
public class SteamTests {

    /** Originalna vrednost sistemskog property-ja os.name pre izmene u testu. */
    private String originalOs;

    /**
     * Cuva originalnu vrednost os.name pre pokretanja testa.
     * Neophodno jer TS-03 menja sistemski property radi simulacije Linux okruzenja.
     */
    @Before
    public void setUp() {
        originalOs = System.getProperty("os.name");
    }

    /**
     * Vraca os.name na originalnu vrednost nakon testa.
     * Sprecava da izmena iz TS-03 utice na ostatak sistema ili naredne testove.
     */
    @After
    public void tearDown() {
        if (originalOs != null) {
            System.setProperty("os.name", originalOs);
        }
    }

    /**
     * TS-01: Validacija praznih polja kod prijave.
     * Proverava da InputValidator ispravno detektuje prazna polja
     * za username i password. Parametar confirmPassword je null
     * jer se radi o prijavi, ne registraciji.
     */
    @Test
    public void testAuthFieldEmptyValidation() {
        // Simulacija praznih polja za prijavu (confirmPassword = null jer je login)
        boolean result = InputValidator.isAuthFieldEmpty("", "", null);
        assertTrue("InputValidator treba da vrati true za prazna polja", result);
    }

    /**
     * TS-02: Pripadnost singletona instanci sesije.
     * Proverava da dva uzastopna poziva getInstance() vracaju
     * identican objekat u memoriji, cime se potvrdjuje ispravnost
     * Singleton dizajn obrasca.
     */
    @Test
    public void testSessionManagerSingletonInstance() {
        // Dva nezavisna poziva ka Singleton getInstance() metodi
        SessionManager instance1 = SessionManager.getInstance();
        SessionManager instance2 = SessionManager.getInstance();

        // Instanca ne sme biti null
        assertNotNull("SessionManager instanca ne sme biti null", instance1);

        // assertSame proverava da li obe reference ukazuju na ISTI objekat (==)
        assertSame("Obe instance moraju ukazivati na isti objekat u memoriji", instance1, instance2);
    }

    /**
     * TS-03: Fabrika OS klijenta na Linuxu.
     * Postavlja sistemski property os.name na "Linux" i proverava
     * da LauncherFactory vraca instancu tipa LinuxLauncher.
     * Originalna vrednost os.name se vraca u tearDown() metodi.
     */
    @Test
    public void testLauncherFactoryForLinux() {
        // Privremena izmena sistemskog property-ja radi simulacije Linux OS-a
        System.setProperty("os.name", "Linux");

        // Poziv fabricke metode koja analizira os.name i kreira odgovarajuci Launcher
        Launcher launcher = LauncherFactory.getLauncher();

        // Launcher ne sme biti null (OS je prepoznat)
        assertNotNull("Launcher ne sme biti null za poznat OS", launcher);

        // Provera da je vracena konkretna Linux implementacija
        assertTrue("Launcher mora biti instanca LinuxLauncher klase", launcher instanceof LinuxLauncher);
    }
}
