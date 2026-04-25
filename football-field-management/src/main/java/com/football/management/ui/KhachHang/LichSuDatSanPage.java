package com.football.management.ui.KhachHang;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LichSuDatSanPage {

    public static Node createView() {
        VBox root = new VBox(16);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        Label lblTitle = new Label("Lich su dat san");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Xem lai cac don dat da tao");
        lblSubtitle.getStyleClass().add("section-subtitle");

        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tim theo ma dat san...");
        txtTimKiem.getStyleClass().add("search-field");
        txtTimKiem.setPrefWidth(280);

        TableView<LichSuDatSanRow> table = new TableView<>();

        TableColumn<LichSuDatSanRow, String> colMa = new TableColumn<>("Ma don");
        colMa.setCellValueFactory(data -> data.getValue().maDatSanProperty());

        TableColumn<LichSuDatSanRow, String> colSan = new TableColumn<>("San");
        colSan.setCellValueFactory(data -> data.getValue().tenSanProperty());

        TableColumn<LichSuDatSanRow, String> colNgay = new TableColumn<>("Ngay dat");
        colNgay.setCellValueFactory(data -> data.getValue().ngayDatProperty());

        TableColumn<LichSuDatSanRow, String> colGio = new TableColumn<>("Khung gio");
        colGio.setCellValueFactory(data -> data.getValue().khungGioProperty());

        TableColumn<LichSuDatSanRow, String> colTrangThai = new TableColumn<>("Trang thai");
        colTrangThai.setCellValueFactory(data -> data.getValue().trangThaiProperty());

        table.getColumns().addAll(colMa, colSan, colNgay, colGio, colTrangThai);
        table.setItems(FXCollections.observableArrayList(
                new LichSuDatSanRow("DS001", "San A", "10/06/2026", "08:00 - 10:00", "DA_XAC_NHAN"),
                new LichSuDatSanRow("DS002", "San B", "12/06/2026", "10:00 - 12:00", "HOAN_THANH"),
                new LichSuDatSanRow("DS003", "San C", "14/06/2026", "14:00 - 16:00", "DA_HUY")
        ));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        root.getChildren().addAll(lblTitle, lblSubtitle, txtTimKiem, table);
        return root;
    }
}