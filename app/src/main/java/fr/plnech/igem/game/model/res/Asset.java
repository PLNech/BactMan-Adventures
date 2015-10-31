package fr.plnech.igem.game.model.res;

/**
 * Created by PLNech on 21/08/2015.
 */
public class Asset {
    final String filename;

    Asset(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
