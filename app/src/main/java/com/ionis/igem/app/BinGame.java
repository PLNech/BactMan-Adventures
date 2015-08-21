package com.ionis.igem.app;

import android.hardware.SensorManager;
import android.util.Log;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.ionis.igem.app.game.bins.Bin;
import com.ionis.igem.app.game.bins.Item;
import com.ionis.igem.app.game.managers.ResMan;
import com.ionis.igem.app.game.model.BaseGame;
import com.ionis.igem.app.game.model.FontAsset;
import com.ionis.igem.app.game.model.GFXAsset;
import com.ionis.igem.app.game.model.HUDElement;
import com.ionis.igem.app.ui.GameActivity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.List;

/**
 * Created by PLN on 21/08/2015.
 */
public class BinGame extends BaseGame {

    private static final String TAG = "BinGame";

    private int gameScore = 0;
    private int gameLives = 3;

    private HUDElement HUDScore;
    private HUDElement HUDLives;

    public BinGame(GameActivity pActivity) {
        super(pActivity);
        activity = pActivity;
    }

    @Override
    public List<GFXAsset> getGraphicalAssets() {
        if (graphicalAssets.isEmpty()) {
            graphicalAssets.add(new GFXAsset(ResMan.BIN1, 696, 1024, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.BIN2, 696, 1024, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.BIN3, 696, 1024, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.BIN4, 696, 1024, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.FACE_BOX_TILED, 696, 1024, 0, 0, 2, 1));
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
    public Vector2 getPhysicsVector() {
        return new Vector2(0, SensorManager.GRAVITY_EARTH);
    }

    @Override
    public List<HUDElement> getHudElements() {
        if (elements.isEmpty()) {
            Vector2 posS = activity.spritePosition(20f, 20f, 0.1f, 0.05f);
            Vector2 posL = activity.spritePosition(20f, 20f, 0.6f, 0.05f);

            IFont fontRoboto = activity.getFont(FontAsset.name(ResMan.F_HUD_BIN, ResMan.F_HUD_BIN_SIZE, ResMan.F_HUD_BIN_COLOR, ResMan.F_HUD_BIN_ANTI));
            final VertexBufferObjectManager vertexBufferObjectManager = activity.getVBOM();

            HUDScore = new HUDElement().buildText("Score: ", "Score: Over 9000.".length(),
                    posS.x, posS.y, fontRoboto, vertexBufferObjectManager);
            HUDLives = new HUDElement().buildText("Lives: ", "Lives: GAME OVER".length(),
                    posL.x, posS.y, fontRoboto, vertexBufferObjectManager);

            elements.add(HUDScore);
            elements.add(HUDLives);
        }

        return elements;
    }

    @Override
    public ContactListener getContactListener() {

        return new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                final Fixture x1 = contact.getFixtureA();
                final Fixture x2 = contact.getFixtureB();

                Bin bin;
                Item item;
                Log.d(TAG, "beginContact - Contact!");
                if (contact.isTouching()) {
                    if (Bin.isOne(x1)) {
                        bin = (Bin) x1.getBody().getUserData();
                        item = (Item) x2.getBody().getUserData();
                    } else if (Bin.isOne(x2)) {
                        bin = (Bin) x2.getBody().getUserData();
                        item = (Item) x1.getBody().getUserData();
                    } else {
                        /* Two items are touching. */
                        return;
                    }
                    Log.d(TAG, "beginContact - Item " + item.toString() + " went in bin " + bin.toString() + ".");
                    if (bin.accepts(item)) {
                        gameScore++;
                        Log.d(TAG, "beginContact - Increasing score to " + gameScore + ".");
                        if (gameScore >= 100) {
                            activity.onWin();
                        }
                        setScoreText("" + gameScore);
                    } else {
                        gameLives--;
                        Log.d(TAG, "beginContact - Decreasing lives to " + gameLives + ".");
                        setLivesText("" + gameLives);
                        if (gameLives == 0) {
                            activity.onLose();
                        }
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        };
    }

    @Override
    public Scene prepareScene() {
        Scene scene = activity.getScene();

        final Background backgroundColor = new Background(0.96862f, 0.77647f, 0.37647f);
        scene.setBackground(backgroundColor);

        createBins();

        final ITextureRegion smileyTextureRegion = activity.getTextureRegion(ResMan.FACE_BOX_TILED);
        createItem(activity.spriteCenter(smileyTextureRegion));
        createItem(activity.spritePosition(smileyTextureRegion, 0.2f, 0.5f));

        scene.setTouchAreaBindingOnActionDownEnabled(true);

        return scene;
    }

    private void setScoreText(CharSequence text) {
        HUDScore.getText().setText("Score: " + text);
    }

    private void setLivesText(CharSequence text) {
        HUDLives.getText().setText("Lives: " + text);
    }


    private void createItem(Vector2 pos) {
        createItem(pos.x, pos.y);
    }

    private void createItem(float posX, float posY) {
        final ITiledTextureRegion smileyTextureRegion = (ITiledTextureRegion) activity.getTextureRegion(ResMan.FACE_BOX_TILED);
        Item item = new Item(Item.Type.PAPER, smileyTextureRegion, posX, posY, activity.getVBOM(), activity.getPhysicsWorld());

        final Scene gameScene = activity.getScene();
        gameScene.getChildByIndex(GameActivity.LAYER_BACKGROUND).attachChild(item);
        gameScene.registerTouchArea(item);
    }

    private void createBin(Bin.Type type, ITextureRegion textureRegion, float posX, float posY) {
        Bin bin = new Bin(type, posX, posY, textureRegion, activity.getVBOM(), activity.getPhysicsWorld());
        activity.getScene().getChildByIndex(GameActivity.LAYER_FOREGROUND).attachChild(bin);
    }

    private void createBins() {
        final float binY = 0.85f;
        final ITextureRegion bin1TextureRegion = activity.getTextureRegion(ResMan.BIN1);
        final ITextureRegion bin2TextureRegion = activity.getTextureRegion(ResMan.BIN2);
        final ITextureRegion bin3TextureRegion = activity.getTextureRegion(ResMan.BIN3);
        final ITextureRegion bin4TextureRegion = activity.getTextureRegion(ResMan.BIN4);

        Vector2 bin1Pos = activity.spritePosition(bin1TextureRegion, 0.30f, binY, Bin.SCALE_DEFAULT);
        Vector2 bin2Pos = activity.spritePosition(bin2TextureRegion, 0.50f, binY, Bin.SCALE_DEFAULT);
        Vector2 bin3Pos = activity.spritePosition(bin3TextureRegion, 0.70f, binY, Bin.SCALE_DEFAULT);
        Vector2 bin4Pos = activity.spritePosition(bin4TextureRegion, 0.90f, binY, Bin.SCALE_DEFAULT);

        createBin(Bin.Type.GLASS, bin1TextureRegion, bin1Pos.x, bin1Pos.y);
        createBin(Bin.Type.LIQUIDS, bin2TextureRegion, bin2Pos.x, bin2Pos.y);
        createBin(Bin.Type.NORMAL, bin3TextureRegion, bin3Pos.x, bin3Pos.y);
        createBin(Bin.Type.BIO, bin4TextureRegion, bin4Pos.x, bin4Pos.y);
    }

}
