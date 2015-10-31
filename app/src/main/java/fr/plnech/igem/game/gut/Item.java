/*
=======================================================================
BactMan Adventures | Scientific popularisation through mini-games
Copyright (C) 2015 IONIS iGEM Team
Distributed under the GNU GPLv3 License.
(See file LICENSE.txt or copy at https://www.gnu.org/licenses/gpl.txt)
=======================================================================
*/

package fr.plnech.igem.game.gut;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import fr.plnech.igem.game.AbstractGameActivity;
import fr.plnech.igem.game.GutGame;
import fr.plnech.igem.game.model.PhysicalWorldObject;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

public class Item extends PhysicalWorldObject {
    public enum Type {
        VITAMIN, PROTEIN, ANTIBIO, IMMUNO;

        public static Type random() {
            final Type[] values = Type.values();
            final int randomIndex = (int) (values.length * Math.random());
            return values[randomIndex];
        }

    }


    public enum Role {
        ONCE,   /* Appears only once */
        EAT,   /* Appears until eaten */
        REPEAT, /* Repeated with same type */
        RANDOM  /* Repeated with random type */
    }
    public static final float SCALE_DEFAULT = 0.1f;

    private final float speedCoeff;
    private final Type type;
    private Role role;

    private final GutGame game;

    public Item(float pX, float pY, float pAngle, Type pType, Role pRole, float speedCoeff, ITiledTextureRegion textureRegion, GutGame game) {
        super(new PhysicalWorldObject.Builder(pX, pY, textureRegion, game.getActivity().getVBOM(), game.getActivity().getPhysicsWorld())
                .angle(pAngle).draggable(false).scaleDefault(SCALE_DEFAULT)
                .category(GutGame.CATEGORY_ITEM).mask(GutGame.MASK_ITEM).groupIndex(GutGame.GROUP_INDEX)
                .shouldAdd(false));
        type = pType;
        role = pRole;
        this.speedCoeff = speedCoeff;
        this.game = game;
    }

    @Override
    protected BodyDef.BodyType getBodyType() {
        return BodyDef.BodyType.DynamicBody;
    }

    @Override
    public void onAddToWorld() {
        super.onAddToWorld();
        body.setLinearVelocity(speedCoeff * GutGame.SPEED_ITEM_PPS / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);
        game.addItem(this);
    }

    @Override
    public void onRemoveFromWorld() {
        super.onRemoveFromWorld();
        game.removeItem(this);
    }

    public static int chooseLayer(Type type) {
        switch (type) {
            case ANTIBIO:
            case IMMUNO:
                return AbstractGameActivity.LAYER_OVERGROUND;
        }
        return AbstractGameActivity.LAYER_FOREGROUND;
    }

    public static boolean isOne(Fixture x1) {
        return x1.getBody().getUserData() instanceof Item;
    }

    public Type getType() {
        return type;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    public Role getRole() {
        return role;
    }
}
