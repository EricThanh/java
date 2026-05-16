package com.football.management.ui.NhanVien;

import com.football.management.dao.DatSanDAO;
import com.football.management.service.DatSanService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Locale;

public class QuanLyDatSanPage {

    private static final DatSanDAO datSanDAO = new DatSanDAO();
    private static final DatSanService datSanService = new DatSanService();

    private static final ObservableList<DatSanRow> danhSachDatSan =
            FXCollections.observableArrayList();

    private static TableView<DatSanRow> bangDatSan;

    private static HBox khuThongKeDatSan;

    public static Node createView() {
        VBox khungChinh = new VBox(18);
        khungChinh.setPadding(new Insets(26));
        khungChinh.getStyleClass().add("content-root");

        Label tieuDe = new Label("Quản lý đặt sân");
        tieuDe.getStyleClass().add("page-title");

        Label moTa = new Label("Tạo mới, cập nhật, hủy đơn, xác nhận khách đến sân và hoàn thành đơn");
        moTa.getStyleClass().add("section-subtitle");

        HBox thanhCongCu = new HBox(10);
        thanhCongCu.setAlignment(Pos.CENTER_LEFT);
        thanhCongCu.getStyleClass().add("filter-bar");

        TextField oTimKiem = new TextField();
        oTimKiem.setPromptText("Tìm theo mã đơn, mã KH, tên khách hàng, tên sân, trạng thái...");
        oTimKiem.getStyleClass().add("search-field");
        oTimKiem.setPrefWidth(420);

        Button nutTaoDon = new Button("Tạo đơn");
        nutTaoDon.getStyleClass().add("primary-button");

        Button nutCapNhat = new Button("Cập nhật");
        nutCapNhat.getStyleClass().add("light-button");

        Button nutHuyDon = new Button("Hủy đơn");
        nutHuyDon.getStyleClass().add("secondary-button");

        Button nutXacNhanDenSan = new Button("Xác nhận đến sân");
        nutXacNhanDenSan.getStyleClass().add("primary-button");

        Button nutHoanThanh = new Button("Hoàn thành");
        nutHoanThanh.getStyleClass().add("light-button");

        nutTaoDon.setMinWidth(90);
        nutCapNhat.setMinWidth(90);
        nutHuyDon.setMinWidth(90);
        nutXacNhanDenSan.setMinWidth(140);
        nutHoanThanh.setMinWidth(110);

        nutXacNhanDenSan.setWrapText(true);
        nutHoanThanh.setWrapText(true); 

        oTimKiem.setPrefWidth(360);

        nutTaoDon.setMinWidth(120);
        nutCapNhat.setMinWidth(120);
        nutHuyDon.setMinWidth(120);
        nutXacNhanDenSan.setMinWidth(190);
        nutHoanThanh.setMinWidth(145);

        nutTaoDon.setPrefWidth(120);
        nutCapNhat.setPrefWidth(120);
        nutHuyDon.setPrefWidth(120);
        nutXacNhanDenSan.setPrefWidth(190);
        nutHoanThanh.setPrefWidth(145);

        thanhCongCu.getChildren().addAll(
                oTimKiem,
                nutTaoDon,
                nutCapNhat,
                nutHuyDon,
                nutXacNhanDenSan,
                nutHoanThanh
        );

        HBox thongKeNhanh = new HBox(14);
thongKeNhanh.setAlignment(Pos.CENTER_LEFT);

VBox theTongDon = taoTheThongKeNhanh(
        "Danh sách đơn",
        "Theo dõi toàn bộ đơn đặt sân"
);

VBox theXuLy = taoTheThongKeNhanh(
        "Xử lý tại quầy",
        "Tạo, cập nhật, hủy đơn khi cần"
);

VBox theTrangThai = taoTheThongKeNhanh(
        "Trạng thái sân",
        "Xác nhận khách đến và hoàn thành đơn"
);

thongKeNhanh.getChildren().addAll(theTongDon, theXuLy, theTrangThai);

        bangDatSan = taoBangDatSan();

        FilteredList<DatSanRow> danhSachLoc = new FilteredList<>(danhSachDatSan, item -> true);

        oTimKiem.textProperty().addListener((observable, giaTriCu, giaTriMoi) -> {
            String tuKhoa = giaTriMoi == null ? "" : giaTriMoi.toLowerCase().trim();

            danhSachLoc.setPredicate(item -> {
                if (tuKhoa.isEmpty()) {
                    return true;
                }

                return chuaTuKhoa(item.maDonProperty().get(), tuKhoa)
                        || chuaTuKhoa(item.maKhachHangProperty().get(), tuKhoa)
                        || chuaTuKhoa(item.tenKhachHangProperty().get(), tuKhoa)
                        || chuaTuKhoa(item.tenSanProperty().get(), tuKhoa)
                        || chuaTuKhoa(item.thoiGianProperty().get(), tuKhoa)
                        || chuaTuKhoa(item.trangThaiProperty().get(), tuKhoa);
            });
        });

        bangDatSan.setItems(danhSachLoc);

        nutTaoDon.setOnAction(event -> moHopThoaiTaoDon());

        nutCapNhat.setOnAction(event -> {
            DatSanRow dongDuocChon = bangDatSan.getSelectionModel().getSelectedItem();

            if (dongDuocChon == null) {
                hienThongBaoLoi("Vui lòng chọn một đơn đặt sân cần cập nhật.");
                return;
            }

            String trangThai = dongDuocChon.trangThaiProperty().get();

            if (!coTheCapNhatDon(trangThai)) {
                hienThongBaoLoi(
                        "Không thể cập nhật đơn đặt sân này.\n" +
                                "Chỉ các đơn ở trạng thái CHỜ XỬ LÝ hoặc ĐÃ XÁC NHẬN mới được cập nhật."
                );
                return;
            }

            moHopThoaiCapNhatDon(dongDuocChon);
        });

        nutHuyDon.setOnAction(event -> {
            DatSanRow dongDuocChon = bangDatSan.getSelectionModel().getSelectedItem();

            if (dongDuocChon == null) {
                hienThongBaoLoi("Vui lòng chọn một đơn đặt sân cần hủy.");
                return;
            }

            String maDon = dongDuocChon.maDonProperty().get();
            String trangThai = dongDuocChon.trangThaiProperty().get();

            if (!coTheHuyDon(trangThai)) {
                hienThongBaoLoi(
                        "Không thể hủy đơn đặt sân này.\n" +
                                "Chỉ các đơn ở trạng thái CHỜ XỬ LÝ hoặc ĐÃ XÁC NHẬN mới được hủy."
                );
                return;
            }

            Alert xacNhan = new Alert(Alert.AlertType.CONFIRMATION);
            xacNhan.setTitle("Xác nhận hủy đơn");
            xacNhan.setHeaderText(null);
            xacNhan.setContentText("Bạn có chắc chắn muốn hủy đơn " + maDon + " không?");

            ButtonType nutDongY = new ButtonType("Đồng ý hủy", ButtonBar.ButtonData.OK_DONE);
            ButtonType nutKhong = new ButtonType("Không", ButtonBar.ButtonData.CANCEL_CLOSE);
            xacNhan.getButtonTypes().setAll(nutDongY, nutKhong);

            xacNhan.showAndWait().ifPresent(ketQua -> {
                if (ketQua != nutDongY) {
                    return;
                }

                try {
                    boolean thanhCong = datSanDAO.huyDonDatSanTheoMaDon(maDon);

                    if (thanhCong) {
                        hienThongBaoThongTin("Hủy đơn đặt sân thành công.");
                        taiLaiDanhSachDatSan();
                    } else {
                        hienThongBaoLoi("Không thể hủy đơn. Đơn có thể đã bị hủy, đã check-in hoặc đã hoàn thành.");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    hienThongBaoLoi("Không thể hủy đơn đặt sân.\nChi tiết lỗi: " + e.getMessage());
                }
            });
        });

        nutXacNhanDenSan.setOnAction(event -> {
            DatSanRow dongDuocChon = bangDatSan.getSelectionModel().getSelectedItem();

            if (dongDuocChon == null) {
                hienThongBaoLoi("Vui lòng chọn một đơn đặt sân để xác nhận khách đến.");
                return;
            }

            String maDon = dongDuocChon.maDonProperty().get();
            String trangThai = dongDuocChon.trangThaiProperty().get();

            if (!coTheXacNhanDenSan(trangThai)) {
                hienThongBaoLoi(
                        "Không thể xác nhận khách đến sân cho đơn này.\n" +
                                "Chỉ các đơn ở trạng thái CHỜ XỬ LÝ hoặc ĐÃ XÁC NHẬN mới được xác nhận đến sân."
                );
                return;
            }

            Alert xacNhan = new Alert(Alert.AlertType.CONFIRMATION);
            xacNhan.setTitle("Xác nhận khách đến sân");
            xacNhan.setHeaderText(null);
            xacNhan.setContentText("Xác nhận khách của đơn " + maDon + " đã đến sân?");

            ButtonType nutDongY = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
            ButtonType nutKhong = new ButtonType("Không", ButtonBar.ButtonData.CANCEL_CLOSE);
            xacNhan.getButtonTypes().setAll(nutDongY, nutKhong);

            xacNhan.showAndWait().ifPresent(ketQua -> {
                if (ketQua != nutDongY) {
                    return;
                }

                try {
                    boolean thanhCong = datSanDAO.xacNhanKhachDenSanTheoMaDon(maDon);

                    if (thanhCong) {
                        hienThongBaoThongTin("Xác nhận khách đến sân thành công.");
                        taiLaiDanhSachDatSan();
                    } else {
                        hienThongBaoLoi("Không thể xác nhận. Đơn có thể đã hủy, đã hoàn thành hoặc đã check-in.");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    hienThongBaoLoi("Không thể xác nhận khách đến sân.\nChi tiết lỗi: " + e.getMessage());
                }
            });
        });

        nutHoanThanh.setOnAction(event -> {
            DatSanRow dongDuocChon = bangDatSan.getSelectionModel().getSelectedItem();

            if (dongDuocChon == null) {
                hienThongBaoLoi("Vui lòng chọn một đơn đặt sân cần hoàn thành.");
                return;
            }

            String maDon = dongDuocChon.maDonProperty().get();
            String trangThai = dongDuocChon.trangThaiProperty().get();

            if (!coTheHoanThanhDon(trangThai)) {
                hienThongBaoLoi(
                        "Không thể hoàn thành đơn đặt sân này.\n" +
                                "Chỉ các đơn ở trạng thái ĐÃ CHECK-IN mới được hoàn thành."
                );
                return;
            }

            Alert xacNhan = new Alert(Alert.AlertType.CONFIRMATION);
            xacNhan.setTitle("Xác nhận hoàn thành đơn");
            xacNhan.setHeaderText(null);
            xacNhan.setContentText("Xác nhận hoàn thành đơn " + maDon + " và giải phóng sân?");

            ButtonType nutDongY = new ButtonType("Hoàn thành", ButtonBar.ButtonData.OK_DONE);
            ButtonType nutKhong = new ButtonType("Không", ButtonBar.ButtonData.CANCEL_CLOSE);
            xacNhan.getButtonTypes().setAll(nutDongY, nutKhong);

            xacNhan.showAndWait().ifPresent(ketQua -> {
                if (ketQua != nutDongY) {
                    return;
                }

                try {
                    boolean thanhCong = datSanDAO.hoanThanhDonDatSanTheoMaDon(maDon);

                    if (thanhCong) {
                        hienThongBaoThongTin("Hoàn thành đơn đặt sân thành công.");
                        taiLaiDanhSachDatSan();
                    } else {
                        hienThongBaoLoi("Không thể hoàn thành đơn. Đơn phải đang ở trạng thái ĐÃ CHECK-IN.");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    hienThongBaoLoi("Không thể hoàn thành đơn đặt sân.\nChi tiết lỗi: " + e.getMessage());
                }
            });
        });

        VBox bangCard = new VBox(12);
bangCard.getStyleClass().add("card");

Label tieuDeBang = new Label("Danh sách đơn đặt sân");
tieuDeBang.getStyleClass().add("section-title");

Label moTaBang = new Label("Chọn một đơn trong bảng để cập nhật, hủy, xác nhận khách đến hoặc hoàn thành đơn.");
moTaBang.getStyleClass().add("section-subtitle");
moTaBang.setWrapText(true);

bangCard.getChildren().addAll(tieuDeBang, moTaBang, bangDatSan);

        khuThongKeDatSan = taoKhuThongKeDatSan();

khungChinh.getChildren().addAll(
        tieuDe,
        moTa,
        thanhCongCu,
        khuThongKeDatSan,
        bangDatSan
);

taiLaiDanhSachDatSan();

return khungChinh;
    }

    private static VBox taoTheThongKeNhanh(String tieuDe, String moTa) {
    VBox card = new VBox(6);
    card.getStyleClass().add("soft-card");
    card.setPrefWidth(310);

    Label lblTieuDe = new Label(tieuDe);
    lblTieuDe.getStyleClass().add("form-label");

    Label lblMoTa = new Label(moTa);
    lblMoTa.getStyleClass().add("muted-text");
    lblMoTa.setWrapText(true);

    card.getChildren().addAll(lblTieuDe, lblMoTa);

    return card;
}

    private static HBox taoKhuThongKeDatSan() {
    HBox khuThongKe = new HBox(12);
    khuThongKe.getStyleClass().add("stats-row");
    capNhatKhuThongKeDatSan(khuThongKe);
    return khuThongKe;
}

private static void capNhatKhuThongKeDatSan(HBox khuThongKe) {
    if (khuThongKe == null) {
        return;
    }

    khuThongKe.getChildren().clear();

    khuThongKe.getChildren().addAll(
            taoTheThongKeDatSan("Tổng đơn", demTongDonDatSan()),
            taoTheThongKeDatSan("Chờ xử lý", demTheoTrangThaiDatSan("CHỜ XỬ LÝ")),
            taoTheThongKeDatSan("Đã xác nhận", demTheoTrangThaiDatSan("ĐÃ XÁC NHẬN")),
            taoTheThongKeDatSan("Đang sử dụng", demTheoTrangThaiDatSan("ĐÃ CHECK-IN")),
            taoTheThongKeDatSan("Hoàn thành", demTheoTrangThaiDatSan("ĐÃ HOÀN THÀNH"))
    );
}

private static VBox taoTheThongKeDatSan(String tieuDe, int soLuong) {
    VBox the = new VBox(6);
    the.getStyleClass().add("booking-stat-card");
    the.setPrefWidth(160);
    the.setPadding(new Insets(14));

    Label lblTieuDe = new Label(tieuDe);
    lblTieuDe.getStyleClass().add("stat-title");

    Label lblSoLuong = new Label(String.valueOf(soLuong));
    lblSoLuong.getStyleClass().add("stat-value");

    the.getChildren().addAll(lblTieuDe, lblSoLuong);

    return the;
}

private static int demTongDonDatSan() {
    return danhSachDatSan.size();
}

private static int demTheoTrangThaiDatSan(String trangThaiCanDem) {
    int soLuong = 0;

    for (DatSanRow dong : danhSachDatSan) {
        String trangThai = dong.trangThaiProperty().get();

        if (trangThai != null && trangThai.equalsIgnoreCase(trangThaiCanDem)) {
            soLuong++;
        }
    }

    return soLuong;
}

    private static TableView<DatSanRow> taoBangDatSan() {
        TableView<DatSanRow> bang = new TableView<>();

        TableColumn<DatSanRow, String> cotMaDon = new TableColumn<>("Mã đơn");
        cotMaDon.setCellValueFactory(data -> data.getValue().maDonProperty());

        TableColumn<DatSanRow, String> cotMaKhachHang = new TableColumn<>("Mã khách hàng");
        cotMaKhachHang.setCellValueFactory(data -> data.getValue().maKhachHangProperty());

        TableColumn<DatSanRow, String> cotTenKhachHang = new TableColumn<>("Tên khách hàng");
        cotTenKhachHang.setCellValueFactory(data -> data.getValue().tenKhachHangProperty());

        TableColumn<DatSanRow, String> cotTenSan = new TableColumn<>("Sân");
        cotTenSan.setCellValueFactory(data -> data.getValue().tenSanProperty());

        TableColumn<DatSanRow, String> cotThoiGian = new TableColumn<>("Thời gian");
        cotThoiGian.setCellValueFactory(data -> data.getValue().thoiGianProperty());

        TableColumn<DatSanRow, String> cotTrangThai = new TableColumn<>("Trạng thái");
cotTrangThai.setCellValueFactory(data -> data.getValue().trangThaiProperty());

cotTrangThai.setCellFactory(column -> new TableCell<DatSanRow, String>() {
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
        badge.getStyleClass().add(layStyleTrangThaiDatSan(trangThai));

        setText(null);
        setGraphic(badge);
    }
});

        cotTrangThai.setCellFactory(column -> new TableCell<DatSanRow, String>() {
    @Override
    protected void updateItem(String trangThai, boolean empty) {
        super.updateItem(trangThai, empty);

        if (empty || trangThai == null || trangThai.trim().isEmpty()) {
            setText(null);
            setGraphic(null);
            return;
        }

        Label nhanTrangThai = new Label(trangThai);
        nhanTrangThai.getStyleClass().add("status-badge");
        nhanTrangThai.getStyleClass().add(layClassTrangThai(trangThai));

        setText(null);
        setGraphic(nhanTrangThai);
    }
});

        bang.getColumns().add(cotMaDon);
        bang.getColumns().add(cotMaKhachHang);
        bang.getColumns().add(cotTenKhachHang);
        bang.getColumns().add(cotTenSan);
        bang.getColumns().add(cotThoiGian);
        bang.getColumns().add(cotTrangThai);

        bang.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        bang.setPrefHeight(520);

        return bang;
    }

    private static void taiLaiDanhSachDatSan() {
    try {
        danhSachDatSan.clear();
        danhSachDatSan.addAll(datSanDAO.layDanhSachDatSanChoNhanVien());

        capNhatKhuThongKeDatSan(khuThongKeDatSan);

    } catch (SQLException e) {
        e.printStackTrace();
        hienThongBaoLoi("Không thể tải danh sách đặt sân từ cơ sở dữ liệu.\nChi tiết lỗi: " + e.getMessage());
    }
}

    private static void moHopThoaiTaoDon() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Tạo đơn đặt sân");
        dialog.setHeaderText("Nhập thông tin đơn đặt sân");
        ganCssChoDialog(dialog);

        ButtonType nutLuu = new ButtonType("Lưu đơn", ButtonBar.ButtonData.OK_DONE);
        ButtonType nutHuy = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(nutLuu, nutHuy);

        TextField oMaSan = new TextField();
        oMaSan.setPromptText("Ví dụ: S001 hoặc 1");

        TextField oMaKhachHang = new TextField();
        oMaKhachHang.setPromptText("Nhập mã KH hoặc SĐT, ví dụ: KH001 hoặc 09xxxxxxxx");

        TextField oTenKhachHang = new TextField();
        oTenKhachHang.setPromptText("Nhập tên khách hàng");

        TextField oSoDienThoai = new TextField();
        oSoDienThoai.setPromptText("Nhập số điện thoại khách hàng");

        oMaKhachHang.focusedProperty().addListener((observable, daFocusCu, dangFocusMoi) -> {
            if (dangFocusMoi) {
                return;
            }

            String giaTriNhap = layText(oMaKhachHang);

            if (giaTriNhap.isEmpty()) {
                return;
            }

            try {
                DatSanDAO.ThongTinKhachHang thongTinKhachHang =
                        datSanDAO.timThongTinKhachHangTheoMaHoacSdt(giaTriNhap);

                if (thongTinKhachHang != null) {
                    oTenKhachHang.setText(thongTinKhachHang.getTenKhachHang());
                    oSoDienThoai.setText(thongTinKhachHang.getSoDienThoai());
                } else {
                    oTenKhachHang.clear();
                    oSoDienThoai.clear();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                hienThongBaoLoi("Không thể tìm thông tin khách hàng.\nChi tiết lỗi: " + e.getMessage());
            }
        });

        DatePicker oNgayDat = new DatePicker();
        oNgayDat.setValue(LocalDate.now());

        ComboBox<String> oGioBatDau = new ComboBox<>(taoDanhSachGio());
        oGioBatDau.setEditable(true);
        oGioBatDau.setVisibleRowCount(8);
        oGioBatDau.setValue("08:00");

        ComboBox<String> oGioKetThuc = new ComboBox<>(taoDanhSachGio());
        oGioKetThuc.setEditable(true);
        oGioKetThuc.setVisibleRowCount(8);
        oGioKetThuc.setValue("10:00");

        TextField oGhiChu = new TextField();
        oGhiChu.setPromptText("Ghi chú nếu có");

        GridPane form = taoFormDatSan(
                oMaSan,
                oMaKhachHang,
                oTenKhachHang,
                oSoDienThoai,
                oNgayDat,
                oGioBatDau,
                oGioKetThuc,
                oGhiChu
        );

        dialog.getDialogPane().setContent(form);

        dialog.showAndWait().ifPresent(ketQua -> {
            if (ketQua != nutLuu) {
                return;
            }

            String maSanNhap = layText(oMaSan);

            Integer maSan;

            try {
                maSan = datSanDAO.timMaSanTheoMaNhap(maSanNhap);
            } catch (SQLException e) {
                e.printStackTrace();
                hienThongBaoLoi("Không thể kiểm tra mã sân.\nChi tiết lỗi: " + e.getMessage());
                return;
            }

            if (maSan == null) {
                hienThongBaoLoi("Không tìm thấy sân với mã: " + maSanNhap);
                return;
            }

            String maKhachHangNhap = layText(oMaKhachHang);
            String tenKhachHangNhap = layText(oTenKhachHang);
            String soDienThoaiNhap = layText(oSoDienThoai);

            DatSanDAO.ThongTinKhachHang thongTinKhachHang = null;

            try {
                if (!maKhachHangNhap.isEmpty()) {
                    thongTinKhachHang = datSanDAO.timThongTinKhachHangTheoMaHoacSdt(maKhachHangNhap);
                }

                if (thongTinKhachHang == null && !soDienThoaiNhap.isEmpty()) {
                    thongTinKhachHang = datSanDAO.timThongTinKhachHangTheoMaHoacSdt(soDienThoaiNhap);
                }

                if (thongTinKhachHang == null) {
                    if (tenKhachHangNhap.isEmpty()) {
                        hienThongBaoLoi("Vui lòng nhập tên khách hàng mới.");
                        return;
                    }

                    if (soDienThoaiNhap.isEmpty()) {
                        hienThongBaoLoi("Vui lòng nhập số điện thoại khách hàng mới.");
                        return;
                    }

                    thongTinKhachHang = datSanDAO.taoKhachHangMoiNhanh(
                            tenKhachHangNhap,
                            soDienThoaiNhap
                    );
                }

            } catch (SQLException e) {
                e.printStackTrace();
                hienThongBaoLoi("Không thể kiểm tra hoặc tạo thông tin khách hàng.\nChi tiết lỗi: " + e.getMessage());
                return;
            }

            Integer maKhachHang = thongTinKhachHang.getMaKhachHang();

            oMaKhachHang.setText(thongTinKhachHang.getMaKhachHangCode());
            oTenKhachHang.setText(thongTinKhachHang.getTenKhachHang());
            oSoDienThoai.setText(thongTinKhachHang.getSoDienThoai());

            LocalDate ngayDat = oNgayDat.getValue();
            String gioBatDau = layGiaTriComboBox(oGioBatDau);
            String gioKetThuc = layGiaTriComboBox(oGioKetThuc);

            String loi = datSanService.kiemTraDuLieuDatSan(
                    maSan,
                    maKhachHang,
                    ngayDat,
                    gioBatDau,
                    gioKetThuc,
                    null
            );

            if (loi != null) {
                hienThongBaoLoi(loi);
                return;
            }

            String ghiChu = layText(oGhiChu);

            try {
                boolean thanhCong = datSanDAO.themDonDatSanNhanVien(
                        maSan,
                        maKhachHang,
                        ngayDat,
                        gioBatDau,
                        gioKetThuc,
                        ghiChu
                );

                if (thanhCong) {
                    hienThongBaoThongTin("Tạo đơn đặt sân thành công.");
                    taiLaiDanhSachDatSan();
                } else {
                    hienThongBaoLoi("Tạo đơn đặt sân thất bại.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                hienThongBaoLoi("Không thể lưu đơn đặt sân vào cơ sở dữ liệu.\nChi tiết lỗi: " + e.getMessage());
            }
        });
    }

    private static void moHopThoaiCapNhatDon(DatSanRow dongDuocChon) {
        if (dongDuocChon == null) {
            hienThongBaoLoi("Vui lòng chọn một đơn đặt sân cần cập nhật.");
            return;
        }

        String maDon = dongDuocChon.maDonProperty().get();

        DatSanDAO.ChiTietDatSan chiTietDatSan;

        try {
            chiTietDatSan = datSanDAO.layChiTietDatSanTheoMaDon(maDon);
        } catch (SQLException e) {
            e.printStackTrace();
            hienThongBaoLoi("Không thể tải thông tin chi tiết đơn đặt sân.\nChi tiết lỗi: " + e.getMessage());
            return;
        }

        if (chiTietDatSan == null) {
            hienThongBaoLoi("Không tìm thấy thông tin đơn đặt sân.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Cập nhật đơn đặt sân");
        dialog.setHeaderText("Cập nhật thông tin đơn " + maDon);
        ganCssChoDialog(dialog);

        ButtonType nutLuu = new ButtonType("Lưu cập nhật", ButtonBar.ButtonData.OK_DONE);
        ButtonType nutHuy = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(nutLuu, nutHuy);

        TextField oMaSan = new TextField();
        oMaSan.setPromptText("Ví dụ: S001");
        oMaSan.setText(chiTietDatSan.getMaSanCode());

        TextField oMaKhachHang = new TextField();
        oMaKhachHang.setPromptText("Nhập mã KH hoặc SĐT");
        oMaKhachHang.setText(chiTietDatSan.getMaKhachHangCode());

        TextField oTenKhachHang = new TextField();
        oTenKhachHang.setPromptText("Tên khách hàng");

        TextField oSoDienThoai = new TextField();
        oSoDienThoai.setPromptText("Số điện thoại khách hàng");

        try {
            DatSanDAO.ThongTinKhachHang thongTinKhachHang =
                    datSanDAO.timThongTinKhachHangTheoMaHoacSdt(chiTietDatSan.getMaKhachHangCode());

            if (thongTinKhachHang != null) {
                oTenKhachHang.setText(thongTinKhachHang.getTenKhachHang());
                oSoDienThoai.setText(thongTinKhachHang.getSoDienThoai());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        oMaKhachHang.focusedProperty().addListener((observable, daFocusCu, dangFocusMoi) -> {
            if (dangFocusMoi) {
                return;
            }

            String giaTriNhap = layText(oMaKhachHang);

            if (giaTriNhap.isEmpty()) {
                return;
            }

            try {
                DatSanDAO.ThongTinKhachHang thongTinKhachHang =
                        datSanDAO.timThongTinKhachHangTheoMaHoacSdt(giaTriNhap);

                if (thongTinKhachHang != null) {
                    oTenKhachHang.setText(thongTinKhachHang.getTenKhachHang());
                    oSoDienThoai.setText(thongTinKhachHang.getSoDienThoai());
                }

            } catch (SQLException e) {
                e.printStackTrace();
                hienThongBaoLoi("Không thể tìm thông tin khách hàng.\nChi tiết lỗi: " + e.getMessage());
            }
        });

        DatePicker oNgayDat = new DatePicker();
        oNgayDat.setValue(chiTietDatSan.getNgayDat());

        ComboBox<String> oGioBatDau = new ComboBox<>(taoDanhSachGio());
        oGioBatDau.setEditable(true);
        oGioBatDau.setVisibleRowCount(8);
        oGioBatDau.setValue(chiTietDatSan.getGioBatDau());

        ComboBox<String> oGioKetThuc = new ComboBox<>(taoDanhSachGio());
        oGioKetThuc.setEditable(true);
        oGioKetThuc.setVisibleRowCount(8);
        oGioKetThuc.setValue(chiTietDatSan.getGioKetThuc());

        TextField oGhiChu = new TextField();
        oGhiChu.setPromptText("Ghi chú nếu có");
        oGhiChu.setText(chiTietDatSan.getGhiChu() == null ? "" : chiTietDatSan.getGhiChu());

        GridPane form = taoFormDatSan(
                oMaSan,
                oMaKhachHang,
                oTenKhachHang,
                oSoDienThoai,
                oNgayDat,
                oGioBatDau,
                oGioKetThuc,
                oGhiChu
        );

        dialog.getDialogPane().setContent(form);

        dialog.showAndWait().ifPresent(ketQua -> {
            if (ketQua != nutLuu) {
                return;
            }

            String maSanNhap = layText(oMaSan);
            String maKhachHangNhap = layText(oMaKhachHang);

            Integer maSan;
            DatSanDAO.ThongTinKhachHang thongTinKhachHang;

            try {
                maSan = datSanDAO.timMaSanTheoMaNhap(maSanNhap);
                thongTinKhachHang = datSanDAO.timThongTinKhachHangTheoMaHoacSdt(maKhachHangNhap);
            } catch (SQLException e) {
                e.printStackTrace();
                hienThongBaoLoi("Không thể kiểm tra thông tin sân hoặc khách hàng.\nChi tiết lỗi: " + e.getMessage());
                return;
            }

            if (maSan == null) {
                hienThongBaoLoi("Không tìm thấy sân với mã: " + maSanNhap);
                return;
            }

            if (thongTinKhachHang == null) {
                hienThongBaoLoi("Không tìm thấy khách hàng với mã hoặc SĐT: " + maKhachHangNhap);
                return;
            }

            Integer maKhachHang = thongTinKhachHang.getMaKhachHang();

            LocalDate ngayDat = oNgayDat.getValue();
            String gioBatDau = layGiaTriComboBox(oGioBatDau);
            String gioKetThuc = layGiaTriComboBox(oGioKetThuc);

            Integer maDatSanNoiBo;

            try {
                maDatSanNoiBo = datSanDAO.timMaDatSanNoiBoTheoMaDon(maDon);
            } catch (SQLException e) {
                e.printStackTrace();
                hienThongBaoLoi("Không thể kiểm tra mã đơn đặt sân.\nChi tiết lỗi: " + e.getMessage());
                return;
            }

            String loi = datSanService.kiemTraDuLieuDatSan(
                    maSan,
                    maKhachHang,
                    ngayDat,
                    gioBatDau,
                    gioKetThuc,
                    maDatSanNoiBo
            );

            if (loi != null) {
                hienThongBaoLoi(loi);
                return;
            }

            String ghiChu = layText(oGhiChu);

            try {
                boolean thanhCong = datSanDAO.capNhatDonDatSanTheoMaDon(
                        maDon,
                        maSan,
                        maKhachHang,
                        ngayDat,
                        gioBatDau,
                        gioKetThuc,
                        ghiChu
                );

                if (thanhCong) {
                    hienThongBaoThongTin("Cập nhật đơn đặt sân thành công.");
                    taiLaiDanhSachDatSan();
                } else {
                    hienThongBaoLoi("Không thể cập nhật đơn. Đơn có thể đã hủy, đã check-in hoặc đã hoàn thành.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                hienThongBaoLoi("Không thể cập nhật đơn đặt sân.\nChi tiết lỗi: " + e.getMessage());
            }
        });
    }

    private static GridPane taoFormDatSan(TextField oMaSan,
                                      TextField oMaKhachHang,
                                      TextField oTenKhachHang,
                                      TextField oSoDienThoai,
                                      DatePicker oNgayDat,
                                      ComboBox<String> oGioBatDau,
                                      ComboBox<String> oGioKetThuc,
                                      TextField oGhiChu) {
    GridPane form = new GridPane();
    form.setHgap(14);
    form.setVgap(14);
    form.setPadding(new Insets(18));
    form.getStyleClass().add("form-grid");

    thietLapInputForm(oMaSan);
    thietLapInputForm(oMaKhachHang);
    thietLapInputForm(oTenKhachHang);
    thietLapInputForm(oSoDienThoai);
    thietLapInputForm(oGhiChu);

    oNgayDat.setPrefWidth(320);
    oNgayDat.setMinWidth(320);
    oNgayDat.setPrefHeight(42);

    thietLapComboBoxGio(oGioBatDau);
    thietLapComboBoxGio(oGioKetThuc);

    form.add(taoNhanForm("Mã sân:"), 0, 0);
    form.add(oMaSan, 1, 0);

    form.add(taoNhanForm("Mã khách hàng:"), 0, 1);
    form.add(oMaKhachHang, 1, 1);

    form.add(taoNhanForm("Tên khách hàng:"), 0, 2);
    form.add(oTenKhachHang, 1, 2);

    form.add(taoNhanForm("Số điện thoại:"), 0, 3);
    form.add(oSoDienThoai, 1, 3);

    form.add(taoNhanForm("Ngày đặt:"), 0, 4);
    form.add(oNgayDat, 1, 4);

    form.add(taoNhanForm("Giờ bắt đầu:"), 0, 5);
    form.add(oGioBatDau, 1, 5);

    form.add(taoNhanForm("Giờ kết thúc:"), 0, 6);
    form.add(oGioKetThuc, 1, 6);

    form.add(taoNhanForm("Ghi chú:"), 0, 7);
    form.add(oGhiChu, 1, 7);

    return form;
}

    private static Label taoNhanForm(String noiDung) {
    Label label = new Label(noiDung);
    label.getStyleClass().add("form-label");
    label.setMinWidth(120);
    return label;
}

private static void thietLapInputForm(TextField textField) {
    textField.setPrefWidth(260);
    textField.getStyleClass().add("input-field");
}
    private static void thietLapComboBoxGio(ComboBox<String> comboBox) {
    comboBox.setPrefWidth(320);
    comboBox.setMinWidth(320);
    comboBox.setPrefHeight(42);
    comboBox.getStyleClass().add("time-combo-box");
}

    private static boolean coTheCapNhatDon(String trangThai) {
        String trangThaiChuanHoa = chuanHoaTrangThai(trangThai);

        return trangThaiChuanHoa.equals("CHỜ XỬ LÝ")
                || trangThaiChuanHoa.equals("ĐÃ XÁC NHẬN")
                || trangThaiChuanHoa.equals("CHO_XU_LY")
                || trangThaiChuanHoa.equals("DA_XAC_NHAN");
    }

    private static boolean coTheHuyDon(String trangThai) {
        String trangThaiChuanHoa = chuanHoaTrangThai(trangThai);

        return trangThaiChuanHoa.equals("CHỜ XỬ LÝ")
                || trangThaiChuanHoa.equals("ĐÃ XÁC NHẬN")
                || trangThaiChuanHoa.equals("CHO_XU_LY")
                || trangThaiChuanHoa.equals("DA_XAC_NHAN");
    }

    private static boolean coTheXacNhanDenSan(String trangThai) {
        String trangThaiChuanHoa = chuanHoaTrangThai(trangThai);

        return trangThaiChuanHoa.equals("CHỜ XỬ LÝ")
                || trangThaiChuanHoa.equals("ĐÃ XÁC NHẬN")
                || trangThaiChuanHoa.equals("CHO_XU_LY")
                || trangThaiChuanHoa.equals("DA_XAC_NHAN");
    }

    private static boolean coTheHoanThanhDon(String trangThai) {
        String trangThaiChuanHoa = chuanHoaTrangThai(trangThai);

        return trangThaiChuanHoa.equals("ĐÃ CHECK-IN")
                || trangThaiChuanHoa.equals("DA_CHECK_IN");
    }

    private static String layClassTrangThai(String trangThai) {
    String trangThaiChuanHoa = chuanHoaTrangThai(trangThai);

    return switch (trangThaiChuanHoa) {
        case "CHỜ XỬ LÝ", "CHO_XU_LY" -> "status-pending";
        case "ĐÃ XÁC NHẬN", "DA_XAC_NHAN" -> "status-confirmed";
        case "ĐÃ CHECK-IN", "DA_CHECK_IN" -> "status-checkin";
        case "ĐÃ HOÀN THÀNH", "DA_HOAN_THANH" -> "status-completed";
        case "ĐÃ HỦY", "DA_HUY" -> "status-cancelled";
        default -> "status-default";
    };
}

    private static String chuanHoaTrangThai(String trangThai) {
        if (trangThai == null) {
            return "";
        }

        return trangThai.trim().toUpperCase(Locale.ROOT);
    }

    private static String layStyleTrangThaiDatSan(String trangThai) {
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

    private static ObservableList<String> taoDanhSachGio() {
        ObservableList<String> danhSachGio = FXCollections.observableArrayList();

        for (int gio = 5; gio <= 23; gio++) {
            danhSachGio.add(String.format("%02d:00", gio));
            danhSachGio.add(String.format("%02d:30", gio));
        }

        return danhSachGio;
    }

    private static String layGiaTriComboBox(ComboBox<String> comboBox) {
        if (comboBox.getEditor() != null) {
            String giaTriTuNhap = comboBox.getEditor().getText();

            if (giaTriTuNhap != null && !giaTriTuNhap.trim().isEmpty()) {
                return giaTriTuNhap.trim();
            }
        }

        String giaTriDuocChon = comboBox.getValue();
        return giaTriDuocChon == null ? "" : giaTriDuocChon.trim();
    }

    private static String layText(TextField textField) {
        if (textField == null || textField.getText() == null) {
            return "";
        }

        return textField.getText().trim();
    }

    private static void ganCssChoDialog(Dialog<ButtonType> dialog) {
    var css = QuanLyDatSanPage.class.getResource("/css/nhanvien-dashboard.css");

    if (css != null) {
        dialog.getDialogPane().getStylesheets().add(css.toExternalForm());
    }
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