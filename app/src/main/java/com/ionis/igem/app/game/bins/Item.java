package com.ionis.igem.app.game.bins;

import android.util.Log;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.ionis.igem.app.game.model.PhysicalWorldObject;
import com.ionis.igem.app.game.model.WorldObject;
import org.andengine.extension.physics.box2d.PhysicsWorld;
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

    Type type;

    int id;

    public Item(Type pType, ITiledTextureRegion texture, float posX, float posY, VertexBufferObjectManager manager, PhysicsWorld physicsWorld) {
        super(posX, posY, new Random().nextFloat(), WorldObject.getIdealScale(SCALE_DEFAULT, texture), true, texture, manager, physicsWorld);
        sprite.setCullingEnabled(true);

        id = ID++;
        type = pType;
        Log.v(TAG, "Item - Created " + type.toString() + " at " + posX + ", " + posY
                + " with texture of w:" + texture.getWidth() + ", h:" + texture.getHeight());
    }

    public Type getType() {
        return type;
    }

    public int getId() {
        return id;
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
