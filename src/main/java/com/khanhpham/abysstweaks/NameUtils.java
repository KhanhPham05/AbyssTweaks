package com.khanhpham.abysstweaks;

import java.util.List;

public class NameUtils {

    private NameUtils() {}

    public static final String ARCANE_STATION = "arcaneStation";
    public static final String SOMNIUM_INFUSER = "somniumInfusing";

    public static String toArrayString(List<String> strings, boolean ignoreLast) {
        return toArrayString(strings.toArray(String[]::new), ignoreLast);
    }

    public static String toArrayString(String[] ingredientString, boolean ignoreLast) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(ingredientString[0]);
        for (int i = 1; i < (ignoreLast ? ingredientString.length - 1 : ingredientString.length); i++) {
            builder.append(", ").append(ingredientString[i]);
        }

        return builder.append("]").toString();
    }
}
