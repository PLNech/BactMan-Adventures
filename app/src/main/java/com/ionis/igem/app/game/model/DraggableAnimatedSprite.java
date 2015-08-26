package com.ionis.igem.app.game.model;

import com.badlogic.gdx.physics.box2d.Body;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by PLNech on 25/08/2015.
 */
public class DraggableAnimatedSprite extends AnimatedSprite {

    private static final String TAG = "DraggableAnimatedSprite";

    private PhysicalWorldObject object; //TODO: Handle objects without bodies
    private boolean isGrabbed;
    private float scaleGrabbed;
    private float initScale;

    public DraggableAnimatedSprite(float pX, float pY, float pScaleGrabbed, ITiledTextureRegion pTiledTextureRegion,
                                   VertexBufferObjectManager pVertexBufferObjectManager, WorldObject pObject) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        object = (PhysicalWorldObject) pObject;
        scaleGrabbed = pScaleGrabbed;
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if (object == null) {
            return false;
        }

        switch (pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                setScale(scaleGrabbed);
                isGrabbed = true;
                break;
            case TouchEvent.ACTION_MOVE:
                if (isGrabbed) {
                    final Body body = object.getBody();
                    final float x = body.getPosition().x;
                    final float y = body.getPosition().y;
                    float velocityX = pSceneTouchEvent.getX() - x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
                    float velocityY = pSceneTouchEvent.getY() - y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
                    body.setLinearVelocity(velocityX, velocityY);
                }
                break;
            case TouchEvent.ACTION_UP:
                if (isGrabbed) {
                    isGrabbed = false;
                    setScale(initScale);
                }
                break;
        }
        return true;
    }

    public void stopDragging() {
        object = null;
    }

    public void setInitialScale(float scale) {
        initScale = scale;
        setScale(initScale);
        setScaleX(initScale * 2);
    }
}
