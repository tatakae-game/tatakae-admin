package com.tatakae.admin.gui.controllers;

import com.tatakae.admin.core.Exceptions.PluginNotFoundException;
import com.tatakae.admin.core.PluginManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PluginViewController {

  private HomeViewController homeViewController;

  @FXML
  private Label pluginNameSelected;

  @FXML
  private TextArea pluginDescriptionSelected;

  @FXML
  private ListView<HBox> pluginsList;

  private PluginManager pluginManager;

  public void initialize() {
    try {
      pluginManager = PluginManager.getInstance();
      displayPluginList();

      final var label = (Label) pluginsList.getItems().get(0).getChildren().get(0);
      pluginNameSelected.setText(label.getText());

      final var env = pluginManager.getEnvironmentByPluginName(label.getText());
      pluginDescriptionSelected.setText(env.getPlugin().getDescription());

    } catch (PluginNotFoundException e) {
      getNoPluginDetails();
    }
  }

  public void setHomeController(HomeViewController homeController) {
    this.homeViewController = homeController;
  }

  public void getNoPluginDetails() {
    pluginNameSelected.setText("Empty");
    pluginDescriptionSelected.setText("No plugin imported yet. Try it!");
  }

  @FXML
  public void getPluginDetails() {
    try {
      final var selectedItem = pluginsList.getSelectionModel().getSelectedItem();
      final var pluginName = (Label) selectedItem.getChildren().get(0);

      final var env = pluginManager.getEnvironmentByPluginName(pluginName.getText());

      pluginNameSelected.setText(pluginName.getText());
      pluginDescriptionSelected.setText(env.getPlugin().getDescription());

    } catch (PluginNotFoundException e) {
      getNoPluginDetails();
      System.out.println(e.getMessage());

    }
  }

  @FXML
  public void importPlugin() {
    final var stage = new Stage();
    final var fileChooser = new FileChooser();

    fileChooser.setTitle("Import a plugin");

    final var file = fileChooser.showOpenDialog(stage);

    if (file != null) {
      if (!pluginManager.importPlugin(file)) {
        System.out.println("File imported is not a valid plugin.");
      } else {
        System.out.println("New plugin added");
        displayPluginList();
      }
    }
  }

  public void displayPluginList() {
    pluginsList.getItems().clear();

    for (final var env : pluginManager.environments.entrySet()) {
      final var label = env.getKey().getPlugin().getName();
      final var button = env.getValue();
      addElementToPluginList(label, button);
    }
  }

  public void addElementToPluginList(final String labelName, final String buttonName) {
    final var label = new Label(labelName);
    label.setFont(new Font(14));
    label.setTextFill(Color.WHITE);

    final var button = new Button(buttonName);
    button.setMinWidth(100);
    button.setTextFill(Color.WHITE);
    button.setStyle("-fx-background-color: #0a041c");

    EventHandler<ActionEvent> event = e -> {
      boolean isMoved = pluginManager.findAndMovePlugin(label.getText(), button.getText());

      if (isMoved) {
        final var text = button.getText().equals("Enable") ? "Disable" : "Enable";
        button.setText(text);

        if (text.equals("Disable")) {
          this.homeViewController.addButtonInVBox(label.getText());
        } else {
          this.homeViewController.removeButtonFromVBox(label.getText());
        }
      } else {
        System.out.println("Display error alert TODO");
      }
    };

    button.setOnAction(event);

    final var region = new Region();
    HBox.setHgrow(region, Priority.ALWAYS);

    var hBox = new HBox(label, region, button);
    hBox.setPadding(new Insets(5, 10, 5, 10));

    pluginsList.getItems().add(hBox);
  }
}
