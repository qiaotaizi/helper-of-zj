package com.jaiz.desktop.entities;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PptArg {

    /**
     * 参数名
     */
    private TextField argName = new TextField();

    /**
     * 参数所在的sheet
     */
    private ChoiceBox<String> argSheet = new ChoiceBox<>();

    /**
     * 参数所在的位置
     */
    private TextField argPosInExcel = new TextField();

    /**
     * 参数值
     */
    private Label argValue = new Label();

    /**
     * 操作按钮
     */
    private Button opBtn=new Button("删除");

}
