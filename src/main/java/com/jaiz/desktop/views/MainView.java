package com.jaiz.desktop.views;

import com.jaiz.desktop.entities.PptArg;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.Setter;

import java.io.File;

public class MainView{

    @FXML
    public Button choosePptBtn;
    @FXML
    public Label pptFileNameLabel;
    @FXML
    public Button chooseExcelBtn;
    @FXML
    public Label excelFileNameLabel;
    @FXML
    public GridPane topGP;
    @FXML
    public TableView<PptArg> middleTV;
    @FXML
    public Button addArgBtn;
    @FXML
    public Button clearArgBtn;
    @FXML
    public TableColumn<PptArg,String> idCol;
    @FXML
    public TableColumn<PptArg,String> argNameCol;
    @FXML
    public TableColumn<PptArg,String> argPosCol;
    @FXML
    public TableColumn<PptArg,String> argValueCol;
    @FXML
    public TableColumn<PptArg,Button> opCol;

    @Setter
    private Window window;

    public void initialize(){
        System.out.println("初始化mainview");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        argNameCol.setCellValueFactory(new PropertyValueFactory<>("argName"));
        argPosCol.setCellValueFactory(new PropertyValueFactory<>("argPosInExcel"));
        argValueCol.setCellValueFactory(new PropertyValueFactory<>("argValue"));
        opCol.setCellValueFactory(new PropertyValueFactory<>("opBtn"));
    }


    public void choosePptBtnAction(ActionEvent actionEvent) {
        commonChooseFile(new FileChooser.ExtensionFilter("PPTX Files (*.pptx)","*.pptx"),pptFileNameLabel);
    }

    private void commonChooseFile(FileChooser.ExtensionFilter filter,Label changeTextLabel){
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(filter);
        File f = fc.showOpenDialog(window);
        if (f==null){
            return;
        }
        changeTextLabel.setText(f.getName());
    }

    public void chooseExcelBtnAction(ActionEvent actionEvent) {
        commonChooseFile(new FileChooser.ExtensionFilter("XLSX Files (*.xlsx)","*.xlsx"),excelFileNameLabel);
    }

    public void addArg(ActionEvent actionEvent) {
        middleTV.getItems().add(new PptArg());
    }

    public void clearArg(ActionEvent actionEvent) {
        middleTV.getItems().clear();
    }
}
