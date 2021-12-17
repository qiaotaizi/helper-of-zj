package com.jaiz.desktop.models;

import com.jaiz.desktop.entities.PptArg;
import com.jaiz.desktop.ex.DesktopException;
import javafx.scene.control.TableView;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public record ArgMapBuilder(Workbook wb) {

    public Map<String,String> build(TableView<PptArg> tableView){
        var args = tableView.getItems();
        var cellReader=new CellReader();
        return args.stream().collect(Collectors.toMap(arg->arg.getArgName().getText(),arg->{
            return cellReader.read(wb,arg.getArgSheet().getSelectionModel().getSelectedItem(),arg.getArgPosInExcel().getText());
        },(oV,nV)->{
            throw new DesktopException("参数名冲突："+oV);
        }));
    }

}
