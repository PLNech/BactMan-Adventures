package com.ionis.igem.app.game.model;

/**
 * Created by PLN on 21/08/2015.
 */
public class FontAsset extends Asset {

    int size;
    int color;
    boolean antialised;

    public FontAsset(String filename, int size, int color, boolean antialised) {
        super(filename);
        this.size = size;
        this.color = color;
        this.antialised = antialised;
    }

    public static String name(String filename, int size, int color, boolean antialised) {
        return "FontAsset{" +
                "antialised=" + antialised +
                ", filename=" + filename +
                ", size=" + size +
                ", color=" + color +
                '}';
    }

    @Override
    public String toString() {
        return "FontAsset{" +
                "antialised=" + antialised +
                ", filename=" + filename +
                ", size=" + size +
                ", color=" + color +
                '}';
    }

    public int getSize() {
        return size;
    }

    public boolean isAntialised() {
        return antialised;
    }

    public int getColor() {
        return color;
    }
}
