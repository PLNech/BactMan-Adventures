package com.ionis.igem.app.ui;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.ionis.igem.app.R;
import com.ionis.igem.app.game.AbstractGameActivity;
import com.ionis.igem.app.game.BinGame;
import com.ionis.igem.app.game.GutGame;
import com.ionis.igem.app.game.PictoGame;
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
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
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
import org.andengine.util.HorizontalAlign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameActivity extends AbstractGameActivity implements MenuScene.IOnMenuItemClickListener {
    private static final String TAG = "GameActivity";
    public static final float SPLASH_DURATION = 0.5f;

    private static final int OPTION_RESET = 0;
    private static final int OPTION_NEXT = OPTION_RESET + 1;
    private static final int OPTION_QUIT = OPTION_NEXT + 1;


    private VertexBufferObjectManager vertexBufferObjectManager;
    private TextureManager textureManager;
    private FontManager fontManager;
    private AssetManager assetManager;
    private SharedPreferences preferences;

    private SmoothCamera gameCamera;
    private PhysicsWorld physicsWorld;

    private HashMap<CharSequence, ITiledTextureRegion> textureMap = new HashMap<>();
    private HashMap<CharSequence, IFont> fontMap = new HashMap<>();

    private BaseGame currentGame;
    private ArrayList<BaseGame> games = new ArrayList<>();
    private int currentGameId = 0;

    private Scene splashScene;
    private MenuScene pauseScene;
    private MenuScene winScene;

    private SpriteMenuItem nextWinMenuItem;
    private SpriteMenuItem nextPauseMenuItem;

    private Text gameOverText;
    private Text winText;

    private ArrayList<PhysicalWorldObject> objectsToDelete = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate - Created Activity.");

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

        if (preferences == null) {
            preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        }

        addGame(GutGame.class);
        addGame(BinGame.class);
        addGame(PictoGame.class);
        currentGame = games.get(currentGameId);
    }

    private void addGame(Class<? extends BaseGame> c) {
        try {
            final BaseGame game = c.getConstructor(GameActivity.class).newInstance(this);
            game.setPosition(games.size());
            games.add(game);
        } catch (Exception e) {
            e.printStackTrace();
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

        loadMenus();
        initMenuPause();
        initMenuWin();
        updateNextStatus();

        Log.d(TAG, "onCreateScene - Splash Scene created.");

        loadGameAsync();
        return splashScene;
    }

    private void loadGameAsync() {
        registerUpdateHandler(SPLASH_DURATION, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mEngine.unregisterUpdateHandler(pTimerHandler);

                initGameScene();
                Log.d(TAG, "onTimePassed - Game Scene created.");
            }
        });
    }

    @Override
    public boolean onKeyDown(final int pKeyCode, @NonNull final KeyEvent pEvent) {
        if (gameScene != null && pauseScene != null &&
                pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
            if (gameScene.hasChildScene()) { // The game is paused
                pauseScene.back();
            } else {
                gameScene.setChildScene(pauseScene, false, true, true);
            }
            return true;
        } else {
            return super.onKeyDown(pKeyCode, pEvent);
        }
    }

    private void updateNextStatus() {
        final boolean isUnlocked = getHighScore(currentGame) >= 50;
        Log.d(TAG, "updateNextStatus: " + isUnlocked);
        updateNextStatus(nextPauseMenuItem, pauseScene, isUnlocked);
        updateNextStatus(nextWinMenuItem, winScene, isUnlocked);
    }

    private void updateNextStatus(SpriteMenuItem item, Scene scene, boolean isUnlocking) {
        item.setVisible(isUnlocking);
        if (isUnlocking) {
            scene.registerTouchArea(item);
        } else {
            scene.unregisterTouchArea(item);
        }
    }

    @Override
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
        switch (pMenuItem.getID()) {
            case OPTION_QUIT:
                onQuit();
                return true;
            case OPTION_RESET:
                currentGame.setPlaying(true);
                currentGame.resetGame();
                gameScene.clearChildScene();
                resetMenus();
                return true;
            case OPTION_NEXT:
                if (currentGame.getPosition() < games.size() - 1) {
                    currentGame = games.get(++currentGameId);
                    updateNextStatus();
                    resetMenus();
                    loadGameAsync();
                    mEngine.setScene(splashScene);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_next_game_soon), Toast.LENGTH_SHORT).show();
                        }
                    });
                    NavUtils.navigateUpFromSameTask(GameActivity.this);
                }
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
                        destroyBody(object.getBody(), object.getSprite());
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
        BitmapTextureAtlas splashTextureAtlas = new BitmapTextureAtlas(textureManager, 699, 985, TextureOptions.DEFAULT);
        TiledTextureRegion splashTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(splashTextureAtlas, this, ResMan.SPLASH, 0, 0, 1, 1);
        splashTextureAtlas.load();
        putTexture(ResMan.SPLASH, splashTextureRegion);
    }

    private void initSplashScene() {
        splashScene = new Scene();

        final ITextureRegion splashTexture = getTexture(ResMan.SPLASH);
        final Vector2 posS = new Vector2(-100, -80);
        Log.d(TAG, "initSplashScene - Center: " + posS.x + ", " + posS.y);

        final Background backgroundColor = new Background(0.78431f, 0.77254f, 0.76862f);
        splashScene.setBackground(backgroundColor);

        DitheredSprite splash = new DitheredSprite(0, 0, splashTexture, getVBOM());
        splash.setScale(0.75f);
        splash.setPosition(posS.x, posS.y);
        splashScene.attachChild(splash);
    }

    private void loadMenus() {
        BitmapTextureAtlas menuAtlas = new BitmapTextureAtlas(this.getTextureManager(), 200, 150, TextureOptions.BILINEAR);
        TiledTextureRegion menuBG = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuAtlas, this, ResMan.MENU_BG, 0, 0, 1, 1);
        TiledTextureRegion menuNext = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuAtlas, this, ResMan.MENU_NEXT, 0, 0, 1, 1);
        TiledTextureRegion menuReset = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuAtlas, this, ResMan.MENU_RESET, 0, 50, 1, 1);
        TiledTextureRegion menuQuit = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuAtlas, this, ResMan.MENU_QUIT, 0, 100, 1, 1);
        putTexture(ResMan.MENU_BG, menuBG);
        putTexture(ResMan.MENU_RESET, menuReset);
        putTexture(ResMan.MENU_NEXT, menuNext);
        putTexture(ResMan.MENU_QUIT, menuQuit);

        menuAtlas.load();
    }

    private void initMenuPause() {
        pauseScene = new MenuScene(gameCamera, this);
        final ITextureRegion textureNext = getTexture(ResMan.MENU_NEXT);
        final ITextureRegion textureReset = getTexture(ResMan.MENU_RESET);
        final ITextureRegion textureQuit = getTexture(ResMan.MENU_QUIT);

        nextPauseMenuItem = new SpriteMenuItem(OPTION_NEXT, textureNext, getVBOM());
        nextPauseMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        pauseScene.addMenuItem(nextPauseMenuItem);

        final SpriteMenuItem resetMenuItem = new SpriteMenuItem(OPTION_RESET, textureReset, getVBOM());
        resetMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        pauseScene.addMenuItem(resetMenuItem);

        final SpriteMenuItem quitMenuItem = new SpriteMenuItem(OPTION_QUIT, textureQuit, getVBOM());
        quitMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        pauseScene.addMenuItem(quitMenuItem);

        pauseScene.buildAnimations();
        pauseScene.setBackgroundEnabled(false);
        pauseScene.setOnMenuItemClickListener(this);
    }

    private void initMenuWin() {
        winScene = new MenuScene(gameCamera, this);
        final ITextureRegion textureNext = getTexture(ResMan.MENU_NEXT);
        final ITextureRegion textureReset = getTexture(ResMan.MENU_RESET);
        final ITextureRegion textureQuit = getTexture(ResMan.MENU_QUIT);

        nextWinMenuItem = new SpriteMenuItem(OPTION_NEXT, textureNext, getVBOM());
        nextWinMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        winScene.addMenuItem(nextWinMenuItem);

        final SpriteMenuItem resetMenuItem = new SpriteMenuItem(OPTION_RESET, textureReset, getVBOM());
        resetMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        winScene.addMenuItem(resetMenuItem);

        final SpriteMenuItem quitMenuItem = new SpriteMenuItem(OPTION_QUIT, textureQuit, getVBOM());
        quitMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        winScene.addMenuItem(quitMenuItem);

        winScene.buildAnimations();
        winScene.setBackgroundEnabled(false);
        winScene.setOnMenuItemClickListener(this);
    }

    public void resetMenus() {
        pauseScene.detachChild(gameOverText);
        pauseScene.reset();

        winScene.detachChild(winText);
        winScene.reset();
    }

    private void initGameScene() {
        loadGFXAssets(currentGame);
        loadFonts(currentGame);
        //loadSounds(currentGame);

        gameScene = new Scene();
        gameScene.setOnAreaTouchTraversalFrontToBack();
        final IOnSceneTouchListener sceneTouchListener = currentGame.getOnSceneTouchListener();
        if (sceneTouchListener != null) {
            gameScene.setOnSceneTouchListener(sceneTouchListener);
        }
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
        currentGame.setPlaying(true);
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

    public void putFont(String fontName, IFont font) {
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

    public void onLose(int score) {
        final IFont menuFont = getFont(FontAsset.name(ResMan.F_HUD_BIN, ResMan.F_HUD_BIN_SIZE, ResMan.F_HUD_BIN_COLOR, ResMan.F_HUD_BIN_ANTI));
        gameOverText = new Text(0, 0, menuFont, getEndTextAndUpdateHighScore(false, score), 32, new TextOptions(HorizontalAlign.CENTER), getVBOM());
        final Vector2 textPosition = spritePosition(gameOverText.getWidth(), gameOverText.getHeight(), 0.5f, 0.2f);
        gameOverText.setPosition(textPosition.x, textPosition.y);
        pauseScene.attachChild(gameOverText);
        gameScene.setChildScene(pauseScene, false, true, true);
    }

    public void onWin(int score) {
        final IFont menuFont = getFont(FontAsset.name(ResMan.F_HUD_BIN, ResMan.F_HUD_BIN_SIZE, ResMan.F_HUD_BIN_COLOR, ResMan.F_HUD_BIN_ANTI));
        winText = new Text(0, 0, menuFont, getEndTextAndUpdateHighScore(true, score), 32, new TextOptions(HorizontalAlign.CENTER), getVBOM());
        final Vector2 textPosition = spritePosition(winText.getWidth(), winText.getHeight(), 0.5f, 0.2f);
        winText.setPosition(textPosition.x, textPosition.y);
        winScene.attachChild(winText);
        gameScene.setChildScene(winScene, false, true, true);
    }

    @NonNull
    private String getEndTextAndUpdateHighScore(boolean win, int score) {
        final StringBuilder winBuilder = new StringBuilder();
        final SharedPreferences preferences = getPreferences();
        final String keyHighScore = currentGame.getClass().getSimpleName() + "_highscore";
        final int highScore = preferences.getInt(keyHighScore, 0);
        final boolean best = score > highScore;
        if (win) {
            winBuilder.append("VICTORY!");
        } else {
            winBuilder.append("GAME OVER");
        }
        if (best) {
            preferences.edit().putInt(keyHighScore, score).apply();
            winBuilder.append("\nNew high score: ").append(score);
            updateNextStatus();
        } else {
            winBuilder.append("\nHigh score: ").append(highScore);
        }
        return winBuilder.toString();
    }

    public int getHighScore(BaseGame game) {
        final String keyHighScore = game.getClass().getSimpleName() + "_highscore";
        return preferences.getInt(keyHighScore, 0);
    }

    public void markForDeletion(PhysicalWorldObject object) {
        if (!objectsToDelete.contains(object)) {
            objectsToDelete.add(object);
        }
    }

    public void registerUpdateHandler(float duration, ITimerCallback pTimerCallback) {
        mEngine.registerUpdateHandler(new TimerHandler(duration, pTimerCallback));
    }

    public SmoothCamera getCamera() {
        return gameCamera;
    }

    public void setPhysicsCoeff(float coeff) {
        final Vector2 gravity = physicsWorld.getGravity();
        final Vector2 physicsVector = currentGame.getPhysicsVector();
        final float minCoeff = 0.5f;
        if (gravity.x < physicsVector.x * minCoeff || gravity.y < physicsVector.y * minCoeff) {
            return;
        }
        physicsWorld.setGravity(gravity.mul(coeff));
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public BaseGame getCurrentGame() {
        return currentGame;
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

}
