package org.example.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        createZipFromFile();
        extractFilesFromTheZipArchive();
    }

    private static void createZipFromFile() {
        String zipFileName = "temporary/create_zip_from_file.zip";

        try {
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(Paths.get(zipFileName)))) {
                // Add a file to the ZIP archive
                String sourceFileName = "test.txt"; // "examples/test.txt"
                ZipEntry entry = new ZipEntry(sourceFileName);
                zipOutputStream.putNextEntry(entry);

                // Write content to the entry
                String content = "Hello Epam.";
                zipOutputStream.write(content.getBytes());

                // Close the entry
                zipOutputStream.closeEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void extractFilesFromTheZipArchive() {
        // Extract files from the ZIP archive
        String zipFileName = "temporary/create_zip_from_file.zip";

        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(Paths.get(zipFileName)))) {
            ZipEntry entry = zipInputStream.getNextEntry();
            while (entry != null) {
                String entryName = entry.getName();
                byte[] buffer = new byte[1024];
                int bytesRead;

                // Extract the entry's content
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                String extractedContent = outputStream.toString();
                outputStream.close();

                System.out.println("Extracted content from " + entryName + ": " + extractedContent);

                entry = zipInputStream.getNextEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}