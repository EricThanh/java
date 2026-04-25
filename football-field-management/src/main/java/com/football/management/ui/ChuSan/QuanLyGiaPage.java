package com.football.management.ui.ChuSan;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class QuanLyGiaPage {

    public static Node createView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        Label lblTitle = new Label("Quan ly gia");
        lblTitle.getStyleClass().add("page-title");

        VBox card = new VBox(18);
        card.getStyleClass().add("form-card");

        GridPane form = new GridPane();
        form.getStyleClass().add("form-grid");

        TextField txtTenBangGia = new TextField();
        ComboBox<String> cbSan = new ComboBox<>();
        cbSan.getItems().addAll("San A", "San B", "San C");

        TextField txtGioBatDau = new TextField();
        TextField txtGioKetThuc = new TextField();
        TextField txtGiaMoiGio = new TextField();

        DatePicker dpTuNgay = new DatePicker();
        DatePicker dpDenNgay = new DatePicker();

        Label l1 = new Label("Ten bang gia");
        l1.getStyleClass().add("form-label");
        Label l2 = new Label("San");
        l2.getStyleClass().add("form-label");
        Label l3 = new Label("Gio bat dau");
        l3.getStyleClass().add("form-label");
        Label l4 = new Label("Gio ket thuc");
        l4.getStyleClass().add("form-label");
        Label l5 = new Label("Gia moi gio");
        l5.getStyleClass().add("form-label");
        Label l6 = new Label("Tu ngay");
        l6.getStyleClass().add("form-label");
        Label l7 = new Label("Den ngay");
        l7.getStyleClass().add("form-label");

        form.add(l1, 0, 0); form.add(txtTenBangGia, 1, 0);
        form.add(l2, 0, 1); form.add(cbSan, 1, 1);
        form.add(l3, 0, 2); form.add(txtGioBatDau, 1, 2);
        form.add(l4, 0, 3); form.add(txtGioKetThuc, 1, 3);
        form.add(l5, 0, 4); form.add(txtGiaMoiGio, 1, 4);
        form.add(l6, 0, 5); form.add(dpTuNgay, 1, 5);
        form.add(l7, 0, 6); form.add(dpDenNgay, 1, 6);

        Button btnLuu = new Button("Luu bang gia");
        btnLuu.getStyleClass().add("primary-button");

        card.getChildren().addAll(form, btnLuu);
        root.getChildren().addAll(lblTitle, card);
        return root;
    }
}