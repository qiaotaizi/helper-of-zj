module helper.of.zj {
    requires java.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires poi;
    requires poi.ooxml;
    requires poi.ooxml.schemas;
    requires lombok;
    requires org.apache.commons.lang3;
    requires common.util;

    exports com.jaiz.desktop;
    exports com.jaiz.desktop.models;
    exports com.jaiz.desktop.views;
    exports com.jaiz.desktop.entities;
    exports com.jaiz.desktop.ex;
}