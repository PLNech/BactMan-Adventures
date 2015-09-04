package com.ionis.igem.app.game.gut;

import com.ionis.igem.app.game.model.WorldObject;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Paul-Louis Nech on 04/09/2015.
 */
public class Flow extends WorldObject {
    public Flow(float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(0, pY, false, 2f, null, pTiledTextureRegion, pVertexBufferObjectManager);
    }
}
