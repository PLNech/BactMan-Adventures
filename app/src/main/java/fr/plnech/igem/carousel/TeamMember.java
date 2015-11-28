/*
=======================================================================
BactMan Adventures | Scientific popularisation through mini-games
Copyright (C) 2015 IONIS iGEM Team
Distributed under the GNU GPLv3 License.
(See file LICENSE.txt or copy at https://www.gnu.org/licenses/gpl.txt)
=======================================================================
*/

package fr.plnech.igem.carousel;

import android.support.annotation.Nullable;

public class TeamMember {
    private final String firstName;
    private final String lastName;
    private final String avatarResName;
    @Nullable
    private final String quote;
    @Nullable
    private final String urlLinkedin;
    @Nullable
    private final String urlTwitter;

    private boolean isTransparent;

    public TeamMember(String name, String avatarResName) {
        this(name, null, avatarResName, null, null, null);
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
        return firstName;
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
                '}';
    }
}
