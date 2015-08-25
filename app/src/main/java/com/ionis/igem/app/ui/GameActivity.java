package com.ionis.igem.app.ui;

import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.ionis.igem.app.BinGame;
import com.ionis.igem.app.game.AbstractGameActivity;
import com.ionis.igem.app.game.managers.ResMan;
import com.ionis.igem.app.game.model.BaseGame;
import com.ionis.igem.app.game.model.HUDElement;
import com.ionis.igem.app.game.model.PhysicalWorldObject;
import com.ionis.igem.app.game.model.res.FontAsset;
import com.ionis.igem.app.game.model.res.GFXAsset;
import com.ionis.igem.app.game.ui.DitheredSprite;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.shape.IShape;
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
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameActivity extends AbstractGameActivity implements MenuScene.IOnMenuItemClickListener {
    private static final String TAG = "GameActivity";
    public static final float SPLASH_DURATION = 0.5f;

    private static final int OPTION_RESET = 0;
    private static final int OPTION_QUIT = OPTION_RESET + 1;


    private VertexBufferObjectManager vertexBufferObjectManager;
    private TextureManager textureManager;
    private FontManager fontManager;
    private AssetManager assetManager;

    private SmoothCamera gameCamera;
    private PhysicsWorld physicsWorld;

    private HashMap<CharSequence, ITiledTextureRegion> textureMap = new HashMap<>();
    private HashMap<CharSequence, IFont> fontMap = new HashMap<>();

    private BaseGame currentGame;

    private Scene splashScene;
    private MenuScene menuScene;

    private ArrayList<PhysicalWorldObject> objectsToDelete = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate - Created Activity.");
        currentGame = new BinGame(this);

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        FontFactory.setAssetBasePath("fonts/");

        if (vertexBufferObjectManager == null) {
            vertexBufferObjectManager = super.getVertexBufferObjectManager();
        }

        if (textureManager == null) {
            textureManager = getTextureManager();
        }

        if (fontManager == null) {
            fontManager = getFontManager();
        }
        if (assetManager == null) {
            assetManager = getAssets();
        }
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
        loadSplashScene();
    }

    @Override
    protected Scene onCreateScene() {
        super.onCreateScene();

        initSplashScene();

        loadMenuPause();
        initMenuPause();

        Log.d(TAG, "onCreateScene - Splash Scene created.");

        mEngine.registerUpdateHandler(new TimerHandler(SPLASH_DURATION, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mEngine.unregisterUpdateHandler(pTimerHandler);

                initGameScene();
                Log.d(TAG, "onTimePassed - Game Scene created.");
            }
        }));

        return splashScene;
    }

    @Override
    public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
        if (gameScene != null && menuScene != null &&
                pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
            if (gameScene.hasChildScene()) {
                /* Remove the menu and reset it. */
                menuScene.back();
            } else {
                /* Attach the menu. */
                gameScene.setChildScene(menuScene, false, true, true);
            }
            return true;
        } else {
            return super.onKeyDown(pKeyCode, pEvent);
        }
    }

    @Override
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
        switch (pMenuItem.getID()) {
            case OPTION_QUIT:
                onQuit();
                return true;
            case OPTION_RESET:
                currentGame.resetGame();
                return true;
        }
        return false;
    }

    private void onQuit() {
        this.finish();
    }

    private void loadGFXAssets(BaseGame game) {
        for (GFXAsset asset : game.getGraphicalAssets()) {
            loadGFXAsset(asset);
        }
        Log.d(TAG, "loadGraphic - Finished loading game assets.");
    }

    private void loadGFXAsset(GFXAsset asset) {
        BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(textureManager, asset.getWidth(), asset.getHeight(), TextureOptions.BILINEAR);
        final String filename = asset.getFilename();
        final int textureX = asset.getTextureX();
        final int textureY = asset.getTextureY();
        final int tileC = asset.getTileColumns();
        final int tileR = asset.getTileRows();
        TiledTextureRegion tiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(textureAtlas, this, filename, textureX, textureY, tileC, tileR);
        putTexture(filename, tiledTextureRegion);

        textureAtlas.load();
    }

    private void loadFonts(BaseGame game) {

        for (FontAsset asset : game.getFontAssets()) {
            Font font = loadFont(asset);
            font.load();
            putFont(asset.toString(), font);
        }

        Log.d(TAG, "loadFonts - Finished loading fonts.");
    }

    private Font loadFont(FontAsset asset) {
        final ITexture fontTexture = new BitmapTextureAtlas(textureManager, 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
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

    private void loadHUD(BaseGame game) {
        List<HUDElement> elements = game.getHudElements();
        final IEntity layerHUD = gameScene.getChildByIndex(LAYER_HUD);
        final IEntity layerText = gameScene.getChildByIndex(LAYER_HUD_TEXT);
        for (HUDElement element : elements) {
            layerHUD.attachChild(element.getSprite());
            layerText.attachChild(element.getText());
        }
    }

    private void loadPhysics(BaseGame game) {
        final ContactListener contactListener = game.getContactListener();
        if (contactListener == null) {
            return;
        }

        physicsWorld = new PhysicsWorld(game.getPhysicsVector(), false) {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                if (!physicsWorld.isLocked()) {
                    for (PhysicalWorldObject object : objectsToDelete) {
                        destroyBody(object.getBody(), object);
                    }
                    objectsToDelete.clear();
                }
            }

            private void destroyBody(final Body body, final IShape mask) {
                unregisterPhysicsConnector(physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(mask));
                body.setActive(false);
                destroyBody(body);
            }
        };
        physicsWorld.setContactListener(contactListener);
        gameScene.registerUpdateHandler(physicsWorld);
    }

    private void loadSplashScene() {
        BitmapTextureAtlas splashTextureAtlas = new BitmapTextureAtlas(textureManager, 349, 512, TextureOptions.DEFAULT);
        TiledTextureRegion splashTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(splashTextureAtlas, this, ResMan.SPLASH, 0, 0, 1, 1);
        splashTextureAtlas.load();
        putTexture(ResMan.SPLASH, splashTextureRegion);
    }

    private void initSplashScene() {
        splashScene = new Scene();

        final ITextureRegion splashTexture = getTexture(ResMan.SPLASH);
        final Vector2 center = new Vector2(75, 120);
        Log.d(TAG, "initSplashScene - Center: " + center.x + ", " + center.y);

        final Background backgroundColor = new Background(0.78431f, 0.77254f, 0.76862f);
        splashScene.setBackground(backgroundColor);

        DitheredSprite splash = new DitheredSprite(0, 0, splashTexture, getVBOM());
        splash.setScale(1.5f);
        splash.setPosition(center.x, center.y);
        splashScene.attachChild(splash);
    }

    private void loadMenuPause() {
        BitmapTextureAtlas menuAtlas = new BitmapTextureAtlas(this.getTextureManager(), 200, 100, TextureOptions.BILINEAR);

        TiledTextureRegion menuBG = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuAtlas, this, ResMan.MENU_BG, 0, 0, 1, 1);
        TiledTextureRegion menuReset = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuAtlas, this, ResMan.MENU_RESET, 0, 0, 1, 1);
        TiledTextureRegion menuQuit = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuAtlas, this, ResMan.MENU_QUIT, 0, 50, 1, 1);
        putTexture(ResMan.MENU_BG, menuBG);
        putTexture(ResMan.MENU_RESET, menuReset);
        putTexture(ResMan.MENU_QUIT, menuQuit);

        menuAtlas.load();
    }

    private void initMenuPause() {
        menuScene = new MenuScene(gameCamera, this);
        final ITextureRegion textureReset = getTexture(ResMan.MENU_RESET);
        final ITextureRegion textureQuit = getTexture(ResMan.MENU_QUIT);

        final SpriteMenuItem resetMenuItem = new SpriteMenuItem(OPTION_RESET, textureReset, getVBOM());
        resetMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        menuScene.addMenuItem(resetMenuItem);

        final SpriteMenuItem quitMenuItem = new SpriteMenuItem(OPTION_QUIT, textureQuit, getVBOM());
        quitMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        menuScene.addMenuItem(quitMenuItem);

        menuScene.buildAnimations();
        menuScene.setBackgroundEnabled(false);
        menuScene.setOnMenuItemClickListener(this);
    }

    public void resetMenuPause() {
        menuScene.reset();
    }

    private void initGameScene() {


        loadGFXAssets(currentGame);
        loadFonts(currentGame);
        //loadSounds(currentGame);

        gameScene = new Scene();
        gameScene.setOnAreaTouchTraversalFrontToBack();
        loadPhysics(currentGame);

        for (int i = 0; i < LAYER_COUNT; i++) {
            final Entity layer = new Entity();
            layer.setZIndex(i);
            gameScene.attachChild(layer);
        }


        loadHUD(currentGame);
        loadScene(currentGame);
        mEngine.setScene(gameScene);
    }

    private void loadScene(BaseGame game) {
        gameScene = game.prepareScene();
    }

    public IFont getFont(String fontName) {
        final IFont font = fontMap.get(fontName);
        if (font != null) {
            Log.v(TAG, "getFont - returning font " + fontName);
        } else {
            Log.v(TAG, "getFont - missing font " + fontName);
        }
        return font;
    }

    void putFont(String fontName, IFont font) {
        fontMap.put(fontName, font);
        Log.v(TAG, "putFont - Added font " + fontName);
    }

    public ITiledTextureRegion getTexture(String textureName) {
        final ITiledTextureRegion texture = textureMap.get(textureName);
        if (texture != null) {
            Log.v(TAG, "getTexture - returning texture " + textureName);
        } else {
            Log.v(TAG, "getTexture - missing texture " + textureName);
        }
        return texture;
    }

    void putTexture(String textureName, ITiledTextureRegion texture) {
        textureMap.put(textureName, texture);
        Log.v(TAG, "putTexture - Added texture " + textureName);
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

    public VertexBufferObjectManager getVBOM() {
        return vertexBufferObjectManager;
    }

    public void markForDeletion(PhysicalWorldObject object) {
        if (!objectsToDelete.contains(object)) {
            objectsToDelete.add(object);
        }
    }

}
