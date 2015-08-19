package com.ionis.igem.app.ui;

import android.graphics.Color;
import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;
import com.badlogic.gdx.math.Vector2;
import com.ionis.igem.app.game.BaseGameActivity;
import com.ionis.igem.app.game.bins.Item;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class GameActivity extends BaseGameActivity {
    private static final String TAG = "GameActivity";

    private TextureRegion bin1TextureRegion;
    private TextureRegion bin2TextureRegion;
    private TextureRegion bin3TextureRegion;
    private TextureRegion bin4TextureRegion;
    private TiledTextureRegion smileyTextureRegion;

    private SmoothCamera gameCamera;
    private PhysicsWorld physicsWorld;

    private Text scoreText;
    private Font fontRoboto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate - Created Activity.");
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        gameCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, MAX_SPEED_X, MAX_SPEED_Y, MAX_ZOOM_CHANGE);
        final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), gameCamera);
        engineOptions.getTouchOptions().setNeedsMultiTouch(true);
        return engineOptions;
    }

    @Override
    public void onCreateResources() {
        final String msg = "onCreateResources - Beginning resource creation.";
        Log.d(TAG, msg);
        toastOnUIThread(msg, Toast.LENGTH_SHORT);
        loadGraphics();
        loadFonts();
//        loadSounds();
    }

    private void loadGraphics() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        BitmapTextureAtlas bin1TextureAtlas = new BitmapTextureAtlas(getTextureManager(), 172, 275, TextureOptions.DEFAULT);
        bin1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bin1TextureAtlas, this, "bin1.png", 0, 0);
        bin1TextureAtlas.load();

        BitmapTextureAtlas bin2TextureAtlas = new BitmapTextureAtlas(getTextureManager(), 169, 256, TextureOptions.DEFAULT);
        bin2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bin2TextureAtlas, this, "bin2.png", 0, 0);
        bin2TextureAtlas.load();

        BitmapTextureAtlas bin3TextureAtlas = new BitmapTextureAtlas(getTextureManager(), 200, 255, TextureOptions.DEFAULT);
        bin3TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bin3TextureAtlas, this, "bin3.png", 0, 0);
        bin3TextureAtlas.load();

        BitmapTextureAtlas bin4TextureAtlas = new BitmapTextureAtlas(getTextureManager(), 173, 260, TextureOptions.DEFAULT);
        bin4TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bin4TextureAtlas, this, "bin4.png", 0, 0);
        bin4TextureAtlas.load();

        BitmapTextureAtlas smileyTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 64, 32, TextureOptions.BILINEAR);
        smileyTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(smileyTextureAtlas, this, "face_box_tiled.png", 0, 0, 2, 1);
        smileyTextureAtlas.load();

        Log.d(TAG, "loadGraphic - Finished loading background texture.");
    }

    private void loadFonts() {
        FontFactory.setAssetBasePath("fonts/");
        final ITexture fontTexture = new BitmapTextureAtlas(getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        fontRoboto = FontFactory.createFromAsset(getFontManager(), fontTexture, getAssets(), "Roboto-Thin.ttf", 40, true, Color.BLACK);
        fontRoboto.load();
        final String msg = "loadFonts - Finished loading fonts.";
        Log.d(TAG, msg);
        toastOnUIThread(msg, Toast.LENGTH_SHORT);
    }

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
        gameScene.setOnAreaTouchTraversalFrontToBack();

        final Background backgroundColor = new Background(0.96862f, 0.77647f, 0.37647f);
        gameScene.setBackground(backgroundColor);

        initPhysics();

        for (int i = 0; i < LAYER_COUNT; i++) {
            final Entity layer = new Entity();
            layer.setZIndex(i);
            gameScene.attachChild(layer);
        }

        createTextDebug();

        createBins();
        createItem(spriteCenter(smileyTextureRegion));
        createItem(spritePosition(smileyTextureRegion, new Pair<>(0.2f, 0.5f)));

        gameScene.setTouchAreaBindingOnActionDownEnabled(true);

        final String msg = "onCreateScene - Scene created.";
        Log.d(TAG, msg);
        toastOnUIThread(msg);
        return gameScene;
    }

    private void initPhysics() {
        physicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
        gameScene.registerUpdateHandler(physicsWorld);
    }

    private void createTextDebug() {
        Pair<Float, Float> posDebug = spritePosition(new Pair<>(20f, 20f), new Pair<>(0.1f, 0.1f));
        scoreText = new Text(posDebug.first, posDebug.second, fontRoboto, "State: ", "State: Long state being here.".length(), this.getVertexBufferObjectManager());
        scoreText.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        scoreText.setAlpha(0.5f);
        gameScene.attachChild(scoreText);
    }

    private void setTextDebug(CharSequence text) {
        scoreText.setText("State: " + text);
    }

    private void createItem(Pair<Float, Float> pos) {
        createItem(pos.first, pos.second);
    }

    private void createItem(float posX, float posY) {
        Item item = new Item(smileyTextureRegion, posX, posY, this.getVertexBufferObjectManager(), physicsWorld);

        gameScene.getChildByIndex(LAYER_BACKGROUND).attachChild(item);
        gameScene.registerTouchArea(item);
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
