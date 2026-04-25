package com.football.management.app;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppNavigator {
    private static Stage mainStage;

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    public static void goTo(Scene scene, String title) {
        if (mainStage == null) {
            throw new IllegalStateException("Main stage chua duoc khoi tao");
        }
        mainStage.setTitle(title);
        mainStage.setScene(scene);
        mainStage.show();
    }
}