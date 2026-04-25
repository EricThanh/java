package com.football.management.ui.ChuSan;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class TongQuanChuSanPage {

    public static Node createView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        Label lblTitle = new Label("TỔNG QUAN");
        lblTitle.getStyleClass().add("page-title");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);

        grid.add(createStatCard("Tong so san", "12"), 0, 0);
        grid.add(createStatCard("Don dat hom nay", "24"), 1, 0);
        grid.add(createStatCard("Doanh thu hom nay", "4.500.000"), 0, 1);
        grid.add(createStatCard("Khach hang", "135"), 1, 1);

        VBox section = new VBox(10);
        section.getStyleClass().add("card");

        Label lblSectionTitle = new Label("Hoat dong gan day");
        lblSectionTitle.getStyleClass().add("section-title");

        Label lblContent = new Label("""
                - Xem lich su dat san
                - Theo doi doanh thu
                - Kiem tra tinh trang san
                - Quan ly nhan vien va uu dai
                """);
        lblContent.getStyleClass().add("section-subtitle");

        section.getChildren().addAll(lblSectionTitle, lblContent);

        root.getChildren().addAll(lblTitle, grid, section);
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