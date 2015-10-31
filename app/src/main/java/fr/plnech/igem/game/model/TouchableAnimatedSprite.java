/*
=======================================================================
BactMan Adventures | Scientific popularisation through mini-games
Copyright (C) 2015 IONIS iGEM Team
Distributed under the GNU GPLv3 License.
(See file LICENSE.txt or copy at https://www.gnu.org/licenses/gpl.txt)
=======================================================================
*/

package fr.plnech.igem.game.model;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

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
