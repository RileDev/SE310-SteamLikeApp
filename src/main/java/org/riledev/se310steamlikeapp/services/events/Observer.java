package org.riledev.se310steamlikeapp.services.events;

/**
 * Interfejs za komponente koje oslusskuju dogadjaje u sistemu.
 * Implementira Observer obrazac dizajna - svaka klasa koja zeli
 * da prima obavestenja o dogadjajima mora implementirati ovaj interfejs
 * i registrovati se putem {@link EventBus#subscribe(Observer)}.
 *
 * @see EventBus
 * @see Event
 */
public interface Observer {

    /**
     * Metoda koja se poziva kada se emituje novi dogadjaj.
     *
     * @param event dogadjaj koji je emitovan putem EventBus-a
     */
    void update(Event event);
}
