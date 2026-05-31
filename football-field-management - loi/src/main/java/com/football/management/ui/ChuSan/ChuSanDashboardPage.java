package com.football.management.ui.ChuSan;

import com.football.management.app.AppNavigator;
import com.football.management.app.AppState;
import com.football.management.ui.auth.DangNhapPage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.util.Arrays;
import java.util.List;

public class ChuSanDashboardPage {

    public static Scene createScene() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("page-root");

        root.setLeft(buildSidebar(root));
        root.setCenter(createScrollableContent(TongQuanChuSanPage.createView()));

        Scene scene = new Scene(root, 1280, 820);
        scene.getStylesheets().add(
                ChuSanDashboardPage.class.getResource("/css/dashboard.css").toExternalForm()
        );
        return scene;
    }

    // ─────────────────────────────────────────────
    //  SIDEBAR
    // ─────────────────────────────────────────────
    private static VBox buildSidebar(BorderPane root) {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");   // teal #0f4c3a

        // ── Header ──
        Label lblVaiTro  = new Label("Chủ sân");
        lblVaiTro.getStyleClass().add("sidebar-title");

        Label lblXinChao = new Label("Xin chào: " + AppState.getTenNguoiDung());
        lblXinChao.getStyleClass().add("sidebar-subtitle");

        VBox header = new VBox(4, lblVaiTro, lblXinChao);
        header.setPadding(new Insets(0, 0, 6, 0));

        // ── Nav buttons ──
        Button btnTongQuan        = navBtn("Tổng quan");
        Button btnQuanLySan       = navBtn("Quản lý sân");
        Button btnQuanLyGia       = navBtn("Quản lý giá");
        Button btnQuanLyUuDai     = navBtn("Quản lý ưu đãi");
        Button btnQuanLyNhanVien  = navBtn("Quản lý nhân viên");
        Button btnBaoCao          = navBtn("Báo cáo");

        List<Button> navBtns = Arrays.asList(
                btnTongQuan, btnQuanLySan, btnQuanLyGia,
                btnQuanLyUuDai, btnQuanLyNhanVien, btnBaoCao
        );

        // ── Logout ──
        Button btnDangXuat = new Button("Đăng xuất");
        btnDangXuat.setMaxWidth(Double.MAX_VALUE);
        btnDangXuat.setPrefHeight(42);
        btnDangXuat.setAlignment(Pos.CENTER_LEFT);
        btnDangXuat.getStyleClass().add("menu-button-logout");

        // Spacer
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sidebar.getChildren().addAll(
                header,
                btnTongQuan,
                btnQuanLySan,
                btnQuanLyGia,
                btnQuanLyUuDai,
                btnQuanLyNhanVien,
                btnBaoCao,
                spacer,
                btnDangXuat
        );

        // Default active
        setActive(btnTongQuan, navBtns);

        // Actions
        btnTongQuan.setOnAction(e -> {
            root.setCenter(createScrollableContent(TongQuanChuSanPage.createView()));
            setActive(btnTongQuan, navBtns);
        });
        btnQuanLySan.setOnAction(e -> {
            root.setCenter(createScrollableContent(QuanLySanPage.createView()));
            setActive(btnQuanLySan, navBtns);
        });
        btnQuanLyGia.setOnAction(e -> {
            root.setCenter(createScrollableContent(QuanLyGiaPage.createView()));
            setActive(btnQuanLyGia, navBtns);
        });
        btnQuanLyUuDai.setOnAction(e -> {
            root.setCenter(createScrollableContent(QuanLyUuDaiPage.createView()));
            setActive(btnQuanLyUuDai, navBtns);
        });
        btnQuanLyNhanVien.setOnAction(e -> {
            root.setCenter(createScrollableContent(QuanLyNhanVienPage.createView()));
            setActive(btnQuanLyNhanVien, navBtns);
        });
        btnBaoCao.setOnAction(e -> {
            root.setCenter(createScrollableContent(BaoCaoPage.createView()));
            setActive(btnBaoCao, navBtns);
        });
        btnDangXuat.setOnAction(e -> {
            AppState.clear();
            AppNavigator.goTo(DangNhapPage.createScene(), "Đăng nhập");
        });

        return sidebar;
    }

    // ─────────────────────────────────────────────
    //  HELPERS
    // ─────────────────────────────────────────────

    private static Button navBtn(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(42);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.getStyleClass().add("menu-button");
        return btn;
    }

    private static void setActive(Button active, List<Button> all) {
        all.forEach(b -> b.getStyleClass().remove("menu-button-active"));
        active.getStyleClass().add("menu-button-active");
    }

    private static ScrollPane createScrollableContent(Node content) {
        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setFitToHeight(false);
        sp.setPannable(true);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        sp.getStyleClass().add("content-scroll");
        return sp;
    }
}