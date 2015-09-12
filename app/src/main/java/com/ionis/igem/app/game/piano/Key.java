package com.ionis.igem.app.game.piano;

import android.util.Log;
import com.badlogic.gdx.math.Vector2;
import com.ionis.igem.app.game.AbstractGameActivity;
import com.ionis.igem.app.game.PianoGame;
import com.ionis.igem.app.game.managers.ResMan;
import com.ionis.igem.app.game.model.TouchableAnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.util.color.Color;

/**
 * Created by Paul-Louis Nech on 10/09/2015.
 */
public class Key extends Base {

    public static final float SCALE_DEFAULT = 0.1f;
    public static final float BIGGER_SHAPE_FACTOR = 3f;

    private PianoGame game;
    private TouchableAnimatedSprite shape;

    public Key(Type type, PianoGame pGame) {
        super(position(type, pGame.getActivity()).x, position(type, pGame.getActivity()).y, type, true, true, pGame.getActivity());
        final float posShapeX = position(type, pGame.getActivity()).x - sprite.getWidth() * SCALE_DEFAULT;
        final float posShapeY = position(type, pGame.getActivity()).y - (sprite.getHeight() / 4) * SCALE_DEFAULT;
        game = pGame;
        shape = new TouchableAnimatedSprite(posShapeX , posShapeY, sprite.getTiledTextureRegion(),
                game.getActivity().getVBOM(), this);
        shape.setColor(Color.TRANSPARENT);
        shape.setRotation(sprite.getRotation());

        setShapeScale(SCALE_DEFAULT);
    }

    protected void setShapeScale(float scale) {
        shape.setScaleCenter(shape.getScaleCenterX() * scale,
                shape.getScaleCenterY() * scale);
        shape.setScale(BIGGER_SHAPE_FACTOR * SCALE_DEFAULT, SCALE_DEFAULT);
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

    public TouchableAnimatedSprite getShape() {
        return shape;
    }
}
