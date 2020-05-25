package com.tatakae.admin.core;

import com.tatakae.admin.core.Exceptions.CannotCreateFileException;
import kong.unirest.json.JSONObject;
import net.harawata.appdirs.AppDirsFactory;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class StoredDataManager {

    public static File getFile(final String filename) throws CannotCreateFileException {
        try {
            final var filePath= getDataDirectory().resolve(filename);
            createIfNotExist(filePath);

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

    private static void createIfNotExist(final Path path) throws CannotCreateFileException, IOException {
        if (!Files.exists(path)) {
            final var extension = FilenameUtils.getExtension(path.toString());
            final var created = extension.isEmpty() ? path.toFile().mkdir() : path.toFile().createNewFile();

            if (!created) {
                throw new CannotCreateFileException("Can't create file.");
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
