package com.tatakae.admin.core;

import com.tatakae.admin.core.Exceptions.CannotCreateFileException;
import kong.unirest.json.JSONObject;
import net.harawata.appdirs.AppDirsFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class LocalDataManager {

    public static File getFile(final String filename) throws CannotCreateFileException {
        try {
            final var filePath = getDataDirectory().resolve(filename);
            createFileIfNotExist(filePath);

            return filePath.toFile();

        } catch (Exception e) {
            throw new CannotCreateFileException(e.getMessage());
        }
    }

    public static File getDirectory(final String filename) throws CannotCreateFileException {
        try {
            final var filePath = getDataDirectory().resolve(filename);
            createDirectoryIfNotExist(filePath);

            return filePath.toFile();

        } catch (Exception e) {
            throw new CannotCreateFileException(e.getMessage());
        }
    }

    private static Path getDataDirectory() {
        final var appDirs = AppDirsFactory.getInstance();
        final var dataDirectory = appDirs.getUserConfigDir("tatakae", null, "tatakae-admin");

        return Paths.get(dataDirectory);
    }

    private static void createFileIfNotExist(final Path path) throws IOException {
        if (!Files.exists(path)) {
            if (!path.toFile().createNewFile()) {
                throw new IOException("Can't create file: " + path);
            }
        }
    }

    private static void createDirectoryIfNotExist(final Path path) throws IOException {
        if (!Files.exists(path)) {
            if (!path.toFile().mkdirs()) {
                throw new IOException("Can't create directory: " + path);
            }
        }
    }

    public static String getToken() throws CannotCreateFileException, FileNotFoundException {
        var config = getFile("config.json");
        if (config.exists()) {
            var sc = new Scanner(config);
            var jsonObject = new JSONObject(sc.nextLine());

            if (jsonObject.has("token")) {
                return jsonObject.getString("token");
            }
        }
        return "";
    }

}
