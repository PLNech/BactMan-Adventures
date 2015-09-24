package fr.plnech.igem.ui.model;

/**
 * Created by PLNech on 13/09/2015.
 */
public abstract class MenuActivity extends LoggedActivity {
    private static final String TAG = "MenuActivity";

    protected final String getContentType() {
        return TAG;
    }
}
