package com.ionis.igem.app.game.model;

import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.Random;

/**
 * Created by PLNech on 24/08/2015.
 */
public class Wall extends PhysicalWorldObject {
    public Wall(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager,
                PhysicsWorld physicsWorld) {
        super(pX, pY, new Random().nextFloat(), SCALE_DEFAULT, false, pTiledTextureRegion, pVertexBufferObjectManager, physicsWorld);
    }

}
