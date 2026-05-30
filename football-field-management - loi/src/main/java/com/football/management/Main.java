//package com.football.management;
//
//import com.formdev.flatlaf.FlatLightLaf;
//import com.football.management.ui.field.FieldManagementFrame;
//
//import javax.swing.SwingUtilities;
//import javax.swing.UIManager;
//import java.util.TreeMap;
//
//public class Main {
//    public static void main(String[] args) {
//        try {
//            UIManager.setLookAndFeel(new FlatLightLaf());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        SwingUtilities.invokeLater(() -> {
//            FieldManagementFrame frame = new FieldManagementFrame();
//            frame.setVisible(true);
//        });
//    }
//}
//
//
//

package com.football.management;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Button btn = new Button("Mo giao dien JavaFX");

        StackPane root = new StackPane(btn);
        Scene scene = new Scene(root, 900, 600);

        stage.setTitle("Quan ly san bong");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}