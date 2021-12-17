package com.jaiz.desktop.models;

import com.jaiz.desktop.ex.DesktopException;
import javafx.scene.control.TextField;
import org.apache.poi.ss.usermodel.Workbook;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CellReader {

    public String read(Workbook wb, String argPosInExcel) {

        var pos=parse(argPosInExcel);
        if (wb == null){
            return "";
        }
        var sheet=wb.getSheetAt(0);
        if (sheet==null){
            return "";
        }
        var row=sheet.getRow(pos.row());
        if (row==null){
            return "";
        }
        var cell=row.getCell(pos.column());
        if (cell==null){
            return "";
        }
        return cell.getStringCellValue();
    }

    CellPosition parse(String argPosInExcel){
        var colRegx="[a-zA-Z]+";
        var rowRegs="[0-9]+";
        Pattern colPat=Pattern.compile(colRegx);
        Pattern rowPat=Pattern.compile(rowRegs);
        Matcher colMat=colPat.matcher(argPosInExcel);
        Matcher rowMat=rowPat.matcher(argPosInExcel);

        boolean colFind = colMat.find();
        if (!colFind){
            throw new DesktopException("格式错误：col");
        }
        var colPos = colMat.group();
        boolean rowFind = rowMat.find();
        if (!rowFind){
            throw new DesktopException("格式错误：row");
        }
        var rowPos=rowMat.group();

        System.out.println(colPos+","+rowPos);

        return new CellPosition(rowPos2Index(rowPos),colPos2Index(colPos));
    }

    int rowPos2Index(String rowPos){
        return Integer.parseInt(rowPos)-1;
    }

    int colPos2Index(String colPos){

        var arr = colPos.toUpperCase().toCharArray();

        int index = 0;

        for (int i = 0; i < arr.length; i++) {
            var c=arr[i];
            var minus = c-'A'+1;
            index+=minus*Math.pow(26,arr.length-i-1);
        }


        return index-1;
    }

    public record CellPosition(int row,int column){

    }
}
