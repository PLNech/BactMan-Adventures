package fr.plnech.igem.game.model;

import com.crashlytics.android.answers.CustomEvent;

/**
 * Created by PLNech on 25/09/2015.
 */
public class GlossaryReadEvent extends CustomEvent{
    private static final String TAG = "GlossaryReadEvent";
    public static final String KEY_ENTRY = "Entry";

    public GlossaryReadEvent(String entryName) {
        super(TAG);
        putCustomAttribute(KEY_ENTRY, entryName);
    }

}
