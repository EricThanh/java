package com.football.management.ui.NhanVien;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ThanhToanPage {

    public static Node createView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        Label lblTitle = new Label("Thanh toan");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Tinh tien, ap dung uu dai va ghi nhan thanh toan");
        lblSubtitle.getStyleClass().add("section-subtitle");

        VBox formCard = new VBox(18);
        formCard.getStyleClass().add("form-card");

        Label lblFormTitle = new Label("Thong tin thanh toan");
        lblFormTitle.getStyleClass().add("section-title");

        GridPane form = new GridPane();
        form.getStyleClass().add("form-grid");

        Label lblMaDon = new Label("Ma dat san");
        lblMaDon.getStyleClass().add("form-label");
        TextField txtMaDon = new TextField();
        txtMaDon.setPromptText("Nhap ma dat san");

        Label lblTongTien = new Label("Tong tien goc");
        lblTongTien.getStyleClass().add("form-label");
        TextField txtTongTien = new TextField();
        txtTongTien.setPromptText("Nhap tong tien");

        Label lblUuDai = new Label("Uu dai");
        lblUuDai.getStyleClass().add("form-label");
        TextField txtUuDai = new TextField();
        txtUuDai.setPromptText("Nhap ma uu dai");

        Label lblThanhTien = new Label("Thanh tien");
        lblThanhTien.getStyleClass().add("form-label");
        TextField txtThanhTien = new TextField();
        txtThanhTien.setPromptText("So tien can thanh toan");

        Label lblHinhThuc = new Label("Hinh thuc thanh toan");
        lblHinhThuc.getStyleClass().add("form-label");
        ComboBox<String> cbHinhThuc = new ComboBox<>();
        cbHinhThuc.getItems().addAll("TIEN_MAT", "CHUYEN_KHOAN", "TRUC_TUYEN");
        cbHinhThuc.setPromptText("Chon hinh thuc");

        form.add(lblMaDon, 0, 0); form.add(txtMaDon, 1, 0);
        form.add(lblTongTien, 0, 1); form.add(txtTongTien, 1, 1);
        form.add(lblUuDai, 0, 2); form.add(txtUuDai, 1, 2);
        form.add(lblThanhTien, 0, 3); form.add(txtThanhTien, 1, 3);
        form.add(lblHinhThuc, 0, 4); form.add(cbHinhThuc, 1, 4);

        HBox actions = new HBox(10);

        Button btnTinhTien = new Button("Tinh tien");
        btnTinhTien.getStyleClass().add("light-button");

        Button btnThanhToan = new Button("Xac nhan thanh toan");
        btnThanhToan.getStyleClass().add("primary-button");

        Button btnHoaDon = new Button("Xuat hoa don");
        btnHoaDon.getStyleClass().add("secondary-button");

        actions.getChildren().addAll(btnTinhTien, btnThanhToan, btnHoaDon);

        formCard.getChildren().addAll(lblFormTitle, form, actions);

        VBox noteCard = new VBox(10);
        noteCard.getStyleClass().add("card");

        Label lblNoteTitle = new Label("Huong dan");
        lblNoteTitle.getStyleClass().add("section-title");

        Label lblNote = new Label("""
                - Kiem tra thong tin dat san truoc khi thanh toan
                - Co the ap dung uu dai neu hop le
                - Sau khi thanh toan thanh cong co the xuat hoa don
                """);
        lblNote.getStyleClass().add("section-subtitle");

        noteCard.getChildren().addAll(lblNoteTitle, lblNote);

        root.getChildren().addAll(lblTitle, lblSubtitle, formCard, noteCard);
        return root;
    }
}