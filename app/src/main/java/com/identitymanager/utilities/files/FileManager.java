package com.identitymanager.utilities.files;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {

    public static File createAppInternalStorageFile(File path, String filename) {
        return new File(path, filename);
    }

    public static boolean  writeToAppInternalStorageFile(String filename, String content, Context context) {

        boolean fileWritten = false;

        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(content.getBytes());
            fileWritten = true;
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        return fileWritten;
    }
}
