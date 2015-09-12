package com.ionis.igem.app.game;

import android.util.Log;
import com.badlogic.gdx.math.Vector2;
import com.ionis.igem.app.game.managers.ResMan;
import com.ionis.igem.app.game.model.BaseGame;
import com.ionis.igem.app.game.model.HUDElement;
import com.ionis.igem.app.game.model.TouchableAnimatedSprite;
import com.ionis.igem.app.game.model.res.FontAsset;
import com.ionis.igem.app.game.model.res.GFXAsset;
import com.ionis.igem.app.game.piano.Base;
import com.ionis.igem.app.game.piano.Key;
import com.ionis.igem.app.game.piano.Polymerase;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PLNech on 31/08/2015.
 */
public class PianoGame extends BaseGame {
    private static final String TAG = "PianoGame";

    public static final int INIT_SCORE = 0;
    public static final float CREATION_INTERVAL = 0.05f;

    private HUDElement HUDScore;
    private int gameScore;
    private ArrayList<Base> bases = new ArrayList<>();
    private ArrayList<Base> baseCpls = new ArrayList<>();

    private int currentBaseIndex = 0;

    public PianoGame(AbstractGameActivity pActivity) {
        super(pActivity);
    }

    @Override
    public List<GFXAsset> getGraphicalAssets() {
        if (graphicalAssets.isEmpty()) {
            graphicalAssets.add(new GFXAsset(ResMan.PIANO_BG, 1841, 1395, 0, 0));
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
            Vector2 offS = new Vector2(50, 20);

            IFont fontRoboto = activity.getFont(FontAsset.name(ResMan.F_HUD_BIN, ResMan.F_HUD_BIN_SIZE, ResMan.F_HUD_BIN_COLOR, ResMan.F_HUD_BIN_ANTI));
            Log.d(TAG, "getHudElements - sprite: " + posS + " - text:" + offS.add(posS));

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

        Polymerase polymerase = new Polymerase(200, 35, activity);
        scene.getChildByIndex(AbstractGameActivity.LAYER_FOREGROUND).attachChild(polymerase.getSprite());
        createKeys();

        createADN();

        scene.setTouchAreaBindingOnActionDownEnabled(true);
        return scene;
    }

    private void createADN() {
        createADN(15);
    }

    private void createADN(final int count) {
        if (count > 0) {
            createBase();
            activity.registerUpdateHandler(CREATION_INTERVAL, new ITimerCallback() {
                @Override
                public void onTimePassed(TimerHandler pTimerHandler) {
                    createADN(count - 1);
                }
            });
        }
    }

    private void createBase() {
        shiftBases();
        createBase(Base.Type.random(), new Vector2(800, 200), false);
    }

    private void createCplBase(Base.Type type) {
        createBase(type, new Vector2(245, 125), true);
    }

    private void createBase(Base.Type t, Vector2 pos, boolean cpl) {
        Base b = new Base(pos.x, pos.y, t, cpl, activity);
        final IEntity layerBG = activity.getScene().getChildByIndex(AbstractGameActivity.LAYER_BACKGROUND);
        layerBG.attachChild(b.getPhosphate());
        layerBG.attachChild(b.getSprite());
        if (cpl) {
            baseCpls.add(b);
        } else {
            bases.add(b);
        }
    }

    private void createKeys() {
        createKey(Base.Type.A);
        createKey(Base.Type.T);
        createKey(Base.Type.G);
        createKey(Base.Type.C);
    }

    private void createKey(Base.Type type) {
        Key key = new Key(type, this);
        final Scene scene = activity.getScene();
        final TouchableAnimatedSprite sprite = key.getSprite();

        scene.getChildByIndex(AbstractGameActivity.LAYER_FOREGROUND).attachChild(sprite);
        scene.registerTouchArea(sprite);
    }

    private Vector2 shiftPos(float x, float y) {
        final float baseWidth = 27; // Maximum sprite width, once scaled
        final float margin = 10;
        return new Vector2(x - baseWidth - margin, y);
    }

    private void shiftBases() {
        for (Base base : bases) {
            shiftBase(base);
        }
        for (Base base : baseCpls) {
            shiftBase(base);
        }
    }

    private void shiftBase(Base base) {
        final Sprite s = base.getSprite();
        final Sprite phosphate = base.getPhosphate();
        final Vector2 newPos = shiftPos(s.getX(), s.getY());
        final Vector2 newPosPho = shiftPos(phosphate.getX(), phosphate.getY());

        Log.d(TAG, "shiftBase: shifted from " + s.getX() + ", " + s.getY() + " to " + newPos.x + ", " + newPos.y);
        s.setPosition(newPos.x, newPos.y);
        phosphate.setPosition(newPosPho.x, newPosPho.y);
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

    public void onKeyPress(Base.Type type) {
        if (bases.get(currentBaseIndex).getCplType() == type) {
            createBase();
            createCplBase(type);
            currentBaseIndex++;
        }
    }

    @Override
    public boolean isPortrait() {
        return false;
    }
}
