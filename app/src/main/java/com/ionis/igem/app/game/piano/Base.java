package com.ionis.igem.app.game.piano;

import com.ionis.igem.app.game.AbstractGameActivity;
import com.ionis.igem.app.game.managers.ResMan;
import com.ionis.igem.app.game.model.WorldObject;
import org.andengine.entity.sprite.Sprite;

/**
 * Created by PLNech on 06/09/2015.
 */
public class Base extends WorldObject {
    public static final String TAG = "WorldObject";

    public static final float SCALE_DEFAULT = 0.08f; //TODO-OPTI: Scale upfront sprites instead of relying on this

    private final Sprite phosphate;

    public enum Type {
        A, T, G, C;

        public static Type random() {
            final Type[] values = Type.values();
            final int randomIndex = (int) (values.length * Math.random());
            return values[randomIndex];
        }

    }

    protected Type type;

    public Base(float pX, float pY, Type pType, boolean cpl, AbstractGameActivity activity) {
        this(pX, pY, pType, cpl, false, activity);
    }

    protected Base(float pX, float pY, Type pType, boolean cpl, boolean touchable, AbstractGameActivity activity) {
        super(pX, pY, touchable, SCALE_DEFAULT, activity.getTexture(chooseTextureName(pType, cpl)), activity.getVBOM());
        phosphate = new Sprite(pX - 0.5f * SCALE_DEFAULT * sprite.getWidth(), pY + sprite.getHeight() * SCALE_DEFAULT, activity.getTexture(ResMan.PIANO_L_PHO), activity.getVBOM());
        setScale(SCALE_DEFAULT);
        type = pType;
    }


    protected void setScale(float scale) {
        sprite.setScaleCenter(sprite.getScaleCenterX() * scale,
                sprite.getScaleCenterY() * scale); //TODO: Correct scaleCenter and position of all WorldObjects
        sprite.setScale(scale);
        phosphate.setScaleCenter(phosphate.getScaleCenterX() * scale,
                phosphate.getScaleCenterY() * scale);
        phosphate.setScale(scale);
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

    public Sprite getPhosphate() {
        return phosphate;
    }
}
