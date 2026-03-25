package org.riledev.se310steamlikeapp.services.events;

import org.riledev.se310steamlikeapp.models.Game;
import org.riledev.se310steamlikeapp.models.User;

public class PurchaseEvent implements Event {
    private final User user;
    private final Game game;

    public PurchaseEvent(User user, Game game){
        this.user = user;
        this.game = game;
    }

    public User getUser() { return user; }
    public Game getGame() { return game; }
}
