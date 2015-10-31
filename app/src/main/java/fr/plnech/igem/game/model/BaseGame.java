package fr.plnech.igem.game.model;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LevelStartEvent;
import fr.plnech.igem.game.*;
import fr.plnech.igem.game.managers.ResMan;
import fr.plnech.igem.game.model.res.FontAsset;
import fr.plnech.igem.game.model.res.GFXAsset;
import fr.plnech.igem.game.model.res.SoundAsset;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.*;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.IModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by PLNech on 21/08/2015.
 */
public abstract class BaseGame {
    public static final String KEY_GAME_ID = "game_id";
    public static final String KEY_WARN = "warn";
    public static final String KEY_GAME_UNLOCKED = "unlocked_";

    private static final String TAG = "BaseGame";
    public static final int ID_NONE = 0;
    public static final int ID_GUT = 1;
    public static final int ID_BIN = 2;
    public static final int ID_PICTO = 3;

    public static final int ID_PIANO = 4;
    public static final int INIT_SCORE = 0;
    public static final int INIT_LIVES = 3;
    public static final int INIT_TIME = 60;

    protected final ArrayList<GFXAsset> graphicalAssets = new ArrayList<>();
    protected final ArrayList<GFXAsset> profAssets = new ArrayList<>();
    protected final ArrayList<FontAsset> fontAssets = new ArrayList<>();
    protected final ArrayList<SoundAsset> soundAssets = new ArrayList<>();
    protected final ArrayList<HUDElement> elements = new ArrayList<>();

    protected boolean playing = false;

    protected final AbstractGameActivity activity;
    protected final Random random;
    protected int gameScore = INIT_SCORE;
    private int nextGameId;
    private int id;

    public BaseGame(AbstractGameActivity pActivity) {
        activity = pActivity;
        random = new Random();
    }

    public abstract List<GFXAsset> getGraphicalAssets();

    public abstract List<GFXAsset> getProfAssets();

    public abstract List<FontAsset> getFontAssets();

    public List<SoundAsset> getSoundAssets() {
        if (soundAssets.isEmpty()) {
            soundAssets.add(new SoundAsset(ResMan.SOUND_SUCCESS, 0.1f));
            soundAssets.add(new SoundAsset(ResMan.SOUND_FAILURE));
        }
        return soundAssets;
    }

    public abstract Scene prepareScene();

    public abstract List<HUDElement> getHudElements();

    public abstract void resetGame();

    public ContactListener getContactListener() {
        return null;
    }

    public IOnSceneTouchListener getOnSceneTouchListener() {
        return null;
    }

    public Vector2 getPhysicsVector() {
        return null;
    }

    protected void createCameraWalls() {
        createCameraWalls(true, true, true, true, false);
    }

    protected void createCameraWalls(boolean up, boolean right, boolean down, boolean left, boolean masked) {
        final Camera camera = activity.getCamera();
        final float camWidth = camera.getWidth();
        final float camHeight = camera.getHeight();
        final float wallDepth = 10;

        final float centerX = camWidth / 2;
        final float centerY = camHeight / 2;

        if (down) createWall(centerX, camHeight + wallDepth / 2, camWidth, wallDepth, Wall.Type.BOTTOM, masked);
        if (up) createWall(centerX, -wallDepth / 2, camWidth, wallDepth, Wall.Type.TOP, masked);
        if (left) createWall(-wallDepth / 2, centerY, wallDepth, camHeight, Wall.Type.LEFT, masked);
        if (right) createWall(camWidth + wallDepth / 2, centerY, wallDepth, camHeight, Wall.Type.RIGHT, masked);
    }

    private void createWall(float x, float y, float width, float height, Wall.Type type, boolean masked) {
        Wall wall = new Wall(x, y, width, height, type, activity.getVBOM(), activity.getPhysicsWorld(), masked);
        activity.getScene().getChildByIndex(PortraitGameActivity.LAYER_BACKGROUND).attachChild(wall);
    }

    protected void animate(final WorldObject object, WorldObject.Animation animation) {
        final Color toColor;

        switch (animation) {
            case VALID_HIT:
                toColor = Color.GREEN;
                break;
            case INVALID_HIT:
                toColor = Color.RED;
                break;
            case VALID_MISS:
                toColor = Color.GREEN;
                break;
            default:
                throw new IllegalStateException("THERE IS NO DEFAULT");
        }

        animate(object, Color.WHITE, toColor);
    }

    protected void animate(final WorldObject object, Color fromColor, Color toColor) {
        final IEntityModifier.IEntityModifierListener logListener = new IEntityModifier.IEntityModifierListener() {
            @Override
            public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) {
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        final String text = "Animation started.";
//                        Log.v(TAG, "onModifierStarted - " + text);
//                    }
//                });
            }

            @Override
            public void onModifierFinished(final IModifier<IEntity> pEntityModifier, final IEntity pEntity) {
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        final String text = "Animation finished.";
//                        Log.v(TAG, "onModifierFinished - " + text);
//                    }
//                });
            }
        };

        final float pDuration = 0.25f;

        final SequenceEntityModifier entityModifier = new SequenceEntityModifier(
                new ColorModifier(pDuration, fromColor, toColor),
                new ColorModifier(pDuration, toColor, fromColor),
                new DelayModifier(pDuration * 2)
        );

        animate(object, entityModifier, logListener);
    }

    protected void animate(final WorldObject object,
                           SequenceEntityModifier modifier, @Nullable IEntityModifier.IEntityModifierListener listener) {
        if (listener == null) {
            object.registerEntityModifier(new LoopEntityModifier(modifier, 1));
        } else {
            object.registerEntityModifier(new LoopEntityModifier(modifier, 1, listener));
        }
    }

    public void logLevelStart() {
        Log.d(TAG, "logLevelStart");
        Answers.getInstance().logLevelStart(new LevelStartEvent()
                .putLevelName(this.getClass().getSimpleName()));
    }

    public void logLevelEnd(int score, boolean success) {
        Log.d(TAG, "logLevelEnd - score:" + score + ", " + (success ? "win" : "lose"));
        Answers.getInstance().logCustom(new GameEndEvent(this, score, success));
    }

    public static boolean getUnlockedStatus(int gameId, SharedPreferences preferences) {
        return preferences.getBoolean(gameId + AbstractGameActivity.SUFFIX_UNLOCKED, false);
    }

    public static void startGame(Activity activity, int gameId) {
        final Intent intent = new Intent(activity, isPortrait(gameId) ?
                PortraitGameActivity.class : LandscapeGameActivity.class);
        intent.putExtra(KEY_GAME_ID, gameId);
        intent.putExtra(KEY_WARN, true);
        activity.startActivity(intent);
    }

    public static BaseGame getGameFromTag(int gameId, AbstractGameActivity activity) {
        final Class<? extends BaseGame> gameClass;
        final int nextGameId;
        switch (gameId) {
            case ID_GUT:
                gameClass = GutGame.class;
                nextGameId = ID_BIN;
                break;
            case ID_BIN:
                gameClass = BinGame.class;
                nextGameId = ID_PICTO;
                break;
            case ID_PICTO:
                gameClass = PictoGame.class;
                nextGameId = ID_PIANO;
                break;
            case ID_PIANO:
                gameClass = PianoGame.class;
                nextGameId = ID_NONE;
                break;
            default:
                throw new IllegalStateException("No default!");
        }
        try {
            BaseGame game = gameClass.getConstructor(AbstractGameActivity.class).newInstance(activity);
            game.setNextGameId(nextGameId);
            game.setId(gameId);
            return game;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("No default!");
        }
    }

    protected void playSoundFailure() {
        activity.getSound(ResMan.SOUND_FAILURE).play();
    }

    protected void playSoundSuccess() {
        activity.getSound(ResMan.SOUND_SUCCESS).play();
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean isPlaying) {
        this.playing = isPlaying;
    }

    public AbstractGameActivity getActivity() {
        return activity;
    }

    public abstract boolean isPortrait();

    public int getScore() {
        return gameScore;
    }

    public void setNextGameId(int nextGameId) {
        this.nextGameId = nextGameId;
    }

    public int getNextGameId() {
        return nextGameId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static boolean isPortrait(int gameId) {
        switch (gameId) {
            case ID_GUT:
            case ID_PIANO:
                return false;
            case ID_BIN:
            case ID_PICTO:
            default:
                return true;
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public abstract int getWidth();

    public abstract int getHeight();

    public static int getLastUnlockedId(SharedPreferences preferences) {
        return preferences.getInt(KEY_GAME_ID, 1);
    }
}
