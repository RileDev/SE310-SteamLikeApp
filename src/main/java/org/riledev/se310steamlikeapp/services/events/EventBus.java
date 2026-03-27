package org.riledev.se310steamlikeapp.services.events;

import java.util.ArrayList;
import java.util.List;

/**
 * Centralna magistrala dogadjaja u sistemu (Singleton).
 * Implementira Observer obrazac dizajna i omogucava labavo
 * povezanim komponentama da komuniciraju putem dogadjaja.
 * <p>
 * Komponente se registruju kao posmatraci putem {@link #subscribe(Observer)}
 * metode, a dogadjaji se emituju putem {@link #publish(Event)} metode.
 *
 * @see Observer
 * @see Event
 */
public class EventBus {

    /** Jedina instanca EventBus klase (Singleton obrazac). */
    private static EventBus instance;

    /** Lista registrovanih posmatraca. */
    private final List<Observer> observers = new ArrayList<>();

    /**
     * Privatni konstruktor - sprecava direktno instanciranje.
     * Pristup instanci se vrsi iskljucivo putem {@link #getInstance()}.
     */
    private EventBus() {
    }

    /**
     * Vraca jedinu instancu EventBus klase.
     * Thread-safe implementacija putem synchronized kljucne reci.
     *
     * @return singleton instanca EventBus-a
     */
    public static synchronized EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    /**
     * Registruje novog posmatraca za primanje dogadjaja.
     * Sprecava dupliranje - ako posmatrac istog tipa vec postoji,
     * novi se nece dodati.
     *
     * @param newObserver posmatrac koji se registruje
     */
    public void subscribe(Observer newObserver) {
        // Provera da li posmatrac istog tipa vec postoji u listi
        for (Observer existingObserver : observers) {
            if (existingObserver.getClass().equals(newObserver.getClass())) {
                return;
            }
        }

        observers.add(newObserver);
    }

    /**
     * Emituje dogadjaj svim registrovanim posmatracima.
     * Svaki posmatrac prima dogadjaj putem svoje {@link Observer#update(Event)} metode.
     *
     * @param event dogadjaj koji se distribuira posmatracima
     */
    public void publish(Event event) {
        for (Observer o : observers) {
            o.update(event);
        }
    }
}
