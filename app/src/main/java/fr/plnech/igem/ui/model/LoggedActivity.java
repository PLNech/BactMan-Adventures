package fr.plnech.igem.ui.model;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import fr.plnech.igem.R;
import fr.plnech.igem.ui.HomeActivity;
import io.fabric.sdk.android.Fabric;

/**
 * Created by PLNech on 14/09/2015.
 */
public abstract class LoggedActivity extends AppCompatActivity {
    private static final String TAG = "LoggedActivity";
    private boolean continueMusic = true;

    protected void logView() {
        final Resources resources = getResources();
        final String contentName = resources.getString(getTitleResId());
        final String contentType = getContentType();
        final String contentId = resources.getResourceName(getLayoutResId()).replace("fr.plnech.igem:layout/", "");
        logView(contentName, contentType, contentId, this);
    }

    public static void logView(String contentName, String contentType, String contentId, Context c) {
        if (!Fabric.isInitialized()) HomeActivity.initFabric(c);
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentType(contentType)
                .putContentName(contentName)
                .putContentId(contentId));
        Log.d(TAG, "logView - " + contentType + ": " + contentName + "(" + contentId + ")");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        logView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!shouldContinueMusic()) {
            MusicManager.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicManager.start(this, MusicManager.MUSIC_MENU);
    }

    @Override
    protected void onUserLeaveHint() {
        MusicManager.pause();
        super.onUserLeaveHint();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//      We pause music in several cases:
//      - when the button pressed is menu, and when back is pressed on HomeActivity
        final boolean isNotHomeActivity = this.getClass().getSimpleName().equals("HomeActivity");
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (isNotHomeActivity) {
                    setContinueMusic(true);
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
//      We pause music in several cases:
//      - when the button pressed is menu, and when back is pressed on HomeActivity
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
            case KeyEvent.KEYCODE_HOME:
                MusicManager.pause();
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    protected boolean shouldContinueMusic() {
        return continueMusic;
    }

    protected void setContinueMusic(boolean val) {
        continueMusic = val;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    protected abstract String getContentType();

    public abstract int getTitleResId();

    public abstract int getLayoutResId();
}
