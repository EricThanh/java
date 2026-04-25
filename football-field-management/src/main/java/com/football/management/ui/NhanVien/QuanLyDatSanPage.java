package com.football.management.ui.NhanVien;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class QuanLyDatSanPage {

    public static Node createView() {
        VBox root = new VBox(16);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        Label lblTitle = new Label("Quan ly dat san");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Tao moi, cap nhat, huy don va xac nhan khach den");
        lblSubtitle.getStyleClass().add("section-subtitle");

        HBox filterBar = new HBox(10);
        filterBar.getStyleClass().add("filter-bar");

        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tim theo ma don, ten khach hang...");
        txtTimKiem.getStyleClass().add("search-field");
        txtTimKiem.setPrefWidth(300);

        Button btnTaoMoi = new Button("Tao don");
        btnTaoMoi.getStyleClass().add("primary-button");

        Button btnCapNhat = new Button("Cap nhat");
        btnCapNhat.getStyleClass().add("light-button");

        Button btnHuy = new Button("Huy don");
        btnHuy.getStyleClass().add("secondary-button");

        Button btnCheckIn = new Button("Xac nhan den");
        btnCheckIn.getStyleClass().add("primary-button");

        filterBar.getChildren().addAll(txtTimKiem, btnTaoMoi, btnCapNhat, btnHuy, btnCheckIn);

        TableView<DatSanRow> table = new TableView<>();

        TableColumn<DatSanRow, String> colMa = new TableColumn<>("Ma don");
        colMa.setCellValueFactory(data -> data.getValue().maDatSanProperty());

        TableColumn<DatSanRow, String> colKhach = new TableColumn<>("Khach hang");
        colKhach.setCellValueFactory(data -> data.getValue().tenKhachHangProperty());

        TableColumn<DatSanRow, String> colSan = new TableColumn<>("San");
        colSan.setCellValueFactory(data -> data.getValue().tenSanProperty());

        TableColumn<DatSanRow, String> colThoiGian = new TableColumn<>("Thoi gian");
        colThoiGian.setCellValueFactory(data -> data.getValue().thoiGianProperty());

        TableColumn<DatSanRow, String> colTrangThai = new TableColumn<>("Trang thai");
        colTrangThai.setCellValueFactory(data -> data.getValue().trangThaiProperty());

        table.getColumns().addAll(colMa, colKhach, colSan, colThoiGian, colTrangThai);
        table.setItems(FXCollections.observableArrayList(
                new DatSanRow("DS001", "Nguyen Van A", "San A", "08:00 - 10:00", "DA_XAC_NHAN"),
                new DatSanRow("DS002", "Tran Thi B", "San B", "10:00 - 12:00", "CHO_XAC_NHAN"),
                new DatSanRow("DS003", "Le Van C", "San C", "14:00 - 16:00", "DA_CHECK_IN")
        ));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        root.getChildren().addAll(lblTitle, lblSubtitle, filterBar, table);
        return root;
    }
}