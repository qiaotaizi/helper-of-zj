package com.jaiz.desktop.views;

import cn.unicom.wireless.common.util.DateUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jaiz.desktop.config.ConfigManager;
import com.jaiz.desktop.entities.AppProperties;
import com.jaiz.desktop.entities.Arg;
import com.jaiz.desktop.entities.ArgsConfig;
import com.jaiz.desktop.entities.PptArg;
import com.jaiz.desktop.ex.DesktopException;
import com.jaiz.desktop.models.ArgMapBuilder;
import com.jaiz.desktop.models.CellReader;
import com.jaiz.desktop.models.PptRenderer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public TableColumn<PptArg, TextField> argNameCol;
    @FXML
    public TableColumn<PptArg,TextField> argPosCol;
    @FXML
    public TableColumn<PptArg,Label> argValueCol;
    @FXML
    public TableColumn<PptArg,Button> opCol;
    @FXML
    public TableColumn<PptArg,ChoiceBox<String>> argSheetCol;

    @FXML
    public Button renderBtn;

    @Setter
    private Window window;

    private ConfigManager<AppProperties> configManager;

    /**
     * 当前配置的引用
     */
    private ArgsConfig currArgsConfig;

    public void initialize() throws IOException {
        System.out.println("初始化mainview");
        argNameCol.setCellValueFactory(new PropertyValueFactory<>("argName"));
        argPosCol.setCellValueFactory(new PropertyValueFactory<>("argPosInExcel"));
        argValueCol.setCellValueFactory(new PropertyValueFactory<>("argValue"));
        opCol.setCellValueFactory(new PropertyValueFactory<>("opBtn"));
        argSheetCol.setCellValueFactory(new PropertyValueFactory<>("argSheet"));

        configManager=new ConfigManager<>("helper-of-zj");
        //初始化config文件夹
        configManager.validateAndInit(new TypeReference<>(){});
        var appProperties=configManager.configBean();
        if (appProperties.getArgsConfigList()==null){
            appProperties.setArgsConfigList(new ArrayList<>());
        }
    }

    /**
     * file转化为ppt
     */
    private final Function<File,?> pptFileConverter = f->{
        try {
            return new XMLSlideShow(new FileInputStream(f));
        } catch (IOException e) {
            e.printStackTrace();
            throw new DesktopException("读取ppt文件失败");
        }

    };

    public void choosePptBtnAction(ActionEvent actionEvent) {
        commonChooseFile(new FileChooser.ExtensionFilter("PPTX Files (*.pptx)","*.pptx"),pptFileNameLabel,pptFileConverter);
    }

    private void commonChooseFile(FileChooser.ExtensionFilter filter, Label changeTextLabel, Function<File,?> fileType){
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(filter);
        File f = fc.showOpenDialog(window);
        if (f==null){
            return;
        }
        changeTextLabel.setText(f.getPath());
        changeTextLabel.setUserData(fileType.apply(f));

        renderBtnEnableCheck();
    }

    private void renderBtnEnableCheck() {
        if (pptFileNameLabel.getUserData()!=null && excelFileNameLabel.getUserData()!=null){
            renderBtn.setDisable(false);
        }
    }

    /**
     * file转化为excel
     */
    private final Function<File,?> excelFileConverter = f->{
        try {
            return new XSSFWorkbook(new FileInputStream(f));
        } catch (IOException e) {
            e.printStackTrace();
            throw new DesktopException("读取excel文件失败");
        }
    };

    public void chooseExcelBtnAction(ActionEvent actionEvent) {
        commonChooseFile(new FileChooser.ExtensionFilter("XLSX Files (*.xlsx)","*.xlsx"),excelFileNameLabel,excelFileConverter);
    }

    public void addArg(ActionEvent actionEvent) {
        addArgToMidTV(new PptArg());
    }

    private void addArgToMidTV(PptArg arg){
        pptArgEquip(arg);
        middleTV.getItems().add(arg);
    }

    /**
     * 添加失焦计算当前值监听
     * @param arg pptArg对象
     */
    private void addOnFocusedListenerOnArgPosTF(PptArg arg){
        var argPosTF = arg.getArgPosInExcel();
        var curValLabel=arg.getArgValue();
        var sheetChoiceBox=arg.getArgSheet();
        argPosTF.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue){
                //失焦了
                //读取指定位置的值
                actionOnArgPosTfLostFocus(arg);
            }
        });
    }

    /**
     * 参数位置文本框失焦时执行的动作
     */
    private void actionOnArgPosTfLostFocus(PptArg arg){
        CellReader cr=new CellReader();
        var value = cr.read((Workbook)excelFileNameLabel.getUserData(),arg.getArgSheet().getSelectionModel().getSelectedItem(),arg.getArgPosInExcel().getText());
        arg.getArgValue().setText(value);
    }

    private void pptArgEquip(PptArg result) {
        var delete = result.getOpBtn();

        if (excelFileNameLabel.getUserData()!=null){
            var wb = (Workbook)excelFileNameLabel.getUserData();
            wb.sheetIterator().forEachRemaining(sheet->
                    result.getArgSheet().getItems().add(sheet.getSheetName()));
        }


        delete.setOnAction(event-> middleTV.getItems().remove(result));
        //添加失焦监听
        addOnFocusedListenerOnArgPosTF(result);
    }

    public void clearArg(ActionEvent actionEvent) {
        middleTV.getItems().clear();
    }

    /**
     *
     * @param actionEvent 事件对象
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
        out.close();
        System.out.println("render done");
    }

    /**
     * 参数保存的通用部分
     */
    private void saveArgsCommon(){
        currArgsConfig.setPptFilePath(pptFileNameLabel.getText());
        currArgsConfig.setExcelFilePath(excelFileNameLabel.getText());
        var argList=middleTV.getItems().stream().map(pptArg->{
            Arg a=new Arg();
            a.setArgName(pptArg.getArgName().getText());
            a.setArgPos(pptArg.getArgPosInExcel().getText());
            a.setArgSheetName(pptArg.getArgSheet().getSelectionModel().getSelectedItem());
            return a;
        }).collect(Collectors.toList());
        currArgsConfig.setArgList(argList);
    }

    public void saveArgs(ActionEvent actionEvent) throws IOException {
        System.out.println("save args");
        //检查当前配置是否已经有引用
        //若有，更新配置，使用配置对象刷新文件
        //若没有，弹窗要求输入名字，之后新建配置对象，加入配置对象列表，刷新文件
        if (currArgsConfig!=null){
            saveArgsCommon();
            configManager.syncConfigFile();
            return;
        }
        TextInputDialog configNameInputDialog=new TextInputDialog();
        configNameInputDialog.setHeaderText("请为新的配置命名");
        configNameInputDialog.setContentText("名称：");
        var configNameInput = configNameInputDialog.showAndWait();
        configNameInput.ifPresent(input->{
            currArgsConfig=new ArgsConfig();
            saveArgsCommon();
            currArgsConfig.setConfigName(input);
            configManager.configBean().getArgsConfigList().add(currArgsConfig);
            try {
                configManager.syncConfigFile();
            } catch (IOException e) {
                System.out.println("同步配置对象至文件失败");
                e.printStackTrace();
            }
        });
    }

    public void loadArgs(ActionEvent actionEvent) {
        System.out.println("load args");
        //弹窗，罗列所有配置对象，提供删除、选择入口
        ChoiceDialog<ArgsConfig> cd=new ChoiceDialog<>();
        cd.getItems().addAll(configManager.configBean().getArgsConfigList());
        cd.setHeaderText("请选择要载入的配置");
        cd.setContentText("配置：");
        var choice = cd.showAndWait();
        choice.ifPresent(argC->{
            currArgsConfig=argC;
            pptFileNameLabel.setText(currArgsConfig.getPptFilePath());
            pptFileNameLabel.setUserData(pptFileConverter.apply(new File(currArgsConfig.getPptFilePath())));
            excelFileNameLabel.setText(currArgsConfig.getExcelFilePath());
            excelFileNameLabel.setUserData(excelFileConverter.apply(new File(currArgsConfig.getExcelFilePath())));
            //清空tableView现有的参数
            middleTV.getItems().clear();
            currArgsConfig.getArgList().forEach(a->{
                PptArg pa=new PptArg();
                pa.getArgName().setText(a.getArgName());
                pa.getArgSheet().getSelectionModel().select(a.getArgSheetName());
                var posTF=pa.getArgPosInExcel();
                posTF.setText(a.getArgPos());
                addArgToMidTV(pa);
                actionOnArgPosTfLostFocus(pa);
            });
            renderBtnEnableCheck();
            renderBtn.requestFocus();
            System.out.println("将配置应用到界面");
        });

    }

    public void deleteArgs(ActionEvent actionEvent) throws IOException {
        System.out.println("delete args");
        //弹窗，选择
        ChoiceDialog<ArgsConfig> cd=new ChoiceDialog<>();
        cd.getItems().addAll(configManager.configBean().getArgsConfigList());
        cd.setHeaderText("请选择要删除的配置");
        cd.setContentText("配置：");
        var choice = cd.showAndWait();
        choice.ifPresent(arg->
                configManager.configBean().getArgsConfigList().remove(arg));
        configManager.syncConfigFile();
    }
}
