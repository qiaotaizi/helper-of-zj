package com.jaiz.desktop.entities;

import javafx.scene.control.Button;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PptArg {

    /**
     * uuid
     */
    private String id;

    /**
     * 参数名
     */
    private String argName;

    /**
     * 参数所在的位置
     */
    private String argPosInExcel;

    /**
     * 参数值
     */
    private String argValue;

    /**
     * 操作按钮
     */
    private Button opBtn=new Button("删除");

}
