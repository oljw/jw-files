package com.samsung.retailexperience.retailhero.util;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

/**
 * Created by smheo on 9/29/2015.
 */
public class FileUtil {

    public static void moveFiles(String fromPath, String toPath) throws IOException {
        final File fromDir = new File(fromPath);
        final File toDir = new File(toPath);

        if (!toDir.exists()) {
            toDir.mkdirs();
        }

        for (File fromFile : fromDir.listFiles()) {
            String toFilePath = toPath + File.separator + fromFile.getName();
            if (fromFile.isDirectory()) {
                moveFiles(fromFile.getAbsolutePath(), toFilePath);
            } else {
                File toFile = new File(toFilePath);
                toFile.delete();
                Files.move(fromFile, toFile);
            }
        }

        fromDir.delete();
    }

    public void makeFolder(String path) {
        File dir = new File(path);
        if (dir != null) {
            dir.mkdirs();
        }
    }
}
