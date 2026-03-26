import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.riledev.se310steamlikeapp.util.InputValidator;
import org.riledev.se310steamlikeapp.services.session.SessionManager;
import org.riledev.se310steamlikeapp.services.launch.LauncherFactory;
import org.riledev.se310steamlikeapp.services.launch.Launcher;
import org.riledev.se310steamlikeapp.services.launch.LinuxLauncher;

public class SteamTests {

    private String originalOs;

    @Before
    public void setUp() {
        originalOs = System.getProperty("os.name");
    }

    @After
    public void tearDown() {
        if (originalOs != null) {
            System.setProperty("os.name", originalOs);
        }
    }

    @Test
    public void testAuthFieldEmptyValidation() {
        boolean result = InputValidator.isAuthFieldEmpty("", "", null);
        assertTrue("InputValidator should return true for empty fields", result);
    }

    @Test
    public void testSessionManagerSingletonInstance() {
        SessionManager instance1 = SessionManager.getInstance();
        SessionManager instance2 = SessionManager.getInstance();

        assertNotNull("SessionManager instance should not be null", instance1);
        assertSame("Both SessionManager instances should point to the same memory reference", instance1, instance2);
    }

    @Test
    public void testLauncherFactoryForLinux() {
        System.setProperty("os.name", "Linux");

        Launcher launcher = LauncherFactory.getLauncher();

        assertNotNull("Launcher should not be null", launcher);
        assertTrue("Launcher should be an instance of LinuxLauncher", launcher instanceof LinuxLauncher);
    }
}
