package com.fudfill.runner.slidingmenu.common;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by praveenthota on 2/4/15.
 */
public class FileUtils {

    public static void SaveFileWithText(String fileName, String jsonStr) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/fudfill/runner");
        myDir.mkdirs();

        try {
        File file = new File (myDir, fileName);
        if (file.exists ())
            file.delete ();
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
}
