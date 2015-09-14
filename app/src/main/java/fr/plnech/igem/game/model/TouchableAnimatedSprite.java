package fr.plnech.igem.game.model;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by PLNech on 25/08/2015.
 */
public class TouchableAnimatedSprite extends AnimatedSprite {

    private WorldObject object;

    public TouchableAnimatedSprite(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
                                   VertexBufferObjectManager pVertexBufferObjectManager, WorldObject pObject)
    {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        object = pObject;
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        return object != null && object.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }

    public void stopDragging() {
        object = null;
    }
}
