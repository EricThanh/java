package com.football.management.ui.KhachHang;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DanhSachSanPage {

    public static Node createView() {
        VBox root = new VBox(16);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        Label lblTitle = new Label("Danh sach san");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Tim kiem va loc san theo nhu cau");
        lblSubtitle.getStyleClass().add("section-subtitle");

        HBox filterBar = new HBox(10);
        filterBar.getStyleClass().add("filter-bar");

        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tim theo ten san...");
        txtTimKiem.getStyleClass().add("search-field");
        txtTimKiem.setPrefWidth(250);

        ComboBox<String> cbLoaiSan = new ComboBox<>();
        cbLoaiSan.getItems().addAll("Tat ca", "San 5", "San 7", "San 11");
        cbLoaiSan.setPromptText("Loai san");

        ComboBox<String> cbMucGia = new ComboBox<>();
        cbMucGia.getItems().addAll("Tat ca", "Duoi 200000", "200000 - 500000", "Tren 500000");
        cbMucGia.setPromptText("Muc gia");

        Button btnLoc = new Button("Loc");
        btnLoc.getStyleClass().add("primary-button");

        filterBar.getChildren().addAll(txtTimKiem, cbLoaiSan, cbMucGia, btnLoc);

        TableView<SanRowKH> table = new TableView<>();

        TableColumn<SanRowKH, String> colMa = new TableColumn<>("Ma san");
        colMa.setCellValueFactory(data -> data.getValue().maSanProperty());

        TableColumn<SanRowKH, String> colTen = new TableColumn<>("Ten san");
        colTen.setCellValueFactory(data -> data.getValue().tenSanProperty());

        TableColumn<SanRowKH, String> colLoai = new TableColumn<>("Loai san");
        colLoai.setCellValueFactory(data -> data.getValue().loaiSanProperty());

        TableColumn<SanRowKH, String> colGia = new TableColumn<>("Gia moi gio");
        colGia.setCellValueFactory(data -> data.getValue().giaMoiGioProperty());

        TableColumn<SanRowKH, String> colTrangThai = new TableColumn<>("Trang thai");
        colTrangThai.setCellValueFactory(data -> data.getValue().trangThaiProperty());

        table.getColumns().addAll(colMa, colTen, colLoai, colGia, colTrangThai);
        table.setItems(FXCollections.observableArrayList(
                new SanRowKH("S01", "San A", "San 5", "150000", "SAN_SANG"),
                new SanRowKH("S02", "San B", "San 7", "250000", "SAN_SANG"),
                new SanRowKH("S03", "San C", "San 11", "500000", "DANG_SU_DUNG")
        ));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        root.getChildren().addAll(lblTitle, lblSubtitle, filterBar, table);
        return root;
    }
}