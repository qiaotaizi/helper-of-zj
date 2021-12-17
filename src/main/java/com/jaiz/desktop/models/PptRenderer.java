package com.jaiz.desktop.models;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.sl.usermodel.TextShape;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import java.util.Map;

public record PptRenderer(XMLSlideShow ppt,
                          Map<String, String> valueMap) {

    /**
     * 遍历ppt中所有文本框
     * 检查文本框中是否包含map中的{变量}，
     * 若有，进行替换
     */
    public void render() {
        //遍历幻灯片
        var slides = ppt.getSlides();
        slides.forEach(this::renderOneSlides);
    }

    /**
     * 在一张幻灯片中，遍历所有文本框
     *
     * @param slide 幻灯片
     */
    private void renderOneSlides(XSLFSlide slide) {
        var shapes = slide.getShapes();
        shapes.forEach(this::renderOneShape);
    }

    /**
     * 在一个shape中，进行文本替换
     *
     * @param shape 形状
     */
    private void renderOneShape(XSLFShape shape) {
        if (shape instanceof TextShape<?,?>){
            //提取文本并替换
            replace((TextShape<?,?>)shape);
        }
    }

    /**
     * 替换shape中的文本
     * TODO 可以考虑使用模板引擎替换简单的文字替换
     * @param shape 转型为textShape的形状
     */
    private void replace(TextShape<?,?> shape) {

        var text = shape.getText();
        System.out.println("shape content is "+text);
        for (Map.Entry<String, String> entry : valueMap.entrySet()) {
            var argName=entry.getKey();
            var argValue=entry.getValue();
            var argPattern="${"+argName+"}";
            if (StringUtils.contains(text,argPattern)){
                text=text.replace(argPattern,argValue);
            }
        }
        shape.setText(text);

    }

}
