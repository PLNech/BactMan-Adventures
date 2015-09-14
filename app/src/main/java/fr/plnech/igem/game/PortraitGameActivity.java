package fr.plnech.igem.game;

import android.util.Log;
import fr.plnech.igem.game.managers.ResMan;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;

public class PortraitGameActivity extends AbstractGameActivity {
    private static final String TAG = "PortraitGameActivity";

    @Override
    public EngineOptions onCreateEngineOptions() {
        gameCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, MAX_SPEED_X, MAX_SPEED_Y, MAX_ZOOM_CHANGE);
        final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_SENSOR, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), gameCamera);
        engineOptions.getTouchOptions().setNeedsMultiTouch(true);
        return engineOptions;
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

    protected void loadSplashScene() {
        checkSetGFXPath();
        BitmapTextureAtlas splashTextureAtlas = new BitmapTextureAtlas(textureManager, 1440, 2400, TextureOptions.BILINEAR);
        TiledTextureRegion splashTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(splashTextureAtlas, this, ResMan.SPLASH, 0, 0, 1, 1);
        splashTextureAtlas.load();
        putTexture(ResMan.SPLASH, splashTextureRegion);
    }

    private void initSplashScene() {
        super.initSplashScene(getTexture(ResMan.SPLASH), CAMERA_WIDTH, CAMERA_HEIGHT);
    }

    private void loadMenus() {
        checkSetGFXPath();
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
}
