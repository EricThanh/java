package com.football.management.ui.NhanVien;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class QuanLyKhachHangPage {

    public static Node createView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        Label lblTitle = new Label("Quan ly khach hang");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Them moi, cap nhat va theo doi thong tin khach hang");
        lblSubtitle.getStyleClass().add("section-subtitle");

        VBox formCard = new VBox(18);
        formCard.getStyleClass().add("form-card");

        Label lblFormTitle = new Label("Thong tin khach hang");
        lblFormTitle.getStyleClass().add("section-title");

        GridPane form = new GridPane();
        form.getStyleClass().add("form-grid");

        Label lblMa = new Label("Ma khach hang");
        lblMa.getStyleClass().add("form-label");
        TextField txtMa = new TextField();
        txtMa.setPromptText("Nhap ma khach hang");

        Label lblTen = new Label("Ho ten");
        lblTen.getStyleClass().add("form-label");
        TextField txtTen = new TextField();
        txtTen.setPromptText("Nhap ho ten");

        Label lblSdt = new Label("So dien thoai");
        lblSdt.getStyleClass().add("form-label");
        TextField txtSdt = new TextField();
        txtSdt.setPromptText("Nhap so dien thoai");

        Label lblEmail = new Label("Email");
        lblEmail.getStyleClass().add("form-label");
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Nhap email");

        Label lblDiaChi = new Label("Dia chi");
        lblDiaChi.getStyleClass().add("form-label");
        TextField txtDiaChi = new TextField();
        txtDiaChi.setPromptText("Nhap dia chi");

        form.add(lblMa, 0, 0); form.add(txtMa, 1, 0);
        form.add(lblTen, 0, 1); form.add(txtTen, 1, 1);
        form.add(lblSdt, 0, 2); form.add(txtSdt, 1, 2);
        form.add(lblEmail, 0, 3); form.add(txtEmail, 1, 3);
        form.add(lblDiaChi, 0, 4); form.add(txtDiaChi, 1, 4);

        HBox actions = new HBox(10);
        Button btnThem = new Button("Them khach hang");
        btnThem.getStyleClass().add("primary-button");

        Button btnCapNhat = new Button("Cap nhat");
        btnCapNhat.getStyleClass().add("light-button");

        Button btnXoa = new Button("Xoa");
        btnXoa.getStyleClass().add("secondary-button");

        actions.getChildren().addAll(btnThem, btnCapNhat, btnXoa);

        formCard.getChildren().addAll(lblFormTitle, form, actions);

        VBox tableCard = new VBox(16);
        tableCard.getStyleClass().add("card");

        Label lblTableTitle = new Label("Danh sach khach hang");
        lblTableTitle.getStyleClass().add("section-title");

        TextField txtTim = new TextField();
        txtTim.setPromptText("Tim kiem khach hang...");
        txtTim.getStyleClass().add("search-field");
        txtTim.setPrefWidth(320);

        TableView<KhachHangRow> table = new TableView<>();

        TableColumn<KhachHangRow, String> colMa = new TableColumn<>("Ma KH");
        colMa.setCellValueFactory(data -> data.getValue().maKhachHangProperty());

        TableColumn<KhachHangRow, String> colTen = new TableColumn<>("Ho ten");
        colTen.setCellValueFactory(data -> data.getValue().hoTenProperty());

        TableColumn<KhachHangRow, String> colSdt = new TableColumn<>("So dien thoai");
        colSdt.setCellValueFactory(data -> data.getValue().soDienThoaiProperty());

        TableColumn<KhachHangRow, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(data -> data.getValue().emailProperty());

        table.getColumns().addAll(colMa, colTen, colSdt, colEmail);
        table.setItems(FXCollections.observableArrayList(
                new KhachHangRow("KH01", "Nguyen Van A", "0912345678", "a@gmail.com"),
                new KhachHangRow("KH02", "Tran Thi B", "0988123456", "b@gmail.com"),
                new KhachHangRow("KH03", "Le Van C", "0909666888", "c@gmail.com")
        ));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        tableCard.getChildren().addAll(lblTableTitle, txtTim, table);

        root.getChildren().addAll(lblTitle, lblSubtitle, formCard, tableCard);
        return root;
    }
}