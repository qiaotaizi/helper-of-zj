package com.jaiz.desktop.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public record MainSceneBuilder(Stage primaryStage) {

    public Scene sceneRender() throws IOException {
        return new Scene(root());

    }

    /**
     * 返回scene构造首参
     * @return
     */
    private Parent root() throws IOException {

        FXMLLoader loader=new FXMLLoader(getClass().getResource("MainView.fxml"));
        AnchorPane root= loader.load();
        MainView mv=loader.getController();
        mv.setWindow(primaryStage);
        return root;
    }
}
