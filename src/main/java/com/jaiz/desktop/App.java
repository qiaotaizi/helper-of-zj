package com.jaiz.desktop;


import com.jaiz.desktop.util.DesktopUtil;
import com.jaiz.desktop.views.MainSceneBuilder;
import com.jaiz.desktop.views.MainView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        DesktopUtil.preDefinedSystemInit();
        Application.launch(HelperApplication.class,args);
    }


    public static class HelperApplication extends Application{

        @Override
        public void start(Stage primaryStage) throws Exception {
            primaryStage.setScene(new MainSceneBuilder(primaryStage).sceneRender());
            primaryStage.setTitle("老妹儿的工作助手");
            primaryStage.show();
        }
    }

}
