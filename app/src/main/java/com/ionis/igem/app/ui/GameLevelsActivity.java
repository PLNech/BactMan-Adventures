package com.ionis.igem.app.ui;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;
import com.ionis.igem.app.game.BaseGameActivity;
import com.ionis.igem.app.game.Game;
import com.ionis.igem.app.utils.CroppedResolutionPolicy;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.color.Color;

import java.util.ArrayList;

public class GameLevelsActivity extends BaseGameActivity {
    private static final String TAG = "GameLevelsActivity";

    public static final int BACKGROUND_WIDTH = 1920;
    public static final int BACKGROUND_HEIGHT = 1440;
    private static final float MAX_SPEED_X = 200.0f;
    private static final float MAX_SPEED_Y = 200.0f;
    private static final float MAX_ZOOM_CHANGE = 0.8f;

    private SmoothCamera gameCamera;

    private TextureRegion backgroundTextureRegion;
    private TextureRegion joyauTextureRegion;
    private TextureRegion star2TextureRegion;
    private BitmapTextureAtlas estrellaTexture;

    private ArrayList<Game> gamesList;
    private float zoomFactor = 0.4f;
    private TiledTextureRegion smileyTextureRegion;
    private Pair<Float, Float> gameBGOrigin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gamesList = new ArrayList<>();
    }

    @Override
    public void onCreateResources() {
        final String msg = "onCreateResources - Beginning resource creation.";
        Log.d(TAG, msg);
        toastOnUIThread(msg, Toast.LENGTH_SHORT);
        loadGraphics();
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        gameCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, MAX_SPEED_X, MAX_SPEED_Y, MAX_ZOOM_CHANGE);
        gameCamera.setZoomFactorDirect(zoomFactor);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new CroppedResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), gameCamera);
    }

    private void loadGraphics() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        BitmapTextureAtlas backgroundTexture = new BitmapTextureAtlas(getTextureManager(), BACKGROUND_WIDTH, BACKGROUND_HEIGHT, TextureOptions.DEFAULT);
        backgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backgroundTexture, this, "Background_labo.png", 0, 0);
        backgroundTexture.load();

        BitmapTextureAtlas smileyTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 64, 32, TextureOptions.BILINEAR);
        smileyTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(smileyTextureAtlas, this, "face_box_tiled.png", 0, 0, 2, 1);
        smileyTextureAtlas.load();

        BitmapTextureAtlas joyauTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 562, 604, TextureOptions.BILINEAR);
        joyauTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(joyauTextureAtlas, this, "Joyau.png", 0, 0);
        joyauTextureAtlas.load();

        Log.d(TAG, "loadGraphics - Finished loading textures.");
    }

    @Override
    protected Scene onCreateScene() {
        super.onCreateScene();
        gameScene = new Scene();
        Color backgroundColor = new Color(0.89803f, 0.90196f, 0.89803f);
        gameScene.setBackground(new Background(backgroundColor));

        for (int i = 0; i < LAYER_COUNT; i++) {
            gameScene.attachChild(new Entity());
        }

        gameBGOrigin = spriteCenter(backgroundTextureRegion);
        final Sprite bgSprite = new Sprite(gameBGOrigin.first, gameBGOrigin.second, this.backgroundTextureRegion, this.getVertexBufferObjectManager());
        gameScene.getChildByIndex(LAYER_BACKGROUND).attachChild(bgSprite);


        final Pair<Float, Float> smileyCenter = spriteCenter(smileyTextureRegion);
        final AnimatedSprite face = new AnimatedSprite(smileyCenter.first, smileyCenter.second, smileyTextureRegion, this.getVertexBufferObjectManager());
        face.animate(100);
        gameScene.attachChild(face);
        Log.d(TAG, "onCreateScene - Posed smiley at " + smileyCenter.first + ", " + smileyCenter.second);

        gameScene.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                moveToFirstLevels();
            }
        }));

        return gameScene;
    }

    private void moveToFirstLevels() {
//        for (int i = 0; i < 100; i++) {
//            final AnimatedSprite smiley = new AnimatedSprite(gameBGOrigin.first + i * 25, gameBGOrigin.second + i * 25,
//                    smileyTextureRegion, this.getVertexBufferObjectManager());
//            smiley.animate(100);
//            gameScene.getChildByIndex(LAYER_FOREGROUND).attachChild(smiley);
//        }
        final float scale = 0.25f;

        Pair<Float, Float> posRel = spritePosition(joyauTextureRegion, new Pair<>(0.1f, 0.1f));
        final Sprite joyau = new Sprite(posRel.first, posRel.second, joyauTextureRegion, this.getVertexBufferObjectManager());
        joyau.setScale(scale);

        gameScene.getChildByIndex(LAYER_FOREGROUND).attachChild(joyau);

//        gameCamera.setCenter(joyauPos.first, joyauPos.second);
//        gameCamera.setZoomFactor(1.5f);
    }

    private Pair<Float, Float> spriteCenter(ITextureRegion textureRegion) {
        /**
         * Returns the appropriate coordinates to center the given textureRegion in the game camera.
         */
        return spritePosition(textureRegion, new Pair<>(0.5f, 0.5f));
    }

    private Pair<Float, Float> spriteOnSpriteCenter(Pair<Float, Float> secondSize, Pair<Float, Float> secondCoords, Pair<Float, Float> firstPos) {
        final float centeredX = secondCoords.first - secondSize.first / 2;
        final float centeredY = secondCoords.second - secondSize.second / 2;
        return spriteOnSprite(new Pair<>(centeredX, centeredY), firstPos);
    }

    private Pair<Float, Float> spriteOnSprite(Pair<Float, Float> secondCoords, Pair<Float, Float> firstPos) {
        /**
         * Returns absolute coordinates from first sprite's position on second and second sprite's position.
         */
        return new Pair<>(secondCoords.first + firstPos.first, secondCoords.second + firstPos.second);
    }

    private Pair<Float, Float> spritePosition(ITextureRegion textureRegion, Pair<Float, Float> positionRatio) {
        final Pair<Float, Float> textureDimensions = new Pair<>(textureRegion.getWidth(), textureRegion.getHeight());
        return spritePosition(textureDimensions, positionRatio);
    }

    private Pair<Float, Float> spritePosition(Pair<Float, Float> textureDims, Pair<Float, Float> positionRatio) {
        return new Pair<>((CAMERA_WIDTH - textureDims.first) * positionRatio.first,
                (CAMERA_HEIGHT - textureDims.second) * positionRatio.second);
    }


    private void addStar(final Game game, int pX, int pY) {

        final Sprite sprite = new Sprite(pX, pY, star2TextureRegion, getVertexBufferObjectManager()) {

            private boolean isTouched = true;

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                switch (pSceneTouchEvent.getAction()) {
                    case TouchEvent.ACTION_DOWN:
                        setScale(1.25f);
                        isTouched = true;
                        if (game.isAvailable()) {
                            toastOnUIThread("I would LOVE to launch this game!");
                            Log.i(TAG, "onAreaTouched - Ready to launch game " + game.getName());
                        } else {
                            toastOnUIThread("I'm afraid this game is still locked...");
                            Log.i(TAG, "onAreaTouched - Refusing to launch game " + game.getName());
                        }
                        GameLevelsActivity.this.toggleStar(game.getIndex());
                        break;
                    case TouchEvent.ACTION_UP:
                        if (isTouched) {
                            isTouched = false;
                            this.setScale(1.0f);
                        }
                        break;
                }
                return true;
            }
        };

        gameScene.getChildByIndex(LAYER_FOREGROUND).attachChild(sprite);
        gameScene.registerTouchArea(sprite);

        final int gameIndex = gamesList.size() - 1;
        gamesList.add(game.setIndex(gameIndex));
    }

    private void toggleStar(int i) {
        Game selectedGame = gamesList.get(i);
        this.estrellaTexture.clearTextureAtlasSources();
        selectedGame.setAvailability(!selectedGame.isAvailable());
        BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.estrellaTexture, this,
                selectedGame.isAvailable() ? "Estrella.png" : "Estrella_down.png", 0, 0);
        toastOnUIThread("Toggled!");
    }

}
