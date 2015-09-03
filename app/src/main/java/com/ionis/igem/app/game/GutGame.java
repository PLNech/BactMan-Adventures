package com.ionis.igem.app.game;

import android.hardware.SensorManager;
import android.util.Log;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.ionis.igem.app.game.gut.Item;
import com.ionis.igem.app.game.gut.Player;
import com.ionis.igem.app.game.managers.ResMan;
import com.ionis.igem.app.game.model.BaseGame;
import com.ionis.igem.app.game.model.HUDElement;
import com.ionis.igem.app.game.model.Wall;
import com.ionis.igem.app.game.model.res.FontAsset;
import com.ionis.igem.app.game.model.res.GFXAsset;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PLNech on 31/08/2015.
 */
public class GutGame extends BaseGame {
    private static final String TAG = "GutGame";

    public static final int INIT_SCORE = 0;
    public static final int INIT_LIVES = 3;

    private int gameScore = INIT_SCORE;
    private int gameLives = INIT_LIVES;

    private HUDElement HUDScore;
    private HUDElement HUDLives;

    private ArrayList<Item> items = new ArrayList<>();
    private Player player;

    public GutGame(AbstractGameActivity pActivity) {
        super(pActivity);
    }

    @Override
    public List<GFXAsset> getGraphicalAssets() {
        if (graphicalAssets.isEmpty()) {
            graphicalAssets.add(new GFXAsset(ResMan.GUT_BACTMAN, 512, 342, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.GUT_ACID, 512, 1077, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.GUT_ANTIBIO, 512, 508, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_GEL, 512, 394, 0, 0));

            /* HUD */
            graphicalAssets.add(new GFXAsset(ResMan.HUD_LIVES, 1479, 1024, 0, 0));
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
    public Vector2 getPhysicsVector() {
        return new Vector2(-SensorManager.GRAVITY_EARTH, 0);
    }


    @Override
    public List<HUDElement> getHudElements() {
        if (elements.isEmpty()) {
            final ITiledTextureRegion textureScore = activity.getTexture(ResMan.HUD_SCORE);
            final ITiledTextureRegion textureLives = activity.getTexture(ResMan.HUD_LIVES);

            final float scale = 0.120f;

            Vector2 posS = new Vector2(5, 0);
            Vector2 posL = new Vector2(325, 0);

            Vector2 offS = new Vector2(120, 45);
            Vector2 offL = new Vector2(310, 45);

            Log.d(TAG, "getHudElements - sprites: " + posS + ", " + posL + " - text:" + offS.add(posS) + ", " + offL.add(posL));
            IFont fontRoboto = activity.getFont(FontAsset.name(ResMan.F_HUD_BIN, ResMan.F_HUD_BIN_SIZE, ResMan.F_HUD_BIN_COLOR, ResMan.F_HUD_BIN_ANTI));
            activity.putFont(ResMan.F_HUD_BIN, fontRoboto);

            final VertexBufferObjectManager vbom = activity.getVBOM();

            HUDScore = new HUDElement()
                    .buildSprite(posS, textureScore, vbom, scale)
                    .buildText("", "31337".length(), offS, fontRoboto, vbom);
            HUDLives = new HUDElement()
                    .buildSprite(posL, textureLives, vbom, scale)
                    .buildText("", "999".length(), offL, fontRoboto, vbom);

            elements.add(HUDScore);
            elements.add(HUDLives);
        }

        return elements;
    }

    @Override
    public Scene prepareScene() {
        Scene scene = activity.getScene();

        final Background backgroundColor = new Background(0.96862f, 0.63921f, 0.35686f);
        scene.setBackground(backgroundColor);

        resetGamePoints();
        createPlayer();
        createItems();

        scene.setTouchAreaBindingOnActionDownEnabled(true);

        return scene;
    }

    private void createPlayer() {
        player = new Player(240, 240, 0, activity.getTexture(ResMan.GUT_BACTMAN), activity);
        final Scene scene = activity.getScene();
        scene.getChildByIndex(AbstractGameActivity.LAYER_FOREGROUND).attachChild(player.getSprite());
    }

    private void createItems() {
        createItem(400, 400, Item.Type.ANTIBIO);
    }

    private void createItem(int x, int y, Item.Type type) {
        Item item = new Item(x, y, 0.5f, type, activity);
        items.add(item);
        activity.getScene().getChildByIndex(AbstractGameActivity.LAYER_FOREGROUND).attachChild(item.getSprite());
    }

    private void deleteItem(final Item item) {
        final AnimatedSprite sprite = item.getSprite();
        sprite.setVisible(false);
        activity.getScene().getChildByIndex(AbstractGameActivity.LAYER_BACKGROUND).detachChild(sprite);
        activity.markForDeletion(item);
    }

    private void recycleItem(final Item item) {
        deleteItem(item);
        activity.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                items.remove(item);
                if (isPlaying()) {
                    createItem(400, 400, Item.Type.random());
                }
            }
        });
    }

    private void resetGamePoints() {
        gameScore = INIT_SCORE;
        gameLives = INIT_LIVES;
        setScore(gameScore);
        setLives(gameLives);
    }


    private void setLives(int value) {
        setLives("" + value);
    }

    private void setLives(CharSequence text) {
        HUDLives.getText().setText(text);
    }

    private void setScore(int score) {
        String padding = "";
        if (score < 10) {
            padding += " ";
        }
        setScore(padding + score);
    }

    private void setScore(CharSequence text) {
        HUDScore.getText().setText(text);
    }

    private void decrementLives() {
        Log.v(TAG, "beginContact - Decreasing lives to " + gameLives + ".");
        if (--gameLives == 0) {
            setPlaying(false);
            if (gameScore >= 50) {
                activity.onWin(gameScore);
            } else {
                activity.onLose(gameScore);
            }
        }

        setLives(gameLives);
    }

    private void incrementScore() {
        setScore(++gameScore);
        Log.v(TAG, "beginContact - Increasing score to " + gameScore + ".");
        Log.v(TAG, "beginContact - Increasing gravity to " + activity.getPhysicsWorld().getGravity() + ".");
    }

    @Override
    public void resetGame() {
        activity.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                resetGamePoints();
                activity.getPhysicsWorld().setGravity(getPhysicsVector());

                for (final Item item : items) {
                    deleteItem(item);
                }
                items.clear();
                Log.d(TAG, "resetGame - Cleared game items.");

                createItems();
            }
        });
    }

    @Override
    public IOnSceneTouchListener getOnSceneTouchListener() {
        return new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
                Log.d(TAG, "onSceneTouchEvent - Touched: " + pSceneTouchEvent.getAction());
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
                    Log.d(TAG, "onSceneTouchEvent - Moving: " + pSceneTouchEvent.getX() + ", " + pSceneTouchEvent.getY());
                    final Body body = player.getBody();
                    final float x = body.getPosition().x;
                    final float y = body.getPosition().y;
                    float velocityX = pSceneTouchEvent.getX() - x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
                    float velocityY = pSceneTouchEvent.getY() - y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
                    body.setLinearVelocity(velocityX, velocityY);
                    return true;
                }
                return false;
            }
        };
    }

    @Override
    public ContactListener getContactListener() {
        return new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                final Fixture x1 = contact.getFixtureA();
                final Fixture x2 = contact.getFixtureB();

                Item item;
                if (contact.isTouching()) {
                    if (Player.isOne(x1) && Item.isOne(x2)) {
                        item = (Item) x2.getBody().getUserData();
                        handlePlayerItemContact(item);
                    } else if (Player.isOne(x2) && Item.isOne(x1)) {
                        item = (Item) x1.getBody().getUserData();
                        handlePlayerItemContact(item);
                    } else if (Wall.isOne(x1) && Item.isOne(x2)) {
                        item = (Item) x2.getBody().getUserData();
                        handleWallItemContact(item);
                    } else if (Wall.isOne(x2) && Item.isOne(x1)) {
                        item = (Item) x1.getBody().getUserData();
                        handleWallItemContact(item);
                    }
                }
            }

            private void handlePlayerItemContact(final Item item) {
                switch (item.getType()) {
                    case NUTRIENT:
                        incrementScore();
                        break;
                    case IMMUNO:
                    case ANTIBIO:
                        decrementLives();
                        break;

                }
                recycleItem(item);
            }

            private void handleWallItemContact(Item item) {
                recycleItem(item);
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
    public boolean isPortrait() {
        return false;
    }

}
