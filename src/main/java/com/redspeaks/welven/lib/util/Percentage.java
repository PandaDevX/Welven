package com.redspeaks.welven.lib.util;

public class Percentage {

    public static int of(int baseValue, int percentage) {
        double value = (double) baseValue * (((double)percentage) / 100);
        return (int)Math.round(value);
    }

    public static int get(int firstValue, int secondValue) {
        double value = (((double) firstValue) / ((double) secondValue)) * 100;
        return (int)Math.round(value);
    }
}
