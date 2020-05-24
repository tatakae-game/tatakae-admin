package com.tatakae.admin.gui;

import com.tatakae.admin.core.PluginManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/HomeView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles/tickets.css").toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.getIcons().add(new Image(getClass().getResource("/images/favicon.png").toString()));
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