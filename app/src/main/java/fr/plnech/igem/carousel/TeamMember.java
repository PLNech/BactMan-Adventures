/**
 * Created by PLNech on 15/09/2015.
 */
package fr.plnech.igem.carousel;

import android.support.annotation.Nullable;

public class TeamMember {
    //TODO: Create own class for sponsors
    String firstName;
    String lastName;
    String avatarResName;
    @Nullable
    String quote;
    @Nullable
    String urlLinkedin;
    @Nullable
    String urlTwitter;

    private boolean isTransparent;
    private boolean noName;

    public TeamMember(String name, String avatarResName) {
        this(name, null, avatarResName, null, null, null);
    }

    public TeamMember(String firstName, String lastName, String avatarResName) {
        this(firstName, lastName, avatarResName, null, null, null);
    }

    public TeamMember(String firstName, String lastName, String avatarResName, @Nullable String quote) {
        this(firstName, lastName, avatarResName, quote, null, null);
    }

    public TeamMember(String firstName, String lastName, String avatarResName,
                      @Nullable String quote, @Nullable String linkedIn) {
        this(firstName, lastName, avatarResName, quote, linkedIn, null);
    }

    public TeamMember(String firstName, String lastName, String avatarResName,
                      @Nullable String quote, @Nullable String linkedin, @Nullable String twitter) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarResName = avatarResName;
        this.quote = quote;
        this.urlLinkedin = linkedin;
        this.urlTwitter = twitter;
    }

    public String getName() {
        return noName ? null : firstName;
    }

    public String getLastName() {
        return lastName;
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

    @Override
    public String toString() {
        return "TeamMember{" +
                "avatarResName='" + avatarResName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", quote='" + quote + '\'' +
                ", urlLinkedin='" + urlLinkedin + '\'' +
                ", urlTwitter='" + urlTwitter + '\'' +
                ", isTransparent=" + isTransparent +
                ", noName=" + noName +
                '}';
    }
}
