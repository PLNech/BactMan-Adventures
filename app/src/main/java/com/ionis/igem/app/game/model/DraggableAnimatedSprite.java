package com.ionis.igem.app.game.model;

import com.badlogic.gdx.math.Vector2;
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
    private Vector2 savedVelocity;
    private float velocityX;
    private float velocityY;

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
                    velocityX = pSceneTouchEvent.getX() - x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
                    velocityY = pSceneTouchEvent.getY() - y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
                    body.setLinearVelocity(0, 0);
                    body.setTransform(pSceneTouchEvent.getX() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                            pSceneTouchEvent.getY() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, body.getAngle());
                    object.onDrag();
                }
                break;
            case TouchEvent.ACTION_UP:
                if (isGrabbed) {
                    isGrabbed = false;
                    setScale(initScale);
                    object.getBody().setLinearVelocity(velocityX, velocityY);
                }
                break;
        }
        return true;
    }

    public void stopDragging() {
        object = null;
    }

    public void setInitialScale(float scale, boolean doubleWidth) {
        initScale = scale;
        final float xScale = doubleWidth ? initScale * 2 : initScale;
        setScale(xScale, initScale);
    }

    public void setInitialScale(Float scale) {
        setInitialScale(scale, false);
    }
}
