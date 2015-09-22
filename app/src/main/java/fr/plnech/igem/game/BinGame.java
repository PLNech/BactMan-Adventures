package fr.plnech.igem.game;

import android.hardware.SensorManager;
import android.util.Log;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import fr.plnech.igem.game.bins.Bin;
import fr.plnech.igem.game.bins.Item;
import fr.plnech.igem.game.managers.ResMan;
import fr.plnech.igem.game.model.HUDElement;
import fr.plnech.igem.game.model.PortraitGame;
import fr.plnech.igem.game.model.TouchableAnimatedSprite;
import fr.plnech.igem.game.model.Wall;
import fr.plnech.igem.game.model.res.FontAsset;
import fr.plnech.igem.game.model.res.GFXAsset;
import fr.plnech.igem.utils.CalcUtils;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by PLNech on 21/08/2015.
 */
public class BinGame extends PortraitGame {

    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<Integer> deadItems = new ArrayList<>();
    private ArrayList<Bin> bins = new ArrayList<>();
    private HashMap<Bin.Type, Bin> binMap = new HashMap<>();

    private static final String TAG = "BinGame";

    private int gameLives = INIT_LIVES;

    private HUDElement HUDScore;
    private HUDElement HUDLives;

    public BinGame(AbstractGameActivity pActivity) {
        super(pActivity);
    }

    @Override
    public List<GFXAsset> getGraphicalAssets() {
        if (graphicalAssets.isEmpty()) {
            graphicalAssets.add(new GFXAsset(ResMan.BIN_BG, 960, 1600));

            /* Bins */
            graphicalAssets.add(new GFXAsset(ResMan.BIN_SHARPS, 512, 753));
            graphicalAssets.add(new GFXAsset(ResMan.BIN_CHEMICAL, 512, 753));
            graphicalAssets.add(new GFXAsset(ResMan.BIN_REGULAR, 512, 753));
            graphicalAssets.add(new GFXAsset(ResMan.BIN_BIO, 512, 753));

            /* Items */

            graphicalAssets.add(new GFXAsset(ResMan.ITEM_TUBE, 99, 512));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_CONE_BLUE, 59, 512));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_PEN, 390, 2048));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_CONE_WHITE, 235, 2048));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_CONE_YELLOW, 235, 2048));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_BECHER_GREEN, 462, 512));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_BECHER_ORANGE, 462, 512));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_BECHER_RED, 462, 512));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_BECHER_BROKEN, 462, 512));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_ERLEN_GREEN, 512, 705));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_ERLEN_ORANGE, 512, 705));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_ERLEN_RED, 512, 705));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_ERLEN_BROKEN, 512, 704));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_SLIDE, 151, 512));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_SLIDE_BROKEN, 152, 512));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_PETRI, 512, 323));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_PAPER, 512, 560));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_GLOVES, 512, 541));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_GEL, 512, 394));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_MICROTUBE_GREEN, 284, 512));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_MICROTUBE_RED, 284, 512));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_ROUNDFLASK_GREEN, 512, 782));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_ROUNDFLASK_ORANGE, 512, 782));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_ROUNDFLASK_RED, 512, 782));
            graphicalAssets.add(new GFXAsset(ResMan.ITEM_ROUNDFLASK_BROKEN, 512, 782));

            /* HUD */
            graphicalAssets.add(new GFXAsset(ResMan.HUD_LIVES, 1479, 1024));
            graphicalAssets.add(new GFXAsset(ResMan.HUD_SCORE, 1885, 1024));
        }
        return graphicalAssets;
    }

    @Override
    public List<GFXAsset> getProfAssets() {
        if (profAssets.isEmpty()) {
            final int profWidth = 1440;
            final int profHeight = 2400;

            if (Locale.getDefault().getLanguage().equals("fr")) {
                profAssets.add(new GFXAsset(ResMan.PROF_BIN_1_FR, profWidth, profHeight));
                profAssets.add(new GFXAsset(ResMan.PROF_BIN_2_FR, profWidth, profHeight));
                profAssets.add(new GFXAsset(ResMan.PROF_BIN_3_FR, profWidth, profHeight));
                profAssets.add(new GFXAsset(ResMan.PROF_BIN_4_FR, profWidth, profHeight));
                profAssets.add(new GFXAsset(ResMan.PROF_BIN_5_FR, profWidth, profHeight));
                profAssets.add(new GFXAsset(ResMan.PROF_BIN_6_FR, profWidth, profHeight));
            } else {
                profAssets.add(new GFXAsset(ResMan.PROF_BIN_1, profWidth, profHeight));
                profAssets.add(new GFXAsset(ResMan.PROF_BIN_2, profWidth, profHeight));
                profAssets.add(new GFXAsset(ResMan.PROF_BIN_3, profWidth, profHeight));
                profAssets.add(new GFXAsset(ResMan.PROF_BIN_4, profWidth, profHeight));
                profAssets.add(new GFXAsset(ResMan.PROF_BIN_5, profWidth, profHeight));
                profAssets.add(new GFXAsset(ResMan.PROF_BIN_6, profWidth, profHeight));
            }
        }
        return profAssets;
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
            final ITiledTextureRegion textureScore = activity.getTexture(ResMan.HUD_SCORE);
            final ITiledTextureRegion textureLives = activity.getTexture(ResMan.HUD_LIVES);

            final float scale = 0.120f;

            Vector2 posS = new Vector2(5, 0); //activity.spritePosition(textureScore, 0.1f, 0.05f, HUDElement.SCALE_DEFAULT);
            Vector2 posL = new Vector2(155, 0); //activity.spritePosition(textureLives, 0.6f, 0.05f, HUDElement.SCALE_DEFAULT);

            Vector2 offS = new Vector2(120, 45);
            Vector2 offL = new Vector2(170, 45);

            IFont fontRoboto = activity.getFont(FontAsset.name(ResMan.F_HUD_BIN, ResMan.F_HUD_BIN_SIZE, ResMan.F_HUD_BIN_COLOR, ResMan.F_HUD_BIN_ANTI));

            final VertexBufferObjectManager vbom = activity.getVBOM();

            HUDScore = new HUDElement()
                    .buildSprite(posS, textureScore, vbom, scale)
                    .buildText("", "31337".length(), posS.add(offS), fontRoboto, vbom);
            HUDLives = new HUDElement()
                    .buildSprite(posL, textureLives, vbom, scale)
                    .buildText("", "999".length(), posL.add(offL), fontRoboto, vbom);

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
                if (contact.isTouching()) {
                    if (Bin.isOne(x1)) {
                        bin = (Bin) x1.getBody().getUserData();
                        item = (Item) x2.getBody().getUserData();
                        handleBinItemContact(bin, item);
                    } else if (Bin.isOne(x2)) {
                        bin = (Bin) x2.getBody().getUserData();
                        item = (Item) x1.getBody().getUserData();
                        handleBinItemContact(bin, item);
                    } else if (Wall.isOne(x1)) {
                        item = (Item) x2.getBody().getUserData();
                        handleWallItemContact(item, (Wall.Type) x1.getBody().getUserData());
                    } else if (Wall.isOne(x2)) {
                        item = (Item) x1.getBody().getUserData();
                        handleWallItemContact(item, (Wall.Type) x2.getBody().getUserData());
                    }
                }
            }

            private void handleBinItemContact(final Bin bin, final Item item) {
                final boolean validMove = bin.accepts(item);
                Log.v(TAG, "beginContact - Item " + item + " went in bin " + bin + (validMove ? " :)" : " :("));

                if (deadItems.contains(item.getId())) {
                    Log.e(TAG, "handleBinItemContact - " + "Contacts an item marked for deletion!");
                    return;
                }
                final Bin.Animation animation = validMove ? Bin.Animation.VALID_HIT : Bin.Animation.INVALID_HIT;

                animate(bin, animation);
                if (validMove) {
                    incrementScore(item.getValue());
                } else {
                    animate(binMap.get(item.getType().getValid()), Bin.Animation.VALID_MISS);
                    decrementLives();
                }
                recycleItem(item);
            }

            private void handleWallItemContact(Item item, Wall.Type wallType) {
                Log.d(TAG, "handleWallItemContact - Item " + item + " did collide wall " + wallType);
                if (wallType == Wall.Type.BOTTOM) {
                    animateBins(Bin.Animation.INVALID_HIT);
                    recycleItem(item);
                    decrementLives();
                } else {
                    item.setValue(item.getValue() + 1);
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

    private void decrementLives() {
        Log.v(TAG, "beginContact - Decreasing lives to " + gameLives + ".");
        if (--gameLives == 0) {
            setPlaying(false);
            final float posY = 0.3f;
            final float posX = 0.5f;
            if (gameScore >= 50) {
                activity.onWin(gameScore, posX, posY);
            } else {
                activity.onLose(gameScore, posX, posY);
            }
        }

        activity.setPhysicsCoeff(0.8f);
        setLives(gameLives);
        Log.v(TAG, "beginContact - Decreasing speed to " + activity.getPhysicsWorld().getGravity() + ".");
    }

    private void incrementScore(int increment) {
        for (int i = 0; i < increment; i++) {
            incrementScore();
        }
    }

    private void incrementScore() {
        setScore(++gameScore);
        activity.setPhysicsCoeff(1.05f);
        Log.v(TAG, "beginContact - Increasing score to " + gameScore + ".");
        Log.v(TAG, "beginContact - Increasing gravity to " + activity.getPhysicsWorld().getGravity() + ".");
    }

    @Override
    public Scene prepareScene() {
        Scene scene = activity.getScene();
        scene.setBackground(new SpriteBackground(new Sprite(0, 0, PortraitGameActivity.CAMERA_WIDTH,
                PortraitGameActivity.CAMERA_HEIGHT, activity.getTexture(ResMan.BIN_BG), activity.getVBOM())));

        resetGamePoints();
        createCameraWalls();
        createBins();
        createItems();

        scene.setTouchAreaBindingOnActionDownEnabled(true);

        return scene;
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

    private void resetGamePoints() {
        gameScore = INIT_SCORE;
        gameLives = INIT_LIVES;
        setScore(gameScore);
        setLives(gameLives);
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

    private void setLives(int value) {
        setLives("" + value);
    }

    private void setLives(CharSequence text) {
        HUDLives.getText().setText(text);
    }

    private void createItems() {
        activity.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                createItem(Item.Type.random());
            }
        });
    }

    private void createItem(Item.Type type) {
        float posRatioX = 0.15f + random.nextFloat() * 0.85f;
        float posRatioY = 0.2f;
        Vector2 itemPos = activity.spritePosition(32, 32, posRatioX, posRatioY);
        createItem(itemPos, type);
    }

    private void createItem(Vector2 pos, Item.Type type) {
        createItem(pos.x, pos.y, type);
    }

    private void createItem(float posX, float posY, Item.Type type) {
        ITiledTextureRegion textureRegion = activity.getTexture(ResMan.FACE_BOX_TILED);

        switch (type) {
            case PAPER:
                textureRegion = activity.getTexture(ResMan.ITEM_PAPER);
                break;
            case CONE:
                switch (CalcUtils.randomOf(3)) {
                    case 0:
                        textureRegion = activity.getTexture(ResMan.ITEM_CONE_BLUE);
                        break;
                    case 1:
                        textureRegion = activity.getTexture(ResMan.ITEM_CONE_YELLOW);
                        break;
                    case 2:
                        textureRegion = activity.getTexture(ResMan.ITEM_CONE_WHITE);
                        break;
                }
                break;
            case TUBE:
                textureRegion = activity.getTexture(ResMan.ITEM_TUBE);
                break;
            case SLIDE:
                textureRegion = activity.getTexture(ResMan.ITEM_SLIDE);
                break;
            case SLIDE_BROKEN:
                textureRegion = activity.getTexture(ResMan.ITEM_SLIDE_BROKEN);
                break;
            case PETRI_DISH:
                textureRegion = activity.getTexture(ResMan.ITEM_PETRI);
                break;
            case PEN:
                textureRegion = activity.getTexture(ResMan.ITEM_PEN);
                break;
            case GLOVES:
                textureRegion = activity.getTexture(ResMan.ITEM_GLOVES);
                break;
            case GEL:
                textureRegion = activity.getTexture(ResMan.ITEM_GEL);
                break;
            case BECHER:
                switch (CalcUtils.randomOf(3)) {
                    case 0:
                        textureRegion = activity.getTexture(ResMan.ITEM_BECHER_GREEN);
                        break;
                    case 1:
                        textureRegion = activity.getTexture(ResMan.ITEM_BECHER_ORANGE);
                        break;
                    case 2:
                        textureRegion = activity.getTexture(ResMan.ITEM_BECHER_RED);
                        break;
                }
                break;
            case BECHER_BROKEN:
                textureRegion = activity.getTexture(ResMan.ITEM_BECHER_BROKEN);
                break;
            case ERLEN:
                switch (CalcUtils.randomOf(3)) {
                    case 0:
                        textureRegion = activity.getTexture(ResMan.ITEM_ERLEN_GREEN);
                        break;
                    case 1:
                        textureRegion = activity.getTexture(ResMan.ITEM_ERLEN_ORANGE);
                        break;
                    case 2:
                        textureRegion = activity.getTexture(ResMan.ITEM_ERLEN_RED);
                        break;
                }
                break;
            case ERLEN_BROKEN:
                textureRegion = activity.getTexture(ResMan.ITEM_ERLEN_BROKEN);
                break;
            case ROUNDFLASK:
                switch (CalcUtils.randomOf(3)) {
                    case 0:
                        textureRegion = activity.getTexture(ResMan.ITEM_ROUNDFLASK_GREEN);
                        break;
                    case 1:
                        textureRegion = activity.getTexture(ResMan.ITEM_ROUNDFLASK_ORANGE);
                        break;
                    case 2:
                        textureRegion = activity.getTexture(ResMan.ITEM_ROUNDFLASK_RED);
                        break;
                }
                break;
            case ROUNDFLASK_BROKEN:
                textureRegion = activity.getTexture(ResMan.ITEM_ROUNDFLASK_BROKEN);
                break;
            case MICROTUBE:
                switch (CalcUtils.randomOf(2)) {
                    case 0:
                        textureRegion = activity.getTexture(ResMan.ITEM_MICROTUBE_GREEN);
                        break;
                    case 1:
                        textureRegion = activity.getTexture(ResMan.ITEM_MICROTUBE_RED);
                        break;
                }
                break;
        }
        createItem(posX, posY, textureRegion, type);
    }

    private void createItem(float posX, float posY, ITiledTextureRegion textureRegion, Item.Type type) {
        //TODO: Mark for addition?
        Item item = new Item(type, textureRegion, posX, posY, this);
        items.add(item);
        final Scene gameScene = activity.getScene();
        final IEntity layerBG = gameScene.getChildByIndex(PortraitGameActivity.LAYER_BACKGROUND);
        final TouchableAnimatedSprite sprite = item.getSprite();
        sprite.setVisible(true);
        layerBG.attachChild(sprite);
        layerBG.attachChild(item.getShape());
        gameScene.registerTouchArea(item.getShape());
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    private void deleteItem(final Item item) {
        final AnimatedSprite sprite = item.getSprite();
        final TouchableAnimatedSprite biggerSprite = item.getShape();
        final Scene scene = activity.getScene();
        final IEntity layerBG = scene.getChildByIndex(PortraitGameActivity.LAYER_BACKGROUND);

        sprite.setVisible(false);
        scene.unregisterTouchArea(biggerSprite);
        layerBG.detachChild(biggerSprite);
        layerBG.detachChild(sprite);
        biggerSprite.stopDragging();
        activity.markForDeletion(item);
    }

    private void createBin(Bin.Type type, ITiledTextureRegion textureRegion, float posX, float posY) {
        Bin bin = new Bin(type, posX, posY, textureRegion, activity.getVBOM(), activity.getPhysicsWorld());
        bins.add(bin);
        binMap.put(type, bin);
        activity.getScene().getChildByIndex(PortraitGameActivity.LAYER_FOREGROUND).attachChild(bin.getSprite());
    }

    private void createBins() {
        final float binY = 1.0f;
        final ITiledTextureRegion bin1TextureRegion = activity.getTexture(ResMan.BIN_SHARPS);
        final ITiledTextureRegion bin2TextureRegion = activity.getTexture(ResMan.BIN_CHEMICAL);
        final ITiledTextureRegion bin3TextureRegion = activity.getTexture(ResMan.BIN_REGULAR);
        final ITiledTextureRegion bin4TextureRegion = activity.getTexture(ResMan.BIN_BIO);

        Vector2 bin1Pos = activity.spritePosition(bin1TextureRegion, 0.20f, binY, Bin.SCALE_DEFAULT);
        Vector2 bin2Pos = activity.spritePosition(bin2TextureRegion, 0.46f, binY, Bin.SCALE_DEFAULT);
        Vector2 bin3Pos = activity.spritePosition(bin3TextureRegion, 0.72f, binY, Bin.SCALE_DEFAULT);
        Vector2 bin4Pos = activity.spritePosition(bin4TextureRegion, 0.98f, binY, Bin.SCALE_DEFAULT);

        createBin(Bin.Type.GLASS, bin1TextureRegion, bin1Pos.x, bin1Pos.y);
        createBin(Bin.Type.LIQUIDS, bin2TextureRegion, bin2Pos.x, bin2Pos.y);
        createBin(Bin.Type.NORMAL, bin3TextureRegion, bin3Pos.x, bin3Pos.y);
        createBin(Bin.Type.BIO, bin4TextureRegion, bin4Pos.x, bin4Pos.y);
    }

    private void animateBins(Bin.Animation animation) {
        for (Bin bin : bins) {
            animate(bin, animation);
        }
    }

    private void recycleItem(final Item item) {
        deleteItem(item);
        deadItems.add(item.getId());
        activity.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                items.remove(item);
                if (isPlaying()) {
                    createItem(Item.Type.random());
                }
            }
        });
    }

}
