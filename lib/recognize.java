/*
 * @copyright Copyright 2013 Andrew Brown. All rights reserved.
 * @license GNU/GPL.
 */
package lib;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

/**
 *
 * @author andrew
 */
public class recognize {

    /**
     * Attempt to find the correct file in the current directory based on a user
     * string which may be shortened or have errors.
     *
     * @param query
     * @return
     */
    public static File find(String query) {
        String current_directory = System.getProperty("user.dir");
        return recognize.find(query, current_directory);
    }

    /**
     * Attempt to find the correct file based on a user string which may be
     * shortened or have errors.
     *
     * @param query
     * @param path
     * @return
     */
    public static File find(String query, String path) {
        // walk files implementation from http://stackoverflow.com/questions/2056221/recursively-list-files-in-java
        File root = new File(path);
        File[] list = root.listFiles();
        if (list != null) {
            for (File f : list) {
                // test
                if (recognize.matches(f, query, false)) {
                    return f;
                }
                // continue searching
                if (f.isDirectory()) {
                    File result = recognize.find(query, f.getAbsolutePath());
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Guess is like find but with no user interaction
     *
     * @param query
     * @return
     */
    public static File guess(String query) {
        String current_directory = System.getProperty("user.dir");
        return recognize.guess(query, current_directory);
    }

    public static File guess_new(final String query, String path) {
        final File found = null;
        try {
            Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (recognize.matches(file.toFile(), query, true)) {
                        //found = file.toFile();
                        return FileVisitResult.TERMINATE;
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
        }
        return found;
    }

    /**
     * Guess is like find but with no user interaction
     *
     * @param query
     * @param path
     * @return
     */
    public static File guess(String query, String path) {
        // walk files implementation from http://stackoverflow.com/questions/2056221/recursively-list-files-in-java
        File root = new File(path);
        File[] list = root.listFiles();
        //System.out.println(Arrays.toString(list));
        if (list != null) {
            for (File f : list) {
                // test
                if (recognize.matches(f, query, true)) {
                    return f;
                }
                // continue searching
                if (f.isDirectory()) {
                    File result = recognize.find(query, f.getAbsolutePath());
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Test whether a file matches against the user query
     *
     * @param file
     * @param query
     * @return
     */
    public static boolean matches(File file, String query, boolean doNotQueryUser) {
        String path = file.getName().toLowerCase();
        query = query.toLowerCase();
        //System.out.println(query + " " + path);
        // starts with same text
        if (path.startsWith(query)) {
            //System.out.println("Starts with...");
            return true;
        }
        // contains significant similar text
        if (query.length() >= 4 && path.contains(query)) {
            if (doNotQueryUser || lib.Console.confirm("Is " + path + " the file you were looking for?")) {
                //System.out.println("Contains...");
                return true;
            }
        }
        // @TODO this causes a bunch of mistakes when looking at files
        // only a bit off
//        if (query.length() > 4 && levenshtein(path.substring(0, query.length()), query) <= 2) {
//            if (doNotQueryUser || lib.Console.confirm("Is " + path + " the file you were looking for?")) {
//                //System.out.println("Close enough...");
//                return true;
//            }
//        }
        // else return
        return false;
    }

    /**
     * From
     * http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    private static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    /**
     * Calculate levenshtein distance to determine if a spelling error exists.
     * From
     * http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
     *
     * @param str1
     * @param str2
     * @return
     */
    public static int levenshtein(CharSequence str1, CharSequence str2) {
        int[][] distance = new int[str1.length() + 1][str2.length() + 1];
        for (int i = 0; i <= str1.length(); i++) {
            distance[i][0] = i;
        }
        for (int j = 1; j <= str2.length(); j++) {
            distance[0][j] = j;
        }
        for (int i = 1; i <= str1.length(); i++) {
            for (int j = 1; j <= str2.length(); j++) {
                distance[i][j] = minimum(
                        distance[i - 1][j] + 1,
                        distance[i][j - 1] + 1,
                        distance[i - 1][j - 1]
                        + ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0
                        : 1));
            }
        }
        return distance[str1.length()][str2.length()];
    }
}
