package fr.plnech.igem.carousel;

import android.support.annotation.Nullable;

/**
 * Created by Paul-Louis Nech on 15/09/2015.
 */
public class TeamMember {
    String name;
    String avatarResName;
    @Nullable String quote;
    @Nullable String urlLinkedin;
    @Nullable String urlTwitter;

    private boolean isTransparent;
    private boolean noName;

    public TeamMember(String name, String avatarResName) {
        this(name, avatarResName, null, null, null);
    }

    public TeamMember(String name, String avatarResName, @Nullable String quote) {
        this(name, avatarResName, quote, null, null);
    }

    public TeamMember(String name, String avatarResName,
                      @Nullable String quote, @Nullable String linkedin) {
        this(name, avatarResName, quote, linkedin, null);
    }

    public TeamMember(String name, String avatarResName,
                      @Nullable String quote, @Nullable String linkedin, @Nullable String twitter) {
        this.name = name;
        this.avatarResName = avatarResName;
        this.quote = quote;
        this.urlLinkedin = linkedin;
        this.urlTwitter = twitter;
    }

    public String getName() {
        return noName ? null : name;
    }

    public String getAvatarResName() {
        return avatarResName;
    }

    @Nullable
    public String getQuote() {
        return quote;
    }

    @Nullable
    public String getUrlLinkedin() {
        return urlLinkedin;
    }

    @Nullable
    public String getUrlTwitter() {
        return urlTwitter;
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
}
