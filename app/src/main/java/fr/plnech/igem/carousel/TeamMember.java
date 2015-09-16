package fr.plnech.igem.carousel;

import android.support.annotation.Nullable;

/**
 * Created by Paul-Louis Nech on 15/09/2015.
 */
public class TeamMember {
    String name;
    String avatarResName;
    String quote;

    private boolean isTransparent;
    private boolean noName;

    public TeamMember(String name, String avatarResName) {
        this.name = name;
        this.avatarResName = avatarResName;
    }

    public TeamMember(String name, String avatarResName, @Nullable String quote) {
        this.name = name;
        this.avatarResName = avatarResName;
        this.quote = quote;

    }

    public String getName() {
        return noName ? null : name;
    }

    public String getAvatarResName() {
        return avatarResName;
    }

    public String getQuote() {
        return quote != null ? quote : name;
    }

    public boolean isTransparent() {
        return isTransparent;
    }

    public void setTransparent(boolean isTransparent) {
        this.isTransparent = isTransparent;
    }

    public void setNoName(boolean noName) {
        this.noName = noName;
    }

    public boolean isNoName() {
        return noName;
    }
}
