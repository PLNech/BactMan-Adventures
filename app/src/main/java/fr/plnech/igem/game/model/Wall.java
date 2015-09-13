package fr.plnech.igem.game.model;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import fr.plnech.igem.game.GutGame;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by PLNech on 25/08/2015.
 */
public class Wall extends Rectangle {

    public enum Type {
        TOP, LEFT, RIGHT, BOTTOM
    }

    private Type type;

    public Wall(float pX, float pY, float pWidth, float pHeight, Type pType,
                VertexBufferObjectManager pVertexBufferObjectManager, PhysicsWorld physicsWorld, boolean masked)
    {
        super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
        setVisible(false);
        type = pType;
        final FixtureDef itemFD;
        if (masked) {
            itemFD = PhysicsFactory.createFixtureDef(PhysicalWorldObject.BODY_DENSITY,
                    PhysicalWorldObject.BODY_ELASTICITY, PhysicalWorldObject.BODY_FRICTION,
                    false, GutGame.CATEGORY_WALL, GutGame.MASK_WALL, GutGame.GROUP_INDEX);
        } else {
            itemFD = PhysicsFactory.createFixtureDef(PhysicalWorldObject.BODY_DENSITY,
                    PhysicalWorldObject.BODY_ELASTICITY, PhysicalWorldObject.BODY_FRICTION);
        }
        Body body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyDef.BodyType.StaticBody, itemFD);
        body.setUserData(type);
        body.setTransform(pX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                pY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false));
    }

    public Type getType() {
        return type;
    }

    public static boolean isOne(Fixture x) {
        return x.getBody().getUserData() instanceof Type;
    }
}
