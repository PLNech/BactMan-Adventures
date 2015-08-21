package com.ionis.igem.app.ui;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.ionis.igem.app.BinGame;
import com.ionis.igem.app.game.BaseGameActivity;
import com.ionis.igem.app.game.bins.Bin;
import com.ionis.igem.app.game.bins.Item;
import com.ionis.igem.app.game.managers.ResMan;
import com.ionis.igem.app.game.model.FontAsset;
import com.ionis.igem.app.game.model.GFXAsset;
import com.ionis.igem.app.game.model.BaseGame;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import java.util.HashMap;

public class GameActivity extends BaseGameActivity {
    private static final String TAG = "GameActivity";

    private SmoothCamera gameCamera;
    private PhysicsWorld physicsWorld;

    private Text gameScoreText;
    private Text gameLivesText;

    private HashMap<CharSequence, ITextureRegion> textureMap = new HashMap<>();
    private HashMap<CharSequence, IFont> fontMap = new HashMap<>();

    private TextureManager textureManager;

    private BinGame gameBin;
    private FontManager fontManager;
    private AssetManager assetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate - Created Activity.");
        gameBin = new BinGame();

    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        gameCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, MAX_SPEED_X, MAX_SPEED_Y, MAX_ZOOM_CHANGE);
        final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), gameCamera);
        engineOptions.getTouchOptions().setNeedsMultiTouch(true);
        return engineOptions;
    }

    @Override
    public void onCreateResources() {
        Log.d(TAG, "onCreateResources - Beginning resource creation.");
        loadGFXAssets(gameBin);
        loadFonts(gameBin);
//        loadSounds();
    }

    private void loadGFXAssets(BaseGame game) {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        for (GFXAsset asset : game.getGraphicalAssets()) {
            loadGFXAsset(asset);
        }
        Log.d(TAG, "loadGraphic - Finished loading game assets.");
    }

    private void loadGFXAsset(GFXAsset asset) {
        if (textureManager == null) {
            textureManager = getTextureManager();
        }

        BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(textureManager, asset.getWidth(), asset.getHeight(), TextureOptions.BILINEAR);
        final String filename = asset.getFilename();
        final int textureX = asset.getTextureX();
        final int textureY = asset.getTextureY();
        if (asset.isTiled()) {
            final int tileC = asset.getTileColumns();
            final int tileR = asset.getTileRows();
            TiledTextureRegion tiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(textureAtlas, this, filename, textureX, textureY, tileC, tileR);
            textureMap.put(filename, tiledTextureRegion);
        } else {
            TextureRegion textureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(textureAtlas, this, filename, textureX, textureY);
            textureMap.put(filename, textureRegion);
        }

        textureAtlas.load();
    }

    private void loadFonts(BaseGame game) {
        FontFactory.setAssetBasePath("fonts/");

        if (fontManager == null) {
            fontManager = getFontManager();
        }
        if (assetManager == null) {
            assetManager = getAssets();
        }

        for (FontAsset asset : game.getFontAssets()) {
            Font font = loadFont(asset);
            font.load();
            fontMap.put(asset.toString(), font);
        }

        Log.d(TAG, "loadFonts - Finished loading fonts.");
    }

    private Font loadFont(FontAsset asset) {
        final ITexture fontTexture = new BitmapTextureAtlas(getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        return FontFactory.createFromAsset(fontManager, fontTexture, assetManager,
                asset.getFilename(), asset.getSize(), asset.isAntialised(), asset.getColor());
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

        createHUD();
        createBins();

        final ITextureRegion smileyTextureRegion = textureMap.get(ResMan.FACE_BOX_TILED);
        createItem(spriteCenter(smileyTextureRegion));
        createItem(spritePosition(smileyTextureRegion, 0.2f, 0.5f));

        gameScene.setTouchAreaBindingOnActionDownEnabled(true);

        Log.d(TAG, "onCreateScene - Scene created.");
        return gameScene;
    }

    private void initPhysics() {
        physicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
        physicsWorld.setContactListener(createContactListener());
        gameScene.registerUpdateHandler(physicsWorld);
    }

    private void createHUD() {
        final float textY = 0.05f;
        Pair<Float, Float> posScore = spritePosition(20f, 20f, 0.1f, textY);
        Log.d(TAG, "createHUD - Score text positioned at " + posScore.first + ", " + posScore.second);
        gameScoreText = new Text(posScore.first, posScore.second, fontRoboto, "Score: ", "Score: Over 9000.".length(), this.getVertexBufferObjectManager());
        gameScoreText.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        gameScoreText.setAlpha(0.5f);
        gameScene.getChildByIndex(LAYER_FOREGROUND).attachChild(gameScoreText);

        Pair<Float, Float> posLives = spritePosition(20f, 20f, 0.6f, textY);
        Log.d(TAG, "createHUD - Lives text positioned at " + posLives.first + ", " + posLives.second);
        gameLivesText = new Text(posLives.first, posLives.second, fontRoboto, "Lives: ", "Lives: GAME OVER".length(), this.getVertexBufferObjectManager());
        gameLivesText.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        gameLivesText.setAlpha(0.5f);
        gameScene.getChildByIndex(LAYER_FOREGROUND).attachChild(gameLivesText);
    }

    private void setScoreText(CharSequence text) {
        gameScoreText.setText("Score: " + text);
    }

    private void setLivesText(CharSequence text) {
        gameLivesText.setText("Lives: " + text);
    }

    private void createItem(Pair<Float, Float> pos) {
        createItem(pos.first, pos.second);
    }

    private void createItem(float posX, float posY) {
        final ITiledTextureRegion smileyTextureRegion = (ITiledTextureRegion) textureMap.get(ResMan.FACE_BOX_TILED);
        Item item = new Item(Item.Type.PAPER, smileyTextureRegion, posX, posY, this.getVertexBufferObjectManager(), physicsWorld);

        gameScene.getChildByIndex(LAYER_BACKGROUND).attachChild(item);
        gameScene.registerTouchArea(item);
    }

    private void createBin(Bin.Type type, ITextureRegion textureRegion, float posX, float posY) {
        Bin bin = new Bin(type, posX, posY, textureRegion, getVertexBufferObjectManager(), physicsWorld);
        gameScene.getChildByIndex(LAYER_FOREGROUND).attachChild(bin);
    }

    private void createBins() {
        final float binY = 0.85f;
        final ITextureRegion bin1TextureRegion = textureMap.get(ResMan.BIN1);
        final ITextureRegion bin2TextureRegion = textureMap.get(ResMan.BIN2);
        final ITextureRegion bin3TextureRegion = textureMap.get(ResMan.BIN3);
        final ITextureRegion bin4TextureRegion = textureMap.get(ResMan.BIN4);

        Pair<Float, Float> bin1Pos = spritePosition(bin1TextureRegion, 0.30f, binY, Bin.SCALE_DEFAULT);
        Pair<Float, Float> bin2Pos = spritePosition(bin2TextureRegion, 0.50f, binY, Bin.SCALE_DEFAULT);
        Pair<Float, Float> bin3Pos = spritePosition(bin3TextureRegion, 0.70f, binY, Bin.SCALE_DEFAULT);
        Pair<Float, Float> bin4Pos = spritePosition(bin4TextureRegion, 0.90f, binY, Bin.SCALE_DEFAULT);

        createBin(Bin.Type.GLASS, bin1TextureRegion, bin1Pos.first, bin1Pos.second);
        createBin(Bin.Type.LIQUIDS, bin2TextureRegion, bin2Pos.first, bin2Pos.second);
        createBin(Bin.Type.NORMAL, bin3TextureRegion, bin3Pos.first, bin3Pos.second);
        createBin(Bin.Type.BIO, bin4TextureRegion, bin4Pos.first, bin4Pos.second);
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
                        setLivesText("" + gameLives);
                        if (gameLives == 0) {
                            onLose();
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

    private void onWin() {
    }

}
