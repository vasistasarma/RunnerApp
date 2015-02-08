package com.fudfill.runner.slidingmenu.common;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by praveenthota on 2/4/15.
 */
public class FileUtils {

    public static void SaveFileWithText(String fileName, String jsonStr) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/fudfill/runner");
        myDir.mkdirs();

        try {
            File file = new File(myDir, fileName);
            if (file.exists())
                file.delete();
            else
                file.createNewFile();


            FileOutputStream out = new FileOutputStream(file);
            byte[] byteString = jsonStr.getBytes();
            out.write(byteString);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String GetTextFromFile(String fileName) {
        StringBuilder text = new StringBuilder();
        try {
            File root = Environment.getExternalStorageDirectory();
            File file = new File(root + "/fudfill/runner", fileName);

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append(' ');
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    public static boolean deleteFile(String fileName) {
        File sdCardRoot = Environment.getExternalStorageDirectory();
        File file = new File(sdCardRoot + "/fudfill/runner", fileName);
        return file.delete();
    }

    public static boolean deleteOldCacheFiles() {
        File sdCardRoot = Environment.getExternalStorageDirectory();
        File myDir = new File(sdCardRoot, "/fudfill/runner");
        File[] fileArray = myDir.listFiles();
        if (fileArray != null && fileArray.length > 0) {
            for (File f : myDir.listFiles()) {
                if (f.isFile()) {
                    Calendar time = Calendar.getInstance();
                    time.add(Calendar.DAY_OF_YEAR, -1);
                    //I store the required attributes here and delete them
                    Date lastModified = new Date(f.lastModified());
                    if (lastModified.before(time.getTime())) {
                        //file is older than a day
                        f.delete();
                    }

                }

            }
        }
        // The directory is now empty so delete it
        return true;
    }

    public static boolean deleteCacheDir() {
        File sdCardRoot = Environment.getExternalStorageDirectory();
        File myDir = new File(sdCardRoot, "/fudfill/runner");
        File[] fileArray = myDir.listFiles();
        if (fileArray != null && fileArray.length > 0) {
            for (File f : myDir.listFiles()) {
                if (f.isFile()) {
                    f.delete();
                }

            }
        }
        // The directory is now empty so delete it
        return myDir.delete();
    }
}
