package com.zlyntlab.octocloud.util;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Directory {
    public static List<Path> listFiles(Path path) {
        List<Path> fileList = new ArrayList<>();
        listFilesRecursive(path, fileList);
        return fileList;
    }

    private static void listFilesRecursive(Path path, List<Path> fileList) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    listFilesRecursive(entry, fileList);
                } else {
                    fileList.add(entry.toAbsolutePath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
