package fr.plnech.igem.game.picto;

import android.util.Log;
import fr.plnech.igem.game.AbstractGameActivity;
import fr.plnech.igem.game.PictoGame;
import fr.plnech.igem.game.managers.ResMan;
import fr.plnech.igem.utils.WorldUtils;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by PLNech on 28/08/2015.
 */
public class Card extends Sprite {
    public enum Type {
        BACK,
        BIOHAZARD,
        CMR,
        ENVIRONMENT,
        FACE,
        FLAMMABLE,
        GLOVES,
        MASK,
        OXIDISING,
        RADIOACTIVE,
        TOXIC,
        EYE,
        SHOWER;

        public static Type random() {
            final Type[] values = Type.values();
            final int randomIndex = 1 + (int) ((values.length - 1) * Math.random()); // Random between second and last
            return values[randomIndex];
        }

        public static Type[] valuesButBack() {
            Type[] array = new Type[12];
            array[0] = BIOHAZARD;
            array[1] = CMR;
            array[2] = ENVIRONMENT;
            array[3] = FACE;
            array[4] = FLAMMABLE;
            array[5] = GLOVES;
            array[6] = MASK;
            array[7] = OXIDISING;
            array[8] = RADIOACTIVE;
            array[9] = TOXIC;
            array[10] = EYE;
            array[11] = SHOWER;
            return array;
        }
    }

    private static final String TAG = "Card";

    public static final float SCALE_DEFAULT = 0.15f;

    private String type;
    private Sprite backSprite;
    private boolean isVisible = false;

    private PictoGame game;

    public Card(float pX, float pY, String resCardName, ITextureRegion pTiledTextureRegion, AbstractGameActivity activity) {
        this(pX, pY, pTiledTextureRegion, activity.getVBOM());
        type = WorldUtils.toUpperCase(resCardName.replace("card_", "").replace(".png", ""));
        game = (PictoGame) activity.getCurrentGame();
        backSprite = new Sprite(pX, pY, activity.getTexture(ResMan.CARD_BACK), activity.getVBOM());
        setScaleAndPosition(this, pX, pY);
        setScaleAndPosition(backSprite, pX, pY);
        setVisibility();
        Log.d(TAG, "Card - created at " + getX() + ", " + getY() + " of type " + type);
    }

    private Card(float pX, float pY, ITextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    private static void setScaleAndPosition(Sprite sprite, float pX, float pY) {
        sprite.setScale(SCALE_DEFAULT);
        sprite.setScaleCenterX(SCALE_DEFAULT * sprite.getScaleCenterX());
        sprite.setScaleCenterY(SCALE_DEFAULT * sprite.getScaleCenterY());
        sprite.setPosition(pX, pY);
    }

    public void flip() {
        isVisible = !isVisible;
        setVisibility();
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        switch (pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                if (game.isDisplayingCards()) {
                    return false;
                }
                game.onTouchCard(this);
                return true;
        }
        return false;
    }

    private void setVisibility() {
        setVisible(isVisible);
        backSprite.setVisible(!isVisible);
    }

    public Sprite getBack() {
        return backSprite;
    }

    public String getType() {
        return type;
    }
}
