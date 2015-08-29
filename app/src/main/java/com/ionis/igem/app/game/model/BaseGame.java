package com.ionis.igem.app.game.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.ionis.igem.app.game.model.res.FontAsset;
import com.ionis.igem.app.game.model.res.GFXAsset;
import com.ionis.igem.app.ui.GameActivity;
import org.andengine.entity.scene.Scene;

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

    protected GameActivity activity;
    protected Random random;
    private int position;

    public BaseGame(GameActivity pActivity) {
        activity = pActivity;
        random = new Random();
    }

    public abstract List<GFXAsset> getGraphicalAssets();

    public abstract List<FontAsset> getFontAssets();

    public abstract Scene prepareScene();

    public ContactListener getContactListener() {
        return null;
    }

    public Vector2 getPhysicsVector() {
        return null;
    }

    public abstract List<HUDElement> getHudElements();

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
}
