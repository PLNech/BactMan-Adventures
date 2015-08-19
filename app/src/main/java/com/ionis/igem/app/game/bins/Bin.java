package com.ionis.igem.app.game.bins;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by PLN on 19/08/2015.
 */
public class Bin extends Sprite {

    public static final float SCALE_DEFAULT = 0.5f;

    public static final int BODY_DENSITY = 1000;
    public static final float BODY_ELASTICITY = 0;
    public static final float BODY_FRICTION = 0;

    Body body;

    public Bin(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObject, PhysicsWorld physicsWorld) {
        super(pX, pY, pTextureRegion, pVertexBufferObject);
        setScale(SCALE_DEFAULT);

        body = createBody(physicsWorld);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false));
    }

    private Body createBody(PhysicsWorld physicsWorld) {
        final FixtureDef itemFixtureDef = PhysicsFactory.createFixtureDef(BODY_DENSITY, BODY_ELASTICITY, BODY_FRICTION);
        final Body body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyDef.BodyType.StaticBody, itemFixtureDef);
        body.setUserData("bin");
        return body;
    }
}
