package fr.plnech.igem.ui.model;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import fr.plnech.igem.ui.HomeActivity;
import io.fabric.sdk.android.Fabric;
import org.andengine.input.touch.TouchEvent;

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
            Log.d("DEBUG", this.getClass().getSimpleName() + " - Should not continue, pausing.");
            MusicManager.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("DEBUG", this.getClass().getSimpleName() + " - Resuming music...");
        MusicManager.start(this, MusicManager.MUSIC_MENU);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        We pause music in two cases: when the button pressed is menu, and when back is pressed on HomeActivity
        final boolean isNotHomeActivity = this.getClass().getSimpleName().equals("HomeActivity");
        if (isNotHomeActivity && event.getAction() == TouchEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK ) {
            Log.d("DEBUG", "onKeyDown - Action is DOWN and key is BACK,");
            setContinueMusic(true);
        }
        return super.onKeyDown(keyCode, event);
    }

    protected boolean shouldContinueMusic() {
        return continueMusic;
    }

    protected void setContinueMusic(boolean val) {
        continueMusic = val;
    }

    protected abstract String getContentType();

    public abstract int getTitleResId();
    public abstract int getLayoutResId();
}
