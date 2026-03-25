package org.riledev.se310steamlikeapp.services.events;

import java.util.ArrayList;
import java.util.List;

public class EventBus {
    private static EventBus instance;
    private final List<Observer> observers = new ArrayList<>();

    private EventBus(){}

    public static synchronized EventBus getInstance(){
        if(instance == null){
            instance = new EventBus();
        }
        return instance;
    }

    public void subscribe(Observer newObserver){
        for (Observer existingObserver : observers) {
            if (existingObserver.getClass().equals(newObserver.getClass())) {
                return;
            }
        }

        observers.add(newObserver);
    }

    public void publish(Event event){
        for (Observer o : observers){
            o.update(event);
        }
    }
}
