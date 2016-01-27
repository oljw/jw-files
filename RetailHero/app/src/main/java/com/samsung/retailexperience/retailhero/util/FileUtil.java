package com.samsung.retailexperience.retailhero.util;

import com.google.common.io.Files;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by smheo on 9/29/2015.
 */
public class FileUtil {
    private static final String TAG = FileUtil.class.getSimpleName();

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


    public String getFolderPath(String filePath) {
        if (filePath == null || filePath.length() == 0) return null;
        return filePath.substring(0, filePath.lastIndexOf("/")+1);
    }

    static int BUFFER_SIZE = 2048;
    public boolean unzipToFolder(String zipFile) throws IOException {
        if (zipFile == null || zipFile.length() == 0) return false;

        int size;
        byte[] buffer = new byte[BUFFER_SIZE];

        try {
            String location = getFolderPath(zipFile);
            ZipInputStream zin = new ZipInputStream(
                    new BufferedInputStream(new FileInputStream(zipFile), BUFFER_SIZE));
            try {
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null) {
                    String path = location + ze.getName();
                    File unzipFile = new File(path);

                    if (ze.isDirectory()) {
                        if (!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    } else {
                        // check for and create parent directories if they don't exist
                        File parentDir = unzipFile.getParentFile();
                        if (null != parentDir) {
                            if (!parentDir.isDirectory()) {
                                parentDir.mkdirs();
                            }
                        }

                        // unzip the file
                        FileOutputStream out = new FileOutputStream(unzipFile, false);
                        BufferedOutputStream fout = new BufferedOutputStream(out, BUFFER_SIZE);
                        try {
                            while ((size = zin.read(buffer, 0, BUFFER_SIZE)) != -1) {
                                fout.write(buffer, 0, size);
                            }
                            zin.closeEntry();
                        } finally {
                            fout.flush();
                            fout.close();
                        }
                    }
                }
            } finally {
                zin.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
