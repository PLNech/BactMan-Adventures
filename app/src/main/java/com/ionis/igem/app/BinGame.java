package com.ionis.igem.app;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.ionis.igem.app.game.managers.ResMan;
import com.ionis.igem.app.game.model.Asset;
import com.ionis.igem.app.game.model.BaseGame;
import org.andengine.entity.scene.Scene;

import java.util.List;

/**
 * Created by PLN on 21/08/2015.
 */
public class BinGame extends BaseGame {
    @Override
    public List<Asset> getAssets() {
        final Asset bin1Asset = new Asset(ResMan.BIN1, 696, 1024, 0, 0);
        final Asset bin2Asset = new Asset(ResMan.BIN2, 696, 1024, 0, 0);
        final Asset bin3Asset = new Asset(ResMan.BIN3, 696, 1024, 0, 0);
        final Asset bin4Asset = new Asset(ResMan.BIN4, 696, 1024, 0, 0);
        final Asset smileyAsset = new Asset(ResMan.FACE_BOX_TILED, 696, 1024, 0, 0, 2, 1);
        assets.add(bin1Asset);
        assets.add(bin2Asset);
        assets.add(bin3Asset);
        assets.add(bin4Asset);
        assets.add(smileyAsset);
        return assets;
    }

    @Override
    public Scene prepareScene(Scene scene) {

        return scene;
    }

    @Override
    public ContactListener getContactListener() {
        return null;
    }

    @Override
    public Vector2 getPhysicsVector() {
        return null;
    }
}
