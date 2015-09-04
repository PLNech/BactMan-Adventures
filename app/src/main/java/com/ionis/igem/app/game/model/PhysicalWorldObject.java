package com.ionis.igem.app.game.model;

import android.support.annotation.Nullable;
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

    public static final int BODY_DENSITY = 1000;
    public static final float BODY_ELASTICITY = 0;
    public static final float BODY_FRICTION = 0;

    protected Body body;

    public PhysicalWorldObject(float pX, float pY, float pAngle, boolean draggable, float scaleDefault,
                               ITiledTextureRegion pTextureRegion, VertexBufferObjectManager pVBOM, PhysicsWorld physicsWorld) {
        this(pX, pY, pAngle, draggable, scaleDefault, SCALE_GRABBED, pTextureRegion, pVBOM, physicsWorld);
    }

    public PhysicalWorldObject(float pX, float pY, float pAngle, boolean draggable, @Nullable Float pScale, @Nullable Float pScaleGrabbed,
                               ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager,
                               PhysicsWorld physicsWorld) {
        super(pX, pY, draggable, pScale, pScaleGrabbed, pTiledTextureRegion, pVertexBufferObjectManager);
        if (pScale != null) {
            sprite.setScale(pScale);
        }

        initBody(physicsWorld);
        body.setTransform(pX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                pY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, pAngle);

        Pair<Boolean, Boolean> updates = getBodyUpdates();
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, updates.first, updates.second));
    }

    @SuppressWarnings("AccessStaticViaInstance")
    protected void initBody(PhysicsWorld physicsWorld) {
        final FixtureDef itemFixtureDef = PhysicsFactory.createFixtureDef(this.getDensity(), this.getElasticity(), this.getFriction());
        body = PhysicsFactory.createBoxBody(physicsWorld, sprite, this.getBodyType(), itemFixtureDef);
        body.setUserData(this);
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

    public float getElasticity() {
        return BODY_ELASTICITY;
    }

    public float getFriction() {
        return BODY_FRICTION;
    }

    public int getDensity() {
        return BODY_DENSITY;
    }

    public Body getBody() {
        return body;
    }
}
