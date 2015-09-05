package com.ionis.igem.app.game;

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
import com.ionis.igem.app.game.managers.ResMan;
import com.ionis.igem.app.game.model.BaseGame;
import com.ionis.igem.app.game.model.HUDElement;
import com.ionis.igem.app.game.model.PhysicalWorldObject;
import com.ionis.igem.app.game.model.res.FontAsset;
import com.ionis.igem.app.game.model.res.GFXAsset;
import com.ionis.igem.app.utils.FontsOverride;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
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
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by PLN on 11/08/2015.
 */
public abstract class AbstractGameActivity extends SimpleBaseGameActivity implements MenuScene.IOnMenuItemClickListener {

    private static final String TAG = "BaseGameActivity";

    private Boolean shouldStartEngine = false;

    protected static final int CAMERA_WIDTH = 480;
    protected static final int CAMERA_HEIGHT = 800;

    public static final float SPLASH_DURATION = 0.5f;

    public static final String ASSET_PATH_GFX = "gfx/";
    public static final String ASSET_PATH_FONT = "fonts/";

    protected static final int OPTION_RESET = 0;
    protected static final int OPTION_NEXT = OPTION_RESET + 1;
    protected static final int OPTION_QUIT = OPTION_NEXT + 1;

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

    protected static final int LAYER_COUNT = 4;
    public static final int LAYER_BACKGROUND = 0;
    public static final int LAYER_FOREGROUND = LAYER_BACKGROUND + 1;
    public static final int LAYER_OVERGROUND = LAYER_FOREGROUND + 1;
    public static final int LAYER_HUD = LAYER_OVERGROUND + 1;
    public static final int LAYER_HUD_TEXT = LAYER_HUD + 1;

    protected static final float MAX_SPEED_X = 200.0f;
    protected static final float MAX_SPEED_Y = 200.0f;
    protected static final float MAX_ZOOM_CHANGE = 0.8f;

    private ArrayList<PhysicalWorldObject> objectsToDelete = new ArrayList<>();

    protected BaseGame currentGame;
    protected ArrayList<BaseGame> games = new ArrayList<>();
    protected int currentGameId = 0;

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

    protected abstract void loadSplashScene();

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

    @Override
    public boolean onKeyDown(final int pKeyCode, @NonNull final KeyEvent pEvent) {
        if (gameScene != null && pauseScene != null &&
                pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN)
        {
            if (gameScene.hasChildScene()) { // The game is paused
                pauseScene.back();
                updateNextStatus();
            } else {
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

    protected void initGameScene() {
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
        registerUpdateHandler(SPLASH_DURATION, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mEngine.unregisterUpdateHandler(pTimerHandler);

                initGameScene();
                Log.d(TAG, "onTimePassed - Game Scene created.");
            }
        });
    }

    public void registerUpdateHandler(float duration, ITimerCallback pTimerCallback) {
        mEngine.registerUpdateHandler(new TimerHandler(duration, pTimerCallback));
    }

    public int getHighScore(BaseGame game) {
        final String keyHighScore = game.getClass().getSimpleName() + "_highscore";
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
    }

    public void onWin(int score, float posRatioX, float posRatioY) {
        final IFont menuFont = getFont(FontAsset.name(ResMan.F_HUD_BIN, ResMan.F_HUD_BIN_SIZE, ResMan.F_HUD_BIN_COLOR, ResMan.F_HUD_BIN_ANTI));
        winText = new Text(0, 0, menuFont, getEndTextAndUpdateHighScore(true, score), 32, new TextOptions(HorizontalAlign.CENTER), getVBOM());
        final Vector2 textPosition = spritePosition(winText.getWidth(), winText.getHeight(), posRatioX, posRatioY);
        winText.setPosition(textPosition.x, textPosition.y);
        winScene.attachChild(winText);
        gameScene.setChildScene(winScene, false, true, true);
    }

    private void onQuit() {
        this.finish();
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

    protected void updateNextStatus() {
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

    protected void addGame(Class<? extends BaseGame> c) {
        try {
            final BaseGame game = c.getConstructor(AbstractGameActivity.class).newInstance(this);
            game.setPosition(games.size());
            games.add(game);
        } catch (Exception e) {
            e.printStackTrace();
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
                                  float ratio)
    {
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
        final Vector2 res = new Vector2(getCamera().getWidth() * positionRatio.x - textureDims.x / 2,
                getCamera().getHeight() * positionRatio.y - -textureDims.y / 2);
        Log.v(TAG, "spritePosition - Returning " + res.x + ", " + res.y);
        return res;
    }

    public SmoothCamera getCamera() {
        return gameCamera;
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
