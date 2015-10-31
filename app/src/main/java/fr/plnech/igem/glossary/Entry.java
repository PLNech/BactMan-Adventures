package fr.plnech.igem.glossary;

/**
 * Created by PLNech on 14/09/2015.
 */
public class Entry {

    private final String name;
    private final String description;

    public Entry(String pName, String pDescription) {
        name = pName;
        description = pDescription;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name;
    }
}
