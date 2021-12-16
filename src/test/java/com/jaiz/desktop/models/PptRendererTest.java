package com.jaiz.desktop.models;

import cn.unicom.wireless.common.util.DateUtil;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.junit.Test;

import java.io.*;
import java.util.Date;
import java.util.HashMap;

public class PptRendererTest {

    @Test
    public void renderTest() throws IOException {
        var filePosition="/Users/jaiz/Desktop/2.pptx";
        var pptFile=new File(filePosition);
        var ppt = new XMLSlideShow(new FileInputStream(pptFile));
        var valueMap=new HashMap<String,String>();
        valueMap.put("name","姜志恒");
        valueMap.put("what","火影");
        var r=new PptRenderer(ppt,valueMap);
        r.render();
        var out=new FileOutputStream(pptFile.getParent()+File.separatorChar+ DateUtil.format(new Date(),"yyyyMMddHHmmss")+".pptx");
        ppt.write(out);
    }

}
