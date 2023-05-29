package com.khanhpham.abysstweaks;

import com.google.common.base.Preconditions;

import java.util.List;

public class ModUtils {

    private ModUtils() {}

    public static final String ARCANE_STATION = "arcaneStation";
    public static final String SOMNIUM_INFUSER = "somniumInfusing";
    public static final String MAP = "mortarAndPestle";

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

    public static <A> void checkArray(A[] array, int bound) {
        Preconditions.checkArgument(array.length == bound, "Array must only have %s members, but %s found".formatted(bound, array.length));
    }
}
