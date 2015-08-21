package com.ionis.igem.app.ui;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import com.ionis.igem.app.BinGame;
import com.ionis.igem.app.game.AbstractGameActivity;
import com.ionis.igem.app.game.model.BaseGame;
import com.ionis.igem.app.game.model.FontAsset;
import com.ionis.igem.app.game.model.GFXAsset;
import com.ionis.igem.app.game.model.HUDElement;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
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
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.HashMap;
import java.util.List;

public class GameActivity extends AbstractGameActivity {
    private static final String TAG = "GameActivity";
    private VertexBufferObjectManager vertexBufferObjectManager;

    private SmoothCamera gameCamera;

    private PhysicsWorld physicsWorld;
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
        gameBin = new BinGame(this);
        vertexBufferObjectManager = super.getVertexBufferObjectManager();
    }

    public VertexBufferObjectManager getVBOM() {
        return vertexBufferObjectManager;
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
            putFont(asset.toString(), font);
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
        loadPhysics(gameBin);

        for (int i = 0; i < LAYER_COUNT; i++) {
            final Entity layer = new Entity();
            layer.setZIndex(i);
            gameScene.attachChild(layer);
        }

        loadScene(gameBin);
        loadHUD(gameBin);

        Log.d(TAG, "onCreateScene - Scene created.");
        return gameScene;
    }

    private void loadPhysics(BaseGame game) {
        physicsWorld = new PhysicsWorld(game.getPhysicsVector(), false);
        physicsWorld.setContactListener(game.getContactListener());
        gameScene.registerUpdateHandler(physicsWorld);
    }

    private void loadHUD(BaseGame game) {
        List<HUDElement> elements = game.getHudElements();
        for (HUDElement element : elements) {
            gameScene.getChildByIndex(LAYER_HUD).attachChild(element.getText());
        }
    }

    private void loadScene(BaseGame game) {
        gameScene = game.prepareScene();
    }

    public IFont getFont(String fontName) {
        final IFont font = fontMap.get(fontName);
        if (font != null) {
            Log.d(TAG, "getFont - returning font " + fontName);
        } else {
            Log.d(TAG, "getFont - missing font " + fontName);
        }
        return font;
    }

    void putFont(String fontName, IFont font) {
        fontMap.put(fontName, font);
        Log.d(TAG, "putFont - Added font " + fontName);
    }

    public ITextureRegion getTextureRegion(String filename) {
        return textureMap.get(filename);
    }

    public void onLose() {
    }

    public void onWin() {
    }

    public Scene getScene() {
        return gameScene;
    }

    public PhysicsWorld getPhysicsWorld() {
        return physicsWorld;
    }
}
