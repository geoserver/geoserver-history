package org.geoserver.data.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for IO related utilities
 * 
 * @author Andrea Aime - TOPP
 * 
 */
public class IOUtils {
    private IOUtils() {

    }

    /**
     * Copies the provided input stream onto a file
     * 
     * @param from
     * @param to
     * @throws IOException
     */
    public static void copy(InputStream from, File to) throws IOException {
        OutputStream out = null;
        try {
            out = new FileOutputStream(to);

            byte[] buffer = new byte[1024 * 16];
            int bytes = 0;
            while ((bytes = from.read(buffer)) != -1)
                out.write(buffer, 0, bytes);

            out.flush();
        } finally {
            from.close();
            out.close();
        }
    }

    /**
     * Copies from a file to another by performing a filtering on certain
     * specified tokens. In particular, each key in the filters map will be
     * looked up in the reader as ${key} and replaced with the associated value.
     * @param to
     * @param filters
     * @param reader
     * 
     * @throws IOException
     */
    public static void filteredCopy(File from, File to, Map<String, String> filters)
            throws IOException {
        filteredCopy(new BufferedReader(new FileReader(from)), to, filters);
    }

    /**
     * Copies from a reader to a file by performing a filtering on certain
     * specified tokens. In particular, each key in the filters map will be
     * looked up in the reader as ${key} and replaced with the associated value.
     * @param to
     * @param filters
     * @param reader
     * 
     * @throws IOException
     */
    public static void filteredCopy(BufferedReader from, File to, Map<String, String> filters)
            throws IOException {
        BufferedWriter out = null;
        // prepare the escaped ${key} keys so that it won't be necessary to do
        // it over and over
        // while parsing the file
        Map<String, String> escapedMap = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            escapedMap.put("${" + entry.getKey() + "}", entry.getValue());
        }
        try {
            out = new BufferedWriter(new FileWriter(to));

            String line = null;
            while ((line = from.readLine()) != null) {
                for (Map.Entry<String, String> entry : escapedMap.entrySet()) {
                    line = line.replace(entry.getKey(), entry.getValue());
                }
                out.write(line);
                out.newLine();
            }
            out.flush();
        } finally {
            from.close();
            out.close();
        }
    }

    /**
     * Copies the provided file onto the specified destination file
     * 
     * @param from
     * @param to
     * @throws IOException
     */
    public static void copy(File from, File to) throws IOException {
        copy(new FileInputStream(from), to);
    }

    /**
     * Copy the contents of fromDir into toDir (if the latter is missing it will
     * be created)
     * 
     * @param fromDir
     * @param toDir
     * @throws IOException
     */
    public static void deepCopy(File fromDir, File toDir) throws IOException {
        if (!fromDir.isDirectory() || !fromDir.exists())
            throw new IllegalArgumentException("Invalid source directory "
                    + "(it's either not a directory, or does not exist");
        if (toDir.exists() && toDir.isFile())
            throw new IllegalArgumentException("Invalid destination directory, "
                    + "it happens to be a file instead");

        // create destination if not available
        if (!toDir.exists())
            if (!toDir.mkdir())
                throw new IOException("Could not create " + toDir);

        File[] files = fromDir.listFiles();
        for (File file : files) {
            File destination = new File(toDir, file.getName());
            if (file.isDirectory())
                deepCopy(file, destination);
            else
                copy(file, destination);
        }
    }

    /**
     * Creates a directory as a child of baseDir. The directory name will be
     * preceded by prefix and followed by suffix
     * 
     * @param basePath
     * @param prefix
     * @return
     * @throws IOException
     */
    public static File createRandomDirectory(String baseDir, String prefix, String suffix)
            throws IOException {
        File tempDir = File.createTempFile(prefix, suffix, new File(baseDir));
        tempDir.delete();
        if (!tempDir.mkdir())
            throw new IOException("Could not create the temp directory " + tempDir.getPath());
        return tempDir;
    }

    /**
     * Recursively deletes the contents of the specified directory. If a file
     * cannot be deleted a log statement will be issued, but no exception will
     * be thrown
     * 
     * @param dir
     * @throws IOException
     */
    public static void delete(File directory) throws IOException {
        if (!directory.isDirectory())
            throw new IllegalArgumentException(directory
                    + " does not appear to be a directory at all...");

        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                delete(files[i]);
            } else {
                if (!files[i].delete())
                    System.out.println("Could not delete " + files[i].getAbsolutePath());
            }
        }

        directory.delete();
    }
}
