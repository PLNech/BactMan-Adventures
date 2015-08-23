package com.ionis.igem.app.game.bins;

import android.util.Log;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.ionis.igem.app.game.model.PhysicalWorldObject;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by PLN on 19/08/2015.
 */
public class Bin extends PhysicalWorldObject {

    private static final String TAG = "Bin";

    public enum Type {
        NORMAL, GLASS, BIO, LIQUIDS
    }

    public static short ID = 0;

    int id;
    Body body;
    Type type;

    public Bin(Type binType, float pX, float pY, ITiledTextureRegion pTextureRegion,
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
