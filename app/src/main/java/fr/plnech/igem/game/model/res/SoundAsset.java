package fr.plnech.igem.game.model.res;

/**
 * Created by PLNech on 24/09/2015.
 */
public class SoundAsset extends Asset {
    private float volume;

    public SoundAsset(String filename, float volume) {
        super(filename);
        this.volume = volume;
    }

    public SoundAsset(String filename) {
        this(filename, 1.0f);
    }

    public float getVolume() {
        return volume;
    }
}
