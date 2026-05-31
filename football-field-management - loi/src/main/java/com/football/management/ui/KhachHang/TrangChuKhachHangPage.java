package com.football.management.ui.KhachHang;

import com.football.management.app.AppState;
import com.football.management.dao.KhachHangDatSanDAO;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class TrangChuKhachHangPage {

    public static Node createView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        // ===== TITLE =====
        Label lblTitle = new Label("Trang chủ khách hàng");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Tìm sân, đặt lịch nhanh và theo dõi hoạt động đặt sân");
        lblSubtitle.getStyleClass().add("section-subtitle");

        // ===== STAT VALUE LABEL =====
        Label lblSanTrong = new Label("...");
        Label lblUuDai = new Label("...");
        Label lblLanDat = new Label("...");
        Label lblYeuThich = new Label("...");

        // ===== GRID =====
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);

        grid.add(createStatCard("Sân trống hôm nay", lblSanTrong), 0, 0);
        grid.add(createStatCard("Khuyến mãi hiện có", lblUuDai), 1, 0);
        grid.add(createStatCard("Lần đặt gần nhất", lblLanDat), 0, 1);
        grid.add(createStatCard("Sân yêu thích", lblYeuThich), 1, 1);

        // ===== QUICK INFO CARD =====
        VBox card = new VBox(12);
        card.getStyleClass().add("card");

        Label lblSectionTitle = new Label("Thông tin nhanh");
        lblSectionTitle.getStyleClass().add("section-title");

        Label lblContent = new Label(
                """
                - Đặt sân theo khung giờ mong muốn
                - Xem thông tin chi tiết từng sân
                - Áp dụng mã giảm giá khi thanh toán
                - Quản lý lịch sử đặt sân
                - Theo dõi sân yêu thích
                """
        );
        lblContent.getStyleClass().add("section-subtitle");
        lblContent.setWrapText(true);

        card.getChildren().addAll(lblSectionTitle, lblContent);

        // ===== ADD ROOT =====
        root.getChildren().addAll(lblTitle, lblSubtitle, grid, card);

        // ===== CHECK LOGIN =====
        if (AppState.getMaKhachHang() <= 0) {
            Platform.runLater(() -> {
                new Alert(
                        Alert.AlertType.ERROR,
                        "Không tìm thấy thông tin khách hàng.\nVui lòng đăng nhập lại."
                ).showAndWait();
            });
            return root;
        }

        // ===== LOAD DATA =====
        Thread t = new Thread(() -> {
            try {
                KhachHangDatSanDAO dao = new KhachHangDatSanDAO();
                KhachHangDatSanDAO.ThongKeKhachHang tk = dao.layThongKeKhachHang(AppState.getMaKhachHang());

                Platform.runLater(() -> {
                    // ===== SÂN TRỐNG =====
                    lblSanTrong.setText(String.valueOf(tk.sanTrongHomNay));

                    // ===== ƯU ĐÃI =====
                    lblUuDai.setText(String.valueOf(tk.soUuDaiDangCo));

                    // ===== LẦN ĐẶT GẦN NHẤT =====
                    if (tk.lanDatGanNhat == null || tk.lanDatGanNhat.isBlank()) {
                        lblLanDat.setText("Chưa có");
                    } else {
                        lblLanDat.setText(tk.lanDatGanNhat);
                    }

                    // ===== YÊU THÍCH =====
                    lblYeuThich.setText(String.valueOf(tk.soSanYeuThich));
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    new Alert(
                            Alert.AlertType.ERROR,
                            "Không tải được thống kê:\n" + ex.getMessage()
                    ).showAndWait();

                    // ===== FALLBACK =====
                    lblSanTrong.setText("0");
                    lblUuDai.setText("0");
                    lblLanDat.setText("N/A");
                    lblYeuThich.setText("0");
                });
            }
        });
        t.setDaemon(true);
        t.start();

        return root;
    }

    // ===== STAT CARD =====
    private static VBox createStatCard(String title, Label valueLabel) {
        VBox card = new VBox(10);
        card.getStyleClass().add("stat-card");
        card.setPadding(new Insets(18));

        Label lblTitle = new Label(title);
        lblTitle.getStyleClass().add("stat-label");

        valueLabel.getStyleClass().add("stat-value");

        card.getChildren().addAll(lblTitle, valueLabel);
        return card;
    }
}