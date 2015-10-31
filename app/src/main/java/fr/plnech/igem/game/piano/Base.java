package fr.plnech.igem.game.piano;

import fr.plnech.igem.game.AbstractGameActivity;
import fr.plnech.igem.game.managers.ResMan;
import fr.plnech.igem.game.model.WorldObject;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

/**
 * Created by PLNech on 06/09/2015.
 */
public class Base extends WorldObject {
    static final String TAG = "WorldObject";

    private static final float SCALE_DEFAULT = 0.08f; //TODO-OPTI: Scale upfront sprites instead of relying on this

    private final Sprite phosphate;

    public enum Type {
        A, T, G, C;

        public static Type random() {
            final Type[] values = Type.values();
            final int randomIndex = (int) (values.length * Math.random());
            return values[randomIndex];
        }

    }

    final Type type;

    public Base(float pX, float pY, Type pType, boolean cpl, AbstractGameActivity activity) {
        this(pX, pY, pType, cpl, false, activity);
    }

    Base(float pX, float pY, Type pType, boolean cpl, boolean touchable, AbstractGameActivity activity) {
        super(pX, pY, touchable, SCALE_DEFAULT, activity.getTexture(chooseTextureName(pType, cpl)), activity.getVBOM());
        final ITiledTextureRegion texture = activity.getTexture(cpl ? ResMan.PIANO_L_PHO_CPL : ResMan.PIANO_L_PHO);
        final float phosphateX = pX - 0.45f * sprite.getWidth() * SCALE_DEFAULT;
        final float phosphateY;
        if (cpl) {
            phosphateY = pY - 0.1f * texture.getHeight() * SCALE_DEFAULT;
        } else {
            phosphateY = pY + sprite.getHeight() * SCALE_DEFAULT;
        }

        phosphate = new Sprite(phosphateX, phosphateY, texture, activity.getVBOM());
        type = pType;

        setScale(SCALE_DEFAULT);
    }

    private void setScale(float scale) {
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

    public Type getType() {return type;}

    public Type getCplType() {
        return getCplType(type);
    }

    private static Type getCplType(Type type) {
        switch (type) {
            case A:
                return Type.T;
            case T:
                return Type.A;
            case G:
                return Type.C;
            case C:
                return Type.G;
            default:
                throw new IllegalStateException("No default");
        }
    }

    public Sprite getPhosphate() {
        return phosphate;
    }
}
