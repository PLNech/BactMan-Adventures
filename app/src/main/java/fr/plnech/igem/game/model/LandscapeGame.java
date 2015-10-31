package fr.plnech.igem.game.model;

import fr.plnech.igem.game.AbstractGameActivity;
import fr.plnech.igem.game.LandscapeGameActivity;

/**
 * Created by PLNech on 22/09/2015.
 */
public abstract class LandscapeGame extends BaseGame {
    protected LandscapeGame(AbstractGameActivity pActivity) {
        super(pActivity);
    }

    @Override
    public int getWidth() {
        return LandscapeGameActivity.CAMERA_WIDTH;
    }

    @Override
    public int getHeight() {
        return LandscapeGameActivity.CAMERA_HEIGHT;
    }

    @Override
    public boolean isPortrait() {
        return false;
    }
}
