package com.football.management.ui.KhachHang;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ChiTietSanPage {

    public static Node createView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        Label lblTitle = new Label("Chi tiet san");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Thong tin chi tiet cua san duoc chon");
        lblSubtitle.getStyleClass().add("section-subtitle");

        VBox card = new VBox(12);
        card.getStyleClass().add("card");

        Label lblTenSan = new Label("San A");
        lblTenSan.getStyleClass().add("section-title");

        Label lblNoiDung = new Label("""
                Loai san: San 5
                Gia moi gio: 150000
                Vi tri: Khu A
                Trang thai: SAN_SANG
                Mo cua: 06:00 - 22:00
                """);
        lblNoiDung.getStyleClass().add("section-subtitle");

        HBox actions = new HBox(10);

        Button btnDatSan = new Button("Dat san");
        btnDatSan.getStyleClass().add("primary-button");

        Button btnYeuThich = new Button("Them yeu thich");
        btnYeuThich.getStyleClass().add("light-button");

        actions.getChildren().addAll(btnDatSan, btnYeuThich);

        card.getChildren().addAll(lblTenSan, lblNoiDung, actions);

        root.getChildren().addAll(lblTitle, lblSubtitle, card);
        return root;
    }
}