/*
=======================================================================
BactMan Adventures | Scientific popularisation through mini-games
Copyright (C) 2015 IONIS iGEM Team
Distributed under the GNU GPLv3 License.
(See file LICENSE.txt or copy at https://www.gnu.org/licenses/gpl.txt)
=======================================================================
*/

package fr.plnech.igem.game.model.res;

public class Asset {
    final String filename;

    Asset(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
