package org.riledev.se310steamlikeapp.services.notification;

import org.riledev.se310steamlikeapp.repositories.CommunityRepository;
import org.riledev.se310steamlikeapp.services.events.Event;
import org.riledev.se310steamlikeapp.services.events.Observer;
import org.riledev.se310steamlikeapp.services.events.PurchaseEvent;

public class SocialService implements Observer {
    private final CommunityRepository communityRepository = new CommunityRepository();

    @Override
    public void update(Event event) {
        if(event instanceof PurchaseEvent){
            PurchaseEvent purchase = (PurchaseEvent) event;
            String message = "I just added " + purchase.getGame().getTitle() + " to my library!";
            communityRepository.createPost(purchase.getUser().getId(), message);
            System.out.println("SocialService: Auto-published community post.");
        }
    }
}
