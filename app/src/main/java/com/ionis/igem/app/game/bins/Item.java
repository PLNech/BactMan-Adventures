package com.ionis.igem.app.game.bins;

import android.util.Log;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.ionis.igem.app.game.model.DraggableAnimatedSprite;
import com.ionis.igem.app.game.model.PhysicalWorldObject;
import com.ionis.igem.app.game.model.WorldObject;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

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
    DraggableAnimatedSprite biggerSprite;

    int id;

    public Item(Type pType, ITiledTextureRegion texture, float posX, float posY, VertexBufferObjectManager manager, PhysicsWorld physicsWorld) {
        super(posX, posY, new Random().nextFloat(), WorldObject.getIdealScale(SCALE_DEFAULT, texture), true, texture, manager, physicsWorld);
        sprite.setCullingEnabled(true);
        biggerSprite = new DraggableAnimatedSprite(posX, posY, getIdealScale(SCALE_DEFAULT, texture), sprite.getTiledTextureRegion(), manager, this) {
            //TODO: Move Draggability to own interface
            @Override
            protected void onManagedUpdate(float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                setPosition(sprite.getX(), sprite.getY());
            }
        };
        biggerSprite.setInitialScale(2f * sprite.getScaleX());
        biggerSprite.setColor(Color.TRANSPARENT);
        biggerSprite.setRotation(sprite.getRotation());
        body.setBullet(true);

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

    public Sprite getBiggerSprite() {
        return biggerSprite;
    }

    @Override
    public int getDensity() {
        return BODY_DENSITY;
    }

    @Override
    public float getElasticity() {
        return BODY_ELASTICITY;
    }

    @Override
    public float getFriction() {
        return BODY_FRICTION;
    }

    @Override
    protected BodyDef.BodyType getBodyType() {
        return BodyDef.BodyType.DynamicBody;
    }

    @Override
    public String toString() {
        String typeString = ", type=" + type.toString();
        return "Item{" +
                "id=" + id +
                typeString +
                '}';
    }
}
