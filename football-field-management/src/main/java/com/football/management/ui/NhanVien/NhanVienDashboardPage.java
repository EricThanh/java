package com.football.management.ui.NhanVien;

import com.football.management.app.AppNavigator;
import com.football.management.app.AppState;
import com.football.management.ui.auth.DangNhapPage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class NhanVienDashboardPage {

    public static Scene createScene() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("page-root");

        VBox menu = new VBox(12);
        menu.getStyleClass().add("sidebar");

        Label lblVaiTro = new Label("Nhan vien");
        lblVaiTro.getStyleClass().add("sidebar-title");

        Label lblXinChao = new Label("Xin chao: " + AppState.getTenNguoiDung());
        lblXinChao.getStyleClass().add("sidebar-subtitle");

        Button btnTongQuan = createMenuButton("Tong quan");
        Button btnQuanLyDatSan = createMenuButton("Quan ly dat san");
        Button btnQuanLyKhachHang = createMenuButton("Quan ly khach hang");
        Button btnXemLichSan = createMenuButton("Xem lich san");
        Button btnThanhToan = createMenuButton("Thanh toan");
        Button btnDangXuat = createMenuButton("Dang xuat");
        btnDangXuat.getStyleClass().add("menu-button-logout");

        menu.getChildren().addAll(
                lblVaiTro,
                lblXinChao,
                btnTongQuan,
                btnQuanLyDatSan,
                btnQuanLyKhachHang,
                btnXemLichSan,
                btnThanhToan,
                btnDangXuat
        );

        root.setLeft(menu);
        root.setCenter(TongQuanNhanVienPage.createView());

        btnTongQuan.setOnAction(e -> root.setCenter(TongQuanNhanVienPage.createView()));
        btnQuanLyDatSan.setOnAction(e -> root.setCenter(QuanLyDatSanPage.createView()));
        btnQuanLyKhachHang.setOnAction(e -> root.setCenter(QuanLyKhachHangPage.createView()));
        btnXemLichSan.setOnAction(e -> root.setCenter(XemLichSanPage.createView()));
        btnThanhToan.setOnAction(e -> root.setCenter(ThanhToanPage.createView()));

        btnDangXuat.setOnAction(e -> {
            AppState.clear();
            AppNavigator.goTo(DangNhapPage.createScene(), "Dang nhap");
        });

        Scene scene = new Scene(root, 1280, 820);
        scene.getStylesheets().add(
                NhanVienDashboardPage.class.getResource("/css/dashboard.css").toExternalForm()
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