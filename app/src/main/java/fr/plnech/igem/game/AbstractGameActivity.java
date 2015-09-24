package fr.plnech.igem.game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
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
import fr.plnech.igem.R;
import fr.plnech.igem.game.managers.ResMan;
import fr.plnech.igem.game.model.BaseGame;
import fr.plnech.igem.game.model.HUDElement;
import fr.plnech.igem.game.model.PhysicalWorldObject;
import fr.plnech.igem.game.model.res.FontAsset;
import fr.plnech.igem.game.model.res.GFXAsset;
import fr.plnech.igem.game.ui.DitheredSprite;
import fr.plnech.igem.ui.model.MusicManager;
import fr.plnech.igem.utils.FontsOverride;
import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
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
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by PLN on 11/08/2015.
 */
public abstract class AbstractGameActivity extends SimpleBaseGameActivity implements MenuScene.IOnMenuItemClickListener {

    private static final String TAG = "BaseGameActivity";
    public static final String SUFFIX_UNLOCKED = "_unlocked";
    public static final String SUFFIX_SCORE = "_highscore";

    private Boolean shouldStartEngine = false;

    public static final int CAMERA_WIDTH = 480;
    public static final int CAMERA_HEIGHT = 800;

    public static final float SPLASH_DURATION = 1.0f;

    public static final String ASSET_PATH_GFX = "gfx/";
    public static final String ASSET_PATH_FONT = "fonts/";

    protected static final int OPTION_RESET = 0;
    protected static final int OPTION_NEXT = OPTION_RESET + 1;
    protected static final int OPTION_QUIT = OPTION_NEXT + 1;

    private int profPosition;

    private HashMap<CharSequence, ITiledTextureRegion> textureMap = new HashMap<>();
    private HashMap<CharSequence, IFont> fontMap = new HashMap<>();

    protected VertexBufferObjectManager vertexBufferObjectManager;
    protected TextureManager textureManager;
    protected FontManager fontManager;
    protected AssetManager assetManager;
    protected SharedPreferences preferences;

    protected Scene gameScene;
    protected Scene splashScene;
    protected MenuScene pauseScene;
    protected MenuScene winScene;

    private SpriteMenuItem nextWinMenuItem;
    private SpriteMenuItem nextPauseMenuItem;

    private Text gameOverText;
    private Text winText;

    protected SmoothCamera gameCamera;
    protected PhysicsWorld physicsWorld;

    public static final int LAYER_BACKGROUND = 0;
    public static final int LAYER_FOREGROUND = LAYER_BACKGROUND + 1;
    public static final int LAYER_OVERGROUND = LAYER_FOREGROUND + 1;
    public static final int LAYER_HUD = LAYER_OVERGROUND + 1;
    public static final int LAYER_HUD_TEXT = LAYER_HUD + 1;
    protected static final int LAYER_COUNT = LAYER_HUD_TEXT + 1;

    protected static final float MAX_SPEED_X = 200.0f;
    protected static final float MAX_SPEED_Y = 200.0f;
    protected static final float MAX_ZOOM_CHANGE = 0.8f;

    private ArrayList<PhysicalWorldObject> objectsToDelete = new ArrayList<>();
    private ArrayList<PhysicalWorldObject> objectsToAdd = new ArrayList<>();

    protected BaseGame currentGame;
    private SpriteBackground splashBackground;
    private Music music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/Roboto-Medium.ttf");

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

        final Intent intent;
        final Bundle extras;
        if ((intent = getIntent()) != null && (extras = intent.getExtras()) != null) {
            try {
                final int gameId = extras.getInt(BaseGame.KEY_GAME_ID);
                currentGame = BaseGame.getGameFromTag(gameId, this);
                Log.d(TAG, "onCreate - Game id: " + gameId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "onCreate - No game given!");
        }
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        gameCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, MAX_SPEED_X, MAX_SPEED_Y, MAX_ZOOM_CHANGE);
        final EngineOptions engineOptions = getEngineOptions();
        engineOptions.getTouchOptions().setNeedsMultiTouch(true);
        engineOptions.getAudioOptions().setNeedsMusic(true);
        engineOptions.getAudioOptions().setNeedsSound(true);
        return engineOptions;
    }

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
    public void onCreateResources() {
        Log.d(TAG, "onCreateResources - Beginning resource creation.");
        loadSplashScene();
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
        initSplashScene();
        Log.d(TAG, "onCreateScene - Splash Scene created.");

        loadMenus();
        initMenuPause();
        initMenuWin();
        updateNextStatus();

        Log.d(TAG, "onCreateScene - Splash Scene created.");

        loadGameAsync();
        return splashScene;
    }

    @Override
    public boolean onKeyDown(final int pKeyCode, @NonNull final KeyEvent pEvent) {
        if (gameScene != null && pauseScene != null &&
                pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
            if (gameScene.hasChildScene()) { // The game is paused
                music.play();
                pauseScene.back();
                updateNextStatus();
            } else {
                music.pause();
                gameScene.setChildScene(pauseScene, false, true, true);
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
                currentGame.logLevelEnd(currentGame.getScore(), false);
                currentGame.logLevelStart();
                currentGame.setPlaying(true);
                currentGame.resetGame();
                gameScene.clearChildScene();
                music.seekTo(0);
                music.play();
                resetMenus();
                return true;
            case OPTION_NEXT:
                final Resources resources = getResources();
                final int nextGameId = currentGame.getNextGameId();

                if (nextGameId != BaseGame.ID_NONE) {
                    final boolean nextGameIsPortrait = BaseGame.isPortrait(nextGameId);
                    if (nextGameIsPortrait == currentGame.isPortrait()) {
                        currentGame = BaseGame.getGameFromTag(nextGameId, this);
                        updateNextStatus();
                        resetMenus();
                        loadGameAsync();
                        mEngine.setScene(splashScene);
                    } else {
                        BaseGame.startGame(this, nextGameId);
                        warnAboutOrientation();
                        onQuit();
                    }

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), resources.getString(R.string.msg_last_game), Toast.LENGTH_SHORT).show();
                        }
                    });
                    NavUtils.navigateUpFromSameTask(AbstractGameActivity.this);
                }
                return true;
        }
        return false;
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

    public void putTexture(String textureName, ITiledTextureRegion texture) {
        textureMap.put(textureName, texture);
        Log.v(TAG, "putTexture - Added texture " + textureName);
    }


    private void loadGFXAssets(BaseGame game) {
        for (GFXAsset asset : game.getGraphicalAssets()) {
            loadGFXAsset(asset);
        }
        Log.d(TAG, "loadGraphic - Finished loading game assets.");
    }

    private String loadGFXAsset(GFXAsset asset) {
        BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(textureManager, asset.getWidth(), asset.getHeight(), TextureOptions.BILINEAR);
        final String filename = asset.getFilename();
        final int textureX = asset.getTextureX();
        final int textureY = asset.getTextureY();
        final int tileC = asset.getTileColumns();
        final int tileR = asset.getTileRows();
        TiledTextureRegion tiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(textureAtlas, this, filename, textureX, textureY, tileC, tileR);
        putTexture(filename, tiledTextureRegion);

        textureAtlas.load();
        return filename;
    }

    private void loadFonts(BaseGame game) {
        checkSetFontPath();

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

    private void loadMusic() {
        if (music == null) {
            try {
                music = MusicFactory.createMusicFromAsset(mEngine.getMusicManager(), this, "mfx/games.mp3");
                music.setLooping(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkSetGFXPath() {
        if (!BitmapTextureAtlasTextureRegionFactory.getAssetBasePath().equals(ASSET_PATH_GFX)) {
            BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(ASSET_PATH_GFX);
        }
    }

    private void checkSetFontPath() {
        if (!FontFactory.getAssetBasePath().equals(ASSET_PATH_FONT)) {
            FontFactory.setAssetBasePath(ASSET_PATH_FONT);
        }
    }

    private void loadHUD(BaseGame game) {
        List<HUDElement> elements = game.getHudElements();
        final IEntity layerHUD = gameScene.getChildByIndex(LAYER_HUD);
        final IEntity layerText = gameScene.getChildByIndex(LAYER_HUD_TEXT);
        for (HUDElement element : elements) {
            final Sprite sprite = element.getSprite();
            final Text text = element.getText();
            sprite.detachSelf();
            text.detachSelf();
            layerHUD.attachChild(sprite);
            layerText.attachChild(text);
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
                        destroyObject(object);
                    }
                    objectsToDelete.clear();

                    for (PhysicalWorldObject object : objectsToAdd) {
                        createObject(object);
                    }
                    objectsToAdd.clear();
                }
            }

            private void createObject(final PhysicalWorldObject object) {
                object.onAddToWorld();
            }

            private void destroyObject(final PhysicalWorldObject object) {
                Body body = object.getBody();
                Sprite sprite = object.getSprite();
                body.setActive(false);
                body.setAwake(false);
                unregisterPhysicsConnector(physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(sprite));
                body.setActive(false);
                destroyBody(body);
                object.onRemoveFromWorld();
            }
        };
        physicsWorld.setContactListener(contactListener);
        gameScene.registerUpdateHandler(physicsWorld);
    }

    protected void initGameScene() {
        loadGFXAssets(currentGame);
        loadFonts(currentGame);
        loadMusic();
//        loadSounds(currentGame);
        setGameScene();
    }

    private void setGameScene() {
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
        MusicManager.pause();
        music.play();
    }

    protected void warnAboutOrientation() {
        Resources resources = getResources();
        final String message = String.format(resources.getString(R.string.msg_next_game),
                resources.getString(getOrientationResId()));
        toastOnUIThread(message, Toast.LENGTH_LONG);
        Log.d(TAG, "warnAboutOrientation - " + message);
    }


    private void loadScene(BaseGame game) {
        currentGame.setPlaying(true);
        gameScene = game.prepareScene();
    }

    protected void initMenuPause() {
        pauseScene = new MenuScene(gameCamera, this);
        final ITextureRegion textureNext = getTexture(ResMan.MENU_NEXT);
        final ITextureRegion textureReset = getTexture(ResMan.MENU_RESET);
        final ITextureRegion textureQuit = getTexture(ResMan.MENU_QUIT);

        nextPauseMenuItem = new SpriteMenuItem(OPTION_NEXT, textureNext, getVBOM());
        nextPauseMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        nextPauseMenuItem.setVisible(false);
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

    protected void initMenuWin() {
        winScene = new MenuScene(gameCamera, this);
        final ITextureRegion textureNext = getTexture(ResMan.MENU_NEXT);
        final ITextureRegion textureReset = getTexture(ResMan.MENU_RESET);
        final ITextureRegion textureQuit = getTexture(ResMan.MENU_QUIT);

        nextWinMenuItem = new SpriteMenuItem(OPTION_NEXT, textureNext, getVBOM());
        nextWinMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        nextWinMenuItem.setVisible(false);
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

    protected void loadGameAsync() {
        loadGameAsync(0, true);
    }

    protected void loadGameAsync(final int givenPosition, boolean shouldDelay) {
        Log.d(TAG, "loadGameAsync - Loading with prof position " + givenPosition);
        profPosition = givenPosition;
        if (shouldDelay) {
            registerUpdateHandler(SPLASH_DURATION, new ITimerCallback() {
                public void onTimePassed(final TimerHandler pTimerHandler) {
                    mEngine.unregisterUpdateHandler(pTimerHandler);
                    showProfOrRunGame();
                }
            });
        } else {
            showProfOrRunGame();
        }
    }

    private void showProfOrRunGame() {
        final List<GFXAsset> profAssets = currentGame.getProfAssets();
        if (profPosition < profAssets.size()) {
            GFXAsset profAsset = profAssets.get(profPosition);
            DitheredSprite splash = new DitheredSprite(0, 0, currentGame.getWidth(), currentGame.getHeight(),
                    getTexture(loadGFXAsset(profAsset)), getVBOM());
            splashScene.setBackground(new SpriteBackground(splash));
            splashScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
                @Override
                public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
                    if (pSceneTouchEvent.isActionDown()) {
                        Log.d(TAG, "onSceneTouchEvent - Touched splashScene: screen at position " + profPosition);
                        loadGameAsync(++profPosition, false);
                        return true;
                    }
                    return false;
                }
            });
        } else {
            splashScene.setBackground(splashBackground);
            Log.d(TAG, "onTimePassed - Finished prof screens, launching game!");
            runCurrentGame();
        }
    }

    private void runCurrentGame() {
        initGameScene();
        currentGame.logLevelStart();
        Log.d(TAG, "runCurrentGame - Game Scene created.");
    }

    public void registerUpdateHandler(float duration, ITimerCallback pTimerCallback) {
        mEngine.registerUpdateHandler(new TimerHandler(duration, pTimerCallback));
    }

    public int getHighScore(BaseGame game) {
        final String keyHighScore = game.getClass().getSimpleName() + SUFFIX_SCORE;
        return preferences.getInt(keyHighScore, 0);
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

    public void markForDeletion(PhysicalWorldObject object) {
        if (!objectsToDelete.contains(object)) {
            objectsToDelete.add(object);
        }
    }

    public void markForAddition(PhysicalWorldObject object) {
        if (!objectsToAdd.contains(object)) {
            objectsToAdd.add(object);
        }
    }

    public void resetMenus() {
        pauseScene.detachChild(gameOverText);
        pauseScene.reset();

        winScene.detachChild(winText);
        winScene.reset();

        updateNextStatus();
    }

    public void onLose(int score, float posRatioX, float posRatioY) {
        final IFont menuFont = getFont(FontAsset.name(ResMan.F_HUD_BIN, ResMan.F_HUD_BIN_SIZE, ResMan.F_HUD_BIN_COLOR, ResMan.F_HUD_BIN_ANTI));
        gameOverText = new Text(0, 0, menuFont, getEndTextAndUpdateHighScore(false, score), 32, new TextOptions(HorizontalAlign.CENTER), getVBOM());
        final Vector2 textPosition = spritePosition(gameOverText.getWidth(), gameOverText.getHeight(), posRatioX, posRatioY);
        gameOverText.setPosition(textPosition.x, textPosition.y);
        pauseScene.attachChild(gameOverText);
        gameScene.setChildScene(pauseScene, false, true, true);
        currentGame.logLevelEnd(score, false);
    }

    public void onWin(int score, float posRatioX, float posRatioY) {
        final IFont menuFont = getFont(FontAsset.name(ResMan.F_HUD_BIN, ResMan.F_HUD_BIN_SIZE, ResMan.F_HUD_BIN_COLOR, ResMan.F_HUD_BIN_ANTI));
        winText = new Text(0, 0, menuFont, getEndTextAndUpdateHighScore(true, score), 32, new TextOptions(HorizontalAlign.CENTER), getVBOM());
        final Vector2 textPosition = spritePosition(winText.getWidth(), winText.getHeight(), posRatioX, posRatioY);
        winText.setPosition(textPosition.x, textPosition.y);
        winScene.attachChild(winText);
        gameScene.setChildScene(winScene, false, true, true);
        currentGame.logLevelEnd(score, true);
        updateNextGame();
    }

    private void onQuit() {
        this.finish();
    }

    @NonNull
    private String getEndTextAndUpdateHighScore(boolean win, int score) {
        final StringBuilder winBuilder = new StringBuilder();
        final String keyHighScore = currentGame.getClass().getSimpleName() + SUFFIX_SCORE;
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

    protected void updateNextStatus() {
        final boolean isUnlocked = true; //TODO REMOVE getHighScore(currentGame) >= 50;
        Log.d(TAG, "updateNextStatus: " + isUnlocked);
        updateNextStatus(nextPauseMenuItem, pauseScene, isUnlocked);
        updateNextStatus(nextWinMenuItem, winScene, isUnlocked);
    }

    private void updateNextGame() {
        int lastUnlockedId = preferences.getInt(BaseGame.KEY_GAME_ID, 0);
        final int nextGameId = Math.max(currentGame.getNextGameId(), BaseGame.ID_GUT);
        if (nextGameId >= lastUnlockedId) {
            preferences.edit()
                    .putInt(BaseGame.KEY_GAME_ID, nextGameId)
                    .putBoolean(nextGameId + AbstractGameActivity.SUFFIX_UNLOCKED, true)
                    .apply();

            Log.d(TAG, "updateNextGame - New value: " + nextGameId);
        }
    }

    private void updateNextStatus(SpriteMenuItem item, Scene scene, boolean isUnlocking) {
        item.setVisible(isUnlocking);
        if (isUnlocking) {
            scene.registerTouchArea(item);
        } else {
            scene.unregisterTouchArea(item);
        }
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
        return new Vector2(getCamera().getWidth() * positionRatio.x - textureDims.x / 2,
                getCamera().getHeight() * positionRatio.y - textureDims.y / 2);
    }

    protected void initSplashScene(ITextureRegion splashTexture, int width, int height) {
        splashScene = new Scene();
        DitheredSprite splash = new DitheredSprite(0, 0, width, height, splashTexture, getVBOM());
        splashBackground = new SpriteBackground(splash);
        splashScene.setBackground(splashBackground);
    }

    public SmoothCamera getCamera() {
        return gameCamera;
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

    protected abstract void loadSplashScene();

    protected abstract void loadMenus();

    protected abstract void initSplashScene();

    protected abstract int getOrientationResId();

    protected abstract EngineOptions getEngineOptions();
}
