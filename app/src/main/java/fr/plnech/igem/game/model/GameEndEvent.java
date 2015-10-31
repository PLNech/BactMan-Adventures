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

class GameEndEvent extends CustomEvent{
    private static final String KEY_SCORE = "Score";
    private static final String KEY_SUCCESS = "Victory";

    public GameEndEvent(BaseGame game, int score, Boolean success) {
        this(game.toString() + "End");
        putCustomAttribute(KEY_SCORE, score);
        putCustomAttribute(KEY_SUCCESS, success ? "Win" : "Lose");
    }

    private GameEndEvent(String eventName) {
        super(eventName);
    }
}
