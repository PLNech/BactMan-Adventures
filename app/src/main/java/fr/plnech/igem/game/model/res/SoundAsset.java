/*
=======================================================================
BactMan Adventures | Scientific popularisation through mini-games
Copyright (C) 2015 IONIS iGEM Team
Distributed under the GNU GPLv3 License.
(See file LICENSE.txt or copy at https://www.gnu.org/licenses/gpl.txt)
=======================================================================
*/

package fr.plnech.igem.game.model.res;

public class SoundAsset extends Asset {
    private final float volume;

    public SoundAsset(String filename, float volume) {
        super(filename);
        this.volume = volume;
    }

    public SoundAsset(String filename) {
        this(filename, 1.0f);
    }

    public float getVolume() {
        return volume;
    }
}
