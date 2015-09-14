package fr.plnech.igem.game.piano;

import android.util.Log;
import com.badlogic.gdx.math.Vector2;
import fr.plnech.igem.game.AbstractGameActivity;
import fr.plnech.igem.game.PianoGame;
import fr.plnech.igem.game.managers.ResMan;
import fr.plnech.igem.game.model.TouchableAnimatedSprite;
import org.andengine.entity.sprite.Sprite;
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
    private Sprite shadowValid;
    private Sprite shadowInvalid;

    public Key(Type type, PianoGame pGame) {
        super(position(type, pGame.getActivity()).x, position(type, pGame.getActivity()).y, type, true, true, pGame.getActivity());
        final float posX = position(type, pGame.getActivity()).x;
        final float posY = position(type, pGame.getActivity()).y;
        final float posShapeX = posX - sprite.getWidth() * SCALE_DEFAULT;
        final float posShapeY = posY - (sprite.getHeight() / 4) * SCALE_DEFAULT;
        final float posShaderX = posX - 6;
        final float posShaderY = posY - 12;

        game = pGame;
        shape = new TouchableAnimatedSprite(posShapeX, posShapeY, sprite.getTiledTextureRegion(),
                game.getActivity().getVBOM(), this);
        shape.setColor(Color.TRANSPARENT);
        shape.setRotation(sprite.getRotation());

        shadowValid = new Sprite(posShaderX, posShaderY, game.getActivity().getTexture(ResMan.PIANO_SHADER_OK), game.getActivity().getVBOM());
        shadowInvalid = new Sprite(posShaderX, posShaderY, game.getActivity().getTexture(ResMan.PIANO_SHADER_KO), game.getActivity().getVBOM());
        hideShadows();
        setMemberScales(SCALE_DEFAULT);
    }

    protected void setMemberScales(float scale) {
        setScaleAndCenter(shape, scale, true);
        setScaleAndCenter(shadowInvalid, scale, false);
        setScaleAndCenter(shadowValid, scale, false);
    }

    private static void setScaleAndCenter(Sprite s, float scale, boolean biggerWidth) {
        s.setScaleCenter(s.getScaleCenterX() * scale,
                s.getScaleCenterY() * scale);
        s.setScale(biggerWidth ? BIGGER_SHAPE_FACTOR * SCALE_DEFAULT : SCALE_DEFAULT, SCALE_DEFAULT);
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

    public void hideShadows() {
        shadowValid.setVisible(false);
        shadowInvalid.setVisible(false);
    }

    public void showShadow(boolean start, Animation animation) {
        final boolean validHit = animation == Animation.VALID_HIT;
        Sprite shadow = validHit ? shadowValid : shadowInvalid;
        shadow.setVisible(start);
    }

    public Sprite getShadowInvalid() {
        return shadowInvalid;
    }

    public Sprite getShadowValid() {
        return shadowValid;
    }
}
