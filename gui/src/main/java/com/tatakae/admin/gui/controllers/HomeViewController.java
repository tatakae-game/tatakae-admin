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
import java.net.URL;

public class HomeViewController {

    private Node pluginView;

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
            viewsContainer.getChildren().clear();

            if (pluginView == null) {
                var loader = new FXMLLoader(getClass().getResource("/views/PluginView.fxml"));
                viewsContainer.getChildren().setAll(pluginView = loader.load());

                PluginViewController pluginViewController = loader.getController();
                pluginViewController.setHomeController(this);
            } else {
                viewsContainer.getChildren().setAll(pluginView);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadTicketsView() {
        try {
            viewsContainer.getChildren().clear();
            viewsContainer.getChildren()
                    .setAll((Node)FXMLLoader.load(getClass().getResource("/views/TicketsView.fxml")));
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

        EventHandler<ActionEvent> event = ev -> {
            try {
                final var viewFileName = pluginManager.getPluginView(name);
                if (!viewFileName.isEmpty()) {
                    FXMLLoader loader = new FXMLLoader(new URL("file:///" + viewFileName));
                    try {
                        final var plugin = pluginManager.getEnvironmentByPluginName(name).getPlugin();
                        loader.setController(plugin.getController());
                    } catch (PluginNotFoundException e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                    viewsContainer.getChildren().setAll((Node) loader.load());
                }
            } catch (IOException e) {
                e.printStackTrace();
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
