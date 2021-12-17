package com.jaiz.desktop.views;

import cn.unicom.wireless.common.util.DateUtil;
import cn.unicom.wireless.common.util.ExcelFillUtil;
import com.jaiz.desktop.entities.PptArg;
import com.jaiz.desktop.ex.DesktopException;
import com.jaiz.desktop.models.ArgMapBuilder;
import com.jaiz.desktop.models.CellReader;
import com.jaiz.desktop.models.PptRenderer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

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
    public TableColumn<PptArg,Label> idCol;
    @FXML
    public TableColumn<PptArg, TextField> argNameCol;
    @FXML
    public TableColumn<PptArg,TextField> argPosCol;
    @FXML
    public TableColumn<PptArg,Label> argValueCol;
    @FXML
    public TableColumn<PptArg,Button> opCol;
    @FXML
    public Button renderBtn;

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
        commonChooseFile(new FileChooser.ExtensionFilter("PPTX Files (*.pptx)","*.pptx"),pptFileNameLabel,f->{
            try {
                return new XMLSlideShow(new FileInputStream(f));
            } catch (IOException e) {
                e.printStackTrace();
                throw new DesktopException("读取ppt文件失败");
            }

        });
    }

    private void commonChooseFile(FileChooser.ExtensionFilter filter, Label changeTextLabel, Function<File,?> fileType){
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(filter);
        File f = fc.showOpenDialog(window);
        if (f==null){
            return;
        }
        changeTextLabel.setText(f.getName());
        changeTextLabel.setUserData(fileType.apply(f));
        if (pptFileNameLabel.getUserData()!=null && excelFileNameLabel.getUserData()!=null){
            renderBtn.setDisable(false);
        }
    }

    public void chooseExcelBtnAction(ActionEvent actionEvent) {
        commonChooseFile(new FileChooser.ExtensionFilter("XLSX Files (*.xlsx)","*.xlsx"),excelFileNameLabel,f->{
            try {
                return new XSSFWorkbook(new FileInputStream(f));
            } catch (IOException e) {
                e.printStackTrace();
                throw new DesktopException("读取excel文件失败");
            }
        });
    }

    public void addArg(ActionEvent actionEvent) {
        middleTV.getItems().add(initPptArg());
    }

    private PptArg initPptArg() {
        var result=new PptArg();
        result.getId().setText(UUID.randomUUID().toString());
        var delete = result.getOpBtn();
        delete.setOnAction(event->{
            middleTV.getItems().remove(result);
        });
        var argPosTF = result.getArgPosInExcel();
        var curValLabel=result.getArgValue();
        argPosTF.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                System.out.println("old = "+oldValue+", new = "+newValue);
                if (!newValue){
                    //失焦了
                    //读取指定位置的值
                    CellReader cr=new CellReader();
                    var value = cr.read((Workbook)excelFileNameLabel.getUserData(),argPosTF.getText());
                    curValLabel.setText(value);
                }
            }
        });
        return result;
    }

    public void clearArg(ActionEvent actionEvent) {
        middleTV.getItems().clear();
    }

    /**
     *
     * @param actionEvent
     */
    public void render(ActionEvent actionEvent) throws IOException {
        //选择生成文件位置
        DirectoryChooser dc=new DirectoryChooser();
        File target = dc.showDialog(window);
        XMLSlideShow pptFile = (XMLSlideShow)pptFileNameLabel.getUserData();
        XSSFWorkbook excelFile = (XSSFWorkbook)excelFileNameLabel.getUserData();
        //组织argMap
        ArgMapBuilder argMapBuilder=new ArgMapBuilder(excelFile);
        PptRenderer render=new PptRenderer(pptFile,argMapBuilder.build(middleTV));
        render.render();
        var out=new FileOutputStream(target.getPath()+File.separatorChar+ DateUtil.format(new Date(),"yyyyMMddHHmmss")+".pptx");
        pptFile.write(out);
        System.out.println("render done");
    }
}
