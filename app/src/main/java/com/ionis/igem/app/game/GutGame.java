package com.ionis.igem.app.game;

import android.hardware.SensorManager;
import android.util.Log;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.ionis.igem.app.game.gut.Flow;
import com.ionis.igem.app.game.gut.Item;
import com.ionis.igem.app.game.gut.Player;
import com.ionis.igem.app.game.managers.ResMan;
import com.ionis.igem.app.game.model.BaseGame;
import com.ionis.igem.app.game.model.HUDElement;
import com.ionis.igem.app.game.model.Wall;
import com.ionis.igem.app.game.model.res.FontAsset;
import com.ionis.igem.app.game.model.res.GFXAsset;
import com.ionis.igem.app.utils.CalcUtils;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PLNech on 31/08/2015.
 */
public class GutGame extends BaseGame {
    private static final String TAG = "GutGame";

    public static final int INIT_SCORE = 0;
    public static final int INIT_LIVES = 3;
    public static final float POS_ITEM_X = 850; // Initial item abscissa
    public static final int SPEED_ITEM_PPS = -150; // Initial item horizontal velocity
    public static final int[] POS_FLOW = {110, 185, 260, 335};
    public static final int NB_ITEMS = 4;

    public static final short CATEGORY_WALL = 1;
    public static final short CATEGORY_PLAYER = 2;
    public static final short CATEGORY_ITEM = 4;
    public static final short MASK_WALL = CATEGORY_ITEM + CATEGORY_PLAYER;
    public static final short MASK_PLAYER = CATEGORY_WALL + CATEGORY_ITEM;
    public static final short MASK_ITEM = CATEGORY_WALL + CATEGORY_PLAYER;

    public static final short GROUP_INDEX = 0;
    public static final float ITEM_PERIOD = 0.25f;

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
            graphicalAssets.add(new GFXAsset(ResMan.GUT_ANTIBIO, 512, 508, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.GUT_VITAMIN, 512, 512, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.GUT_PROTEIN, 512, 512, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.GUT_PHAGE, 512, 663, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.GUT_BG, 2048, 1125, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.GUT_FLOW, 512, 33, 0, 0));

            /* HUD */
            graphicalAssets.add(new GFXAsset(ResMan.HUD_LIVES, 1479, 1024, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.HUD_SCORE, 1885, 1024, 0, 0));
        }
        return graphicalAssets;
    }

    @Override
    public List<FontAsset> getFontAssets() {
        if (fontAssets.isEmpty()) {
            fontAssets.add(new FontAsset(ResMan.F_HUD_GUT, ResMan.F_HUD_GUT_SIZE, ResMan.F_HUD_BIN_COLOR, ResMan.F_HUD_BIN_ANTI));
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

            final float scale = 0.08f;

            Vector2 posS = new Vector2(5, 0);
            Vector2 posL = new Vector2(350, 0);

            Vector2 offS = new Vector2(75, 30);
            Vector2 offL = new Vector2(340, 27.5f);

            IFont fontRoboto = activity.getFont(FontAsset.name(ResMan.F_HUD_GUT, ResMan.F_HUD_GUT_SIZE, ResMan.F_HUD_BIN_COLOR, ResMan.F_HUD_BIN_ANTI));
            Log.d(TAG, "getHudElements - sprites: " + posS + ", " + posL + " - text:" + offS.add(posS) + ", " + offL.add(posL));

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

        resetGamePoints();
        createBackground(scene);
        createCameraWalls(true, false, true, true, true);
        createPlayer();
        createItems(NB_ITEMS);

        scene.setTouchAreaBindingOnActionDownEnabled(true);

        return scene;
    }

    private void createBackground(Scene scene) {
        final SmoothCamera camera = activity.getCamera();
        final VertexBufferObjectManager vbom = activity.getVBOM();
        final ITiledTextureRegion texFlow = activity.getTexture(ResMan.GUT_FLOW);
        final IEntity layerBG = scene.getChildByIndex(AbstractGameActivity.LAYER_BACKGROUND);

        scene.setBackground(new SpriteBackground(new Sprite(0, 0, camera.getWidth(), camera.getHeight(),
                activity.getTexture(ResMan.GUT_BG), vbom)));

        for (int i = 0; i < 4; i++) {
            layerBG.attachChild(new Flow(POS_FLOW[i], texFlow, vbom).getSprite());
        }
    }

    private void createPlayer() {
        player = new Player(150, 240, 0, activity.getTexture(ResMan.GUT_BACTMAN), activity);
        final Scene scene = activity.getScene();
        scene.getChildByIndex(AbstractGameActivity.LAYER_FOREGROUND).attachChild(player.getSprite());
    }

    private void createItems(final int count) {
        if (count > 0) {
            createItem();
            activity.registerUpdateHandler(ITEM_PERIOD, new ITimerCallback() {
                @Override
                public void onTimePassed(TimerHandler pTimerHandler) {
                    createItems(count - 1);
                }
            });
        }
    }

    private void createItem() {
        createItem(Item.Type.random());
    }

    private void createItem(Item.Type type) {
        final ITiledTextureRegion texture;
        float angleD;
        switch (type) {
            case NUTRIENT:
                if (random.nextBoolean()) {
                    texture = activity.getTexture(ResMan.GUT_VITAMIN);
                    angleD = -45 + CalcUtils.randomOf(90, random);
                } else {
                    texture = activity.getTexture(ResMan.GUT_PROTEIN);
                    angleD = CalcUtils.randomOf(360, random);
                }
                break;
            case IMMUNO:
                angleD = 90;
                texture = activity.getTexture(ResMan.GUT_PHAGE);
                break;
            case ANTIBIO:
                angleD = CalcUtils.randomOf(360, random);
                texture = activity.getTexture(ResMan.GUT_ANTIBIO);
                break;
            default:
                throw new IllegalStateException("No default!");
        }

        float texHeight = type == Item.Type.IMMUNO ? texture.getWidth() : texture.getHeight();
        texHeight *= Item.SCALE_DEFAULT;
        final float lanePos = POS_FLOW[CalcUtils.randomOf(4, random)] + 5;
        final float laneHeight = POS_FLOW[1] - POS_FLOW[0];
        float itemY = lanePos + (laneHeight - texHeight) / 2;

        Item item = new Item(POS_ITEM_X, itemY, (float) Math.toRadians(angleD), type, (float) ((50.0 + gameScore) / 50), texture, activity);
        items.add(item);
        activity.getScene().getChildByIndex(Item.chooseLayer(type)).attachChild(item.getSprite());
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
                    createItem();
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
        if (score < 100) {
            padding += " ";
        }
        setScore(padding + score);
    }

    private void setScore(CharSequence text) {
        HUDScore.getText().setText(text);
    }

    private void decrementLives() {
        Log.v(TAG, "beginContact - Decreasing lives to " + gameLives + ".");
        if (--gameLives <= 0) {
            setPlaying(false);
            final float posRatioX = 0.5f;
            final float posRatioY = 0f;
            if (gameScore >= 50) {
                activity.onWin(gameScore, posRatioX, posRatioY);
            } else {
                activity.onLose(gameScore, posRatioX, posRatioY);
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

                createItems(NB_ITEMS);
            }
        });
    }

    @Override
    public IOnSceneTouchListener getOnSceneTouchListener() {
        return new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
                    final Body body = player.getBody();
                    float ratio = PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
                    body.setTransform(body.getPosition().x, pSceneTouchEvent.getY() / ratio, body.getAngle());
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
                    } else if (Item.isOne(x1) && Item.isOne(x2)) {
                        x1.setSensor(true);
                    }
                }
            }

            private void handlePlayerItemContact(final Item item) {
                final Color toColor;
                switch (item.getType()) {
                    case NUTRIENT:
                        incrementScore();
                        toColor = Color.GREEN;
                        break;
                    case IMMUNO:
                    case ANTIBIO:
                        toColor = Color.RED;
                        decrementLives();
                        break;
                    default: throw new IllegalStateException();
                }

                final float pDuration = 0.25f;

                player.getSprite().registerEntityModifier(new SequenceEntityModifier(
                        new ColorModifier(pDuration, Color.WHITE, toColor),
                        new ColorModifier(pDuration, toColor, Color.WHITE),
                        new DelayModifier(pDuration * 2)
                ));
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
