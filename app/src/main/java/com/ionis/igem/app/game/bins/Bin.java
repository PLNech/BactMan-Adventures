package com.ionis.igem.app.game.bins;

import android.util.Log;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by PLN on 19/08/2015.
 */
public class Bin extends Sprite {

    private static final String TAG = "Bin";

    public enum Type {
        NORMAL, GLASS, BIO, LIQUIDS
    }

    public static final float SCALE_DEFAULT = 0.125f;

    public static final int BODY_DENSITY = 1000;
    public static final float BODY_ELASTICITY = 0;
    public static final float BODY_FRICTION = 0;

    public static short ID = 0;

    int id;
    Body body;
    Type type;

    public Bin(Type binType, float pX, float pY, ITextureRegion pTextureRegion,
               VertexBufferObjectManager pVertexBufferObject, PhysicsWorld physicsWorld) {
        super(pX, pY, pTextureRegion, pVertexBufferObject);
        setScale(SCALE_DEFAULT);
        Log.v(TAG, "Bin - Created at " + pX + ", " + pY);

        id = ID++;
        this.type = binType;
        body = createBody(physicsWorld);
        body.setTransform(pX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, pY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false));
    }

    private Body createBody(PhysicsWorld physicsWorld) {
        final FixtureDef itemFixtureDef = PhysicsFactory.createFixtureDef(BODY_DENSITY, BODY_ELASTICITY, BODY_FRICTION);
        final Body body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyDef.BodyType.StaticBody, itemFixtureDef);
        body.setUserData(this);
        return body;
    }

    public static boolean isOne(Fixture x1) {
        return x1.getBody().getUserData() instanceof Bin;
    }

    public boolean accepts(Item item) {
        return item.getType().getValid().equals(type);
    }

    @Override
    public String toString() {
        String typeString;
        switch (type) {
            case BIO:
                typeString = "Biologique";
                break;
            case GLASS:
                typeString = "Verre";
                break;
            case LIQUIDS:
                typeString = "Liquides";
                break;
            case NORMAL:
            default:
                typeString = "Normale";
                break;
        }
        return "Bin{" +
                "id=" + id + ", " +
                typeString +
                '}';
    }
}
