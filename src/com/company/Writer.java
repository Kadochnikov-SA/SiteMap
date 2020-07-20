package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Writer {

    private String path;
    private List<String> links;

    public Writer(String path, List<String> links) {
        this.path = path;
        this.links = links;
    }


    public void write() throws IOException {
        File file = createFile(path);
        if (file!=null) {
            try (FileWriter fileWriter = new FileWriter(file)) {
                for (String link : links) {
                    fileWriter.write(link + "\n");
                }
                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else System.out.println("Reason: File is already exists.");
    }

    private File createFile(String path) throws IOException {
        File file = new File(path + "/siteMap.txt");
        if (file.createNewFile()) {
            System.out.println("The file was created: " + file.getPath());
        } else {
            System.out.println("Failed to create a file: " + file.getPath());
            return null;
        }
        return file;
    }

}
