package fr.plnech.igem.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.crashlytics.android.Crashlytics;
import fr.plnech.igem.R;
import fr.plnech.igem.ui.model.LoggedActivity;
import fr.plnech.igem.ui.model.MusicManager;
import io.fabric.sdk.android.Fabric;

public class HomeActivity extends LoggedActivity implements SurfaceHolder.Callback {
    private static final String TAG = "HomeActivity";
    public static final long TIME_PRESENTATION_GMT = 1443360600000L;
    private MediaPlayer mPlayer;

    @InjectView(R.id.videoView)
    protected SurfaceView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFabric(this);
        ButterKnife.inject(this);

        SurfaceHolder holder = videoView.getHolder();
        holder.addCallback(this);

        alertAboutEvent();
    }

    private void alertAboutEvent() {
        final long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis <= TIME_PRESENTATION_GMT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.msg_event_title))
                    .setMessage(getString(R.string.msg_event_message))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.msg_event_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .create().show();
        }
    }

    @Override
    protected String getContentType() {
        return TAG;
    }

    @Override
    public int getTitleResId() {
        return R.string.app_name;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_home;
    }

    @OnClick(R.id.button_home_new_game)
    protected void onClickNewGame() {
        startActivity(new Intent(this, GameMenuActivity.class));
    }

    @OnClick(R.id.button_home_team)
    protected void onClickTeam() {
        startActivity(new Intent(this, UsActivity.class));
    }

    @OnClick(R.id.button_home_iGEM)
    protected void onClickIGEM() {
        startActivity(new Intent(this, iGEMActivity.class));
    }

    @OnClick(R.id.button_home_credits)
    protected void onClickCredits() {
        startActivity(new Intent(this, CreditsActivity.class));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        final Display defaultDisplay = getWindowManager().getDefaultDisplay();
        int screenWidth;

        if (Build.VERSION.SDK_INT >= 13) {
            Point screenSize = new Point();
            defaultDisplay.getSize(screenSize);
            screenWidth = screenSize.x;
        } else {
            //noinspection deprecation
            screenWidth = defaultDisplay.getWidth();
        }

        ViewGroup.LayoutParams lp = videoView.getLayoutParams();
        lp.width = screenWidth;
        lp.height = (int) (((float) videoView.getHeight() / (float) videoView.getWidth()) * (float) screenWidth);
        videoView.setLayoutParams(lp);

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.animation);
        mPlayer = MediaPlayer.create(getApplicationContext(), uri, holder);
        if (mPlayer != null) {
            mPlayer.setLooping(true);
            mPlayer.start();
        }
        Log.d(TAG, "onCreate - MediaPlayer started.");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed - Destroying MediaPlayer.");
        if (mPlayer != null) {
            mPlayer.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicManager.release();
    }

    public static void initFabric(Context context) {
        /* Disabled on DEBUG builds */
//        final CrashlyticsCore crashlyticsCore = new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build();
//        Fabric.with(this, new Crashlytics.Builder().core(crashlyticsCore).build());
        /* Normal */
        Fabric.with(context, new Crashlytics());
        /* Debug mode */
//        final Fabric fabric = new Fabric.Builder(this)
//                .kits(new Crashlytics())
//                .debuggable(true)
//                .build();
//        Fabric.with(fabric);
    }
}
