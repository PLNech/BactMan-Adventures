package com.ionis.igem.app.game.model;

import android.util.Log;
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

    private final PhysicalWorldObject object; //TODO: Handle objects without bodies
    private boolean isGrabbed;

    public DraggableAnimatedSprite(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager, WorldObject pObject) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        object = (PhysicalWorldObject) pObject;
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if (object == null) {
            return false;
        }

        switch (pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                setScale(WorldObject.getIdealScale(object.getScaleGrabbed(), getTiledTextureRegion()));
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
                    Log.v(TAG, "onAreaTouched - velocity set to " + velocityX + ", " + velocityY);
                }
                break;
            case TouchEvent.ACTION_UP:
                if (isGrabbed) {
                    isGrabbed = false;
                    setScale(WorldObject.getIdealScale(object.getScaleDefault(), getTiledTextureRegion()));
                }
                break;
        }
        return true;
    }

}
