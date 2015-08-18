package com.ionis.igem.app.game;

import android.util.Log;
import org.andengine.engine.Engine;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.ui.activity.SimpleBaseGameActivity;

/**
 * Created by PLN on 11/08/2015.
 */
public abstract class BaseGameActivity extends SimpleBaseGameActivity {

    private static final String TAG = "BaseGameActivity";

    private Boolean shouldStartEngine = false;

    protected static final int CAMERA_WIDTH = 800;
    protected static final int CAMERA_HEIGHT = 480;

    protected Scene gameScene;

    protected static final int LAYER_COUNT = 2;
    protected static final int LAYER_BACKGROUND = 0;
    protected static final int LAYER_FOREGROUND = LAYER_BACKGROUND + 1;

    protected static final float MAX_SPEED_X = 200.0f;
    protected static final float MAX_SPEED_Y = 200.0f;
    protected static final float MAX_ZOOM_CHANGE = 0.8f;


    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions) {
        Engine engine = new Engine(pEngineOptions);
        if (shouldStartEngine) {
            Log.v(TAG, "onCreateEngine - Starting engine.");
            engine.start();
            shouldStartEngine = !shouldStartEngine;
        } else {
            Log.v(TAG, "onCreateEngine - Not starting engine.");
        }
        return engine;
    }


    @Override
    public synchronized void onResumeGame() {
        if (mEngine != null) {
            super.onResumeGame();
            shouldStartEngine = true;
            Log.v(TAG, "onResumeGame - We have an engine: marked as starting.");
        } else {
            Log.v(TAG, "onResumeGame - No engine to start.");
        }
    }


    @Override
    protected Scene onCreateScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger());

        return null;
    }
}
