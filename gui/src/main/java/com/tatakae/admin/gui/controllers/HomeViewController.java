package com.tatakae.admin.gui.controllers;

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

    @FXML
    public void initialize() {
        try {
            viewsContainer.getChildren().setAll((Node)FXMLLoader.load(getClass().getResource("/views/TicketsView.fxml")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadPluginsView() {
        try {
            viewsContainer.getChildren().setAll((Node)FXMLLoader.load(getClass().getResource("/views/PluginView.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadErrorsView() {
        System.out.println("Display errors view");
    }

    @FXML
    public void loadTicketsView() {
        try {
            viewsContainer.getChildren().setAll((Node)FXMLLoader.load(getClass().getResource("/views/TicketsView.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadGroupsView() {
        System.out.println("Display groups of permissions view");
    }

    public void addButtonInVBox(final String name) {
        Button button = new Button(name);
        button.setStyle("-fx-background-color: #ca792b");
        button.setFont(new Font("system", 14));
        button.setTextFill(Color.WHITE);
        button.setMinWidth(178);

        VBox.setMargin(button, new Insets(24, 0, 8, 0));
        vBoxMenuContainer.getChildren().add(button);
    }
}
