package com.football.management.ui.KhachHang;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class TrangChuKhachHangPage {

    public static Node createView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        Label lblTitle = new Label("Trang chu khach hang");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Tim san, dat lich nhanh va theo doi cac lan dat san");
        lblSubtitle.getStyleClass().add("section-subtitle");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);

        grid.add(createStatCard("San trong hom nay", "8"), 0, 0);
        grid.add(createStatCard("Khuyen mai dang co", "3"), 1, 0);
        grid.add(createStatCard("Lan dat gan nhat", "2"), 0, 1);
        grid.add(createStatCard("San yeu thich", "4"), 1, 1);

        VBox card = new VBox(10);
        card.getStyleClass().add("card");

        Label lblSectionTitle = new Label("Thong tin nhanh");
        lblSectionTitle.getStyleClass().add("section-title");

        Label lblContent = new Label("""
                - Dat san theo khung gio mong muon
                - Xem danh sach san va thong tin chi tiet
                - Ap dung ma giam gia neu co
                - Theo doi lich su dat san da tao
                """);
        lblContent.getStyleClass().add("section-subtitle");

        card.getChildren().addAll(lblSectionTitle, lblContent);

        root.getChildren().addAll(lblTitle, lblSubtitle, grid, card);
        return root;
    }

    private static VBox createStatCard(String title, String value) {
        VBox card = new VBox(10);
        card.getStyleClass().add("stat-card");

        Label lblTitle = new Label(title);
        lblTitle.getStyleClass().add("stat-label");

        Label lblValue = new Label(value);
        lblValue.getStyleClass().add("stat-value");

        card.getChildren().addAll(lblTitle, lblValue);
        return card;
    }
}