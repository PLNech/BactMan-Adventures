package com.ionis.igem.app.game.model;

import android.opengl.GLES20;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

/**
 * Created by PLN on 23/08/2015.
 */
public abstract class WorldObject extends AnimatedSprite {
    public static final float SCALE_DEFAULT = 0.150f;
    public static final float SCALE_GRABBED = 0.250f;

    private Color defaultColor;

    public WorldObject(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        this.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        defaultColor = new Color(getColor());
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
}
