package fr.plnech.igem.game.model.res;

/**
 * Created by PLN on 21/08/2015.
 */
public class GFXAsset extends Asset {
    int width;
    int height;
    int textureX;
    int textureY;

    int tileColumns;
    int tileRows;

    public GFXAsset(String filename, int width, int height, int textureX, int textureY, int tileColumns, int tileRows) {
        super(filename);
        this.width = width;
        this.height = height;
        this.textureX = textureX;
        this.textureY = textureY;
        this.tileColumns = tileColumns;
        this.tileRows = tileRows;
    }

    public GFXAsset(String filename, int width, int height, int textureX, int textureY) {
        this(filename, width, height, textureX, textureY, 1, 1);
    }

    public GFXAsset(String filename, int width, int height) {
        this(filename, width, height, 0, 0, 1, 1);
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
        return tileColumns != 1 || tileRows != 1;
    }

    public int getTileRows() {
        return tileRows;
    }

    public int getWidth() {
        return width;
    }
}
