package com.ionis.igem.app.game.model;

import android.opengl.GLES20;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

/**
 * Created by PLN on 23/08/2015.
 */
public abstract class WorldObject {
    public static final float SCALE_DEFAULT = 0.150f;
    public static final float SCALE_GRABBED = 0.250f;

    protected DraggableAnimatedSprite sprite;

    private Color defaultColor;

    public WorldObject(float pX, float pY, boolean draggable, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        if (draggable) {
            sprite = new DraggableAnimatedSprite(pX, pY, SCALE_DEFAULT, pTiledTextureRegion, pVertexBufferObjectManager, this);
        } else {
            sprite = new DraggableAnimatedSprite(pX, pY, SCALE_DEFAULT, pTiledTextureRegion, pVertexBufferObjectManager, null);
        }
        sprite.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        defaultColor = new Color(sprite.getColor());
    }

    public void registerEntityModifier(final IEntityModifier entityModifier) {
        sprite.registerEntityModifier(entityModifier);
    }

    public static float getIdealScale(float scale, ITiledTextureRegion textureRegion) {
        if (textureRegion.getHeight() + textureRegion.getWidth() <= 256) {
            return scale * 4;
        }
        return scale;
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    public DraggableAnimatedSprite getSprite() {
        return sprite;
    }

    public float getScaleDefault() {
        return SCALE_DEFAULT;
    }

    public float getScaleGrabbed() {
        return SCALE_GRABBED;
    }
}
