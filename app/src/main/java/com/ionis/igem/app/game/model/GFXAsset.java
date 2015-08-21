package com.ionis.igem.app.game.model;

/**
 * Created by PLN on 21/08/2015.
 */
public class GFXAsset extends Asset {
    int width;
    int height;
    int textureX;
    int textureY;

    boolean tiled;
    int tileColumns;
    int tileRows;

    public GFXAsset(String filename, int width, int height, int textureX, int textureY, int tileColumns, int tileRows) {
        super(filename);
        this.width = width;
        this.height = height;
        this.textureX = textureX;
        this.textureY = textureY;
        this.tileColumns = tileColumns;
        this.tiled = true;
        this.tileRows = tileRows;
    }

    public GFXAsset(String filename, int width, int height, int textureX, int textureY) {
        this(filename, width, height, textureX, textureY, 0, 0);
        this.tiled = false;
    }

    public int getHeight() {
        return height;
    }

    public int getTextureX() {
        return textureX;
    }

    public int getTextureY() {
        return textureY;
    }

    public int getTileColumns() {
        return tileColumns;
    }

    public boolean isTiled() {
        return tiled;
    }

    public int getTileRows() {
        return tileRows;
    }

    public int getWidth() {
        return width;
    }
}
