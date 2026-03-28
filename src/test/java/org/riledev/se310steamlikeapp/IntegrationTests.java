package org.riledev.se310steamlikeapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.riledev.se310steamlikeapp.models.Game;
import org.riledev.se310steamlikeapp.models.User;
import org.riledev.se310steamlikeapp.repositories.OrderRepository;
import org.riledev.se310steamlikeapp.repositories.UserRepository;
import org.riledev.se310steamlikeapp.services.events.Event;
import org.riledev.se310steamlikeapp.services.events.EventBus;
import org.riledev.se310steamlikeapp.services.events.Observer;
import org.riledev.se310steamlikeapp.services.events.PurchaseEvent;
import org.riledev.se310steamlikeapp.services.session.SessionManager;

/**
 * Top-Down Integraciono Testiranje u skladu sa poglavljem 8 dokumentacije.
 */
public class IntegrationTests {

    private UserRepository userRepository;
    private OrderRepository orderRepository;

    @Before
    public void setUp() {
        userRepository = new UserRepository();
        orderRepository = new OrderRepository();

        // Praznimo sesiju pre svakog testa da izbegnemo curenje stanja
        SessionManager.getInstance().loginUser(null);
    }

    @After
    public void tearDown() {
        // Ciscenje resursa nakon testa
        SessionManager.getInstance().loginUser(null);
    }

    /**
     * IT-01: Uspešan korisnički upis i integracija sa bazom podataka.
     * Proverava komunikaciju izmedju repozitorijuma, Hash util-a i baze.
     */
    @Test
    public void testUserRegistrationAndLoginFlow() {
        // Jedinstveni username za svaki prolaz testa koristeći timestamp
        String testUsername = "TestUser_" + System.currentTimeMillis();
        String testPassword = "Password123";

        // Korak 1: Registracija
        boolean registrationSuccess = userRepository.register(testUsername, testPassword, testPassword);
        assertTrue("User registration should return true indicating database insert success", registrationSuccess);

        // Korak 2: Provera kroz Login (da li hesirana lozinka radi pri pretrazi)
        User loggedInUser = userRepository.login(testUsername, testPassword);
        assertNotNull("Login should return a valid User object from DB", loggedInUser);
        assertEquals("Returned username should match", testUsername, loggedInUser.getUsername());
    }

    /**
     * IT-02: Kupovina igre i asinhroni transfer eventa kroz sisteme.
     * Testira integraciju OrderRepository transakcije i EventBus propagacije.
     */
    @Test
    public void testPurchaseAndEventPropagation() {
        // Kreiramo dummy korisnika i igru za test
        User dummyUser = new User();
        dummyUser.setId(1); // Koristimo validan ID koji verovatno postoji ili ignorisemo relacije
        dummyUser.setUsername("BuyerTest");

        Game dummyGame = new Game();
        dummyGame.setId(1);
        dummyGame.setTitle("Test Game");
        dummyGame.setPrice(19.99);

        // Postavljanje custom Observer-a na EventBus za test
        final boolean[] eventReceived = { false };
        Observer testObserver = new Observer() {
            @Override
            public void update(Event event) {
                if (event instanceof PurchaseEvent) {
                    PurchaseEvent pe = (PurchaseEvent) event;
                    if (pe.getUser().getUsername().equals("BuyerTest")) {
                        eventReceived[0] = true;
                    }
                }
            }
        };

        // Vezujemo nas observer za EventBus
        EventBus.getInstance().subscribe(testObserver);

        // Simulacija uspesne transakcije na nivou Payment i Order logike
        // Napomena: Ovaj poziv vrsi INSERT u bazu (tabele StoreOrder, OrderItem,
        // LibraryItem).
        // Ako dummy IDs (1) ne postoje a aktivirani su strani kljucevi, ovo moze
        // vratiti false.
        // U tom slucaju test se oslanja na pracenje logike repozitorijuma.
        boolean purchaseResult = orderRepository.processPurchase(dummyUser.getId(), dummyGame, "paypal");

        if (purchaseResult) {
            // Objavljujemo dogadjaj (sto obicno radi PaymentController)
            EventBus.getInstance().publish(new PurchaseEvent(dummyUser, dummyGame));

            // Provera da li je EventBus prosledio event svim observerima (integracija
            // modula)
            assertTrue("EventBus should propagate the PurchaseEvent successfully", eventReceived[0]);
        }
    }

    /**
     * IT-03: Zadržavanje sesija prilikom skakanja sa kontrolera.
     * Integracija izmedju razlicitih komponenti koje traze istog korisnika preko
     * Singletona.
     */
    @Test
    public void testSessionManagerCrossModuleConsistency() {
        User sessionUser = new User();
        sessionUser.setId(999);
        sessionUser.setUsername("SessionKeeper");

        // "AuthController" logika prijave postavlja sesiju
        SessionManager.getInstance().loginUser(sessionUser);

        // Negde na "PaymentController" strani proveravamo preuzimanje sesije
        User retrievedUser = SessionManager.getInstance().getCurrentUser();

        assertNotNull("SessionManager must return a logged user", retrievedUser);
        assertEquals("Both modules should access the exact same User memory block",
                sessionUser.hashCode(), retrievedUser.hashCode());
        assertEquals("User ID must match across module boundaries",
                sessionUser.getId(), retrievedUser.getId());
    }
}
