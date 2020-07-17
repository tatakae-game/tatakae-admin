package com.tatakae.admin.gui;

import com.tatakae.admin.core.LocalDataManager;
import com.tatakae.admin.core.PluginManager;

import com.tatakae.admin.core.services.UserService;
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
            FXMLLoader loader;
            final var token = LocalDataManager.getToken();

            if (!token.isEmpty() && !token.isBlank()) {
                final var user = UserService.getUserByToken(token);

                if (user.getId().isEmpty() || user.getId().isBlank()) {
                    loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
                } else {
                    loader = new FXMLLoader(getClass().getResource("/views/HomeView.fxml"));
                }
            } else {
                loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
            }

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
            final var manager = PluginManager.getInstance();
            manager.startPlugins();

            launch();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }

    }
}