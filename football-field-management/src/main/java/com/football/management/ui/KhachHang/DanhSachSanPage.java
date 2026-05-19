package com.football.management.ui.KhachHang;

import com.football.management.dao.KhachHangDatSanDAO;
import com.football.management.dao.KhachHangDatSanDAO.SanKhachHangRow;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DanhSachSanPage {

    /**
     * Callback khi chọn sân
     */
    @SuppressWarnings("unchecked")
    public static Node createView(Consumer<SanKhachHangRow> onChonSan) {
        VBox root = new VBox(16);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        // ===== TITLE =====
        Label lblTitle = new Label("Danh sách sân");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Tìm kiếm và lọc sân theo nhu cầu");
        lblSubtitle.getStyleClass().add("section-subtitle");

        // ===== FILTER BAR =====
        HBox filterBar = new HBox(10);
        filterBar.getStyleClass().add("filter-bar");

        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tìm theo tên sân...");
        txtTimKiem.setPrefWidth(250);

        ComboBox<String> cbLoaiSan = new ComboBox<>();
        cbLoaiSan.getItems().addAll("Tất cả", "Sân 5", "Sân 7", "Sân 11");
        cbLoaiSan.setValue("Tất cả");

        ComboBox<String> cbMucGia = new ComboBox<>();
        cbMucGia.getItems().addAll("Tất cả", "Dưới 200,000", "200,000 – 500,000", "Trên 500,000");
        cbMucGia.setValue("Tất cả");

        Button btnLoc = new Button("Lọc");
        btnLoc.getStyleClass().add("primary-button");

        filterBar.getChildren().addAll(txtTimKiem, cbLoaiSan, cbMucGia, btnLoc);

        // ===== TABLE =====
        TableView<SanKhachHangRow> table = new TableView<>();

        // Mã sân
        TableColumn<SanKhachHangRow, String> colMa = new TableColumn<>("Mã sân");
        colMa.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getMaSanCode()));

        // Tên sân
        TableColumn<SanKhachHangRow, String> colTen = new TableColumn<>("Tên sân");
        colTen.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTenSan()));

        // Loại sân
        TableColumn<SanKhachHangRow, String> colLoai = new TableColumn<>("Loại sân");
        colLoai.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTenLoaiSan()));

        // Giá
        TableColumn<SanKhachHangRow, String> colGia = new TableColumn<>("Giá/giờ");
        colGia.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getGiaHienThi()));

        // Trạng thái
        TableColumn<SanKhachHangRow, String> colTrangThai = new TableColumn<>("Trạng thái");
        colTrangThai.setCellValueFactory(d -> new SimpleStringProperty(chuyenTrangThai(d.getValue().getTrangThaiSan())));

        // ===== ACTION =====
        TableColumn<SanKhachHangRow, Void> colAction = new TableColumn<>("Chi tiết");
        colAction.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Xem chi tiết");

            {
                btn.getStyleClass().add("light-button");
                btn.setOnAction(e -> {
                    SanKhachHangRow row = getTableView().getItems().get(getIndex());
                    if (onChonSan != null) {
                        onChonSan.accept(row);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        table.getColumns().addAll(colMa, colTen, colLoai, colGia, colTrangThai, colAction);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // ===== DOUBLE CLICK ROW =====
        table.setRowFactory(tv -> {
            TableRow<SanKhachHangRow> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    SanKhachHangRow san = row.getItem();
                    if (onChonSan != null) {
                        onChonSan.accept(san);
                    }
                }
            });
            return row;
        });

        // ===== DATA =====
        ObservableList<SanKhachHangRow> danhSachGoc = FXCollections.observableArrayList();
        ObservableList<SanKhachHangRow> danhSachHienThi = FXCollections.observableArrayList();
        table.setItems(danhSachHienThi);

        // ===== FILTER LOGIC =====
        Runnable applyFilter = () -> {
            String keyword = txtTimKiem.getText().trim().toLowerCase();
            String loai = cbLoaiSan.getValue();
            String mucGia = cbMucGia.getValue();

            List<SanKhachHangRow> filtered = danhSachGoc.stream()
                    .filter(s -> keyword.isEmpty() 
                            || s.getTenSan().toLowerCase().contains(keyword) 
                            || s.getMaSanCode().toLowerCase().contains(keyword))
                    .filter(s -> "Tất cả".equals(loai) 
                            || s.getTenLoaiSan().equalsIgnoreCase(loai))
                    .filter(s -> {
                        if ("Tất cả".equals(mucGia)) {
                            return true;
                        }

                        BigDecimal gia = s.getGiaMoiGioHienTai();
                        if (gia == null) {
                            return false;
                        }

                        return switch (mucGia) {
                            case "Dưới 200,000" -> gia.compareTo(BigDecimal.valueOf(200000)) < 0;
                            case "200,000 – 500,000" -> gia.compareTo(BigDecimal.valueOf(200000)) >= 0 
                                    && gia.compareTo(BigDecimal.valueOf(500000)) <= 0;
                            case "Trên 500,000" -> gia.compareTo(BigDecimal.valueOf(500000)) > 0;
                            default -> true;
                        };
                    })
                    .collect(Collectors.toList());

            danhSachHienThi.setAll(filtered);
        };

        // ===== EVENTS =====
        btnLoc.setOnAction(e -> applyFilter.run());
        txtTimKiem.textProperty().addListener((obs, oldVal, newVal) -> applyFilter.run());
        cbLoaiSan.setOnAction(e -> applyFilter.run());
        cbMucGia.setOnAction(e -> applyFilter.run());

        // ===== PLACEHOLDER =====
        Label lblTrong = new Label("Đang tải dữ liệu...");
        table.setPlaceholder(lblTrong);

        // ===== ADD UI =====
        root.getChildren().addAll(lblTitle, lblSubtitle, filterBar, table);

        // ===== LOAD DATA =====
        Thread t = new Thread(() -> {
            try {
                KhachHangDatSanDAO dao = new KhachHangDatSanDAO();
                List<SanKhachHangRow> ds = dao.layDanhSachSanChoKhachHang();

                Platform.runLater(() -> {
                    danhSachGoc.setAll(ds);
                    danhSachHienThi.setAll(ds);
                    lblTrong.setText(ds.isEmpty() ? "Không có sân nào." : "");
                });
            } catch (Exception ex) {
                Platform.runLater(() -> lblTrong.setText("Lỗi tải dữ liệu: " + ex.getMessage()));
            }
        });
        t.setDaemon(true);
        t.start();

        return root;
    }

    private static String chuyenTrangThai(String trangThai) {
        return switch (trangThai == null ? "" : trangThai) {
            case "SAN_SANG" -> "Sẵn sàng";
            case "DANG_SU_DUNG" -> "Đang sử dụng";
            case "BAO_TRI" -> "Bảo trì";
            default -> trangThai;
        };
    }
}