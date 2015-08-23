package com.ionis.igem.app.game.model;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by PLN on 23/08/2015.
 */
public abstract class WorldObject extends AnimatedSprite {
    public static final float SCALE_DEFAULT = 0.125f;
    public static final float SCALE_GRABBED = 0.250f;

    public WorldObject(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }
}
