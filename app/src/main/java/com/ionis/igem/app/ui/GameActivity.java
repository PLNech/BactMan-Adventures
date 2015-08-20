package com.ionis.igem.app.ui;

import android.graphics.Color;
import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.ionis.igem.app.game.BaseGameActivity;
import com.ionis.igem.app.game.bins.Bin;
import com.ionis.igem.app.game.bins.Item;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
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

    private int gameScore = 0;
    private int gameLives = 3;

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
        Log.d(TAG, "onCreateResources - Beginning resource creation.");
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
        Log.d(TAG, "loadFonts - Finished loading fonts.");
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

        createScoreText();

        createBins();
        createItem(spriteCenter(smileyTextureRegion));
        createItem(spritePosition(smileyTextureRegion, new Pair<>(0.2f, 0.5f)));

        gameScene.setTouchAreaBindingOnActionDownEnabled(true);

        Log.d(TAG, "onCreateScene - Scene created.");
        return gameScene;
    }

    private void initPhysics() {
        physicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
        physicsWorld.setContactListener(createContactListener());
        gameScene.registerUpdateHandler(physicsWorld);
    }

    private void createScoreText() {
        Pair<Float, Float> posDebug = spritePosition(new Pair<>(20f, 20f), new Pair<>(0.1f, 0.1f));
        scoreText = new Text(posDebug.first, posDebug.second, fontRoboto, "State: ", "State: Long state being here.".length(), this.getVertexBufferObjectManager());
        scoreText.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        scoreText.setAlpha(0.5f);
        gameScene.getChildByIndex(LAYER_FOREGROUND).attachChild(scoreText);
    }

    private void setScoreText(CharSequence text) {
        scoreText.setText("State: " + text);
    }

    private void createItem(Pair<Float, Float> pos) {
        createItem(pos.first, pos.second);
    }

    private void createItem(float posX, float posY) {
        Item item = new Item(Item.Type.PAPER, smileyTextureRegion, posX, posY, this.getVertexBufferObjectManager(), physicsWorld);

        gameScene.getChildByIndex(LAYER_BACKGROUND).attachChild(item);
        gameScene.registerTouchArea(item);
    }

    private void createBins() {
        final VertexBufferObjectManager vertexBufferObjectManager = getVertexBufferObjectManager();
        Bin bin1 = new Bin(50, 300, bin1TextureRegion, Bin.Type.GLASS, vertexBufferObjectManager, physicsWorld);
        Bin bin2 = new Bin(200, 300, bin2TextureRegion, Bin.Type.LIQUIDS, vertexBufferObjectManager, physicsWorld);
        Bin bin3 = new Bin(350, 300, bin3TextureRegion, Bin.Type.DEFAULT, vertexBufferObjectManager, physicsWorld);
        Bin bin4 = new Bin(500, 300, bin4TextureRegion, Bin.Type.BIO, vertexBufferObjectManager, physicsWorld);

        final IEntity foreground = gameScene.getChildByIndex(LAYER_FOREGROUND);
        foreground.attachChild(bin1);
        foreground.attachChild(bin2);
        foreground.attachChild(bin3);
        foreground.attachChild(bin4);
    }

    private ContactListener createContactListener() {
        return new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                final Fixture x1 = contact.getFixtureA();
                final Fixture x2 = contact.getFixtureB();
                Bin bin;
                Item item;
                Log.d(TAG, "beginContact - Contact!");
                if (contact.isTouching()) {
                    Log.d(TAG, "beginContact - isTouching!");
                    if (Bin.isOne(x1)) {
                        bin = (Bin) x1.getBody().getUserData();
                        item = (Item) x2.getBody().getUserData();
                    } else if (Bin.isOne(x2)) {
                        bin = (Bin) x2.getBody().getUserData();
                        item = (Item) x1.getBody().getUserData();
                    } else {
                        /* Two items are touching. */
                        return;
                    }
                    Log.d(TAG, "beginContact - Item " + item.toString() + " went in bin " + bin.toString() + ".");
                    if (bin.accepts(item)) {
                        gameScore++;
                        Log.d(TAG, "beginContact - Increasing score to " + gameScore + ".");
                        setScoreText("" + gameScore);
                    } else {
                        gameLives--;
                        Log.d(TAG, "beginContact - Decreasing lives to " + gameLives + ".");
                        if (gameLives == 0) {
                            gameOver();
                        }
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        };
    }

    private void gameOver() {
        //TODO: Painfully handwritten method stub
    }

}
