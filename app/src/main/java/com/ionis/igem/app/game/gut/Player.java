package com.ionis.igem.app.game.gut;

import android.util.Pair;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.ionis.igem.app.game.AbstractGameActivity;
import com.ionis.igem.app.game.GutGame;
import com.ionis.igem.app.game.model.PhysicalWorldObject;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

/**
 * Created by PLNech on 31/08/2015.
 */
public class Player extends PhysicalWorldObject {
    public static float SCALE_DEFAULT = 0.25f;

    public Player(float pX, float pY, float pAngle, ITiledTextureRegion pTiledTextureRegion, AbstractGameActivity activity) {
        super(new PhysicalWorldObject.Builder(pX, pY, pTiledTextureRegion, activity.getVBOM(), activity.getPhysicsWorld())
                .angle(pAngle).draggable(true).scaleDefault(SCALE_DEFAULT)
                .category(GutGame.CATEGORY_PLAYER).mask(GutGame.MASK_PLAYER).groupIndex(GutGame.GROUP_INDEX));
    }

    @Override
    protected BodyDef.BodyType getBodyType() {
        return BodyDef.BodyType.KinematicBody;
    }

    @Override
    protected Pair<Boolean, Boolean> getBodyUpdates() {
        /**
         *  Describes the updates to apply : position/rotation
         */
        return new Pair<>(true, false);
    }

    public static boolean isOne(Fixture x1) {
        return x1.getBody().getUserData() instanceof Player;
    }
}
