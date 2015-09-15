package fr.plnech.igem.carousel;

/**
 * Created by Paul-Louis Nech on 15/09/2015.
 */
public class TeamMember {
    String name;
    String avatarResName;
    String quote;

    public TeamMember(String name, String quote, String avatarResName) {
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
        return quote;
    }
}
