package com.ionis.igem.app.game.bins;

import android.util.Log;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.ionis.igem.app.game.model.PhysicalWorldObject;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.Random;

/**
 * Created by PLN on 19/08/2015.
 */
public class Item extends PhysicalWorldObject {


    public enum Type {
        PAPER(Bin.Type.NORMAL),
        CONE_BLUE(Bin.Type.BIO),
        CONE_WHITE(Bin.Type.BIO),
        CONE_YELLOW(Bin.Type.BIO),
        TUBE(Bin.Type.GLASS),
        MICROSCOPE_SLIDE(Bin.Type.GLASS),
        PETRI_DISH(Bin.Type.BIO),
        PEN(Bin.Type.NORMAL),
        SUBSTRATE_BOX(Bin.Type.BIO),
        SOLVENT(Bin.Type.LIQUIDS);

        Bin.Type validBinType;

        Type(Bin.Type pType) {
            validBinType = pType;
        }

        Bin.Type getValid() {
            return validBinType;
        }

        public static Type random() {
            final Type[] values = Type.values();
            final int randomIndex = (int) (values.length * Math.random());
            return values[randomIndex];
        }

    }

    private static final String TAG = "Item";

    public static final int BODY_DENSITY = 1;
    public static final float BODY_ELASTICITY = 0.5f;
    public static final float BODY_FRICTION = 0.125f;

    public static short ID = 0;
    Boolean isGrabbed = false;
    Body body;

    Type type;

    int id;

    public Item(Type pType, ITiledTextureRegion texture, float posX, float posY, VertexBufferObjectManager manager, PhysicsWorld physicsWorld) {
        super(posX, posY, texture, manager);
        setCullingEnabled(true);
        setScale(getIdealScale(SCALE_DEFAULT, texture));

        id = ID++;
        type = pType;
        body = createBody(physicsWorld);
        body.setTransform(posX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                posY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, new Random().nextFloat());
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, true));

        Log.v(TAG, "Item - Created " + type.toString() + " at " + posX + ", " + posY
                + " with texture of w:" + texture.getWidth() + ", h:" + texture.getHeight());
        Log.v(TAG, "Item - Body at " + body.getPosition().x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT + ", "
                + body.getPosition().y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        switch (pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                setScale(getIdealScale(SCALE_GRABBED, this.getTiledTextureRegion()));
                isGrabbed = true;
                break;
            case TouchEvent.ACTION_MOVE:
                if (isGrabbed) {
                    final float x = body.getPosition().x;
                    final float y = body.getPosition().y;
                    float velocityX = pSceneTouchEvent.getX() - x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
                    float velocityY = pSceneTouchEvent.getY() - y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
                    body.setLinearVelocity(velocityX, velocityY);
                    Log.v(TAG, "onAreaTouched - velocity set to " + velocityX + ", " + velocityY);
                }
                break;
            case TouchEvent.ACTION_UP:
                if (isGrabbed) {
                    isGrabbed = false;
                    setScale(getIdealScale(SCALE_DEFAULT, this.getTiledTextureRegion()));
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

    public Body getBody() {
        return body;
    }

    @Override
    protected int getDensity() {
        return BODY_DENSITY;
    }

    @Override
    protected float getElasticity() {
        return BODY_ELASTICITY;
    }

    @Override
    protected float getFriction() {
        return BODY_FRICTION;
    }

    @Override
    protected BodyDef.BodyType getBodyType() {
        return BodyDef.BodyType.DynamicBody;
    }

    @Override
    public String toString() {
        String typeString = ", type=";
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
