package com.football.management.ui.KhachHang;

import com.football.management.app.AppNavigator;
import com.football.management.app.AppState;
import com.football.management.ui.auth.DangNhapPage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class KhachHangHomePage {

    public static Scene createScene() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("page-root");

        VBox menu = new VBox(12);
        menu.getStyleClass().add("sidebar");

        Label lblVaiTro = new Label("Khach hang");
        lblVaiTro.getStyleClass().add("sidebar-title");

        Label lblXinChao = new Label("Xin chao: " + AppState.getTenNguoiDung());
        lblXinChao.getStyleClass().add("sidebar-subtitle");

        Button btnTrangChu = createMenuButton("Trang chu");
        Button btnDanhSachSan = createMenuButton("Danh sach san");
        Button btnChiTietSan = createMenuButton("Chi tiet san");
        Button btnDatSan = createMenuButton("Dat san");
        Button btnLichSu = createMenuButton("Lich su dat san");
        Button btnDangXuat = createMenuButton("Dang xuat");
        btnDangXuat.getStyleClass().add("menu-button-logout");

        menu.getChildren().addAll(
                lblVaiTro,
                lblXinChao,
                btnTrangChu,
                btnDanhSachSan,
                btnChiTietSan,
                btnDatSan,
                btnLichSu,
                btnDangXuat
        );

        root.setLeft(menu);
        root.setCenter(TrangChuKhachHangPage.createView());

        btnTrangChu.setOnAction(e -> root.setCenter(TrangChuKhachHangPage.createView()));
        btnDanhSachSan.setOnAction(e -> root.setCenter(DanhSachSanPage.createView()));
        btnChiTietSan.setOnAction(e -> root.setCenter(ChiTietSanPage.createView()));
        btnDatSan.setOnAction(e -> root.setCenter(DatSanPage.createView()));
        btnLichSu.setOnAction(e -> root.setCenter(LichSuDatSanPage.createView()));

        btnDangXuat.setOnAction(e -> {
            AppState.clear();
            AppNavigator.goTo(DangNhapPage.createScene(), "Dang nhap");
        });

        Scene scene = new Scene(root, 1280, 820);
        scene.getStylesheets().add(
                KhachHangHomePage.class.getResource("/css/dashboard.css").toExternalForm()
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