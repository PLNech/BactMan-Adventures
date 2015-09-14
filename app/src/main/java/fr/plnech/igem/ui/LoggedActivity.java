package fr.plnech.igem.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;

/**
 * Created by PLNech on 14/09/2015.
 */
public abstract class LoggedActivity extends AppCompatActivity {
    private static final String TAG = "LoggedActivity";

    protected void logView() {
        final Resources resources = getResources();
        final String contentName = resources.getString(getTitleResId());
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentType(getContentType())
                .putContentName(contentName)
                .putContentId(resources.getResourceName(getLayoutResId())));
        Log.d(TAG, "logView - " + contentName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        logView();
    }

    abstract String getContentType();

    abstract int getTitleResId();
    abstract int getLayoutResId();
}
