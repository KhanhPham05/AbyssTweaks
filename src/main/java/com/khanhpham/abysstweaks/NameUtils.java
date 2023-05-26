package com.khanhpham.abysstweaks;

import java.util.List;

public class NameUtils {

    private NameUtils() {}

    public static final String ARCANE_STATION = "arcaneStation";

    public static String toArrayString(List<String> ingredientString, boolean ignoreLast) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(ingredientString.get(0));
        for (int i = 1; i < (ignoreLast ? ingredientString.size() - 1 : ingredientString.size()); i++) {
            builder.append(", ").append(ingredientString.get(i));
        }

        return builder.append("]").toString();
    }
}
