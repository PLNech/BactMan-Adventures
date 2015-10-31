/*
=======================================================================
BactMan Adventures | Scientific popularisation through mini-games
Copyright (C) 2015 IONIS iGEM Team
Distributed under the GNU GPLv3 License.
(See file LICENSE.txt or copy at https://www.gnu.org/licenses/gpl.txt)
=======================================================================
*/

package fr.plnech.igem.game.bins;

import android.util.Log;
import android.util.Pair;
import com.badlogic.gdx.physics.box2d.Fixture;
import fr.plnech.igem.game.model.PhysicalWorldObject;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Bin extends PhysicalWorldObject {

    private static final String TAG = "Bin";

    public enum Type {
        NORMAL, GLASS, BIO, LIQUIDS
    }

    private static short ID = 0;
    public static final float SCALE_DEFAULT = 0.17f;

    private final int id;
    private final Type type;

    public Bin(Type binType, float pX, float pY, ITiledTextureRegion pTextureRegion,
               VertexBufferObjectManager pVertexBufferObject, PhysicsWorld physicsWorld) {
        super(new PhysicalWorldObject.Builder(pX, pY, pTextureRegion, pVertexBufferObject, physicsWorld)
                .angle(0).draggable(false).scaleDefault(SCALE_DEFAULT));
        Log.v(TAG, "Bin - Created at " + pX + ", " + pY);

        id = ID++;
        this.type = binType;
    }


    public static boolean isOne(Fixture x1) {
        return x1.getBody().getUserData() instanceof Bin;
    }

    public boolean accepts(Item item) {
        return item.getType().getValid().equals(type);
    }

    @Override
    protected Pair<Boolean, Boolean> getBodyUpdates() {
        return new Pair<>(true, false);
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

    @Override
    public float getScaleDefault() {
        return SCALE_DEFAULT;
    }
}
