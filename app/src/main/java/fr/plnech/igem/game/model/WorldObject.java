package fr.plnech.igem.game.model;

import android.opengl.GLES20;
import android.support.annotation.Nullable;
import android.util.Log;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

/**
 * Created by PLNech on 23/08/2015.
 */
public abstract class WorldObject {
    private static final String TAG = "WorldObject";
    protected static final float SCALE_DEFAULT = 0.150f;
    protected static final float SCALE_GRABBED = 0.200f;

    protected final TouchableAnimatedSprite sprite;

    private final Color defaultColor;

    protected WorldObject(float pX, float pY, boolean touchable, @Nullable Float pScale,
                          ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        if (pScale == null) {
            pScale = SCALE_DEFAULT;
        }
        if (touchable) {
            sprite = new TouchableAnimatedSprite(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager, this);
        } else {
            sprite = new TouchableAnimatedSprite(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager, null);
        }
        sprite.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        sprite.setScale(pScale);
        sprite.setPosition(pX, pY);
        defaultColor = new Color(sprite.getColor());
        Log.d(TAG, "WorldObject - New " + this.getClass().getSimpleName() + " of scale " + pScale + " at " + pX + ", " + pY);
    }

    public void registerEntityModifier(final IEntityModifier entityModifier) {
        sprite.registerEntityModifier(entityModifier);
    }

    protected static float getIdealScale(float scale, ITiledTextureRegion textureRegion) {
        if (textureRegion.getHeight() + textureRegion.getWidth() <= 256) {
            return scale * 4;
        }
        return scale;
    }

    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        return false;
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    public TouchableAnimatedSprite getSprite() {
        return sprite;
    }

    public float getScaleDefault() {
        return SCALE_DEFAULT;
    }

    public float getScaleGrabbed() {
        return SCALE_GRABBED;
    }

    public enum Animation {
        VALID_HIT, INVALID_HIT, VALID_MISS
    }
}
