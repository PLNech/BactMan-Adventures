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
import fr.plnech.igem.game.LandscapeGameActivity;

public abstract class LandscapeGame extends BaseGame {
    protected LandscapeGame(AbstractGameActivity pActivity) {
        super(pActivity);
    }

    @Override
    public int getWidth() {
        return LandscapeGameActivity.CAMERA_WIDTH;
    }

    @Override
    public int getHeight() {
        return LandscapeGameActivity.CAMERA_HEIGHT;
    }

    @Override
    public boolean isPortrait() {
        return false;
    }
}
