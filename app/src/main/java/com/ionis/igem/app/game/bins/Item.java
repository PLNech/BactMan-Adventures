package com.ionis.igem.app.game.bins;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by PLN on 19/08/2015.
 */
public class Item extends AnimatedSprite {
    public static final int DURATION_EACH_ANIM = 100;
    public static final float SCALE_GRABBED = 1.5f;
    public static final float SCALE_NORMAL = 1.0f;

    public static final int BODY_DENSITY = 1;
    public static final float BODY_ELASTICITY = 0.5f;
    public static final float BODY_FRICTION = 0.5f;

    Boolean isGrabbed = false;
    Body body;

    public Item(TiledTextureRegion texture, float posX, float posY, VertexBufferObjectManager manager, PhysicsWorld physicsWorld) {
        super(posX, posY, texture, manager);
        animate(DURATION_EACH_ANIM);

        body = createBody(physicsWorld);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body));
    }

    public Body createBody(PhysicsWorld physicsWorld) {
        final FixtureDef itemFixtureDef = PhysicsFactory.createFixtureDef(BODY_DENSITY, BODY_ELASTICITY, BODY_FRICTION);
        final Body body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyDef.BodyType.DynamicBody, itemFixtureDef);
        body.setUserData("item");
        return body;
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        switch (pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                setScale(SCALE_GRABBED);
                isGrabbed = true;
                break;
            case TouchEvent.ACTION_MOVE:
                if (isGrabbed) {
                    setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
                }
                break;
            case TouchEvent.ACTION_UP:
                if (isGrabbed) {
                    isGrabbed = false;
                    setScale(SCALE_NORMAL);
                }
                break;
        }
        return true;
    }
}
