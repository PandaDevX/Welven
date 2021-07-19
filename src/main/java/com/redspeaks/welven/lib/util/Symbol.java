package com.redspeaks.welven.lib.util;

public enum Symbol {

    HEART("\u2764"), SHIELD("\uD83D\uDEE1"), STAR_16("✺"), STAR_12("✸"),
    SIMPLE_STAR("★"), SQUARE("⬛"), SWORDS("\u2694"), ARROW("➸"), CHECK("\u2713"), CROSS("x");
    private final String utf;
    Symbol(String utf) {
        this.utf = utf;
    }

    public String get() {
        return utf;
    }
}
