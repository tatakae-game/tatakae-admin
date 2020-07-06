package com.tatakae.admin.gui.controllers;

import com.tatakae.admin.core.services.AuthService;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class LoginViewController {
  @FXML
  AnchorPane mainAnchorPane;

  @FXML
  TextField username;

  @FXML
  PasswordField password;

  @FXML
  public void initialize() {
  }

  @FXML
  public void onSubmit() {
    if (!username.getText().isEmpty() && !password.getText().isEmpty()) {
      try {
        final var user = AuthService.authenticate(username.getText(), password.getText());
        if (AuthService.isAuthed()) {
          mainAnchorPane.getChildren()
                  .setAll((Node)FXMLLoader.load(getClass().getResource("/views/HomeView.fxml")));
        }
      } catch (ExecutionException | IOException e) {
        System.out.println(e.getMessage());
      }
    } else {
      System.out.println("Credentials do not match.");
    }
  }
}
