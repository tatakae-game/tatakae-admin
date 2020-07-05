package com.tatakae.admin.gui.controllers;

import com.tatakae.admin.core.Exceptions.PluginNotFoundException;
import com.tatakae.admin.core.PluginManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;

public class HomeViewController {

    @FXML
    private AnchorPane viewsContainer;

    @FXML
    private VBox vBoxMenuContainer;

    private PluginManager pluginManager;

    @FXML
    public void initialize() {
        try {
            pluginManager = PluginManager.getInstance();

            for(final var env : pluginManager.environmentsActive) {
                addButtonInVBox(env.getPlugin().getName());
            }

            viewsContainer.getChildren()
                    .setAll((Node)FXMLLoader.load(getClass().getResource("/views/TicketsView.fxml")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadPluginsView() {
        try {
            var loader = new FXMLLoader(getClass().getResource("/views/PluginView.fxml"));
            viewsContainer.getChildren().setAll((Node) loader.load());
            PluginViewController pluginViewController = loader.getController();
            pluginViewController.setHomeController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadTicketsView() {
        try {
            viewsContainer.getChildren().setAll((Node)FXMLLoader.load(getClass().getResource("/views/TicketsView.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addButtonInVBox(final String name) {
        Button button = new Button(name);
        button.setId(name);
        button.setStyle("-fx-background-color: #ca792b");
        button.setFont(new Font("system", 14));
        button.setTextFill(Color.WHITE);
        button.setMinWidth(178);

        EventHandler<ActionEvent> event = e -> {
            try {
                final var env = pluginManager.getEnvironmentByPluginName(name);
                viewsContainer.getChildren().setAll((Node)FXMLLoader.load(env.getPlugin().getView()));
            } catch (PluginNotFoundException | IOException pluginNotFoundException) {
                pluginNotFoundException.printStackTrace();
            }
        };

        button.setOnAction(event);

        VBox.setMargin(button, new Insets(24, 0, 8, 0));
        vBoxMenuContainer.getChildren().add(button);
    }

    public void removeButtonFromVBox(final String name) {
        vBoxMenuContainer.getChildren().removeIf(btn -> btn.getId().equals(name));
    }
}
