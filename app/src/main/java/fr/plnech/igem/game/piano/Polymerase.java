package fr.plnech.igem.game.piano;

import fr.plnech.igem.game.AbstractGameActivity;
import fr.plnech.igem.game.managers.ResMan;
import fr.plnech.igem.game.model.WorldObject;

/**
 * Created by PLNech on 06/09/2015.
 */
public class Polymerase extends WorldObject {

    public Polymerase(float pX, float pY, AbstractGameActivity activity) {
        super(pX, pY, false, SCALE_DEFAULT, activity.getTexture(ResMan.PIANO_POLY), activity.getVBOM());
        sprite.setScaleCenter(sprite.getScaleCenterX() * SCALE_DEFAULT,
                sprite.getScaleCenterY() * SCALE_DEFAULT); //TODO: Correct scaleCenter and position of all WorldObjects
        sprite.animate(100);
    }
}
