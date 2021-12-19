package com.jaiz.desktop.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DesktopUtil {

    /**
     * 预定义的系统初始化工作
     */
    public static void preDefinedSystemInit(){
        //字体抗锯齿
        System.setProperty("prism.lcdtext", "false");
    }

}
