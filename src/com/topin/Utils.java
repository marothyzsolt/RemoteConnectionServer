package com.topin;

public class Utils {
    public static String randomGetNumericString(int n)
    {
        String AlphaNumericString = "0123456789";
        return generateRandom(n, AlphaNumericString);
    }

    public static String randomGetAlphaNumericString(int n)
    {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        return generateRandom(n, AlphaNumericString);
    }

    private static String generateRandom(int n, String AlphaNumericString) {
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }
}
