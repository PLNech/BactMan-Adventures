/*
=======================================================================
BactMan Adventures | Scientific popularisation through mini-games
Copyright (C) 2015 IONIS iGEM Team
Distributed under the GNU GPLv3 License.
(See file LICENSE.txt or copy at https://www.gnu.org/licenses/gpl.txt)
=======================================================================
*/
package fr.plnech.igem.utils;

import java.util.Random;

public class CalcUtils {
    private static Random random;

    public static int randomOf(int max) {
        if (random == null) {
            random = new Random();
        }
        return randomOf(max, random);
    }
    public static int randomOf(int max, Random random) {
        return Math.abs(random.nextInt() % max);
    }
}
