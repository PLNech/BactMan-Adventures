package com.ionis.igem.app.game;

import android.util.Log;
import com.badlogic.gdx.math.Vector2;
import org.andengine.engine.Engine;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

/**
 * Created by PLN on 11/08/2015.
 */
public abstract class AbstractGameActivity extends SimpleBaseGameActivity {

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

    public Vector2 spriteCenter(ITextureRegion textureRegion) {
        /**
         * Returns the appropriate coordinates to center the given textureRegion in the game camera.
         */
        return spritePosition(textureRegion, 0.5f, 0.5f);
    }

    public Vector2 spritePosition(ITextureRegion textureRegion,
                                  float positionRatioX, float positionRatioY,
                                  float ratio) {
        final float widthToRatio = textureRegion.getWidth() * ratio;
        final float heightToRatio = textureRegion.getHeight() * ratio;
        return spritePosition(new Vector2(widthToRatio, heightToRatio), new Vector2(positionRatioX, positionRatioY));
    }

    public Vector2 spritePosition(ITextureRegion textureRegion, float positionRatioX, float positionRatioY) {
        final Vector2 textureDimensions = new Vector2(textureRegion.getWidth(), textureRegion.getHeight());
        return spritePosition(textureDimensions, new Vector2(positionRatioX, positionRatioY));
    }

    protected Vector2 spritePosition(ITextureRegion textureRegion, Vector2 positionRatio) {
        return spritePosition(new Vector2(textureRegion.getWidth(), textureRegion.getHeight()), positionRatio);
    }

    public Vector2 spritePosition(float textureDimX, float textureDimY, float posRatioX, float posRatioY) {
        return spritePosition(new Vector2(textureDimX, textureDimY), new Vector2(posRatioX, posRatioY));
    }

    protected Vector2 spritePosition(Vector2 textureDims, Vector2 positionRatio) {
        final Vector2 res = new Vector2(CAMERA_WIDTH * positionRatio.x - textureDims.x / 2,
                CAMERA_HEIGHT * positionRatio.y - -textureDims.y / 2);
        Log.v(TAG, "spritePosition - Returning " + res.x + ", " + res.y);
        return res;
    }


}
