package com.football.management.ui.KhachHang;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DatSanPage {

    public static Node createView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        Label lblTitle = new Label("Dat san");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Chon san, thoi gian va xac nhan thong tin dat");
        lblSubtitle.getStyleClass().add("section-subtitle");

        VBox formCard = new VBox(18);
        formCard.getStyleClass().add("form-card");

        Label lblFormTitle = new Label("Thong tin dat san");
        lblFormTitle.getStyleClass().add("section-title");

        GridPane form = new GridPane();
        form.getStyleClass().add("form-grid");

        Label lblSan = new Label("San");
        lblSan.getStyleClass().add("form-label");
        ComboBox<String> cbSan = new ComboBox<>();
        cbSan.getItems().addAll("San A", "San B", "San C");
        cbSan.setPromptText("Chon san");

        Label lblNgay = new Label("Ngay dat");
        lblNgay.getStyleClass().add("form-label");
        DatePicker dpNgay = new DatePicker();

        Label lblGioBatDau = new Label("Gio bat dau");
        lblGioBatDau.getStyleClass().add("form-label");
        TextField txtGioBatDau = new TextField();
        txtGioBatDau.setPromptText("VD: 08:00");

        Label lblGioKetThuc = new Label("Gio ket thuc");
        lblGioKetThuc.getStyleClass().add("form-label");
        TextField txtGioKetThuc = new TextField();
        txtGioKetThuc.setPromptText("VD: 10:00");

        Label lblMaGiamGia = new Label("Ma giam gia");
        lblMaGiamGia.getStyleClass().add("form-label");
        TextField txtMaGiamGia = new TextField();
        txtMaGiamGia.setPromptText("Neu co");

        Label lblGhiChu = new Label("Ghi chu");
        lblGhiChu.getStyleClass().add("form-label");
        TextField txtGhiChu = new TextField();
        txtGhiChu.setPromptText("Nhap ghi chu");

        form.add(lblSan, 0, 0); form.add(cbSan, 1, 0);
        form.add(lblNgay, 0, 1); form.add(dpNgay, 1, 1);
        form.add(lblGioBatDau, 0, 2); form.add(txtGioBatDau, 1, 2);
        form.add(lblGioKetThuc, 0, 3); form.add(txtGioKetThuc, 1, 3);
        form.add(lblMaGiamGia, 0, 4); form.add(txtMaGiamGia, 1, 4);
        form.add(lblGhiChu, 0, 5); form.add(txtGhiChu, 1, 5);

        HBox actions = new HBox(10);

        Button btnTinhTien = new Button("Tinh tien");
        btnTinhTien.getStyleClass().add("light-button");

        Button btnXacNhan = new Button("Xac nhan dat san");
        btnXacNhan.getStyleClass().add("primary-button");

        actions.getChildren().addAll(btnTinhTien, btnXacNhan);

        formCard.getChildren().addAll(lblFormTitle, form, actions);

        root.getChildren().addAll(lblTitle, lblSubtitle, formCard);
        return root;
    }
}