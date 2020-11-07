package com.github.martinpaoloni.downloader;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Downloads audio files for DaF Leight books.
 *
 * https://www.klett-sprachen.de/dafleicht-online/
 */
public class DafLeightAudiosDownloader {

    public static void main(String[] args) {

        Map<String, String> books = new HashMap<>();
        books.put("_1", "A1.1");
        books.put("_2", "A1.2");
        books.put("_21", "A2.1");
        books.put("_22", "A2.2");
        books.put("_B11", "B1.1");
        books.put("_B12", "B1.2");

        String pathFormat = "https://www.klett-sprachen.de/dafleicht-online/%s/_inhalte/audios/mp3/";
        String[] fileNameFormats = {"kb_%02d.mp3", "ub_%02d.mp3"};
        String downloadDirectoryFormat = "download/DaF leight/%s";

        //For each book
        for(Map.Entry<String, String> book : books.entrySet()) {
            System.out.println("Downloading audios for book " + book.getValue());
            String basePath = String.format(pathFormat, book.getKey());
            String downloadDir = String.format(downloadDirectoryFormat, book.getValue()) + "/";
            new File(downloadDir).mkdirs();
            //For each book type
            for(String bookType : fileNameFormats) {
                System.out.println("= Downloading files with format " + bookType);
                int i = 1;
                boolean moreFiles = true;
                //Until we get a 404
                while (moreFiles) {
                    String fileName = String.format(bookType, i);
                    String fileUrl = basePath + fileName;
                    System.out.println("== Downloading file " + fileUrl);
                    try (BufferedInputStream in = new BufferedInputStream(new URL(fileUrl).openStream());
                         FileOutputStream fileOutputStream = new FileOutputStream(downloadDir + fileName)) {
                        byte dataBuffer[] = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                            fileOutputStream.write(dataBuffer, 0, bytesRead);
                        }
                    } catch (FileNotFoundException e) {
                        System.out.println("== Not found: "+ e.getMessage());
                        moreFiles = false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
            }
        }
        System.out.println("Done!");
    }
}
