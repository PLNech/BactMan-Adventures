package com.ionis.igem.app;

import android.util.Log;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.ionis.igem.app.game.bins.Bin;
import com.ionis.igem.app.game.bins.Item;
import com.ionis.igem.app.game.managers.ResMan;
import com.ionis.igem.app.game.model.FontAsset;
import com.ionis.igem.app.game.model.GFXAsset;
import com.ionis.igem.app.game.model.BaseGame;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.font.IFont;

import java.util.List;

/**
 * Created by PLN on 21/08/2015.
 */
public class BinGame extends BaseGame {

    private static final String TAG = "BinGame";

    private int gameScore = 0;
    private int gameLives = 3;

    @Override
    public List<GFXAsset> getGraphicalAssets() {
        graphicalAssets.add(new GFXAsset(ResMan.BIN1, 696, 1024, 0, 0));
        graphicalAssets.add(new GFXAsset(ResMan.BIN2, 696, 1024, 0, 0));
        graphicalAssets.add(new GFXAsset(ResMan.BIN3, 696, 1024, 0, 0));
        graphicalAssets.add(new GFXAsset(ResMan.BIN4, 696, 1024, 0, 0));
        graphicalAssets.add(new GFXAsset(ResMan.FACE_BOX_TILED, 696, 1024, 0, 0, 2, 1));
        return graphicalAssets;
    }

    @Override
    public List<FontAsset> getFontAssets() {
        fontAssets.add(new FontAsset(ResMan.F_HUD_BIN, ResMan.F_HUD_BIN_SIZE, ResMan.F_HUD_BIN_COLOR, ResMan.F_HUD_BIN_ANTI));
        return fontAssets;
    }

    @Override
    public Scene prepareScene(Scene scene) {

        return scene;
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
                            setScoreText("" + gameScore);
                        } else {
                            gameLives--;
                            Log.d(TAG, "beginContact - Decreasing lives to " + gameLives + ".");
                            setLivesText("" + gameLives);
                            if (gameLives == 0) {
                                onLose();
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

    private void setScoreText(CharSequence text) {
        gameScoreText.setText("Score: " + text);
    }

    private void setLivesText(CharSequence text) {
        gameLivesText.setText("Lives: " + text);
    }

    @Override
    public Vector2 getPhysicsVector() {
        return null;
    }
}
