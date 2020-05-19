package com.tatakae.admin.gui;

import com.tatakae.admin.core.PluginManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Logger.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            final var manager = new PluginManager();
            manager.startPlugins();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        launch();
    }
}