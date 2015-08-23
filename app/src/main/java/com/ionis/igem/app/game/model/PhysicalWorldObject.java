package com.ionis.igem.app.game.model;

import android.util.Log;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by PLN on 23/08/2015.
 */
public abstract class PhysicalWorldObject extends WorldObject {
    private static final String TAG = "PhysicalWorldObject";

    public static final int BODY_DENSITY = 1000;
    public static final float BODY_ELASTICITY = 0;
    public static final float BODY_FRICTION = 0;

    public PhysicalWorldObject(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    protected Body createBody(PhysicsWorld physicsWorld) {
        final FixtureDef itemFixtureDef = PhysicsFactory.createFixtureDef(this.getDensity(), this.getElasticity(), this.getFriction());
        final Body body = PhysicsFactory.createBoxBody(physicsWorld, this, this.getBodyType(), itemFixtureDef);
        body.setUserData(this);
        return body;
    }

    protected BodyDef.BodyType getBodyType() {
        return BodyDef.BodyType.StaticBody;
    }

    protected float getElasticity() {
        return BODY_ELASTICITY;
    }

    protected float getFriction() {
        return BODY_FRICTION;
    }

    protected int getDensity() {
        return BODY_DENSITY;
    }

}
