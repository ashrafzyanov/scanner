package com.ashrafzyanov.scanner.catalog.file;

public class ParseNameFile {

    public static String getNameFile(String fullName) {
        String name = fullName;
        int position = name.lastIndexOf('.');
        if ((position <= 0) || (position == name.length() - 1)) {
            return name; //просто возвращаем имя
        }

        return  name.substring(0, position);
    }

    public static String TypeFile(String fullName) {
        String name = fullName;
        int  position = position = name.lastIndexOf('.');
        if((position <= 0) || (position == name.length() - 1)) {
            return null;
        }

        return name.substring(position + 1, name.length());
    }

    public static boolean TypeArchiv(String TypeFile) {
        if ((TypeFile.equalsIgnoreCase("rar")) || ((TypeFile.equalsIgnoreCase("zip")))) {
            return true;
        }
        return false;
    }

}
