/*
=======================================================================
BactMan Adventures | Scientific popularisation through mini-games
Copyright (C) 2015 IONIS iGEM Team
Distributed under the GNU GPLv3 License.
(See file LICENSE.txt or copy at https://www.gnu.org/licenses/gpl.txt)
=======================================================================
*/

package fr.plnech.igem.game.model.res;

import android.graphics.Color;

/**
 * Created by PLNech on 21/08/2015.
 */
public class FontAsset extends Asset {

    private final int size;
    private final int color;
    private final boolean antialised;

    public FontAsset(String filename, int size, int color, boolean antialised) {
        super(filename);
        this.size = size;
        this.color = color;
        this.antialised = antialised;
    }

    public FontAsset(String filename, int size, boolean antialised) {
        this(filename, size, Color.WHITE, antialised);
    }

    public static String name(String filename, int size, int color, boolean antialised) {
        return "FontAsset{" +
                "antialised=" + antialised +
                ", filename=" + filename +
                ", size=" + size +
                ", color=" + color +
                '}';
    }

    @Override
    public String toString() {
        return "FontAsset{" +
                "antialised=" + antialised +
                ", filename=" + filename +
                ", size=" + size +
                ", color=" + color +
                '}';
    }

    public int getSize() {
        return size;
    }

    public boolean isAntialised() {
        return antialised;
    }

    public int getColor() {
        return color;
    }
}
