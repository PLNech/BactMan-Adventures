package com.ionis.igem.app.game.model;

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

    public static final float BODY_DENSITY = 1000;
    public static final float BODY_ELASTICITY = 0;
    public static final float BODY_FRICTION = 0;

    protected Body body;

    public static class Builder {
        private final float pX;
        private final float pY;
        private final ITiledTextureRegion textureRegion;
        private final VertexBufferObjectManager manager;
        private final PhysicsWorld physicsWorld;

        private float angle = 0;
        private boolean draggable = false;
        private float scaleDefault = SCALE_DEFAULT;
        private float scaleGrabbed = SCALE_GRABBED;

        private Short category = null;
        private Short mask = null;
        private Short groupIndex = null;

        public Builder(float pX, float pY, ITiledTextureRegion textureRegion, VertexBufferObjectManager manager, PhysicsWorld physicsWorld) {
            this.pX = pX;
            this.pY = pY;
            this.textureRegion = textureRegion;
            this.manager = manager;
            this.physicsWorld = physicsWorld;
        }

        public Builder angle(float pAngle) {
            angle = pAngle;
            return this;
        }

        public Builder scaleDefault(float pScale) {
            scaleDefault = pScale;
            return this;
        }

        public Builder scaleGrabbed(float pScale) {
            scaleGrabbed = pScale;
            return this;
        }

        public Builder draggable(boolean pDraggable) {
            draggable = pDraggable;
            return this;
        }

        public Builder category(short val) {
            category = val;
            return this;
        }

        public Builder mask(short val) {
            mask = val;
            return this;
        }

        public Builder groupIndex(short val) {
            groupIndex = val;
            return this;
        }
    }

    protected PhysicalWorldObject(Builder b) {
        super(b.pX, b.pY, b.draggable, b.scaleDefault, b.scaleGrabbed, b.textureRegion, b.manager);
        sprite.setScale(b.scaleDefault);

        initBody(b.physicsWorld, b.category, b.mask, b.groupIndex);
        body.setTransform(b.pX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                b.pY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, b.angle);

        Pair<Boolean, Boolean> updates = getBodyUpdates();
        b.physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, updates.first, updates.second));
    }

    protected void initBody(PhysicsWorld physicsWorld) {
        initBody(physicsWorld, null, null, null);
    }

    @SuppressWarnings("AccessStaticViaInstance")
    protected void initBody(PhysicsWorld physicsWorld, Short category, Short mask, Short groupIndex) {
        final FixtureDef itemFixtureDef;
        if (category != null && mask != null && groupIndex != null) {
            itemFixtureDef = PhysicsFactory.createFixtureDef(this.getDensity(), this.getElasticity(), this.getFriction(), false, category, mask, groupIndex);
        } else {
            itemFixtureDef = PhysicsFactory.createFixtureDef(this.getDensity(), this.getElasticity(), this.getFriction());
        }

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

    public float getDensity() {
        return BODY_DENSITY;
    }

    public Body getBody() {
        return body;
    }
}
