package com.ionis.igem.app.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.ionis.igem.app.game.BaseGameActivity;
import org.andengine.audio.sound.Sound;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;

public abstract class GameActivity extends BaseGameActivity {
    private static final String TAG = "GameActivity";

    private TextureRegion backgroundTextureRegion;
    private BitmapTextureAtlas backgroundTexture;

    private Font FontAgencyB;

    private Sound SoundExplosion;

    private enum Layer {
        LAYER_BACKGROUND(0), LAYER_FOREGROUND(1);
        private int index;

        Layer(int index) {
            this.index = index;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate - Created Activity.");
    }

    @Override
    public void onCreateResources() {
        final String msg = "onCreateResources - Beginning resource creation.";
        Log.d(TAG, msg);
        toastOnUIThread(msg, Toast.LENGTH_SHORT);
        loadGraphics();
//        loadFonts();
//        loadSounds();
    }

    private void loadGraphics() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        backgroundTexture = new BitmapTextureAtlas(getTextureManager(), 986, 698, TextureOptions.DEFAULT);
        backgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backgroundTexture, this, "background.png", 0, 0);
        backgroundTexture.load();
        final String msg = "loadGraphics - Finished loading background texture.";
        Log.d(TAG, msg);
        toastOnUIThread(msg, Toast.LENGTH_SHORT);
    }

    private void loadFonts() {
        FontFactory.setAssetBasePath("fonts/");
//        final ITexture fontTexture = new BitmapTextureAtlas(getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//        FontAgencyB = FontFactory.createFromAsset(getFontManager(), fontTexture, getAssets(), "AGENCYB.TTF", 40, true, Color.BLACK);
        final String msg = "loadFonts - Finished loading fonts.";
        Log.d(TAG, msg);
        toastOnUIThread(msg, Toast.LENGTH_SHORT);
    }

    private void loadSounds() {
//        try {
//            SoundFactory.setAssetBasePath("mfx/");
//            SoundExplosion = SoundFactory.createSoundFromAsset(getEngine().getSoundManager(), getApplicationContext(), "explosion3.ogg");
//            Log.d(TAG, "loadSounds - Finished loading sounds.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    protected Scene onCreateScene() {
        super.onCreateScene();

        gameScene = new Scene();
        final Background backgroundColor = new Background(0.42352f, 0.79215f, 0.62745f);
        gameScene.setBackground(backgroundColor);

        for (int i = 0; i < Layer.values().length; i++) {
            gameScene.attachChild(new Entity());
        }

        this.gameScene.getChildByIndex(Layer.LAYER_BACKGROUND.index)
                .attachChild(new Sprite(0, 0, this.backgroundTextureRegion, this.getVertexBufferObjectManager()));

        Log.d(TAG, "onCreateScene - Scene created.");
        return gameScene;
    }
}
