package ru.prodcontest.countries;

import java.util.Arrays;

public record Country(String name, String alpha2, String alpha3, String region) {
    private static final String[] validRegions = {"Europe", "Africa", "Americas", "Oceania", "Asia"};

    public static boolean validateRegion(String region) {
        return Arrays.asList(validRegions).contains(region);
    }
}
