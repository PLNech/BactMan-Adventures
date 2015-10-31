/*
=======================================================================
BactMan Adventures | Scientific popularisation through mini-games
Copyright (C) 2015 IONIS iGEM Team
Distributed under the GNU GPLv3 License.
(See file LICENSE.txt or copy at https://www.gnu.org/licenses/gpl.txt)
=======================================================================
*/

package fr.plnech.igem.game.piano;

import fr.plnech.igem.game.AbstractGameActivity;
import fr.plnech.igem.game.managers.ResMan;
import fr.plnech.igem.game.model.WorldObject;

public class Polymerase extends WorldObject {

    public Polymerase(float pX, float pY, AbstractGameActivity activity) {
        super(pX, pY, false, SCALE_DEFAULT, activity.getTexture(ResMan.PIANO_POLY), activity.getVBOM());
        sprite.setScaleCenter(sprite.getScaleCenterX() * SCALE_DEFAULT,
                sprite.getScaleCenterY() * SCALE_DEFAULT); //TODO: Correct scaleCenter and position of all WorldObjects
        sprite.animate(100);
    }
}
