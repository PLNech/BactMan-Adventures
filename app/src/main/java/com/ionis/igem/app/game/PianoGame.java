package com.ionis.igem.app.game;

import android.util.Log;
import com.badlogic.gdx.math.Vector2;
import com.ionis.igem.app.game.managers.ResMan;
import com.ionis.igem.app.game.model.BaseGame;
import com.ionis.igem.app.game.model.HUDElement;
import com.ionis.igem.app.game.model.res.FontAsset;
import com.ionis.igem.app.game.model.res.GFXAsset;
import com.ionis.igem.app.game.piano.Polymerase;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.List;

/**
 * Created by PLNech on 31/08/2015.
 */
public class PianoGame extends BaseGame {
    private static final String TAG = "PianoGame";

    public static final int INIT_SCORE = 0;

    private HUDElement HUDScore;
    private int gameScore;

    public PianoGame(AbstractGameActivity pActivity) {
        super(pActivity);
    }

    @Override
    public List<GFXAsset> getGraphicalAssets() {
        if (graphicalAssets.isEmpty()) {
            graphicalAssets.add(new GFXAsset(ResMan.PIANO_BG, 2048, 1509, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.PIANO_L_PHO, 732, 512, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.PIANO_POLY, 1536, 1014, 0, 0, 2, 1));
            graphicalAssets.add(new GFXAsset(ResMan.PIANO_A, 317, 1024, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.PIANO_A_CPL, 317, 1024, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.PIANO_T, 334, 1024, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.PIANO_T_CPL, 334, 1024, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.PIANO_G, 334, 1024, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.PIANO_G_CPL, 334, 1024, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.PIANO_C, 331, 1024, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.PIANO_C_CPL, 329, 1024, 0, 0));

            /* HUD */
            graphicalAssets.add(new GFXAsset(ResMan.HUD_SCORE, 1885, 1024, 0, 0));
        }
        return graphicalAssets;
    }

    @Override
    public List<FontAsset> getFontAssets() {
        if (fontAssets.isEmpty()) {
            fontAssets.add(new FontAsset(ResMan.F_HUD_BIN, ResMan.F_HUD_BIN_SIZE, ResMan.F_HUD_BIN_COLOR, ResMan.F_HUD_BIN_ANTI));
        }
        return fontAssets;
    }

    @Override
    public List<HUDElement> getHudElements() {
        if (elements.isEmpty()) {
            final ITiledTextureRegion textureScore = activity.getTexture(ResMan.HUD_SCORE);

            final float scale = 0.08f;

            Vector2 posS = new Vector2(5, 0);
            Vector2 posL = new Vector2(350, 0);

            Vector2 offS = new Vector2(75, 30);
            Vector2 offL = new Vector2(340, 27.5f);

            IFont fontRoboto = activity.getFont(FontAsset.name(ResMan.F_HUD_BIN, ResMan.F_HUD_BIN_SIZE, ResMan.F_HUD_BIN_COLOR, ResMan.F_HUD_BIN_ANTI));
            Log.d(TAG, "getHudElements - sprites: " + posS + ", " + posL + " - text:" + offS.add(posS) + ", " + offL.add(posL));

            final VertexBufferObjectManager vbom = activity.getVBOM();

            HUDScore = new HUDElement()
                    .buildSprite(posS, textureScore, vbom, scale)
                    .buildText("", "31337".length(), offS, fontRoboto, vbom);

            elements.add(HUDScore);
        }

        return elements;
    }

    @Override
    public Scene prepareScene() {
        Scene scene = activity.getScene();

        resetGamePoints();
        final SmoothCamera camera = activity.getCamera();
        final VertexBufferObjectManager vbom = activity.getVBOM();

        scene.setBackground(new SpriteBackground(new Sprite(0, 0, camera.getWidth(), camera.getHeight(),
                activity.getTexture(ResMan.PIANO_BG), vbom)));
//        createCameraWalls(true, false, true, true, true);
        for (int i = 0; i < 10; i++) {
            Polymerase polymerase = new Polymerase(35 * i, 35 * i, activity);
            scene.getChildByIndex(AbstractGameActivity.LAYER_FOREGROUND).attachChild(polymerase.getSprite());
        }

        scene.setTouchAreaBindingOnActionDownEnabled(true);
        return scene;
    }

    private void resetGamePoints() {
        gameScore = INIT_SCORE;
        setScore(gameScore);
    }

    private void setScore(int score) {
        String padding = "";
        if (score < 10) {
            padding += " ";
        }
        if (score < 100) {
            padding += " ";
        }
        setScore(padding + score);
    }

    private void setScore(CharSequence text) {
        HUDScore.getText().setText(text);
    }

    private void gameOver() {
        final float posRatioX = 0.5f;
        final float posRatioY = 0f;
        if (gameScore >= 50) {
            activity.onWin(gameScore, posRatioX, posRatioY);
        } else {
            activity.onLose(gameScore, posRatioX, posRatioY);
        }
    }

    private void incrementScore() {
        setScore(++gameScore);
        Log.v(TAG, "beginContact - Increasing score to " + gameScore + ".");
    }

    @Override
    public void resetGame() {
        //TODO
    }

    @Override
    public boolean isPortrait() {
        return false;
    }

}
