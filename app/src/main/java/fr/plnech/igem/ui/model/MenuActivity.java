/*
=======================================================================
BactMan Adventures | Scientific popularisation through mini-games
Copyright (C) 2015 IONIS iGEM Team
Distributed under the GNU GPLv3 License.
(See file LICENSE.txt or copy at https://www.gnu.org/licenses/gpl.txt)
=======================================================================
*/

package fr.plnech.igem.ui.model;

public abstract class MenuActivity extends LoggedActivity {
    private static final String TAG = "MenuActivity";

    protected final String getContentType() {
        return TAG;
    }
}
