package com.pld.agile.observer;

import java.util.ArrayList;
import java.util.Collection;

public class Observable {
    private Collection<Observer> observers;

    public Observable(){
        observers = new ArrayList<Observer>();
    }

    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) observers.add(observer);
    }

    public void notifyObservers(Object arg) {
        for (Observer o : observers)
            o.update(this, arg);
    }

    public void notifyObservers() {
        notifyObservers(null);
    }
}
