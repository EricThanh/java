package com.football.management.ui.ChuSan;

import com.football.management.app.AppNavigator;
import com.football.management.app.AppState;
import com.football.management.ui.auth.DangNhapPage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

public class ChuSanDashboardPage {

    public static Scene createScene() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("page-root");

        VBox menu = new VBox(12);
        menu.getStyleClass().add("sidebar");

        Label lblVaiTro = new Label("Chủ sân");
        lblVaiTro.getStyleClass().add("sidebar-title");

        Label lblXinChao = new Label("Xin chào: " + AppState.getTenNguoiDung());
        lblXinChao.getStyleClass().add("sidebar-subtitle");

        Button btnTongQuan = createMenuButton("Tổng quan");
        Button btnQuanLySan = createMenuButton("Quản lý sân");
        Button btnQuanLyGia = createMenuButton("Quản lý giá");
        Button btnQuanLyUuDai = createMenuButton("Quản lý ưu đãi");
        Button btnQuanLyNhanVien = createMenuButton("Quản lý nhân viên");
        Button btnBaoCao = createMenuButton("Báo cáo");
        Button btnDangXuat = createMenuButton("Đăng xuất");
        btnDangXuat.getStyleClass().add("menu-button-logout");

        List<Button> menuButtons = Arrays.asList(
                btnTongQuan,
                btnQuanLySan,
                btnQuanLyGia,
                btnQuanLyUuDai,
                btnQuanLyNhanVien,
                btnBaoCao
        );

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

        root.setCenter(createScrollableContent(TongQuanChuSanPage.createView()));
        setActiveButton(btnTongQuan, menuButtons);

        btnTongQuan.setOnAction(e -> {
            root.setCenter(createScrollableContent(TongQuanChuSanPage.createView()));
            setActiveButton(btnTongQuan, menuButtons);
        });

        btnQuanLySan.setOnAction(e -> {
            root.setCenter(createScrollableContent(QuanLySanPage.createView()));
            setActiveButton(btnQuanLySan, menuButtons);
        });

        btnQuanLyGia.setOnAction(e -> {
            root.setCenter(createScrollableContent(QuanLyGiaPage.createView()));
            setActiveButton(btnQuanLyGia, menuButtons);
        });

        btnQuanLyUuDai.setOnAction(e -> {
            root.setCenter(createScrollableContent(QuanLyUuDaiPage.createView()));
            setActiveButton(btnQuanLyUuDai, menuButtons);
        });

        btnQuanLyNhanVien.setOnAction(e -> {
            root.setCenter(createScrollableContent(QuanLyNhanVienPage.createView()));
            setActiveButton(btnQuanLyNhanVien, menuButtons);
        });

        btnBaoCao.setOnAction(e -> {
            root.setCenter(createScrollableContent(BaoCaoPage.createView()));
            setActiveButton(btnBaoCao, menuButtons);
        });

        btnDangXuat.setOnAction(e -> {
            AppState.clear();
            AppNavigator.goTo(DangNhapPage.createScene(), "Đăng nhập");
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

    private static void setActiveButton(Button activeButton, List<Button> buttons) {
        for (Button button : buttons) {
            button.getStyleClass().remove("menu-button-active");
        }
        activeButton.getStyleClass().add("menu-button-active");
    }

    private static ScrollPane createScrollableContent(Node content) {
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("content-scroll");

        return scrollPane;
    }
}