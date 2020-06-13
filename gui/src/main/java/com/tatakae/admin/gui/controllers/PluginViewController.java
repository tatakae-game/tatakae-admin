package com.tatakae.admin.gui.controllers;

import com.tatakae.admin.core.PluginManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PluginViewController {

  @FXML
  TextField PluginFilePathField;

  @FXML
  private Button selectPluginButton;

  @FXML
  private VBox pluginsListContainer;

  private PluginManager pluginManager;

  public void initialize() {
    pluginManager = new PluginManager();

    displayPluginList();
  }

  @FXML
  public void importPlugin() {
    final var stage = new Stage();
    final var fileChooser = new FileChooser();

    fileChooser.setTitle("Import a plugin");

    final var file = fileChooser.showOpenDialog(stage);

    if (!pluginManager.importPlugin(file)) {
      System.out.println("File imported is not a valid plugin.");
    } else {
      System.out.println("New plugin added");
      displayPluginList();
    }
  }

  public void displayPluginList() {
    pluginsListContainer.getChildren().clear();

    for (final var env : pluginManager.environments.entrySet()) {
      final var label = env.getKey().getPlugin().getName();
      final var button = env.getValue();
      addElementToPluginList(label, button);
    }
  }

  public void addElementToPluginList(final String labelName, final String buttonName) {
    final var label = new Label(labelName);
    label.setFont(new Font(14));
    label.setPrefWidth(1500);

    final var button = new Button(buttonName);
    button.setMinWidth(100);

    EventHandler<ActionEvent> event = e -> {
      final var text = button.getText().equals("Enable") ? "Disable" : "Enable";
      button.setText(text);
    };
    button.setOnAction(event);

    var hBox = new HBox();
    hBox.getChildren().addAll(label, button);
    hBox.setPadding(new Insets(5, 10, 5, 10));
    hBox.setPrefWidth(pluginsListContainer.getMaxWidth());

    pluginsListContainer.getChildren().add(hBox);
  }
}
