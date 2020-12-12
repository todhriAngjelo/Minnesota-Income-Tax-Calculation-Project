package com.minesota.tax.calculator.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * A collection of useful file utilities that are commonly used or TO BE used multiple times
 */
public class FileUtils {

    private FileUtils() {
        throw new IllegalStateException("Utility class. This class can not be initialized.");
    }

    public static Scanner getScanner(String afmInfoFileFolderPath, String afmInfoFile) {
        try {
            return new Scanner(new FileInputStream(afmInfoFileFolderPath + "\\" + afmInfoFile));
        } catch (FileNotFoundException e) {
            System.out.println("Problem opening " + afmInfoFile + " file.");
            System.exit(0);
        }
        return null;
    }
}
