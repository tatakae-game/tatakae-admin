package com.tatakae.admin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.nio.file.Paths;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        try {
            String userDirectory = Paths.get("")
                    .toAbsolutePath()
                    .toString();

            System.out.println(userDirectory);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Parent root = loader.load();

            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}