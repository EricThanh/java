package com.football.management.ui.ChuSan;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class QuanLySanPage {

    public static Node createView() {
        VBox root = new VBox(16);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        Label lblTitle = new Label("Quan ly san");
        lblTitle.getStyleClass().add("page-title");

        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tim kiem san...");
        txtTimKiem.getStyleClass().add("search-field");
        txtTimKiem.setPrefWidth(260);

        Button btnThem = new Button("Them");
        btnThem.getStyleClass().add("primary-button");

        Button btnSua = new Button("Sua");
        btnSua.getStyleClass().add("light-button");

        Button btnXoa = new Button("Xoa");
        btnXoa.getStyleClass().add("secondary-button");

        HBox actionBar = new HBox(10, txtTimKiem, btnThem, btnSua, btnXoa);
        actionBar.getStyleClass().add("filter-bar");

        TableView<SanRow> table = new TableView<>();

        TableColumn<SanRow, String> colMa = new TableColumn<>("Ma san");
        colMa.setCellValueFactory(data -> data.getValue().maSanProperty());

        TableColumn<SanRow, String> colTen = new TableColumn<>("Ten san");
        colTen.setCellValueFactory(data -> data.getValue().tenSanProperty());

        TableColumn<SanRow, String> colLoai = new TableColumn<>("Loai san");
        colLoai.setCellValueFactory(data -> data.getValue().loaiSanProperty());

        TableColumn<SanRow, String> colTrangThai = new TableColumn<>("Trang thai");
        colTrangThai.setCellValueFactory(data -> data.getValue().trangThaiProperty());

        table.getColumns().addAll(colMa, colTen, colLoai, colTrangThai);
        table.setItems(FXCollections.observableArrayList(
                new SanRow("S01", "San A", "San 5", "SAN_SANG"),
                new SanRow("S02", "San B", "San 7", "DANG_SU_DUNG"),
                new SanRow("S03", "San C", "San 11", "BAO_TRI")
        ));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        root.getChildren().addAll(lblTitle, actionBar, table);
        return root;
    }
}