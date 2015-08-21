package com.ionis.igem.app.game;

import android.util.Log;
import android.util.Pair;
import org.andengine.engine.Engine;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

/**
 * Created by PLN on 11/08/2015.
 */
public abstract class BaseGameActivity extends SimpleBaseGameActivity {

    private static final String TAG = "BaseGameActivity";

    private Boolean shouldStartEngine = false;

    protected static final int CAMERA_WIDTH = 480;
    protected static final int CAMERA_HEIGHT = 800;

    protected Scene gameScene;

    protected static final int LAYER_COUNT = 3;
    public static final int LAYER_BACKGROUND = 0;
    public static final int LAYER_FOREGROUND = LAYER_BACKGROUND + 1;
    protected static final int LAYER_HUD = LAYER_FOREGROUND + 1;

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

    public Pair<Float, Float> spriteCenter(ITextureRegion textureRegion) {
        /**
         * Returns the appropriate coordinates to center the given textureRegion in the game camera.
         */
        return spritePosition(textureRegion, 0.5f, 0.5f);
    }

    public Pair<Float, Float> spritePosition(ITextureRegion textureRegion,
                                             float positionRatioX, float positionRatioY,
                                             float ratio) {
        final float widthToRatio = textureRegion.getWidth() * ratio;
        final float heightToRatio = textureRegion.getHeight() * ratio;
        return spritePosition(new Pair<>(widthToRatio, heightToRatio), new Pair<>(positionRatioX, positionRatioY));
    }
    public Pair<Float, Float> spritePosition(ITextureRegion textureRegion, float positionRatioX, float positionRatioY) {
        final Pair<Float, Float> textureDimensions = new Pair<>(textureRegion.getWidth(), textureRegion.getHeight());
        return spritePosition(textureDimensions, new Pair<>(positionRatioX, positionRatioY));
    }

    protected Pair<Float, Float> spritePosition(ITextureRegion textureRegion, Pair<Float, Float> positionRatio) {
        return spritePosition(new Pair<>(textureRegion.getWidth(), textureRegion.getHeight()), positionRatio);
    }

    public Pair<Float,Float> spritePosition(float textureDimX, float textureDimY, float posRatioX, float posRatioY) {
        return spritePosition(new Pair<>(textureDimX, textureDimY),new Pair<>(posRatioX, posRatioY));
    }

    protected Pair<Float, Float> spritePosition(Pair<Float, Float> textureDims, Pair<Float, Float> positionRatio) {
        final Pair<Float, Float> res = new Pair<>(CAMERA_WIDTH  * positionRatio.first - textureDims.first / 2,
                CAMERA_HEIGHT * positionRatio.second - - textureDims.second / 2);
        Log.v(TAG, "spritePosition - Returning " + res.first + ", " + res.second);
        return res;
    }


}
