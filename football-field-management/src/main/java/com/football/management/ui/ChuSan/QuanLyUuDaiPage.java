package com.football.management.ui.ChuSan;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class QuanLyUuDaiPage {

    public static Node createView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        Label lblTitle = new Label("Quan ly uu dai");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Tao moi, cap nhat va quan ly cac chuong trinh giam gia");
        lblSubtitle.getStyleClass().add("section-subtitle");

        VBox formCard = new VBox(18);
        formCard.getStyleClass().add("form-card");

        Label lblFormTitle = new Label("Thong tin uu dai");
        lblFormTitle.getStyleClass().add("section-title");

        GridPane form = new GridPane();
        form.getStyleClass().add("form-grid");

        Label lblMaGiamGia = new Label("Ma giam gia");
        lblMaGiamGia.getStyleClass().add("form-label");
        TextField txtMaGiamGia = new TextField();
        txtMaGiamGia.setPromptText("Nhap ma giam gia");

        Label lblTenUuDai = new Label("Ten uu dai");
        lblTenUuDai.getStyleClass().add("form-label");
        TextField txtTenUuDai = new TextField();
        txtTenUuDai.setPromptText("Nhap ten uu dai");

        Label lblLoaiGiam = new Label("Loai giam");
        lblLoaiGiam.getStyleClass().add("form-label");
        ComboBox<String> cbLoaiGiam = new ComboBox<>();
        cbLoaiGiam.setItems(FXCollections.observableArrayList("PHAN_TRAM", "SO_TIEN"));
        cbLoaiGiam.setPromptText("Chon loai giam");

        Label lblGiaTriGiam = new Label("Gia tri giam");
        lblGiaTriGiam.getStyleClass().add("form-label");
        TextField txtGiaTriGiam = new TextField();
        txtGiaTriGiam.setPromptText("Nhap gia tri giam");

        Label lblGiaTriToiThieu = new Label("Gia tri dat toi thieu");
        lblGiaTriToiThieu.getStyleClass().add("form-label");
        TextField txtGiaTriToiThieu = new TextField();
        txtGiaTriToiThieu.setPromptText("Nhap gia tri dat toi thieu");

        Label lblNgayBatDau = new Label("Ngay bat dau");
        lblNgayBatDau.getStyleClass().add("form-label");
        DatePicker dpNgayBatDau = new DatePicker();

        Label lblNgayKetThuc = new Label("Ngay ket thuc");
        lblNgayKetThuc.getStyleClass().add("form-label");
        DatePicker dpNgayKetThuc = new DatePicker();

        Label lblGioBatDau = new Label("Gio bat dau");
        lblGioBatDau.getStyleClass().add("form-label");
        TextField txtGioBatDau = new TextField();
        txtGioBatDau.setPromptText("VD: 08:00");

        Label lblGioKetThuc = new Label("Gio ket thuc");
        lblGioKetThuc.getStyleClass().add("form-label");
        TextField txtGioKetThuc = new TextField();
        txtGioKetThuc.setPromptText("VD: 22:00");

        form.add(lblMaGiamGia, 0, 0);
        form.add(txtMaGiamGia, 1, 0);

        form.add(lblTenUuDai, 0, 1);
        form.add(txtTenUuDai, 1, 1);

        form.add(lblLoaiGiam, 0, 2);
        form.add(cbLoaiGiam, 1, 2);

        form.add(lblGiaTriGiam, 0, 3);
        form.add(txtGiaTriGiam, 1, 3);

        form.add(lblGiaTriToiThieu, 0, 4);
        form.add(txtGiaTriToiThieu, 1, 4);

        form.add(lblNgayBatDau, 0, 5);
        form.add(dpNgayBatDau, 1, 5);

        form.add(lblNgayKetThuc, 0, 6);
        form.add(dpNgayKetThuc, 1, 6);

        form.add(lblGioBatDau, 0, 7);
        form.add(txtGioBatDau, 1, 7);

        form.add(lblGioKetThuc, 0, 8);
        form.add(txtGioKetThuc, 1, 8);

        Button btnThem = new Button("Them uu dai");
        btnThem.getStyleClass().add("primary-button");

        Button btnCapNhat = new Button("Cap nhat");
        btnCapNhat.getStyleClass().add("light-button");

        Button btnLamMoi = new Button("Lam moi");
        btnLamMoi.getStyleClass().add("secondary-button");

        HBox actionBox = new HBox(10, btnThem, btnCapNhat, btnLamMoi);

        formCard.getChildren().addAll(lblFormTitle, form, actionBox);

        VBox tableCard = new VBox(16);
        tableCard.getStyleClass().add("card");

        Label lblTableTitle = new Label("Danh sach uu dai");
        lblTableTitle.getStyleClass().add("section-title");

        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tim kiem theo ma hoac ten uu dai...");
        txtTimKiem.getStyleClass().add("search-field");
        txtTimKiem.setPrefWidth(320);

        TableView<UuDaiRow> table = new TableView<>();

        TableColumn<UuDaiRow, String> colMa = new TableColumn<>("Ma giam gia");
        colMa.setCellValueFactory(data -> data.getValue().maGiamGiaProperty());

        TableColumn<UuDaiRow, String> colTen = new TableColumn<>("Ten uu dai");
        colTen.setCellValueFactory(data -> data.getValue().tenUuDaiProperty());

        TableColumn<UuDaiRow, String> colLoai = new TableColumn<>("Loai giam");
        colLoai.setCellValueFactory(data -> data.getValue().loaiGiamProperty());

        TableColumn<UuDaiRow, String> colGiaTri = new TableColumn<>("Gia tri giam");
        colGiaTri.setCellValueFactory(data -> data.getValue().giaTriGiamProperty());

        TableColumn<UuDaiRow, String> colThoiGian = new TableColumn<>("Thoi gian");
        colThoiGian.setCellValueFactory(data -> data.getValue().thoiGianApDungProperty());

        table.getColumns().addAll(colMa, colTen, colLoai, colGiaTri, colThoiGian);
        table.setItems(FXCollections.observableArrayList(
                new UuDaiRow("SALE10", "Giam 10 phan tram", "PHAN_TRAM", "10", "01/05 - 31/05"),
                new UuDaiRow("GIAM50K", "Giam 50 nghin", "SO_TIEN", "50000", "01/06 - 15/06"),
                new UuDaiRow("VIP20", "Uu dai VIP", "PHAN_TRAM", "20", "10/06 - 30/06")
        ));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        tableCard.getChildren().addAll(lblTableTitle, txtTimKiem, table);

        root.getChildren().addAll(lblTitle, lblSubtitle, formCard, tableCard);
        return root;
    }
}