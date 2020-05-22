package com.example.pictureit.Utils;

public class StringManipulation {

    public static String expandUsername(String username) {
        return username.replace(".", " ");
    }

    public static String condenseUsername(String username) {
        return username.replace(" ", ".");
    }

    public static String timeStampConverter(String timeStamp) {
        int index;
        index = timeStamp.indexOf("T");
        return timeStamp.substring(0, index);
    }

    public static Integer photoPosition(String photoName) {
        return Integer.parseInt(photoName.substring(photoName.length() - 1));
    }
}
