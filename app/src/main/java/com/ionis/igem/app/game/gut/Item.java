package com.ionis.igem.app.game.gut;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.ionis.igem.app.game.managers.ResMan;
import com.ionis.igem.app.game.model.PhysicalWorldObject;
import com.ionis.igem.app.game.AbstractGameActivity;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

/**
 * Created by PLNech on 31/08/2015.
 */
public class Item extends PhysicalWorldObject {
    public enum Type {
        NUTRIENT, ANTIBIO, IMMUNO;

        public static Type random() {
            final Type[] values = Type.values();
            final int randomIndex = (int) (values.length * Math.random());
            return values[randomIndex];
        }
    }

    public static float SCALE_DEFAULT = 0.1f;

    private Type type;

    public Item(float pX, float pY, float pAngle, Type pType, ITiledTextureRegion textureRegion, AbstractGameActivity activity) {
        super(pX, pY, pAngle, SCALE_DEFAULT, false, textureRegion, activity.getVBOM(), activity.getPhysicsWorld());
        type = pType;
    }

    @Override
    protected BodyDef.BodyType getBodyType() {
        return BodyDef.BodyType.DynamicBody;
    }

    public static ITiledTextureRegion getTexture(AbstractGameActivity activity, Type type) {
        final ITiledTextureRegion texture;
        switch (type) {
            case ANTIBIO:
                texture = activity.getTexture(ResMan.GUT_ANTIBIO);
                break;
            case IMMUNO:
                texture = activity.getTexture(ResMan.GUT_PHAGE);
                break;
            case NUTRIENT:
                texture = activity.getTexture(ResMan.GUT_VITAMIN);
                break;
            default:
                throw new IllegalStateException("No default!");
        }
        return texture;
    }

    public static boolean isOne(Fixture x1) {
        return x1.getBody().getUserData() instanceof Item;
    }

    public Type getType() {
        return type;
    }
}
