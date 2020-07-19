package com.tatakae.admin.core;

import java.nio.file.Path;
import java.util.ArrayList;

public interface Plugin {
    void start();

    String getMainViewName();

    String getName();

    String getDescription();

    void startCLI();

    Object getController();

    ArrayList<String> getClasses();

    void autoExtractResources(Path destination);

    String getExtractionDirectoryName();
}
