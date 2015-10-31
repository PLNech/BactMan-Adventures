package fr.plnech.igem.game.gut;

import fr.plnech.igem.game.model.WorldObject;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by PLNech on 04/09/2015.
 */
public class Flow extends WorldObject {
    public Flow(float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(50, pY, false, 2f, pTiledTextureRegion, pVertexBufferObjectManager);
    }
}
