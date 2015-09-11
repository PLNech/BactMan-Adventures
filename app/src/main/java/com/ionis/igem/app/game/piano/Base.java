package com.ionis.igem.app.game.piano;

import android.util.Log;
import com.ionis.igem.app.game.AbstractGameActivity;
import com.ionis.igem.app.game.managers.ResMan;
import com.ionis.igem.app.game.model.WorldObject;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

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
        final ITiledTextureRegion texture = activity.getTexture(ResMan.PIANO_L_PHO);
        final float phosphateX;
        final float phosphateY;
        if (cpl) {
            phosphateX = pX;
            phosphateY = pY - texture.getHeight() * SCALE_DEFAULT;
        } else {
            phosphateX = pX - 0.5f * sprite.getWidth() * SCALE_DEFAULT;
            phosphateY = pY + sprite.getHeight() * SCALE_DEFAULT;
        }

        phosphate = new Sprite(phosphateX, phosphateY, texture, activity.getVBOM());
        setScale(SCALE_DEFAULT);
        phosphate.setRotationCenter(phosphate.getX() / 2, phosphate.getY() / 2);
        if (cpl) { phosphate.setRotation(180);}
        type = pType;
        Log.d(TAG, "Base " + (cpl? "cpl" : "") + ": phosphate asked at " + phosphateX + ", " + phosphateY
                + ", is at " + phosphate.getX() + ", " + phosphate.getY()
                + ", w:" + phosphate.getWidth() + ", s:" + phosphate.getScaleX());
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
