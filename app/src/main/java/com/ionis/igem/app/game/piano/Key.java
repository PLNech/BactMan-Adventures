package com.ionis.igem.app.game.piano;

import android.util.Log;
import com.badlogic.gdx.math.Vector2;
import com.ionis.igem.app.game.AbstractGameActivity;
import com.ionis.igem.app.game.PianoGame;
import com.ionis.igem.app.game.managers.ResMan;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

/**
 * Created by Paul-Louis Nech on 10/09/2015.
 */
public class Key extends Base {

    public static final float SCALE_DEFAULT = 0.1f;

    private PianoGame game;

    public Key(Type type, PianoGame pGame) {
        super(position(type, pGame.getActivity()).x, position(type, pGame.getActivity()).y, type, true, true, pGame.getActivity());
        setScale(SCALE_DEFAULT);
        game = pGame;
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if (pSceneTouchEvent.isActionDown()) {
            Log.d(TAG, "onAreaTouched - Touched key " + type + ".");
            game.onKeyPress(type);
            return true;
        }
        return false;
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
