package com.ionis.igem.app.game.model;

import android.opengl.GLES20;
import android.util.Log;
import com.badlogic.gdx.math.Vector2;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by PLN on 21w/08/2015.
 */
public class HUDElement {
    public static final float SCALE_DEFAULT = 0.125f;
    Text text;
    Sprite sprite;

    public HUDElement() {
    }

    public Text getText() {
        return text;
    }

    public HUDElement buildText(CharSequence textDefault, int textMaxLength, float textX, float textY, IFont font, VertexBufferObjectManager manager) {
        text = new Text(textX, textY, font, textDefault, textMaxLength, manager);
        text.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        text.setAlpha(0.5f);
        return this;
    }

    public HUDElement buildText(CharSequence textDefault, int textMaxLength, Vector2 pos, IFont font, VertexBufferObjectManager manager) {
        return buildText(textDefault, textMaxLength, pos.x, pos.y, font, manager);
    }

    public HUDElement buildSprite(float pX, float pY, ITextureRegion textureRegion, VertexBufferObjectManager vbom,
                                  float scale) {
        sprite = new Sprite(pX, pY, textureRegion, vbom);
        sprite.setScale(scale);
        sprite.setScaleCenter(pX, pY);
        return this;
    }

    public HUDElement buildSprite(Vector2 pos, ITextureRegion textureRegion, VertexBufferObjectManager vbom, float scale) {
        return buildSprite(pos.x, pos.y, textureRegion, vbom, scale);
    }

    public HUDElement buildSprite(Vector2 pos, ITextureRegion textureRegion, VertexBufferObjectManager vbom) {
        return buildSprite(pos.x, pos.y, textureRegion, vbom, SCALE_DEFAULT);
    }

    public HUDElement buildSprite(float pX, float pY, ITextureRegion textureRegion, VertexBufferObjectManager vbom) {
        return buildSprite(pX, pY, textureRegion, vbom, SCALE_DEFAULT);
    }

    public Sprite getSprite() {
        return sprite;
    }
}
