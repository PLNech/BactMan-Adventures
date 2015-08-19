package com.ionis.igem.app.game;

/**
 * Created by PLN on 14/08/2015.
 */
public class Game {

    private final String name;
    private final Class<? extends BaseGameActivity> gameActivity;

    private boolean isAvailable;

    private int index;

    public Game(String name, Class<? extends BaseGameActivity> gameActivity) {
        this.name = name;
        this.gameActivity = gameActivity;
    }

    public String getName() {
        return name;
    }


    public Class<? extends BaseGameActivity> getActivity() {
        return gameActivity;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public Game setAvailability(boolean isAvailable) {
        this.isAvailable = isAvailable;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public Game setIndex(int index) {
        this.index = index;
        return this;
    }
}
