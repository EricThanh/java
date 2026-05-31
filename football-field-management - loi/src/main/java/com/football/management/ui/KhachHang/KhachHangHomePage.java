package com.football.management.ui.KhachHang;

import com.football.management.app.AppNavigator;
import com.football.management.app.AppState;
import com.football.management.dao.KhachHangDatSanDAO.SanKhachHangRow;
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

        // ── Sidebar ──
        VBox menu = new VBox(12);
        menu.getStyleClass().add("sidebar");

        Label lblVaiTro  = new Label("Khách hàng");
        lblVaiTro.getStyleClass().add("sidebar-title");

        Label lblXinChao = new Label("Xin chào: " + AppState.getTenNguoiDung());
        lblXinChao.getStyleClass().add("sidebar-subtitle");

        Button btnTrangChu   = createMenuButton("Trang chủ");
        Button btnDanhSach   = createMenuButton("Danh sách sân");
        Button btnDatSan     = createMenuButton("Đặt sân");
        Button btnLichSu     = createMenuButton("Lịch sử đặt sân");
        Button btnDangXuat   = createMenuButton("Đăng xuất");
        btnDangXuat.getStyleClass().add("menu-button-logout");

        menu.getChildren().addAll(
                lblVaiTro, lblXinChao,
                btnTrangChu, btnDanhSach, btnDatSan, btnLichSu,
                btnDangXuat);

        root.setLeft(menu);

        // ── Trang chủ mặc định ──
        root.setCenter(TrangChuKhachHangPage.createView());

        // ── Navigation ──
        btnTrangChu.setOnAction(e -> root.setCenter(TrangChuKhachHangPage.createView()));

        // Danh sách sân → Chi tiết sân → Đặt sân
btnDanhSach.setOnAction(e ->
    root.setCenter(DanhSachSanPage.createView((san, ngayDat, gioBD, gioKT) -> {

        // Mở chi tiết sân, giữ lại ngày/giờ khách đã chọn
        root.setCenter(ChiTietSanPage.createView(
                san,
                ngayDat,
                gioBD,
                gioKT,
                (sanChon, ngayChon, gioBDChon, gioKTChon) -> {

                    // Khi bấm Đặt sân ở trang chi tiết,
                    // chuyển sang DatSanPage và tự điền sân + ngày + giờ
                    root.setCenter(DatSanPage.createView(
                            sanChon,
                            ngayChon,
                            gioBDChon,
                            gioKTChon
                    ));
                }
        ));
    }))
);

        // Đặt sân trực tiếp (không chọn sân trước)
        btnDatSan.setOnAction(e -> root.setCenter(DatSanPage.createView()));

        btnLichSu.setOnAction(e -> root.setCenter(LichSuDatSanPage.createView()));

        btnDangXuat.setOnAction(e -> {
            AppState.clear();
            AppNavigator.goTo(DangNhapPage.createScene(), "Đăng nhập");
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