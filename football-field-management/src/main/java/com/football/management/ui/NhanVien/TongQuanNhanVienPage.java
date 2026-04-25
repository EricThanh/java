package com.football.management.ui.NhanVien;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class TongQuanNhanVienPage {

    public static Node createView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        Label lblTitle = new Label("Tong quan nhan vien");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Theo doi lich san, don dat va thanh toan trong ngay");
        lblSubtitle.getStyleClass().add("section-subtitle");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);

        grid.add(createStatCard("Don dat hom nay", "18"), 0, 0);
        grid.add(createStatCard("Khach cho check-in", "5"), 1, 0);
        grid.add(createStatCard("Don chua thanh toan", "7"), 0, 1);
        grid.add(createStatCard("San dang su dung", "4"), 1, 1);

        VBox section = new VBox(10);
        section.getStyleClass().add("card");

        Label lblSectionTitle = new Label("Cong viec can xu ly");
        lblSectionTitle.getStyleClass().add("section-title");

        Label lblContent = new Label("""
                - Xac nhan khach den
                - Cap nhat trang thai san
                - Ghi nhan thanh toan
                - Ho tro dat san tai quay
                """);
        lblContent.getStyleClass().add("section-subtitle");

        section.getChildren().addAll(lblSectionTitle, lblContent);

        root.getChildren().addAll(lblTitle, lblSubtitle, grid, section);
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