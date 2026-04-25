package com.football.management.ui.ChuSan;

import com.football.management.app.AppNavigator;
import com.football.management.app.AppState;
import com.football.management.ui.auth.DangNhapPage;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ChuSanDashboardPage {

    public static Scene createScene() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("page-root");

        VBox menu = new VBox(12);
        menu.getStyleClass().add("sidebar");

        Label lblVaiTro = new Label("Chu san");
        lblVaiTro.getStyleClass().add("sidebar-title");

        Label lblXinChao = new Label("Xin chao: " + AppState.getTenNguoiDung());
        lblXinChao.getStyleClass().add("sidebar-subtitle");

        Button btnTongQuan = createMenuButton("Tong quan");
        Button btnQuanLySan = createMenuButton("Quan ly san");
        Button btnQuanLyGia = createMenuButton("Quan ly gia");
        Button btnQuanLyUuDai = createMenuButton("Quan ly uu dai");
        Button btnQuanLyNhanVien = createMenuButton("Quan ly nhan vien");
        Button btnBaoCao = createMenuButton("Bao cao");
        Button btnDangXuat = createMenuButton("Dang xuat");
        btnDangXuat.getStyleClass().add("menu-button-logout");

        menu.getChildren().addAll(
                lblVaiTro,
                lblXinChao,
                btnTongQuan,
                btnQuanLySan,
                btnQuanLyGia,
                btnQuanLyUuDai,
                btnQuanLyNhanVien,
                btnBaoCao,
                btnDangXuat
        );

        root.setLeft(menu);
        root.setCenter(TongQuanChuSanPage.createView());

        btnTongQuan.setOnAction(e -> root.setCenter(TongQuanChuSanPage.createView()));
        btnQuanLySan.setOnAction(e -> root.setCenter(QuanLySanPage.createView()));
        btnQuanLyGia.setOnAction(e -> root.setCenter(QuanLyGiaPage.createView()));
        btnQuanLyUuDai.setOnAction(e -> root.setCenter(QuanLyUuDaiPage.createView()));
        btnQuanLyNhanVien.setOnAction(e -> root.setCenter(QuanLyNhanVienPage.createView()));
        btnBaoCao.setOnAction(e -> root.setCenter(BaoCaoPage.createView()));

        btnDangXuat.setOnAction(e -> {
            AppState.clear();
            AppNavigator.goTo(DangNhapPage.createScene(), "Dang nhap");
        });

        Scene scene = new Scene(root, 1280, 820);
        scene.getStylesheets().add(
                ChuSanDashboardPage.class.getResource("/css/dashboard.css").toExternalForm()
        );
        return scene;
    }

    private static Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(42);
        button.getStyleClass().add("menu-button");
        return button;
    }
}