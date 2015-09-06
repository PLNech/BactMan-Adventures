package com.ionis.igem.app.game.piano;

import com.ionis.igem.app.game.AbstractGameActivity;
import com.ionis.igem.app.game.managers.ResMan;
import com.ionis.igem.app.game.model.WorldObject;

/**
 * Created by PLNech on 06/09/2015.
 */
public class Base extends WorldObject {
    public enum Type {
        A, T, G, C;
    }

    public Base(float pX, float pY, Type type, boolean cpl, AbstractGameActivity activity) {
        super(pX, pY, false, SCALE_DEFAULT, null, activity.getTexture(chooseTextureName(type, cpl)), activity.getVBOM());
    }

    private static String chooseTextureName(Type type, boolean cpl) {
        String retStr = "";
        switch (type) {
            case A:
                retStr = ResMan.PIANO_A;
                break;
            case T:
                retStr = ResMan.PIANO_T;
                break;
            case G:
                retStr = ResMan.PIANO_G;
                break;
            case C:
                retStr = ResMan.PIANO_C;
                break;
        }
        if (cpl) retStr = retStr.replace(".png", "_cpl.png");
        return retStr;
    }
}
