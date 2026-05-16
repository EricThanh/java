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

        Label lblVaiTro = new Label("Nhân viên");
        lblVaiTro.getStyleClass().add("sidebar-title");

        Label lblXinChao = new Label("Xin chào: " + AppState.getTenNguoiDung());
        lblXinChao.getStyleClass().add("sidebar-subtitle");

        Button btnTongQuan = createMenuButton("Tổng quan");
        Button btnQuanLyDatSan = createMenuButton("Quản lý đặt sân");
        Button btnQuanLyKhachHang = createMenuButton("Quản lý khách hàng");
        Button btnXemLichSan = createMenuButton("Xem lịch sân");
        Button btnThanhToan = createMenuButton("Thanh toán");
        Button btnDangXuat = createMenuButton("Đăng xuất");

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

        datMenuDangChon(
                btnTongQuan,
                btnTongQuan,
                btnQuanLyDatSan,
                btnQuanLyKhachHang,
                btnXemLichSan,
                btnThanhToan
        );

        btnTongQuan.setOnAction(e -> {
            root.setCenter(TongQuanNhanVienPage.createView());
            datMenuDangChon(
                    btnTongQuan,
                    btnTongQuan,
                    btnQuanLyDatSan,
                    btnQuanLyKhachHang,
                    btnXemLichSan,
                    btnThanhToan
            );
        });

        btnQuanLyDatSan.setOnAction(e -> {
            root.setCenter(QuanLyDatSanPage.createView());
            datMenuDangChon(
                    btnQuanLyDatSan,
                    btnTongQuan,
                    btnQuanLyDatSan,
                    btnQuanLyKhachHang,
                    btnXemLichSan,
                    btnThanhToan
            );
        });

        btnQuanLyKhachHang.setOnAction(e -> {
            root.setCenter(QuanLyKhachHangPage.createView());
            datMenuDangChon(
                    btnQuanLyKhachHang,
                    btnTongQuan,
                    btnQuanLyDatSan,
                    btnQuanLyKhachHang,
                    btnXemLichSan,
                    btnThanhToan
            );
        });

        btnXemLichSan.setOnAction(e -> {
            root.setCenter(XemLichSanPage.createView());
            datMenuDangChon(
                    btnXemLichSan,
                    btnTongQuan,
                    btnQuanLyDatSan,
                    btnQuanLyKhachHang,
                    btnXemLichSan,
                    btnThanhToan
            );
        });

        btnThanhToan.setOnAction(e -> {
            root.setCenter(ThanhToanPage.createView());
            datMenuDangChon(
                    btnThanhToan,
                    btnTongQuan,
                    btnQuanLyDatSan,
                    btnQuanLyKhachHang,
                    btnXemLichSan,
                    btnThanhToan
            );
        });

        btnDangXuat.setOnAction(e -> {
            AppState.clear();
            AppNavigator.goTo(DangNhapPage.createScene(), "Đăng nhập");
        });

        Scene scene = new Scene(root, 1280, 820);

scene.getStylesheets().add(
        NhanVienDashboardPage.class.getResource("/css/dashboard.css").toExternalForm()
);

scene.getStylesheets().add(
        NhanVienDashboardPage.class.getResource("/css/nhanvien.css").toExternalForm()
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

    private static void datMenuDangChon(Button nutDangChon, Button... tatCaNut) {
        for (Button nut : tatCaNut) {
            nut.getStyleClass().remove("menu-button-active");
        }

        if (!nutDangChon.getStyleClass().contains("menu-button-active")) {
            nutDangChon.getStyleClass().add("menu-button-active");
        }
    }
}