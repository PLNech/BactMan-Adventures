package com.ionis.igem.app.game.piano;

import com.badlogic.gdx.math.Vector2;
import com.ionis.igem.app.game.AbstractGameActivity;
import com.ionis.igem.app.game.managers.ResMan;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

/**
 * Created by Paul-Louis Nech on 10/09/2015.
 */
public class Key extends Base {

    public static final float SCALE_DEFAULT = 0.1f;

    public Key(Type type, AbstractGameActivity activity) {
        super(position(type, activity).x, position(type, activity).y, type, true, activity);
        setScale(SCALE_DEFAULT);
        sprite.setInitialScale(SCALE_DEFAULT);
    }

    private static Vector2 position(Type type, AbstractGameActivity activity) {
        final float keyY = 0.85f;
        final float keyX;
        final ITiledTextureRegion baseTextureRegion = activity.getTexture(ResMan.PIANO_A);

        switch (type) {
            case A:
                keyX = 0.25f;
                break;
            case T:
                keyX = 0.42f;
                break;
            case G:
                keyX = 0.58f;
                break;
            case C:
                keyX = 0.75f;
                break;
            default:
                throw new IllegalStateException("No default");
        }
        return activity.spritePosition(baseTextureRegion, keyX, keyY, Key.SCALE_DEFAULT);
    }
}
