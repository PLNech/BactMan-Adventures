package com.ionis.igem.app.game.bins;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by PLN on 19/08/2015.
 */
public class Item extends AnimatedSprite {
    public static final int DURATION_EACH_ANIM = 100;
    public static final float SCALE_GRABBED = 1.5f;
    public static final float SCALE_NORMAL = 1.0f;

    Boolean isGrabbed = false;

    public Item(TiledTextureRegion texture, float posX, float posY, VertexBufferObjectManager manager) {
        super(posX, posY, texture, manager);
        animate(DURATION_EACH_ANIM);
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        switch (pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                setScale(SCALE_GRABBED);
                isGrabbed = true;
                break;
            case TouchEvent.ACTION_MOVE:
                if (isGrabbed) {
                    setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
                }
                break;
            case TouchEvent.ACTION_UP:
                if (isGrabbed) {
                    isGrabbed = false;
                    setScale(SCALE_NORMAL);
                }
                break;
        }
        return true;
    }
}
