/*
=======================================================================
BactMan Adventures | Scientific popularisation through mini-games
Copyright (C) 2015 IONIS iGEM Team
Distributed under the GNU GPLv3 License.
(See file LICENSE.txt or copy at https://www.gnu.org/licenses/gpl.txt)
=======================================================================
*/

package fr.plnech.igem.game.model;

import com.crashlytics.android.answers.CustomEvent;

public class GlossaryReadEvent extends CustomEvent{
    private static final String TAG = "GlossaryReadEvent";
    private static final String KEY_ENTRY = "Entry";

    public GlossaryReadEvent(String entryName) {
        super(TAG);
        putCustomAttribute(KEY_ENTRY, entryName);
    }

}
