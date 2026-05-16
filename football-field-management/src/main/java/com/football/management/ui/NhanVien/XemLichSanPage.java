package com.football.management.ui.NhanVien;

import com.football.management.dao.LichSanDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableCell;

import java.sql.SQLException;
import java.time.LocalDate;

public class XemLichSanPage {

    private static final LichSanDAO lichSanDAO = new LichSanDAO();

    private static final ObservableList<LichSanRow> danhSachLichSan =
            FXCollections.observableArrayList();

    private static TableView<LichSanRow> bangLichSan;
    private static DatePicker oNgayXem;
    private static ComboBox<String> oLocSan;

    public static Node createView() {
        VBox khungChinh = new VBox(16);
        khungChinh.setPadding(new Insets(24));
        khungChinh.getStyleClass().add("content-root");

        Label tieuDe = new Label("Xem lịch sân");
        tieuDe.getStyleClass().add("page-title");

        Label moTa = new Label("Tra cứu lịch đặt sân theo ngày, sân, khách hàng và trạng thái");
        moTa.getStyleClass().add("section-subtitle");

        HBox thanhCongCu = new HBox(10);
        thanhCongCu.getStyleClass().add("filter-bar");

        oNgayXem = new DatePicker();
        oNgayXem.setValue(LocalDate.now());

        oLocSan = new ComboBox<>();
        oLocSan.setPrefWidth(220);
        taiDanhSachSanChoBoLoc();

        Button nutXemLich = new Button("Xem lịch");
        nutXemLich.getStyleClass().add("primary-button");

        Button nutHomNay = new Button("Hôm nay");
        nutHomNay.getStyleClass().add("light-button");

        TextField oTimKiem = new TextField();
        oTimKiem.setPromptText("Tìm theo mã đơn, sân, khách hàng, trạng thái...");
        oTimKiem.getStyleClass().add("search-field");
        oTimKiem.setPrefWidth(360);

        thanhCongCu.getChildren().addAll(
                oNgayXem,
                oLocSan,
                nutXemLich,
                nutHomNay,
                oTimKiem
        );

        bangLichSan = taoBangLichSan();

        FilteredList<LichSanRow> danhSachLoc =
                new FilteredList<>(danhSachLichSan, item -> true);

        oTimKiem.textProperty().addListener((observable, giaTriCu, giaTriMoi) -> {
            danhSachLoc.setPredicate(item ->
                    khopBoLoc(item, giaTriMoi, oLocSan.getValue())
            );
        });

        oLocSan.valueProperty().addListener((observable, giaTriCu, giaTriMoi) -> {
            danhSachLoc.setPredicate(item ->
                    khopBoLoc(item, oTimKiem.getText(), giaTriMoi)
            );
        });

        bangLichSan.setItems(danhSachLoc);

        nutXemLich.setOnAction(event -> taiLaiLichSanTheoNgay());

        nutHomNay.setOnAction(event -> {
            oNgayXem.setValue(LocalDate.now());
            taiLaiLichSanTheoNgay();
        });

        taiLaiLichSanTheoNgay();

        khungChinh.getChildren().addAll(
                tieuDe,
                moTa,
                thanhCongCu,
                bangLichSan
        );

        return khungChinh;
    }

    private static TableView<LichSanRow> taoBangLichSan() {
        TableView<LichSanRow> bang = new TableView<>();

        TableColumn<LichSanRow, String> cotMaDon = new TableColumn<>("Mã đơn");
        cotMaDon.setCellValueFactory(data -> data.getValue().maDonProperty());

        TableColumn<LichSanRow, String> cotTenSan = new TableColumn<>("Sân");
        cotTenSan.setCellValueFactory(data -> data.getValue().tenSanProperty());

        TableColumn<LichSanRow, String> cotTenKhachHang = new TableColumn<>("Khách hàng");
        cotTenKhachHang.setCellValueFactory(data -> data.getValue().tenKhachHangProperty());

        TableColumn<LichSanRow, String> cotNgayDat = new TableColumn<>("Ngày đặt");
        cotNgayDat.setCellValueFactory(data -> data.getValue().ngayDatProperty());

        TableColumn<LichSanRow, String> cotGioBatDau = new TableColumn<>("Giờ bắt đầu");
        cotGioBatDau.setCellValueFactory(data -> data.getValue().gioBatDauProperty());

        TableColumn<LichSanRow, String> cotGioKetThuc = new TableColumn<>("Giờ kết thúc");
        cotGioKetThuc.setCellValueFactory(data -> data.getValue().gioKetThucProperty());

        TableColumn<LichSanRow, String> cotTrangThai = new TableColumn<>("Trạng thái");
cotTrangThai.setCellValueFactory(data -> data.getValue().trangThaiProperty());

cotTrangThai.setCellFactory(column -> new TableCell<LichSanRow, String>() {
    @Override
    protected void updateItem(String trangThai, boolean empty) {
        super.updateItem(trangThai, empty);

        if (empty || trangThai == null || trangThai.trim().isEmpty()) {
            setText(null);
            setGraphic(null);
            return;
        }

        Label badge = new Label(trangThai);
        badge.getStyleClass().add("status-badge");
        badge.getStyleClass().add(layStyleTrangThaiLichSan(trangThai));

        setText(null);
        setGraphic(badge);
    }
});

        bang.getColumns().add(cotMaDon);
        bang.getColumns().add(cotTenSan);
        bang.getColumns().add(cotTenKhachHang);
        bang.getColumns().add(cotNgayDat);
        bang.getColumns().add(cotGioBatDau);
        bang.getColumns().add(cotGioKetThuc);
        bang.getColumns().add(cotTrangThai);

        bang.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        bang.setPrefHeight(560);
        bang.setPlaceholder(new Label("Không có lịch sân trong ngày được chọn."));

        return bang;
    }

    private static void taiLaiLichSanTheoNgay() {
        try {
            LocalDate ngayCanXem = oNgayXem == null ? LocalDate.now() : oNgayXem.getValue();

            if (ngayCanXem == null) {
                ngayCanXem = LocalDate.now();
                oNgayXem.setValue(ngayCanXem);
            }

            danhSachLichSan.clear();
            danhSachLichSan.addAll(lichSanDAO.layLichSanTheoNgay(ngayCanXem));

        } catch (SQLException e) {
            e.printStackTrace();
            hienThongBaoLoi("Không thể tải lịch sân từ cơ sở dữ liệu.\nChi tiết lỗi: " + e.getMessage());
        }
    }

    private static void taiDanhSachSanChoBoLoc() {
        try {
            oLocSan.getItems().clear();
            oLocSan.getItems().add("Tất cả sân");
            oLocSan.getItems().addAll(lichSanDAO.layDanhSachSanChoBoLoc());
            oLocSan.setValue("Tất cả sân");

        } catch (SQLException e) {
            e.printStackTrace();

            oLocSan.getItems().clear();
            oLocSan.getItems().add("Tất cả sân");
            oLocSan.setValue("Tất cả sân");

            hienThongBaoLoi("Không thể tải danh sách sân.\nChi tiết lỗi: " + e.getMessage());
        }
    }

    private static boolean khopBoLoc(LichSanRow item, String tuKhoa, String sanDuocChon) {
        if (item == null) {
            return false;
        }

        String tuKhoaChuanHoa = tuKhoa == null ? "" : tuKhoa.toLowerCase().trim();
        String sanChuanHoa = sanDuocChon == null ? "Tất cả sân" : sanDuocChon.trim();

        String tenSan = layGiaTri(item.tenSanProperty().get());

        boolean khopSan = sanChuanHoa.equals("Tất cả sân")
                || tenSan.equalsIgnoreCase(sanChuanHoa);

        if (!khopSan) {
            return false;
        }

        if (tuKhoaChuanHoa.isEmpty()) {
            return true;
        }

        return chuaTuKhoa(item.maDonProperty().get(), tuKhoaChuanHoa)
                || chuaTuKhoa(item.tenSanProperty().get(), tuKhoaChuanHoa)
                || chuaTuKhoa(item.tenKhachHangProperty().get(), tuKhoaChuanHoa)
                || chuaTuKhoa(item.ngayDatProperty().get(), tuKhoaChuanHoa)
                || chuaTuKhoa(item.gioBatDauProperty().get(), tuKhoaChuanHoa)
                || chuaTuKhoa(item.gioKetThucProperty().get(), tuKhoaChuanHoa)
                || chuaTuKhoa(item.trangThaiProperty().get(), tuKhoaChuanHoa);
    }

        private static String layStyleTrangThaiLichSan(String trangThai) {
    if (trangThai == null) {
        return "status-done";
    }

    String trangThaiChuanHoa = trangThai.trim().toUpperCase();

    return switch (trangThaiChuanHoa) {
        case "CHỜ XỬ LÝ", "CHO_XU_LY" -> "status-pending";
        case "ĐÃ XÁC NHẬN", "DA_XAC_NHAN" -> "status-confirmed";
        case "ĐÃ CHECK-IN", "DA_CHECK_IN" -> "status-checkin";
        case "ĐÃ HỦY", "DA_HUY" -> "status-cancelled";
        case "ĐÃ HOÀN THÀNH", "DA_HOAN_THANH" -> "status-done";
        default -> "status-done";
    };
}

    private static boolean chuaTuKhoa(String giaTri, String tuKhoa) {
        if (giaTri == null) {
            return false;
        }

        return giaTri.toLowerCase().contains(tuKhoa);
    }

    private static String layGiaTri(String giaTri) {
        return giaTri == null ? "" : giaTri.trim();
    }

    private static void hienThongBaoLoi(String noiDung) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(noiDung);
        alert.showAndWait();
    }
}