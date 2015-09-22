package fr.plnech.igem.game;

import fr.plnech.igem.R;
import fr.plnech.igem.game.managers.ResMan;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;

public class LandscapeGameActivity extends AbstractGameActivity {
    private static final String TAG = "LandscapeGameActivity";

    public static final int CAMERA_WIDTH = AbstractGameActivity.CAMERA_HEIGHT;
    public static final int CAMERA_HEIGHT = AbstractGameActivity.CAMERA_WIDTH;

    @Override
    public EngineOptions onCreateEngineOptions() {
        gameCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, MAX_SPEED_X, MAX_SPEED_Y, MAX_ZOOM_CHANGE);
        final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), gameCamera);
        engineOptions.getTouchOptions().setNeedsMultiTouch(true);
        return engineOptions;
    }

    @Override
    protected int getOrientationResId() {
        return R.string.msg_orientation_portrait;
    }

    protected void loadSplashScene() {
        checkSetGFXPath();
        BitmapTextureAtlas splashTextureAtlas = new BitmapTextureAtlas(textureManager, 2400, 1440, TextureOptions.BILINEAR);
        TiledTextureRegion splashTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(splashTextureAtlas, this, ResMan.SPLASH_LAND, 0, 0, 1, 1);
        splashTextureAtlas.load();
        putTexture(ResMan.SPLASH_LAND, splashTextureRegion);
    }

    protected void initSplashScene() {
        super.initSplashScene(getTexture(ResMan.SPLASH_LAND), CAMERA_WIDTH, CAMERA_HEIGHT);
    }

    protected void loadMenus() {
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
