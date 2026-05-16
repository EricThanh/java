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
import javafx.scene.control.Separator;
import javafx.scene.layout.*;

import java.util.Arrays;
import java.util.List;

public class ChuSanDashboardPage {

    public static Scene createScene() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("page-root");

        // ===== SIDEBAR =====
        VBox sidebar = buildSidebar(root);
        root.setLeft(sidebar);

        // ===== DEFAULT CONTENT =====
        root.setCenter(createScrollableContent(TongQuanChuSanPage.createView()));

        Scene scene = new Scene(root, 1280, 820);
        scene.getStylesheets().add(
                ChuSanDashboardPage.class.getResource("/css/dashboard.css").toExternalForm()
        );
        return scene;
    }

    // ────────────────────────────────────────────────────────
    //  SIDEBAR
    // ────────────────────────────────────────────────────────
    private static VBox buildSidebar(BorderPane root) {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(220);
        sidebar.setMinWidth(220);
        sidebar.setMaxWidth(220);

        // --- Header: avatar + tên + vai trò ---
        VBox header = new VBox(4);
        header.getStyleClass().add("sidebar-header");
        header.setPadding(new Insets(20, 20, 16, 20));

        // Avatar circle (initials)
        Label lblAvatar = new Label("CS");
        lblAvatar.getStyleClass().add("sidebar-avatar");

        Label lblVaiTro = new Label("Chủ sân");
        lblVaiTro.getStyleClass().add("sidebar-title");

        Label lblXinChao = new Label("Xin chào: " + AppState.getTenNguoiDung());
        lblXinChao.getStyleClass().add("sidebar-subtitle");

        header.getChildren().addAll(lblAvatar, lblVaiTro, lblXinChao);

        // Separator dưới header
        Separator sep1 = new Separator();
        sep1.setStyle("-fx-background-color: rgba(255,255,255,0.08); -fx-pref-height: 1;");

        // --- Navigation ---
        VBox nav = new VBox(0);
        nav.setPadding(new Insets(8, 0, 8, 0));

        Label secTongQuan = navSectionLabel("Tổng quan");
        Button btnTongQuan = createMenuButton("⊞  Tổng quan");

        Label secQuanLy = navSectionLabel("Quản lý");
        Button btnQuanLySan     = createMenuButton("⬡  Quản lý sân");
        Button btnQuanLyGia     = createMenuButton("⊕  Quản lý giá");
        Button btnQuanLyUuDai   = createMenuButton("♦  Quản lý ưu đãi");
        Button btnQuanLyNhanVien = createMenuButton("⊙  Quản lý nhân viên");

        Label secHeThong = navSectionLabel("Hệ thống");
        Button btnBaoCao = createMenuButton("▣  Báo cáo");
        Button btnCaiDat = createMenuButton("⚙  Cài đặt");

        nav.getChildren().addAll(
                secTongQuan,
                btnTongQuan,
                secQuanLy,
                btnQuanLySan,
                btnQuanLyGia,
                btnQuanLyUuDai,
                btnQuanLyNhanVien,
                secHeThong,
                btnBaoCao,
                btnCaiDat
        );

        // Spacer đẩy logout xuống đáy
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Separator trước logout
        Separator sep2 = new Separator();
        sep2.setStyle("-fx-background-color: rgba(255,255,255,0.08); -fx-pref-height: 1;");

        Button btnDangXuat = createMenuButton("⏻  Đăng xuất");
        btnDangXuat.getStyleClass().add("menu-button-logout");
        btnDangXuat.setPadding(new Insets(12, 20, 12, 20));

        sidebar.getChildren().addAll(
                header, sep1, nav, spacer, sep2, btnDangXuat
        );

        // ── Active button list ──
        List<Button> menuButtons = Arrays.asList(
                btnTongQuan, btnQuanLySan, btnQuanLyGia,
                btnQuanLyUuDai, btnQuanLyNhanVien, btnBaoCao, btnCaiDat
        );
        setActiveButton(btnTongQuan, menuButtons);

        // ── Button actions ──
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
        btnCaiDat.setOnAction(e -> setActiveButton(btnCaiDat, menuButtons));
        btnDangXuat.setOnAction(e -> {
            AppState.clear();
            AppNavigator.goTo(DangNhapPage.createScene(), "Đăng nhập");
        });

        return sidebar;
    }

    // ────────────────────────────────────────────────────────
    //  HELPERS
    // ────────────────────────────────────────────────────────

    /** Nhãn section nhỏ (TỔng quan / Quản lý / Hệ thống) */
    private static Label navSectionLabel(String text) {
        Label lbl = new Label(text.toUpperCase());
        lbl.getStyleClass().add("nav-section-label");
        lbl.setMaxWidth(Double.MAX_VALUE);
        return lbl;
    }

    /** Tạo menu button chuẩn */
    private static Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(38);
        btn.getStyleClass().add("menu-button");
        // Đảm bảo text căn trái
        btn.setAlignment(Pos.CENTER_LEFT);
        return btn;
    }

    /** Đổi active class cho menu button */
    private static void setActiveButton(Button active, List<Button> all) {
        for (Button b : all) {
            b.getStyleClass().remove("menu-button-active");
        }
        active.getStyleClass().add("menu-button-active");
    }

    /** Bọc content trong ScrollPane */
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