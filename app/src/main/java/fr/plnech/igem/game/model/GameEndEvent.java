package fr.plnech.igem.game.model;

import com.crashlytics.android.answers.CustomEvent;

/**
 * Created by PLNech on 25/09/2015.
 */
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
