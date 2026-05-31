package com.football.management.ui.KhachHang;

import com.football.management.dao.KhachHangDatSanDAO;
import com.football.management.dao.KhachHangDatSanDAO.SanKhachHangRow;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DanhSachSanPage {

    @FunctionalInterface
    public interface ChonSanCallback {
        void accept(SanKhachHangRow san, LocalDate ngayDat, String gioBD, String gioKT);
    }

    /**
     * Callback khi chọn sân.
     */
    @SuppressWarnings("unchecked")
    public static Node createView(ChonSanCallback onChonSan) {
        VBox root = new VBox(16);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        // ===== TITLE =====
        Label lblTitle = new Label("Danh sách sân");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Tìm kiếm và lọc sân theo nhu cầu");
        lblSubtitle.getStyleClass().add("section-subtitle");

        // ===== FILTER BAR =====
        // Chia bộ lọc thành 2 hàng để không bị bóp chữ thành "Tất...", "18...", "..."
        VBox filterBar = new VBox(12);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.getStyleClass().add("filter-bar");

        HBox filterRow1 = new HBox(14);
        filterRow1.setAlignment(Pos.CENTER_LEFT);

        HBox filterRow2 = new HBox(14);
        filterRow2.setAlignment(Pos.CENTER_LEFT);

        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tìm theo tên sân...");

        ComboBox<String> cbLoaiSan = new ComboBox<>();
        cbLoaiSan.getItems().addAll("Tất cả", "Sân 5", "Sân 7", "Sân 11");
        cbLoaiSan.setValue("Tất cả");

        ComboBox<String> cbMucGia = new ComboBox<>();
        cbMucGia.getItems().addAll("Tất cả", "Dưới 200,000", "200,000 – 500,000", "Trên 500,000");
        cbMucGia.setValue("Tất cả");

        // Ngày khách muốn đặt sân
        DatePicker dpNgayDat = new DatePicker(LocalDate.now());
        dpNgayDat.setPromptText("Ngày đặt");

        // Giờ bắt đầu khách muốn đặt
        ComboBox<String> cbGioBatDau = new ComboBox<>();
        cbGioBatDau.getItems().addAll(
                "06:00", "07:00", "08:00", "09:00", "10:00",
                "11:00", "12:00", "13:00", "14:00", "15:00",
                "16:00", "17:00", "18:00", "19:00", "20:00",
                "21:00", "22:00"
        );
        cbGioBatDau.setValue("18:00");

        // Giờ kết thúc khách muốn đặt
        ComboBox<String> cbGioKetThuc = new ComboBox<>();
        cbGioKetThuc.getItems().addAll(
                "07:00", "08:00", "09:00", "10:00", "11:00",
                "12:00", "13:00", "14:00", "15:00", "16:00",
                "17:00", "18:00", "19:00", "20:00", "21:00",
                "22:00", "23:00"
        );
        cbGioKetThuc.setValue("19:00");

        Button btnLoc = new Button("Lọc sân trống");
        btnLoc.getStyleClass().add("primary-button");

        // Hàng 1: tìm kiếm, loại sân, mức giá
        filterRow1.getChildren().addAll(
                taoOFilter("Tên sân", txtTimKiem, 360),
                taoOFilter("Loại sân", cbLoaiSan, 180),
                taoOFilter("Mức giá", cbMucGia, 220)
        );

        // Hàng 2: ngày, giờ bắt đầu, giờ kết thúc, nút lọc
        filterRow2.getChildren().addAll(
                taoOFilter("Ngày đặt", dpNgayDat, 180),
                taoOFilter("Giờ bắt đầu", cbGioBatDau, 160),
                taoOFilter("Giờ kết thúc", cbGioKetThuc, 160),
                taoNutFilter(btnLoc, 150)
        );

        filterBar.getChildren().addAll(filterRow1, filterRow2);

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

        // Giờ khách chọn
        TableColumn<SanKhachHangRow, String> colGio = new TableColumn<>("Giờ");
        colGio.setCellValueFactory(d -> {
            String gioBD = cbGioBatDau.getValue();
            String gioKT = cbGioKetThuc.getValue();

            if (gioBD == null || gioKT == null) {
                return new SimpleStringProperty("");
            }

            return new SimpleStringProperty(gioBD + " - " + gioKT);
        });

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
                        onChonSan.accept(
                                row,
                                dpNgayDat.getValue(),
                                cbGioBatDau.getValue(),
                                cbGioKetThuc.getValue()
                        );
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        table.getColumns().addAll(colMa, colTen, colLoai, colGio, colGia, colTrangThai, colAction);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // ===== DOUBLE CLICK ROW =====
        table.setRowFactory(tv -> {
            TableRow<SanKhachHangRow> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    SanKhachHangRow san = row.getItem();
                    if (onChonSan != null) {
                        onChonSan.accept(
                                san,
                                dpNgayDat.getValue(),
                                cbGioBatDau.getValue(),
                                cbGioKetThuc.getValue()
                        );
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

        // ===== PLACEHOLDER =====
        Label lblTrong = new Label("Chọn ngày, giờ rồi bấm Lọc để tìm sân trống.");
        table.setPlaceholder(lblTrong);

        // ===== HÀM LOAD SÂN TRỐNG THEO NGÀY + GIỜ =====
        Runnable loadSanTrongTheoNgayGio = () -> {
            LocalDate ngayDat = dpNgayDat.getValue();
            String gioBD = cbGioBatDau.getValue();
            String gioKT = cbGioKetThuc.getValue();

            // Kiểm tra ngày đặt
            if (ngayDat == null) {
                thongBao("Vui lòng chọn ngày đặt sân.");
                return;
            }

            // Kiểm tra giờ bắt đầu / giờ kết thúc
            if (gioBD == null || gioKT == null) {
                thongBao("Vui lòng chọn đầy đủ giờ bắt đầu và giờ kết thúc.");
                return;
            }

            // Giờ bắt đầu phải nhỏ hơn giờ kết thúc
            if (!LocalTime.parse(gioBD).isBefore(LocalTime.parse(gioKT))) {
                thongBao("Giờ bắt đầu phải nhỏ hơn giờ kết thúc.");
                return;
            }

            lblTrong.setText("Đang tải dữ liệu...");
            btnLoc.setDisable(true);

            Thread t = new Thread(() -> {
                try {
                    KhachHangDatSanDAO dao = new KhachHangDatSanDAO();

                    // Gọi hàm mới đã thêm trong DAO
                    List<SanKhachHangRow> ds = dao.timSanTrongChoKhachHang(
                            ngayDat,
                            gioBD,
                            gioKT
                    );

                    Platform.runLater(() -> {
                        // danhSachGoc bây giờ chỉ chứa các sân còn trống
                        danhSachGoc.setAll(ds);

                        // Sau đó vẫn lọc tiếp theo tên sân, loại sân, mức giá
                        applyFilter.run();

                        if (danhSachHienThi.isEmpty()) {
                            lblTrong.setText("Không có sân trống phù hợp với bộ lọc.");
                        } else {
                            lblTrong.setText("");
                        }

                        table.refresh();
                        btnLoc.setDisable(false);
                    });

                } catch (Exception ex) {
                    Platform.runLater(() -> {
                        danhSachGoc.clear();
                        danhSachHienThi.clear();
                        lblTrong.setText("Lỗi tải dữ liệu: " + ex.getMessage());
                        btnLoc.setDisable(false);
                    });
                }
            });

            t.setDaemon(true);
            t.start();
        };

        // ===== EVENTS =====

        // Bấm Lọc thì truy vấn lại DB để lấy sân còn trống
        btnLoc.setOnAction(e -> loadSanTrongTheoNgayGio.run());

        // Các bộ lọc này chỉ lọc trên danh sách sân trống đã lấy từ DB
        txtTimKiem.textProperty().addListener((obs, oldVal, newVal) -> applyFilter.run());
        cbLoaiSan.setOnAction(e -> applyFilter.run());
        cbMucGia.setOnAction(e -> applyFilter.run());

        // Khi đổi ngày hoặc giờ, xóa kết quả cũ để tránh hiểu nhầm
        Runnable clearKetQuaCu = () -> {
            danhSachGoc.clear();
            danhSachHienThi.clear();
            lblTrong.setText("Bạn đã đổi ngày/giờ. Bấm Lọc để tìm sân trống mới.");
            table.refresh();
        };

        dpNgayDat.setOnAction(e -> clearKetQuaCu.run());
        cbGioBatDau.setOnAction(e -> clearKetQuaCu.run());
        cbGioKetThuc.setOnAction(e -> clearKetQuaCu.run());

        // ===== ADD UI =====
        root.getChildren().addAll(lblTitle, lblSubtitle, filterBar, table);

        // ===== LOAD DATA BAN ĐẦU =====
        // Khi vừa vào trang, tự load sân trống theo ngày hôm nay và giờ mặc định
        loadSanTrongTheoNgayGio.run();

        return root;
    }

    /**
     * Giữ lại cách gọi cũ: DanhSachSanPage.createView(san -> ...)
     */
    public static Node createView(Consumer<SanKhachHangRow> onChonSanCu) {
        return createView((san, ngayDat, gioBD, gioKT) -> {
            if (onChonSanCu != null) {
                onChonSanCu.accept(san);
            }
        });
    }

    private static VBox taoOFilter(String tieuDe, Control control, double width) {
        Label label = new Label(tieuDe);
        label.getStyleClass().add("filter-label");
        label.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 700;" +
                "-fx-text-fill: #374151;"
        );

        control.setMinWidth(width);
        control.setPrefWidth(width);
        control.setMaxWidth(width);
        control.setPrefHeight(44);

        VBox box = new VBox(6);
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().addAll(label, control);

        return box;
    }

    private static VBox taoNutFilter(Button button, double width) {
        Label label = new Label(" ");
        label.setStyle("-fx-font-size: 13px;");

        button.setMinWidth(width);
        button.setPrefWidth(width);
        button.setMaxWidth(width);
        button.setPrefHeight(44);

        VBox box = new VBox(6);
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().addAll(label, button);

        return box;
    }

    private static void thongBao(String noiDung) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(noiDung);
        alert.showAndWait();
    }

    private static String chuyenTrangThai(String trangThai) {
        return switch (trangThai == null ? "" : trangThai) {
            // Gộp chung trạng thái Sẵn sàng và Đang sử dụng thành 1 chữ thân thiện
            case "SAN_SANG", "DANG_SU_DUNG" -> "Đang hoạt động";
            case "BAO_TRI" -> "Bảo trì";
            case "NGUNG_HOAT_DONG" -> "Ngừng hoạt động";
            default -> trangThai;
        };
    }
}
