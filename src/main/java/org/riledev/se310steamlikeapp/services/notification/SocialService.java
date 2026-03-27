package org.riledev.se310steamlikeapp.services.notification;

import org.riledev.se310steamlikeapp.repositories.CommunityRepository;
import org.riledev.se310steamlikeapp.services.events.Event;
import org.riledev.se310steamlikeapp.services.events.Observer;
import org.riledev.se310steamlikeapp.services.events.PurchaseEvent;

/**
 * Servis koji automatski objavljuje post u zajednici nakon kupovine igre.
 * Implementira Observer interfejs i reaguje na PurchaseEvent
 * kreiranjem objave u ime korisnika putem CommunityRepository-ja.
 *
 * @see Observer
 * @see PurchaseEvent
 */
public class SocialService implements Observer {

    private final CommunityRepository communityRepository = new CommunityRepository();

    /**
     * Obradjuje primljeni dogadjaj.
     * Ako je u pitanju PurchaseEvent, automatski kreira post u zajednici.
     *
     * @param event primljeni dogadjaj iz EventBus-a
     */
    @Override
    public void update(Event event) {
        if (event instanceof PurchaseEvent) {
            PurchaseEvent purchase = (PurchaseEvent) event;

            // Automatski generisana poruka za community feed
            String message = "I just added " + purchase.getGame().getTitle() + " to my library!";
            communityRepository.createPost(purchase.getUser().getId(), message);
            System.out.println("SocialService: Auto-published community post.");
        }
    }
}
