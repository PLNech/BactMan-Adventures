/*
=======================================================================
BactMan Adventures | Scientific popularisation through mini-games
Copyright (C) 2015 IONIS iGEM Team
Distributed under the GNU GPLv3 License.
(See file LICENSE.txt or copy at https://www.gnu.org/licenses/gpl.txt)
=======================================================================
*/

package fr.plnech.igem.game.gut;

import fr.plnech.igem.game.model.WorldObject;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Flow extends WorldObject {
    public Flow(float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(50, pY, false, 2f, pTiledTextureRegion, pVertexBufferObjectManager);
    }
}
