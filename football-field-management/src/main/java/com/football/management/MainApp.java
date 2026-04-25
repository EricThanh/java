//package com.football.management;
//
//import javafx.application.Application;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
//public class MainApp extends Application {
//
//    @Override
//    public void start(Stage stage) {
//        Label lblLogo = new Label("Quản lý sân bóng");
//        lblLogo.getStyleClass().add("app-logo");
//
//        Label lblTitle = new Label("Đăng nhập");
//        lblTitle.getStyleClass().add("login-title");
//
//
//        TextField txtTenDangNhap = new TextField();
//        txtTenDangNhap.setPromptText("Tên đăng nhập");
//        txtTenDangNhap.getStyleClass().add("input-field");
//
//        PasswordField txtMatKhau = new PasswordField();
//        txtMatKhau.setPromptText("Mật khẩu");
//        txtMatKhau.getStyleClass().add("input-field");
//
//        Button btnDangNhap = new Button("Đăng nhập");
//        btnDangNhap.getStyleClass().add("primary-button");
//        btnDangNhap.setMaxWidth(Double.MAX_VALUE);
//
//        Hyperlink linkQuenMatKhau = new Hyperlink("Quên mật khẩu?");
//        linkQuenMatKhau.getStyleClass().add("text-link");
//
//
//
//        VBox card = new VBox(16,
//                lblLogo,
//                lblTitle,
//                txtTenDangNhap,
//                txtMatKhau,
//                btnDangNhap,
//                linkQuenMatKhau
//        );
//        card.setAlignment(Pos.CENTER);
//        card.getStyleClass().add("login-card");
//        card.setMaxWidth(420);
//
//        VBox root = new VBox(card);
//        root.setAlignment(Pos.CENTER);
//        root.getStyleClass().add("page-root");
//
//        Scene scene = new Scene(root, 1100, 760);
//        scene.getStylesheets().add(
//                getClass().getResource("/css/login.css").toExternalForm()
//        );
//
//        stage.setTitle("Quan ly san bong");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

package com.football.management;

import com.football.management.app.AppNavigator;
import com.football.management.ui.auth.DangNhapPage;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        AppNavigator.setMainStage(stage);
        AppNavigator.goTo(DangNhapPage.createScene(), "Dang nhap");
    }

    public static void main(String[] args) {
        launch(args);
    }
}