package fr.plnech.igem.carousel;

import android.support.annotation.Nullable;

/**
 * Created by Paul-Louis Nech on 15/09/2015.
 */
public class TeamMember {
    String name;
    String avatarResName;
    String quote;

    public TeamMember(String name, String avatarResName, @Nullable String quote) {
        this.name = name;
        this.avatarResName = avatarResName;
        this.quote = quote;

    }

    public String getName() {
        return name;
    }

    public String getAvatarResName() {
        return avatarResName;
    }

    public String getQuote() {
        return quote != null ? quote : name;
    }
}
