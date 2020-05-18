package com.tatakae.admin.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class HomeController {

    @FXML
    private AnchorPane homeAnchorPane;

    @FXML
    private AnchorPane viewsContainer;

    @FXML
    private Button pluginsViewButton;

    @FXML
    private Button errorsViewButton;

    @FXML
    private Button ticketsViewButton;

    @FXML
    private Button groupsViewButton;

    @FXML
    public void initialize() {
        try {
            viewsContainer.getChildren().setAll((Node)FXMLLoader.load(getClass().getResource("/views/TestInsertView.fxml")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadPluginsView() {
        System.out.println("Display plugins view");
    }

    @FXML
    public void loadErrorsView() {
        System.out.println("Display errors view");
    }

    @FXML
    public void loadTicketsView() {
        System.out.println("Display tickets view");
    }


    @FXML
    public void loadGroupsView() {
        System.out.println("Display groups of permissions view");
    }
}
