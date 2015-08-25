package com.ionis.igem.app.game.model;

import android.util.Log;
import android.util.Pair;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
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

    protected Body body;

    public PhysicalWorldObject(float pX, float pY, float pAngle, float pScale, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager,
                               PhysicsWorld physicsWorld) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        setScale(pScale);
        initBody(physicsWorld);
        body.setTransform(pX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                pY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, pAngle);

        Pair<Boolean, Boolean> updates = getBodyUpdates();
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, updates.first, updates.second));
    }

    protected void initBody(PhysicsWorld physicsWorld) {
        final FixtureDef itemFixtureDef = PhysicsFactory.createFixtureDef(this.getDensity(), this.getElasticity(), this.getFriction());
        body = PhysicsFactory.createBoxBody(physicsWorld, this, this.getBodyType(), itemFixtureDef);
        body.setUserData(this);
        Log.d(TAG, "initBody - Body initialised: " + body);
    }

    protected BodyDef.BodyType getBodyType() {
        return BodyDef.BodyType.StaticBody;
    }

    protected Pair<Boolean, Boolean> getBodyUpdates() {
        /**
         *  Describes the updates to apply : position/rotation
         */
        return new Pair<>(true, true);
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

    public Body getBody() {
        return body;
    }
}
