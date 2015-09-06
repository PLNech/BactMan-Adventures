package com.ionis.igem.app.game.piano;

import com.ionis.igem.app.game.AbstractGameActivity;
import com.ionis.igem.app.game.managers.ResMan;
import com.ionis.igem.app.game.model.WorldObject;

/**
 * Created by PLNech on 06/09/2015.
 */
public class Polymerase extends WorldObject {

    public Polymerase(float pX, float pY, AbstractGameActivity activity) {
        super(pX, pY, false, SCALE_DEFAULT, null, activity.getTexture(ResMan.PIANO_POLY), activity.getVBOM());
        sprite.animate(100);
    }
}
