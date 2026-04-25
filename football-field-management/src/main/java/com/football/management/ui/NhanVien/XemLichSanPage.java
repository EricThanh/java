package com.football.management.ui.NhanVien;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class XemLichSanPage {

    public static Node createView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        Label lblTitle = new Label("Xem lich san");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Theo doi lich su dung san theo ngay va theo san");
        lblSubtitle.getStyleClass().add("section-subtitle");

        HBox filterBar = new HBox(10);
        filterBar.getStyleClass().add("filter-bar");

        DatePicker dpNgay = new DatePicker();

        ComboBox<String> cbSan = new ComboBox<>();
        cbSan.getItems().addAll("Tat ca san", "San A", "San B", "San C");
        cbSan.setPromptText("Chon san");

        filterBar.getChildren().addAll(dpNgay, cbSan);

        VBox lichCard = new VBox(10);
        lichCard.getStyleClass().add("card");

        Label lblLich = new Label("Lich san trong ngay");
        lblLich.getStyleClass().add("section-title");

        Label lblNoiDung = new Label("""
                08:00 - 10:00 | San A | Da dat
                10:00 - 12:00 | San B | Trong
                14:00 - 16:00 | San C | Dang su dung
                16:00 - 18:00 | San A | Da dat
                """);
        lblNoiDung.getStyleClass().add("section-subtitle");

        lichCard.getChildren().addAll(lblLich, lblNoiDung);

        root.getChildren().addAll(lblTitle, lblSubtitle, filterBar, lichCard);
        return root;
    }
}