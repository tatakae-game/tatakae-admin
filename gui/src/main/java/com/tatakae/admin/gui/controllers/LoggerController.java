package com.tatakae.admin.gui.controllers;

import com.tatakae.admin.core.services.AuthService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.concurrent.ExecutionException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoggerController {
  @FXML
  Button loginButton;

  @FXML
  TextField username;

  @FXML
  PasswordField password;

  @FXML
  public void initialize() {
  }

  @FXML
  public void login() {
    try {
      var user = AuthService.authenticate(username.getText(), password.getText());
      System.out.println(user.toString());
      if (AuthService.isAuthed()) {
        try {
          FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Menu.fxml"));
          Parent root = (Parent) fxmlLoader.load();
          Stage stage = new Stage();
          stage.setScene(new Scene(root));
          stage.show();
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        // TODO Implement pop up manager
      }

    } catch (ExecutionException exception) {

    }
  }
}
