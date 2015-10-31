/*
=======================================================================
BactMan Adventures | Scientific popularisation through mini-games
Copyright (C) 2015 IONIS iGEM Team
Distributed under the GNU GPLv3 License.
(See file LICENSE.txt or copy at https://www.gnu.org/licenses/gpl.txt)
=======================================================================
*/

package fr.plnech.igem.game.model;

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

public abstract class PhysicalWorldObject extends WorldObject {
    private static final String TAG = "PhysicalWorldObject";

    public static final float BODY_DENSITY = 1000;
    public static final float BODY_ELASTICITY = 0;
    public static final float BODY_FRICTION = 0;

    protected Body body;
    private final Builder b;

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
        private boolean shouldAdd = true;

        private float density = BODY_DENSITY;
        private float elasticity = BODY_ELASTICITY;
        private float friction = BODY_FRICTION;

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

        public Builder shouldAdd(boolean val) {
            shouldAdd = val;
            return this;
        }

        public Builder density(float val) {
            density = val;
            return this;
        }

        public Builder elasticity(float val) {
            elasticity = val;
            return this;
        }

        public Builder friction(float val) {
            friction = val;
            return this;
        }
    }

    protected PhysicalWorldObject(Builder b) {
        super(b.pX, b.pY, b.draggable, b.scaleDefault, b.textureRegion, b.manager);
        sprite.setScale(b.scaleDefault);
        this.b = b;
        if (b.shouldAdd) {
            onAddToWorld();
        }
    }

    protected void initBody(PhysicsWorld physicsWorld) {
        initBody(physicsWorld, null, null, null);
    }

    @SuppressWarnings("AccessStaticViaInstance")
    protected void initBody(PhysicsWorld physicsWorld, Short category, Short mask, Short groupIndex) {
        final FixtureDef itemFixtureDef;
        if (category != null && mask != null && groupIndex != null) {
            itemFixtureDef = PhysicsFactory.createFixtureDef(b.density, b.elasticity, b.friction, false, category, mask, groupIndex);
        } else {
            itemFixtureDef = PhysicsFactory.createFixtureDef(b.density, b.elasticity, b.friction);
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

    public void onAddToWorld() {
        Log.d(TAG, "onAddToWorld - Adding to world a " + this.getClass().getSimpleName());
        initBody(b.physicsWorld, b.category, b.mask, b.groupIndex);
        body.setTransform(b.pX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                b.pY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, b.angle);

        Pair<Boolean, Boolean> updates = getBodyUpdates();
        b.physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, updates.first, updates.second));
    }

    public void onRemoveFromWorld() {
        sprite.detachChildren();
        sprite.detachSelf();
    }

    public Body getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "PhysicalWorldObject (" + this.getClass().getSimpleName() + "): " + super.toString();
    }
}
