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
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    exports com.jaiz.desktop;
    exports com.jaiz.desktop.models;
    exports com.jaiz.desktop.views;
    exports com.jaiz.desktop.entities;
    exports com.jaiz.desktop.ex;
    exports com.jaiz.desktop.config;
}