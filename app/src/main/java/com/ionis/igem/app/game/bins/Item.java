package com.ionis.igem.app.game.bins;

import android.util.Log;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.ionis.igem.app.game.BinGame;
import com.ionis.igem.app.game.model.TouchableAnimatedSprite;
import com.ionis.igem.app.game.model.PhysicalWorldObject;
import com.ionis.igem.app.game.model.WorldObject;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
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

    private int id;
    private int value = 1;
    private Type type;
    private TouchableAnimatedSprite shape;

    private boolean isGrabbed;
    private float velocityX;
    private float velocityY;


    public Item(Type pType, ITiledTextureRegion texture, float posX, float posY, BinGame game) {
        super(new PhysicalWorldObject.Builder(posX, posY, texture,
                game.getActivity().getVBOM(), game.getActivity().getPhysicsWorld())
                .angle(new Random().nextFloat()).draggable(true)
                .scaleDefault(WorldObject.getIdealScale(SCALE_DEFAULT, texture))
                .scaleGrabbed(WorldObject.getIdealScale(SCALE_GRABBED, texture))
                .density(BODY_DENSITY).elasticity(BODY_ELASTICITY).friction(BODY_FRICTION));
        this.game = game;
        sprite.setCullingEnabled(true);
        shape = new TouchableAnimatedSprite(posX, posY, sprite.getTiledTextureRegion(),
                game.getActivity().getVBOM(), this) {
            @Override
            protected void onManagedUpdate(float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                setPosition(sprite.getX(), sprite.getY());
                setRotation(sprite.getRotation());
            }
        };
        shape.setScale(BIGGER_SHAPE_FACTOR * SCALE_DEFAULT, SCALE_DEFAULT);
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

    public TouchableAnimatedSprite getShape() {
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
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        switch (pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                sprite.setScale(SCALE_GRABBED);
                isGrabbed = true;
                return true;
            case TouchEvent.ACTION_MOVE:
                if (isGrabbed) {
                    final float x = body.getPosition().x;
                    final float y = body.getPosition().y;
                    velocityX = pSceneTouchEvent.getX() - x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
                    velocityY = pSceneTouchEvent.getY() - y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
                    body.setLinearVelocity(0, 0);
                    body.setTransform(pSceneTouchEvent.getX() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                            pSceneTouchEvent.getY() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, body.getAngle());
                    setValue(1);
                }
                return true;
            case TouchEvent.ACTION_UP:
                if (isGrabbed) {
                    isGrabbed = false;
                    sprite.setScale(SCALE_DEFAULT);
                    body.setLinearVelocity(velocityX, velocityY);
                }
                return true;
        }
        return false;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
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
