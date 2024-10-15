package com.zlyntlab.octocloud.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StringToFile {
    public static void saveStringToFile(String filePath, String data) {
        try {
            // Create a File object for the file path
            File file = new File(filePath);

            // Create the parent directories if they do not exist
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            // Create a FileWriter object
            FileWriter writer = new FileWriter(file);

            // Write the string to the file
            writer.write(data);

            // Close the writer
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
