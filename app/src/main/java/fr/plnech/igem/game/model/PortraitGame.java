package fr.plnech.igem.game.model;

import fr.plnech.igem.game.AbstractGameActivity;
import fr.plnech.igem.game.PortraitGameActivity;

/**
 * Created by PLNech on 22/09/2015.
 */
public abstract class PortraitGame extends BaseGame {
    protected PortraitGame(AbstractGameActivity pActivity) {
        super(pActivity);
    }

    @Override
    public int getWidth() {
        return PortraitGameActivity.CAMERA_WIDTH;
    }

    @Override
    public int getHeight() {
        return PortraitGameActivity.CAMERA_HEIGHT;
    }

    @Override
    public boolean isPortrait() {
        return true;
    }
}
