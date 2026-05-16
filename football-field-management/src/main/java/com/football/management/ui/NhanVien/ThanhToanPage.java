package com.football.management.ui.NhanVien;

import com.football.management.dao.ThanhToanDAO;

import java.text.DecimalFormat;

import javafx.scene.control.ComboBox;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableCell;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.sql.SQLException;

public class ThanhToanPage {

    private static final ThanhToanDAO thanhToanDAO = new ThanhToanDAO();

    private static final ObservableList<ThanhToanRow> danhSachThanhToan =
            FXCollections.observableArrayList();

    private static TableView<ThanhToanRow> bangThanhToan;
    private static ComboBox<String> oLocThanhToan;

    public static Node createView() {
        VBox khungChinh = new VBox(16);
        khungChinh.setPadding(new Insets(24));
        khungChinh.getStyleClass().add("content-root");

        Label tieuDe = new Label("Thanh toán");
        tieuDe.getStyleClass().add("page-title");

        Label moTa = new Label("Theo dõi, tính tiền và xác nhận thanh toán cho các đơn đặt sân");
        moTa.getStyleClass().add("section-subtitle");

        HBox thanhCongCu = new HBox(10);
        thanhCongCu.getStyleClass().add("filter-bar");

        TextField oTimKiem = new TextField();
        oTimKiem.setPromptText("Tìm theo mã đơn, khách hàng, sân, trạng thái...");
        oTimKiem.getStyleClass().add("search-field");
        oTimKiem.setPrefWidth(380);

        oLocThanhToan = new ComboBox<>();
        oLocThanhToan.getItems().addAll(
        "Chưa thanh toán",
        "Đã thanh toán",
        "Tất cả"
);
oLocThanhToan.setValue("Chưa thanh toán");
oLocThanhToan.setPrefWidth(180);    

        Button nutTinhTien = new Button("Tính tiền");
        nutTinhTien.getStyleClass().add("primary-button");

        Button nutXacNhanThanhToan = new Button("Xác nhận thanh toán");
        nutXacNhanThanhToan.getStyleClass().add("primary-button");

        Button nutTaiLai = new Button("Tải lại");
        nutTaiLai.getStyleClass().add("light-button");

        thanhCongCu.getChildren().addAll(
        oTimKiem,
        oLocThanhToan,
        nutTinhTien,
        nutXacNhanThanhToan,
        nutTaiLai
);

        bangThanhToan = taoBangThanhToan();

        FilteredList<ThanhToanRow> danhSachLoc =
                new FilteredList<>(danhSachThanhToan, item -> true);

        oTimKiem.textProperty().addListener((observable, giaTriCu, giaTriMoi) -> {
            String tuKhoa = giaTriMoi == null ? "" : giaTriMoi.toLowerCase().trim();

            danhSachLoc.setPredicate(item -> {
                if (tuKhoa.isEmpty()) {
                    return true;
                }

                return chuaTuKhoa(item.maDonProperty().get(), tuKhoa)
                        || chuaTuKhoa(item.tenKhachHangProperty().get(), tuKhoa)
                        || chuaTuKhoa(item.tenSanProperty().get(), tuKhoa)
                        || chuaTuKhoa(item.thoiGianProperty().get(), tuKhoa)
                        || chuaTuKhoa(item.trangThaiDonProperty().get(), tuKhoa)
                        || chuaTuKhoa(item.trangThaiThanhToanProperty().get(), tuKhoa)
                        || chuaTuKhoa(item.tongTienProperty().get(), tuKhoa);
            });
        });

        bangThanhToan.setItems(danhSachLoc);

        nutTaiLai.setOnAction(event -> taiLaiDanhSachThanhToan());

        oLocThanhToan.setOnAction(event -> taiLaiDanhSachThanhToan());

        nutTinhTien.setOnAction(event -> {
    ThanhToanRow dongDuocChon = bangThanhToan.getSelectionModel().getSelectedItem();

    if (dongDuocChon == null) {
        hienThongBaoLoi("Vui lòng chọn một đơn cần tính tiền.");
        return;
    }

    if (daThanhToan(dongDuocChon)) {
    hienThongBaoLoi("Đơn này đã thanh toán, không thể tính tiền lại.");
    return;
}

String maDon = dongDuocChon.maDonProperty().get();

try {
    double tongTien = thanhToanDAO.tinhVaCapNhatTongTienTheoMaDon(maDon);

        hienThongBaoThongTin(
                "Tính tiền thành công cho đơn " + maDon + ".\n" +
                "Tổng tiền thanh toán: " + dinhDangTien(tongTien)
        );

        taiLaiDanhSachThanhToan();

    } catch (SQLException e) {
        e.printStackTrace();
        hienThongBaoLoi("Không thể tính tiền cho đơn đặt sân.\nChi tiết lỗi: " + e.getMessage());
    }
});

        nutXacNhanThanhToan.setOnAction(event -> {
    ThanhToanRow dongDuocChon = bangThanhToan.getSelectionModel().getSelectedItem();

    if (dongDuocChon == null) {
        hienThongBaoLoi("Vui lòng chọn một đơn cần xác nhận thanh toán.");
        return;
    }

    if (daThanhToan(dongDuocChon)) {
    hienThongBaoLoi("Đơn này đã được thanh toán trước đó.");
    return;
}

        String maDon = dongDuocChon.maDonProperty().get();
         String tongTien = dongDuocChon.tongTienProperty().get();

    if (tongTien == null || tongTien.equalsIgnoreCase("Chưa tính")) {
        hienThongBaoLoi("Vui lòng tính tiền trước khi xác nhận thanh toán.");
        return;
    }

    Alert xacNhan = new Alert(Alert.AlertType.CONFIRMATION);
    xacNhan.setTitle("Xác nhận thanh toán");
    xacNhan.setHeaderText(null);
    xacNhan.setContentText(
            "Xác nhận khách hàng đã thanh toán cho đơn " + maDon + "?\n" +
            "Tổng tiền: " + tongTien
    );

    ButtonType nutDongY = new ButtonType("Xác nhận thanh toán", ButtonBar.ButtonData.OK_DONE);
    ButtonType nutKhong = new ButtonType("Không", ButtonBar.ButtonData.CANCEL_CLOSE);
    xacNhan.getButtonTypes().setAll(nutDongY, nutKhong);

    xacNhan.showAndWait().ifPresent(ketQua -> {
        if (ketQua != nutDongY) {
            return;
        }

        try {
            boolean thanhCong = thanhToanDAO.xacNhanThanhToanTheoMaDon(maDon);

            if (thanhCong) {
                hienThongBaoThongTin("Xác nhận thanh toán thành công.");
                taiLaiDanhSachThanhToan();
            } else {
                hienThongBaoLoi(
                        "Không thể xác nhận thanh toán.\n" +
                        "Đơn có thể đã thanh toán, chưa tính tiền hoặc không hợp lệ."
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
            hienThongBaoLoi("Không thể xác nhận thanh toán.\nChi tiết lỗi: " + e.getMessage());
        }
    });
});

        taiLaiDanhSachThanhToan();

        khungChinh.getChildren().addAll(
                tieuDe,
                moTa,
                thanhCongCu,
                bangThanhToan
        );

        return khungChinh;
    }

    private static TableView<ThanhToanRow> taoBangThanhToan() {
        TableView<ThanhToanRow> bang = new TableView<>();

        TableColumn<ThanhToanRow, String> cotMaDon = new TableColumn<>("Mã đơn");
        cotMaDon.setCellValueFactory(data -> data.getValue().maDonProperty());

        TableColumn<ThanhToanRow, String> cotTenKhachHang = new TableColumn<>("Khách hàng");
        cotTenKhachHang.setCellValueFactory(data -> data.getValue().tenKhachHangProperty());

        TableColumn<ThanhToanRow, String> cotTenSan = new TableColumn<>("Sân");
        cotTenSan.setCellValueFactory(data -> data.getValue().tenSanProperty());

        TableColumn<ThanhToanRow, String> cotThoiGian = new TableColumn<>("Thời gian");
        cotThoiGian.setCellValueFactory(data -> data.getValue().thoiGianProperty());

        TableColumn<ThanhToanRow, String> cotTrangThaiDon = new TableColumn<>("Trạng thái đơn");
cotTrangThaiDon.setCellValueFactory(data -> data.getValue().trangThaiDonProperty());

cotTrangThaiDon.setCellFactory(column -> new TableCell<ThanhToanRow, String>() {
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
        badge.getStyleClass().add(layStyleTrangThaiDon(trangThai));

        setText(null);
        setGraphic(badge);
    }
});

        TableColumn<ThanhToanRow, String> cotTrangThaiThanhToan = new TableColumn<>("Trạng thái thanh toán");
cotTrangThaiThanhToan.setCellValueFactory(data -> data.getValue().trangThaiThanhToanProperty());

cotTrangThaiThanhToan.setCellFactory(column -> new TableCell<ThanhToanRow, String>() {
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
        badge.getStyleClass().add(layStyleTrangThaiThanhToan(trangThai));

        setText(null);
        setGraphic(badge);
    }
});

        TableColumn<ThanhToanRow, String> cotTongTien = new TableColumn<>("Tổng tiền");
        cotTongTien.setCellValueFactory(data -> data.getValue().tongTienProperty());

        bang.getColumns().add(cotMaDon);
        bang.getColumns().add(cotTenKhachHang);
        bang.getColumns().add(cotTenSan);
        bang.getColumns().add(cotThoiGian);
        bang.getColumns().add(cotTrangThaiDon);
        bang.getColumns().add(cotTrangThaiThanhToan);
        bang.getColumns().add(cotTongTien);

        bang.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        bang.setPrefHeight(560);
        bang.setPlaceholder(new Label("Chưa có đơn cần thanh toán."));

        return bang;
    }

    private static void taiLaiDanhSachThanhToan() {
    try {
        String boLoc = layMaBoLocThanhToan();

        danhSachThanhToan.clear();
        danhSachThanhToan.addAll(
                thanhToanDAO.layDanhSachDonTheoBoLocThanhToan(boLoc)
        );

    } catch (Exception e) {
        e.printStackTrace();

        danhSachThanhToan.clear();

        hienThongBaoLoi(
                "Không thể tải màn hình thanh toán.\n" +
                "Có thể lỗi nằm ở ThanhToanDAO hoặc câu SQL.\n\n" +
                "Chi tiết lỗi: " + e.getMessage()
        );
    }
}

    private static String layMaBoLocThanhToan() {
    if (oLocThanhToan == null || oLocThanhToan.getValue() == null) {
        return "CHUA_THANH_TOAN";
    }

    String giaTri = oLocThanhToan.getValue();

    return switch (giaTri) {
        case "Đã thanh toán" -> "DA_THANH_TOAN";
        case "Tất cả" -> "TAT_CA";
        default -> "CHUA_THANH_TOAN";
    };
}

    private static boolean daThanhToan(ThanhToanRow dong) {
    if (dong == null || dong.trangThaiThanhToanProperty().get() == null) {
        return false;
    }

    String trangThai = dong.trangThaiThanhToanProperty().get().trim().toUpperCase();

    return trangThai.equals("ĐÃ THANH TOÁN")
            || trangThai.equals("DA_THANH_TOAN");
}

    private static String layStyleTrangThaiDon(String trangThai) {
    if (trangThai == null) {
        return "status-done";
    }

    String trangThaiChuanHoa = trangThai.trim().toUpperCase();

    return switch (trangThaiChuanHoa) {
        case "ĐÃ CHECK-IN", "DA_CHECK_IN" -> "status-checkin";
        case "ĐÃ HOÀN THÀNH", "DA_HOAN_THANH" -> "status-done";
        case "ĐÃ HỦY", "DA_HUY" -> "status-cancelled";
        case "ĐÃ XÁC NHẬN", "DA_XAC_NHAN" -> "status-confirmed";
        case "CHỜ XỬ LÝ", "CHO_XU_LY" -> "status-pending";
        default -> "status-done";
    };
}

private static String layStyleTrangThaiThanhToan(String trangThai) {
    if (trangThai == null) {
        return "status-pending";
    }

    String trangThaiChuanHoa = trangThai.trim().toUpperCase();

    return switch (trangThaiChuanHoa) {
        case "CHƯA THANH TOÁN", "CHUA_THANH_TOAN" -> "status-pending";
        case "ĐÃ THANH TOÁN", "DA_THANH_TOAN" -> "status-checkin";
        case "THANH TOÁN MỘT PHẦN", "THANH_TOAN_MOT_PHAN" -> "status-confirmed";
        case "ĐÃ HOÀN TIỀN", "DA_HOAN_TIEN" -> "status-done";
        default -> "status-done";
    };
}

    private static boolean chuaTuKhoa(String giaTri, String tuKhoa) {
        if (giaTri == null) {
            return false;
        }

        return giaTri.toLowerCase().contains(tuKhoa);
    }

    private static String dinhDangTien(double soTien) {
    DecimalFormat decimalFormat = new DecimalFormat("#,###");
    return decimalFormat.format(soTien) + " VND";
}

    private static void hienThongBaoLoi(String noiDung) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(noiDung);
        alert.showAndWait();
    }

    private static void hienThongBaoThongTin(String noiDung) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(noiDung);
        alert.showAndWait();
    }
}