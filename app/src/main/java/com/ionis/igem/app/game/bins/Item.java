package com.ionis.igem.app.game.bins;

import android.util.Log;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.ionis.igem.app.game.BinGame;
import com.ionis.igem.app.game.model.DraggableAnimatedSprite;
import com.ionis.igem.app.game.model.PhysicalWorldObject;
import com.ionis.igem.app.game.model.WorldObject;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.util.color.Color;

import java.util.Random;

/**
 * Created by PLN on 19/08/2015.
 */
public class Item extends PhysicalWorldObject {


    public static final float BIGGER_SHAPE_FACTOR = 3f;

    public enum Type {
        PAPER(Bin.Type.NORMAL),
        CONE(Bin.Type.BIO),
        TUBE(Bin.Type.BIO),
        SLIDE(Bin.Type.BIO),
        SLIDE_BROKEN(Bin.Type.GLASS),
        PETRI_DISH(Bin.Type.BIO),
        GEL(Bin.Type.BIO),
        PEN(Bin.Type.NORMAL),
        GLOVES(Bin.Type.BIO),
        BECHER(Bin.Type.LIQUIDS),
        BECHER_BROKEN(Bin.Type.GLASS),
        ERLEN(Bin.Type.LIQUIDS),
        ERLEN_BROKEN(Bin.Type.GLASS),
        ROUNDFLASK(Bin.Type.LIQUIDS),
        ROUNDFLASK_BROKEN(Bin.Type.GLASS),
        MICROTUBE(Bin.Type.BIO);

        Bin.Type validBinType;

        Type(Bin.Type pType) {
            validBinType = pType;
        }

        public Bin.Type getValid() {
            return validBinType;
        }

        public static Type random() {
            final Type[] values = Type.values();
            final int randomIndex = (int) (values.length * Math.random());
            return values[randomIndex];
        }
    }

    private static final String TAG = "Item";

    public static final float BODY_DENSITY = 1;
    public static final float BODY_ELASTICITY = 0.5f;
    public static final float BODY_FRICTION = 0.125f;

    public static short ID = 0;

    private final BinGame game;

    int id;
    Type type;
    DraggableAnimatedSprite shape;

    public Item(Type pType, ITiledTextureRegion texture, float posX, float posY, BinGame game) {
        super(new PhysicalWorldObject.Builder(posX, posY, texture,
                game.getActivity().getVBOM(), game.getActivity().getPhysicsWorld())
                .angle(new Random().nextFloat()).draggable(true)
                .scaleDefault(WorldObject.getIdealScale(SCALE_DEFAULT, texture))
                .scaleGrabbed(WorldObject.getIdealScale(SCALE_GRABBED, texture))
                .density(BODY_DENSITY).elasticity(BODY_ELASTICITY).friction(BODY_FRICTION));
        this.game = game;
        sprite.setCullingEnabled(true);
        shape = new DraggableAnimatedSprite(posX, posY, getIdealScale(SCALE_GRABBED, texture), sprite.getTiledTextureRegion(),
                game.getActivity().getVBOM(), this) {
            //TODO: Move Draggability to own interface
            @Override
            protected void onManagedUpdate(float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                setPosition(sprite.getX(), sprite.getY());
                setRotation(sprite.getRotation());
            }
        };
        shape.setInitialScale(BIGGER_SHAPE_FACTOR * sprite.getScaleX());
        shape.setColor(Color.TRANSPARENT);
        shape.setRotation(sprite.getRotation());

        id = ID++;
        type = pType;
        Log.v(TAG, "Item - Created " + type.toString() + " at " + sprite.getX() + ", " + sprite.getY()
                + " with texture of w:" + texture.getWidth() + ", h:" + texture.getHeight());
    }

    public Type getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public DraggableAnimatedSprite getShape() {
        return shape;
    }

    @Override
    public void onAddToWorld() {
        super.onAddToWorld();
        body.setBullet(true);
    }

    @Override
    public void onRemoveFromWorld() {
        super.onRemoveFromWorld();
        game.removeItem(this);
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
