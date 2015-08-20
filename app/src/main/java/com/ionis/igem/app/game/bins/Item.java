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

    private Type type;

    public enum Type {
        PAPER(Bin.Type.DEFAULT),
        MICROSCOPE_SLIDE(Bin.Type.GLASS),
        PETRI_DISH(Bin.Type.BIO),
        PEN(Bin.Type.DEFAULT),
        SUBSTRATE_BOX(Bin.Type.BIO),
        SOLVENT(Bin.Type.LIQUIDS);

        Bin.Type validBinType;

        Type(Bin.Type pType) {
            validBinType = pType;
        }

        Bin.Type getValid() {
            return validBinType;
        }

    }
    public static final int DURATION_EACH_ANIM = 100;

    public static final float SCALE_GRABBED = 1.5f;
    public static final float SCALE_NORMAL = 1.0f;
    public static final int BODY_DENSITY = 1;

    public static final float BODY_ELASTICITY = 0.5f;
    public static final float BODY_FRICTION = 0.5f;
    public static short ID = 0;

    Boolean isGrabbed = false;

    Body body;
    int id;
    public Item(Type pType, TiledTextureRegion texture, float posX, float posY, VertexBufferObjectManager manager, PhysicsWorld physicsWorld) {
        super(posX, posY, texture, manager);
        setCullingEnabled(true);
        animate(DURATION_EACH_ANIM);

        id = ID++;
        type = pType;
        body = createBody(physicsWorld);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false));
    }

    private Body createBody(PhysicsWorld physicsWorld) {
        final FixtureDef itemFixtureDef = PhysicsFactory.createFixtureDef(BODY_DENSITY, BODY_ELASTICITY, BODY_FRICTION);
        final Body body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyDef.BodyType.DynamicBody, itemFixtureDef);
        body.setUserData(this);
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
                    float velocityX = pTouchAreaLocalX - body.getPosition().x;
                    float velocityY = pTouchAreaLocalY - body.getPosition().y;
                    body.setLinearVelocity(velocityX, velocityY);
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

    public Type getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        String typeString = "type=";
        switch (type) {
            case PAPER:
                typeString += "Papier";
                break;
            case MICROSCOPE_SLIDE:
                typeString += "Lame";
                break;
            case PEN:
                typeString += "Marqueur";
                break;
            case SOLVENT:
                typeString += "Solvant";
                break;
            case SUBSTRATE_BOX:
                typeString += "Boite de substrat";
                break;
            case PETRI_DISH:
                typeString += "Boite de Petri";
                break;
        }

        return "Item{" +
                "id=" + id +
                typeString +
                '}';
    }
}
