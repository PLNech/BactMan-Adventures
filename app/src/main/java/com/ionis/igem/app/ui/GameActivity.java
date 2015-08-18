package com.ionis.igem.app.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.ionis.igem.app.game.BaseGameActivity;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class GameActivity extends BaseGameActivity {
    private static final String TAG = "GameActivity";

    private TextureRegion bin1TextureRegion;
    private BitmapTextureAtlas bin1Texture;
    private TextureRegion bin2TextureRegion;
    private BitmapTextureAtlas bin2Texture;
    private TextureRegion bin3TextureRegion;
    private BitmapTextureAtlas bin3Texture;
    private TextureRegion bin4TextureRegion;
    private BitmapTextureAtlas bin4Texture;
    private SmoothCamera gameCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate - Created Activity.");
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        gameCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, MAX_SPEED_X, MAX_SPEED_Y, MAX_ZOOM_CHANGE);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), gameCamera);
    }

    @Override
    public void onCreateResources() {
        final String msg = "onCreateResources - Beginning resource creation.";
        Log.d(TAG, msg);
        toastOnUIThread(msg, Toast.LENGTH_SHORT);
        loadGraphics();
//        loadFonts();
//        loadSounds();
    }

    private void loadGraphics() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        bin1Texture = new BitmapTextureAtlas(getTextureManager(), 172, 275, TextureOptions.DEFAULT);
        bin1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bin1Texture, this, "bin1.png", 0, 0);
        bin1Texture.load();

        bin2Texture = new BitmapTextureAtlas(getTextureManager(), 169, 256, TextureOptions.DEFAULT);
        bin2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bin2Texture, this, "bin2.png", 0, 0);
        bin2Texture.load();

        bin3Texture = new BitmapTextureAtlas(getTextureManager(), 200, 255, TextureOptions.DEFAULT);
        bin3TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bin3Texture, this, "bin3.png", 0, 0);
        bin3Texture.load();

        bin4Texture = new BitmapTextureAtlas(getTextureManager(), 173, 260, TextureOptions.DEFAULT);
        bin4TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bin4Texture, this, "bin4.png", 0, 0);
        bin4Texture.load();

        Log.d(TAG, "loadGraphic - Finished loading background texture.");
    }

//    private void loadFonts() {
//        FontFactory.setAssetBasePath("fonts/");
//        final ITexture fontTexture = new BitmapTextureAtlas(getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//        FontAgencyB = FontFactory.createFromAsset(getFontManager(), fontTexture, getAssets(), "AGENCYB.TTF", 40, true, Color.BLACK);
//        final String msg = "loadFonts - Finished loading fonts.";
//        Log.d(TAG, msg);
//        toastOnUIThread(msg, Toast.LENGTH_SHORT);
//    }
//
//    private void loadSounds() {
//        try {
//            SoundFactory.setAssetBasePath("mfx/");
//            SoundExplosion = SoundFactory.createSoundFromAsset(getEngine().getSoundManager(), getApplicationContext(), "explosion3.ogg");
//            Log.d(TAG, "loadSounds - Finished loading sounds.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected Scene onCreateScene() {
        super.onCreateScene();

        gameScene = new Scene();

        final Background backgroundColor = new Background(0.96862f, 0.77647f, 0.37647f);
        gameScene.setBackground(backgroundColor);

        for (int i = 0; i < LAYER_COUNT; i++) {
            gameScene.attachChild(new Entity());
        }

        createBins();

        final String msg = "onCreateScene - Scene created.";
        Log.d(TAG, msg);
        toastOnUIThread(msg);
        return gameScene;
    }

    private void createBins() {
        final VertexBufferObjectManager vertexBufferObjectManager = getVertexBufferObjectManager();
        Sprite bin1Sprite = new Sprite(50, 300, bin1TextureRegion, vertexBufferObjectManager);
        Sprite bin2Sprite = new Sprite(200, 300, bin2TextureRegion, vertexBufferObjectManager);
        Sprite bin3Sprite = new Sprite(350, 300, bin3TextureRegion, vertexBufferObjectManager);
        Sprite bin4Sprite = new Sprite(500, 300, bin4TextureRegion, vertexBufferObjectManager);

        bin1Sprite.setScale(0.5f);
        bin2Sprite.setScale(0.5f);
        bin3Sprite.setScale(0.5f);
        bin4Sprite.setScale(0.5f);

        final IEntity foreground = gameScene.getChildByIndex(LAYER_FOREGROUND);
        foreground.attachChild(bin1Sprite);
        foreground.attachChild(bin2Sprite);
        foreground.attachChild(bin3Sprite);
        foreground.attachChild(bin4Sprite);
    }

}
