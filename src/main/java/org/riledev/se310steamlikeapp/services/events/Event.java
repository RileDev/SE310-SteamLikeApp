package org.riledev.se310steamlikeapp.services.events;

/**
 * Marker interfejs koji predstavlja dogadjaj u sistemu.
 * Sve konkretne klase dogadjaja (npr. {@link PurchaseEvent}) moraju
 * implementirati ovaj interfejs kako bi mogle biti distribuirane
 * putem {@link EventBus} mehanizma.
 *
 * @see EventBus
 * @see Observer
 */
public interface Event {
}
