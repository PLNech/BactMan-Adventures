package com.ionis.igem.app.game.model;

import android.support.annotation.Nullable;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.ionis.igem.app.game.AbstractGameActivity;
import com.ionis.igem.app.game.PortraitGameActivity;
import com.ionis.igem.app.game.model.res.FontAsset;
import com.ionis.igem.app.game.model.res.GFXAsset;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.*;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.IModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by PLN on 21/08/2015.
 */
public abstract class BaseGame {
    protected ArrayList<GFXAsset> graphicalAssets = new ArrayList<>();
    protected ArrayList<FontAsset> fontAssets = new ArrayList<>();
    protected ArrayList<HUDElement> elements = new ArrayList<>();

    protected boolean playing = false;

    protected AbstractGameActivity activity;
    protected Random random;
    private int position;

    public BaseGame(AbstractGameActivity pActivity) {
        activity = pActivity;
        random = new Random();
    }

    public abstract List<GFXAsset> getGraphicalAssets();

    public abstract List<FontAsset> getFontAssets();

    public abstract Scene prepareScene();

    public ContactListener getContactListener() {
        return null;
    }

    public IOnSceneTouchListener getOnSceneTouchListener() {
        return null;
    }

    public Vector2 getPhysicsVector() {
        return null;
    }

    public abstract List<HUDElement> getHudElements();

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

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean isPlaying) {
        this.playing = isPlaying;
    }

    public abstract void resetGame();

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public AbstractGameActivity getActivity() {
        return activity;
    }

    public boolean isPortrait() {
        return true;
    }

    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        return false;
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
    }

    protected void animate(final WorldObject object,
                           SequenceEntityModifier modifier, @Nullable IEntityModifier.IEntityModifierListener listener) {
        if (listener == null) {
            object.registerEntityModifier(new LoopEntityModifier(modifier, 1));
        } else {
            object.registerEntityModifier(new LoopEntityModifier(modifier, 1, listener));
        }
    }

}
