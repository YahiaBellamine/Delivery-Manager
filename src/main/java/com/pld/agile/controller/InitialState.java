package com.pld.agile.controller;

import com.pld.agile.view.Window;

public class InitialState implements State {
    @Override
    public void loadMap(Controller c, Window w) {
        // TODO:
        // If successful
        c.setCurrentState(c.loadedMapState);
        // If not successful
        c.setCurrentState(c.initialState);
    }
}
