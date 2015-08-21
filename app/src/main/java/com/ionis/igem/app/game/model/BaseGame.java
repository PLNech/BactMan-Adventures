package com.ionis.igem.app.game.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactListener;
import org.andengine.entity.scene.Scene;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PLN on 21/08/2015.
 */
public abstract class BaseGame {
    protected ArrayList<Asset> assets = new ArrayList<>();

    public abstract List<Asset> getAssets();

    public abstract Scene prepareScene(Scene scene);

    public abstract ContactListener getContactListener();

    public abstract Vector2 getPhysicsVector();
//    public abstract List<HUD> getHudParts();

}
