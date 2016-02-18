package com.samsung.retailexperience.retailhero.util;

import android.content.Context;
import android.util.Log;

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


    /**
     *  unzip files : for frame, bg, chapter directors
     */
    public void startUnzipFiles(Context context, String[] forUnzipList) {
        for (String ZipFileName : forUnzipList) {
            try {
                //Log.d(TAG, "@@@ zip filename : " + ZipFileName);

                final String fullFilePath = String.format("%s/%s",
                        context.getExternalFilesDir(null).toString(), ZipFileName);

                File zipFile = new File(fullFilePath);
                if (zipFile.exists()) {
                    //Log.d(TAG, "@@@ start unzip : " + fullFilePath);
                    UnZipTask unZipTask = new UnZipTask();
                    unZipTask.execute(fullFilePath);
                } else {
                    Log.e(TAG, "This device don't have a" + zipFile);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
