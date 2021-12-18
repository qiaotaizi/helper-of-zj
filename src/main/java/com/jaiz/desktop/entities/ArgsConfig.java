package com.jaiz.desktop.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArgsConfig {

    private String configName;

    private String pptFilePath;

    private String excelFilePath;

    private List<Arg> argList;

    @Override
    public String toString(){
        return this.configName;
    }

}
