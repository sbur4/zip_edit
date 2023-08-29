package org.example.apache_commons;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.Deflater;

public class Main {
    public static void main(String[] args) {
        // gzip tar jar 7z ...
        createZipFromFile();
        extractFilesFromTheZipArchive();
    }

    private static void createZipFromFile() {
        String zipFileName = "temporary/create_zip_from_file_apache.zip";

        try (ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(Files.newOutputStream(Paths.get(zipFileName)))) {
            // Set compression level (optional)
            zipOutputStream.setLevel(Deflater.BEST_COMPRESSION);

            // Create an entry for the file
            String fileName = "file.txt";
            ZipArchiveEntry entry = new ZipArchiveEntry(fileName);
            zipOutputStream.putArchiveEntry(entry);

            // Write content to the entry
            String content = "This is the content of the file.";
            zipOutputStream.write(content.getBytes());

            // Finish the entry
            zipOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void extractFilesFromTheZipArchive() {
        String zipFilePath = "temporary/create_zip_from_file_apache.zip";
        String extractionPath = "temporary";

        try (InputStream fileInputStream = Files.newInputStream(Paths.get(zipFilePath));
             ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(fileInputStream)) {

            ZipArchiveEntry entry;
            while ((entry = zipInputStream.getNextZipEntry()) != null) {
                String entryName = entry.getName();
                Path outputPath = Paths.get(extractionPath, entryName);

                if (entry.isDirectory()) {
                    Files.createDirectories(outputPath);
                } else {
                    Files.copy(zipInputStream, outputPath, StandardCopyOption.REPLACE_EXISTING);
                }

                System.out.println("Extracted: " + outputPath);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}