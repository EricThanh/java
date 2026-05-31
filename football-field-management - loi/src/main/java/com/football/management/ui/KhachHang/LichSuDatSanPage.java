package com.football.management.ui.KhachHang;

import com.football.management.app.AppState;
import com.football.management.dao.KhachHangDatSanDAO;
import com.football.management.dao.KhachHangDatSanDAO.LichSuDatSanKHRow;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

public class LichSuDatSanPage {

    @SuppressWarnings("unchecked")
    public static Node createView() {
        VBox root = new VBox(16);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        // ===== TITLE =====
        Label lblTitle = new Label("Lịch sử đặt sân");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Xem lại các đơn đặt đã tạo");
        lblSubtitle.getStyleClass().add("section-subtitle");

        // ===== FILTER BAR =====
        HBox filterBar = new HBox(10);
        filterBar.setPadding(new Insets(0, 0, 4, 0));

        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tìm theo mã đơn hoặc tên sân...");
        txtTimKiem.getStyleClass().add("search-field");
        txtTimKiem.setPrefWidth(280);

        ComboBox<String> cbTrangThai = new ComboBox<>();
        cbTrangThai.getItems().addAll("Tất cả", "CHO_XU_LY", "DA_XAC_NHAN", "HOAN_THANH", "DA_HUY");
        cbTrangThai.setValue("Tất cả");

        Button btnLoc = new Button("Lọc");
        btnLoc.getStyleClass().add("primary-button");

        filterBar.getChildren().addAll(txtTimKiem, cbTrangThai, btnLoc);

        // ===== TABLE =====
        TableView<LichSuDatSanKHRow> table = new TableView<>();

        // ===== MÃ ĐƠN =====
        TableColumn<LichSuDatSanKHRow, String> colMa = new TableColumn<>("Mã đơn");
        colMa.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getMaDatSanCode()));

        // ===== TÊN SÂN =====
        TableColumn<LichSuDatSanKHRow, String> colSan = new TableColumn<>("Sân");
        colSan.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTenSan()));

        // ===== NGÀY =====
        TableColumn<LichSuDatSanKHRow, String> colNgay = new TableColumn<>("Ngày đặt");
        colNgay.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNgayDat()));

        // ===== KHUNG GIỜ =====
        TableColumn<LichSuDatSanKHRow, String> colGio = new TableColumn<>("Khung giờ");
        colGio.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getKhungGio()));

        // ===== TRẠNG THÁI =====
        TableColumn<LichSuDatSanKHRow, String> colTrangThai = new TableColumn<>("Trạng thái");
        colTrangThai.setCellValueFactory(d -> new SimpleStringProperty(chuyenTrangThai(d.getValue().getTrangThaiDatSan())));

        // ===== THANH TOÁN =====
        TableColumn<LichSuDatSanKHRow, String> colThanhToan = new TableColumn<>("Thanh toán");
        colThanhToan.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTongTienHienThi()));

        // ===== ACTION =====
        TableColumn<LichSuDatSanKHRow, Void> colHuy = new TableColumn<>("Hành động");
        colHuy.setCellFactory(col -> new TableCell<>() {
            private final Button btnHuy = new Button("Hủy đơn");

            {
                btnHuy.getStyleClass().add("danger-button");
                btnHuy.setOnAction(e -> {
                    LichSuDatSanKHRow row = getTableView().getItems().get(getIndex());

                    Alert confirm = new Alert(
                            Alert.AlertType.CONFIRMATION,
                            "Xác nhận hủy đơn " + row.getMaDatSanCode() + " ?",
                            ButtonType.YES,
                            ButtonType.NO
                    );
                    confirm.setTitle("Xác nhận hủy");

                    confirm.showAndWait().ifPresent(btn -> {
                        if (btn == ButtonType.YES) {
                            btnHuy.setDisable(true);
                            Thread t = new Thread(() -> {
                                try {
                                    KhachHangDatSanDAO dao = new KhachHangDatSanDAO();
                                    boolean ok = dao.huyDatSanCuaKhach(AppState.getMaKhachHang(), row.getMaDatSanCode());

                                    Platform.runLater(() -> {
                                        if (ok) {
                                            new Alert(Alert.AlertType.INFORMATION, "Hủy đơn thành công!").showAndWait();
                                            // ===== UPDATE STATUS =====
                                            row.setTrangThaiDatSan("DA_HUY");
                                            getTableView().refresh();
                                        } else {
                                            new Alert(Alert.AlertType.WARNING, "Không thể hủy đơn này.").showAndWait();
                                            btnHuy.setDisable(false);
                                        }
                                    });
                                } catch (Exception ex) {
                                    Platform.runLater(() -> {
                                        new Alert(Alert.AlertType.ERROR, "Lỗi:\n" + ex.getMessage()).showAndWait();
                                        btnHuy.setDisable(false);
                                    });
                                }
                            });
                            t.setDaemon(true);
                            t.start();
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }

                LichSuDatSanKHRow row = getTableView().getItems().get(getIndex());
                if (row.coTheHuy()) {
                    btnHuy.setDisable(false);
                    setGraphic(btnHuy);
                } else {
                    setGraphic(null);
                }
            }
        });

        // ===== ADD COLUMNS =====
        table.getColumns().addAll(colMa, colSan, colNgay, colGio, colTrangThai, colThanhToan, colHuy);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // ===== DATA =====
        ObservableList<LichSuDatSanKHRow> danhSachGoc = FXCollections.observableArrayList();
        ObservableList<LichSuDatSanKHRow> danhSachHienThi = FXCollections.observableArrayList();
        table.setItems(danhSachHienThi);

        // ===== FILTER =====
        Runnable applyFilter = () -> {
            String keyword = txtTimKiem.getText().trim().toLowerCase();
            String trangThai = cbTrangThai.getValue();

            List<LichSuDatSanKHRow> filtered = danhSachGoc.stream()
                    .filter(r -> keyword.isEmpty()
                            || r.getMaDatSanCode().toLowerCase().contains(keyword)
                            || r.getTenSan().toLowerCase().contains(keyword))
                    .filter(r -> "Tất cả".equals(trangThai)
                            || trangThai.equals(r.getTrangThaiDatSan()))
                    .collect(Collectors.toList());

            danhSachHienThi.setAll(filtered);
        };

        // ===== EVENTS =====
        btnLoc.setOnAction(e -> applyFilter.run());
        txtTimKiem.textProperty().addListener((obs, oldVal, newVal) -> applyFilter.run());
        cbTrangThai.setOnAction(e -> applyFilter.run());

        // ===== PLACEHOLDER =====
        Label lblTrong = new Label("Đang tải dữ liệu...");
        table.setPlaceholder(lblTrong);

        // ===== ADD UI =====
        root.getChildren().addAll(lblTitle, lblSubtitle, filterBar, table);

        // ===== LOAD DATA =====
        taiLaiDuLieuVao(danhSachGoc, danhSachHienThi, lblTrong);

        return root;
    }

    // ===== LOAD DATA =====
    private static void taiLaiDuLieuVao(
            ObservableList<LichSuDatSanKHRow> goc,
            ObservableList<LichSuDatSanKHRow> hienThi,
            Label placeholder) {

        Thread t = new Thread(() -> {
            try {
                KhachHangDatSanDAO dao = new KhachHangDatSanDAO();
                List<LichSuDatSanKHRow> ds = dao.layLichSuDatSan(AppState.getMaKhachHang(), null);

                Platform.runLater(() -> {
                    goc.setAll(ds);
                    hienThi.setAll(ds);
                    placeholder.setText(ds.isEmpty() ? "Chưa có đơn đặt sân nào." : "");
                });
            } catch (Exception ex) {
                Platform.runLater(() -> placeholder.setText("Lỗi tải dữ liệu: " + ex.getMessage()));
            }
        });
        t.setDaemon(true);
        t.start();
    }

    // ===== STATUS =====
    private static String chuyenTrangThai(String trangThai) {
        return switch (trangThai == null ? "" : trangThai) {
            case "CHO_XU_LY" -> "Chờ xử lý";
            case "DA_XAC_NHAN" -> "Đã xác nhận";
            case "HOAN_THANH" -> "Hoàn thành";
            case "DA_HUY" -> "Đã hủy";
            default -> trangThai;
        };
    }
}