/*
=======================================================================
BactMan Adventures | Scientific popularisation through mini-games
Copyright (C) 2015 IONIS iGEM Team
Distributed under the GNU GPLv3 License.
(See file LICENSE.txt or copy at https://www.gnu.org/licenses/gpl.txt)
=======================================================================
*/

package fr.plnech.igem.game.model;

import fr.plnech.igem.game.AbstractGameActivity;
import fr.plnech.igem.game.PortraitGameActivity;

public abstract class PortraitGame extends BaseGame {
    protected PortraitGame(AbstractGameActivity pActivity) {
        super(pActivity);
    }

    @Override
    public int getWidth() {
        return PortraitGameActivity.CAMERA_WIDTH;
    }

    @Override
    public int getHeight() {
        return PortraitGameActivity.CAMERA_HEIGHT;
    }

    @Override
    public boolean isPortrait() {
        return true;
    }
}
