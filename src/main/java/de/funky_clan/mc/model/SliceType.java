package de.funky_clan.mc.model;

/**
* @author synopia
*/
public enum SliceType {
    X("Side view X"),    // y-z
    Y("Top down view"),
    Z("Side view Z");

    public String name;

    SliceType(String name) {
        this.name = name;
    }
}
