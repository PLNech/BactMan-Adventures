package com.ionis.igem.app.game.model;

import android.opengl.GLES20;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by PLN on 21/08/2015.
 */
public class HUDElement {
    Text text;

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
}
