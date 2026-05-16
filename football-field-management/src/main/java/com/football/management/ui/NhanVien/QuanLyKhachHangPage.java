package com.football.management.ui.NhanVien;

import com.football.management.dao.KhachHangDAO;

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

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TableCell;

import java.sql.SQLException;

public class QuanLyKhachHangPage {

    private static final KhachHangDAO khachHangDAO = new KhachHangDAO();

    private static final ObservableList<KhachHangRow> danhSachKhachHang =
            FXCollections.observableArrayList();

    private static TableView<KhachHangRow> bangKhachHang;

    public static Node createView() {
        VBox khungChinh = new VBox(16);
        khungChinh.setPadding(new Insets(24));
        khungChinh.getStyleClass().add("content-root");

        Label tieuDe = new Label("Quản lý khách hàng");
        tieuDe.getStyleClass().add("page-title");

        Label moTa = new Label("Theo dõi, tìm kiếm và quản lý thông tin khách hàng");
        moTa.getStyleClass().add("section-subtitle");

        HBox thanhCongCu = new HBox(10);
        thanhCongCu.getStyleClass().add("filter-bar");

        TextField oTimKiem = new TextField();
        oTimKiem.setPromptText("Tìm theo mã KH, họ tên, SĐT, email, trạng thái...");
        oTimKiem.getStyleClass().add("search-field");
        oTimKiem.setPrefWidth(380);

        Button nutThem = new Button("Thêm khách hàng");
        nutThem.getStyleClass().add("primary-button");

        Button nutCapNhat = new Button("Cập nhật");
        nutCapNhat.getStyleClass().add("light-button");

        Button nutNgungHoatDong = new Button("Ngưng hoạt động");
        nutNgungHoatDong.getStyleClass().add("secondary-button");

        Button nutTaiLai = new Button("Tải lại");
        nutTaiLai.getStyleClass().add("light-button");

        thanhCongCu.getChildren().addAll(
                oTimKiem,
                nutThem,
                nutCapNhat,
                nutNgungHoatDong,
                nutTaiLai
        );

        bangKhachHang = taoBangKhachHang();

        FilteredList<KhachHangRow> danhSachLoc =
                new FilteredList<>(danhSachKhachHang, item -> true);

        oTimKiem.textProperty().addListener((observable, giaTriCu, giaTriMoi) -> {
            String tuKhoa = giaTriMoi == null ? "" : giaTriMoi.toLowerCase().trim();

            danhSachLoc.setPredicate(item -> {
                if (tuKhoa.isEmpty()) {
                    return true;
                }

                return chuaTuKhoa(item.maKhachHangProperty().get(), tuKhoa)
                        || chuaTuKhoa(item.hoTenProperty().get(), tuKhoa)
                        || chuaTuKhoa(item.soDienThoaiProperty().get(), tuKhoa)
                        || chuaTuKhoa(item.emailProperty().get(), tuKhoa)
                        || chuaTuKhoa(item.diaChiProperty().get(), tuKhoa)
                        || chuaTuKhoa(item.trangThaiProperty().get(), tuKhoa);
            });
        });

        bangKhachHang.setItems(danhSachLoc);

        nutTaiLai.setOnAction(event -> taiLaiDanhSachKhachHang());

        nutThem.setOnAction(event -> moHopThoaiThemKhachHang());

        nutCapNhat.setOnAction(event -> {
    KhachHangRow dongDuocChon = bangKhachHang.getSelectionModel().getSelectedItem();

    if (dongDuocChon == null) {
        hienThongBaoLoi("Vui lòng chọn một khách hàng cần cập nhật.");
        return;
    }

    moHopThoaiCapNhatKhachHang(dongDuocChon);
});

        nutNgungHoatDong.setOnAction(event -> {
    KhachHangRow dongDuocChon = bangKhachHang.getSelectionModel().getSelectedItem();

    if (dongDuocChon == null) {
        hienThongBaoLoi("Vui lòng chọn một khách hàng cần ngưng hoạt động.");
        return;
    }

    String trangThai = dongDuocChon.trangThaiProperty().get();

    if (!coTheNgungHoatDongKhachHang(trangThai)) {
        hienThongBaoLoi("Khách hàng này đã ngưng hoạt động hoặc không thể ngưng hoạt động.");
        return;
    }

    String maKhachHang = dongDuocChon.maKhachHangProperty().get();
    String hoTen = dongDuocChon.hoTenProperty().get();

    Alert xacNhan = new Alert(Alert.AlertType.CONFIRMATION);
    xacNhan.setTitle("Xác nhận ngưng hoạt động");
    xacNhan.setHeaderText(null);
    xacNhan.setContentText(
            "Bạn có chắc chắn muốn ngưng hoạt động khách hàng:\n" +
            maKhachHang + " - " + hoTen + " không?"
    );

    ButtonType nutDongY = new ButtonType("Ngưng hoạt động", ButtonBar.ButtonData.OK_DONE);
    ButtonType nutKhong = new ButtonType("Không", ButtonBar.ButtonData.CANCEL_CLOSE);
    xacNhan.getButtonTypes().setAll(nutDongY, nutKhong);

    xacNhan.showAndWait().ifPresent(ketQua -> {
        if (ketQua != nutDongY) {
            return;
        }

        try {
            boolean thanhCong = khachHangDAO.ngungHoatDongKhachHangChoNhanVien(maKhachHang);
            if (thanhCong) {
                hienThongBaoThongTin("Ngưng hoạt động khách hàng thành công.");
                taiLaiDanhSachKhachHang();
            } else {
                hienThongBaoLoi("Không thể ngưng hoạt động khách hàng. Khách hàng có thể đã ngưng hoạt động.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            hienThongBaoLoi("Không thể ngưng hoạt động khách hàng.\nChi tiết lỗi: " + e.getMessage());
        }
    });
});

        taiLaiDanhSachKhachHang();

        khungChinh.getChildren().addAll(
                tieuDe,
                moTa,
                thanhCongCu,
                bangKhachHang
        );

        return khungChinh;
    }

    private static void moHopThoaiThemKhachHang() {
    Dialog<ButtonType> dialog = new Dialog<>();
    dialog.setTitle("Thêm khách hàng");
    dialog.setHeaderText("Nhập thông tin khách hàng mới");

    ButtonType nutLuu = new ButtonType("Lưu khách hàng", ButtonBar.ButtonData.OK_DONE);
    ButtonType nutHuy = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
    dialog.getDialogPane().getButtonTypes().addAll(nutLuu, nutHuy);

    TextField oHoTen = new TextField();
    oHoTen.setPromptText("Nhập họ tên khách hàng");

    TextField oSoDienThoai = new TextField();
    oSoDienThoai.setPromptText("Nhập số điện thoại");

    TextField oEmail = new TextField();
    oEmail.setPromptText("Nhập email nếu có");

    TextField oDiaChi = new TextField();
    oDiaChi.setPromptText("Nhập địa chỉ nếu có");

    GridPane form = new GridPane();
    form.setHgap(12);
    form.setVgap(12);
    form.setPadding(new Insets(12));

    form.add(new Label("Họ tên:"), 0, 0);
    form.add(oHoTen, 1, 0);

    form.add(new Label("Số điện thoại:"), 0, 1);
    form.add(oSoDienThoai, 1, 1);

    form.add(new Label("Email:"), 0, 2);
    form.add(oEmail, 1, 2);

    form.add(new Label("Địa chỉ:"), 0, 3);
    form.add(oDiaChi, 1, 3);

    dialog.getDialogPane().setContent(form);

    dialog.showAndWait().ifPresent(ketQua -> {
        if (ketQua != nutLuu) {
            return;
        }

        String hoTen = layText(oHoTen);
        String soDienThoai = layText(oSoDienThoai);
        String email = layText(oEmail);
        String diaChi = layText(oDiaChi);

        if (hoTen.isEmpty()) {
            hienThongBaoLoi("Vui lòng nhập họ tên khách hàng.");
            return;
        }

        if (soDienThoai.isEmpty()) {
            hienThongBaoLoi("Vui lòng nhập số điện thoại khách hàng.");
            return;
        }

        try {
            boolean thanhCong = khachHangDAO.themKhachHangChoNhanVien(
                    hoTen,
                    soDienThoai,
                    email,
                    diaChi
            );

            if (thanhCong) {
                hienThongBaoThongTin("Thêm khách hàng thành công.");
                taiLaiDanhSachKhachHang();
            } else {
                hienThongBaoLoi("Thêm khách hàng thất bại.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            hienThongBaoLoi("Không thể thêm khách hàng.\nChi tiết lỗi: " + e.getMessage());
        }
    });
}

    private static void moHopThoaiCapNhatKhachHang(KhachHangRow dongDuocChon) {
    if (dongDuocChon == null) {
        hienThongBaoLoi("Vui lòng chọn một khách hàng cần cập nhật.");
        return;
    }

    String maKhachHang = dongDuocChon.maKhachHangProperty().get();

    Dialog<ButtonType> dialog = new Dialog<>();
    dialog.setTitle("Cập nhật khách hàng");
    dialog.setHeaderText("Cập nhật thông tin khách hàng " + maKhachHang);

    ButtonType nutLuu = new ButtonType("Lưu cập nhật", ButtonBar.ButtonData.OK_DONE);
    ButtonType nutHuy = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
    dialog.getDialogPane().getButtonTypes().addAll(nutLuu, nutHuy);

    TextField oHoTen = new TextField();
    oHoTen.setPromptText("Nhập họ tên khách hàng");
    oHoTen.setText(dongDuocChon.hoTenProperty().get());

    TextField oSoDienThoai = new TextField();
    oSoDienThoai.setPromptText("Nhập số điện thoại");
    oSoDienThoai.setText(dongDuocChon.soDienThoaiProperty().get());

    TextField oEmail = new TextField();
    oEmail.setPromptText("Nhập email nếu có");
    oEmail.setText(dongDuocChon.emailProperty().get());

    TextField oDiaChi = new TextField();
    oDiaChi.setPromptText("Nhập địa chỉ nếu có");
    oDiaChi.setText(dongDuocChon.diaChiProperty().get());

    GridPane form = new GridPane();
    form.setHgap(12);
    form.setVgap(12);
    form.setPadding(new Insets(12));

    form.add(new Label("Mã khách hàng:"), 0, 0);
    form.add(new Label(maKhachHang), 1, 0);

    form.add(new Label("Họ tên:"), 0, 1);
    form.add(oHoTen, 1, 1);

    form.add(new Label("Số điện thoại:"), 0, 2);
    form.add(oSoDienThoai, 1, 2);

    form.add(new Label("Email:"), 0, 3);
    form.add(oEmail, 1, 3);

    form.add(new Label("Địa chỉ:"), 0, 4);
    form.add(oDiaChi, 1, 4);

    dialog.getDialogPane().setContent(form);

    dialog.showAndWait().ifPresent(ketQua -> {
        if (ketQua != nutLuu) {
            return;
        }

        String hoTen = layText(oHoTen);
        String soDienThoai = layText(oSoDienThoai);
        String email = layText(oEmail);
        String diaChi = layText(oDiaChi);

        if (hoTen.isEmpty()) {
            hienThongBaoLoi("Vui lòng nhập họ tên khách hàng.");
            return;
        }

        if (soDienThoai.isEmpty()) {
            hienThongBaoLoi("Vui lòng nhập số điện thoại khách hàng.");
            return;
        }

        try {
            boolean thanhCong = khachHangDAO.capNhatKhachHangChoNhanVien(
                    maKhachHang,
                    hoTen,
                    soDienThoai,
                    email,
                    diaChi
            );

            if (thanhCong) {
                hienThongBaoThongTin("Cập nhật khách hàng thành công.");
                taiLaiDanhSachKhachHang();
            } else {
                hienThongBaoLoi("Cập nhật khách hàng thất bại.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            hienThongBaoLoi("Không thể cập nhật khách hàng.\nChi tiết lỗi: " + e.getMessage());
        }
    });
}

    private static TableView<KhachHangRow> taoBangKhachHang() {
        TableView<KhachHangRow> bang = new TableView<>();

        TableColumn<KhachHangRow, String> cotMaKhachHang = new TableColumn<>("Mã khách hàng");
        cotMaKhachHang.setCellValueFactory(data -> data.getValue().maKhachHangProperty());

        TableColumn<KhachHangRow, String> cotHoTen = new TableColumn<>("Họ tên");
        cotHoTen.setCellValueFactory(data -> data.getValue().hoTenProperty());

        TableColumn<KhachHangRow, String> cotSoDienThoai = new TableColumn<>("Số điện thoại");
        cotSoDienThoai.setCellValueFactory(data -> data.getValue().soDienThoaiProperty());

        TableColumn<KhachHangRow, String> cotEmail = new TableColumn<>("Email");
        cotEmail.setCellValueFactory(data -> data.getValue().emailProperty());

        TableColumn<KhachHangRow, String> cotDiaChi = new TableColumn<>("Địa chỉ");
        cotDiaChi.setCellValueFactory(data -> data.getValue().diaChiProperty());

        TableColumn<KhachHangRow, String> cotTrangThai = new TableColumn<>("Trạng thái");
cotTrangThai.setCellValueFactory(data -> data.getValue().trangThaiProperty());

cotTrangThai.setCellFactory(column -> new TableCell<KhachHangRow, String>() {
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
        badge.getStyleClass().add(layStyleTrangThaiKhachHang(trangThai));

        setText(null);
        setGraphic(badge);
    }
});

        bang.getColumns().add(cotMaKhachHang);
        bang.getColumns().add(cotHoTen);
        bang.getColumns().add(cotSoDienThoai);
        bang.getColumns().add(cotEmail);
        bang.getColumns().add(cotDiaChi);
        bang.getColumns().add(cotTrangThai);

        bang.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        bang.setPrefHeight(520);

        return bang;
    }

    private static void taiLaiDanhSachKhachHang() {
        try {
            danhSachKhachHang.clear();
            danhSachKhachHang.addAll(khachHangDAO.layDanhSachKhachHangChoNhanVien());
        } catch (SQLException e) {
            e.printStackTrace();
            hienThongBaoLoi("Không thể tải danh sách khách hàng từ cơ sở dữ liệu.\nChi tiết lỗi: " + e.getMessage());
        }
    }

        private static boolean coTheNgungHoatDongKhachHang(String trangThai) {
    if (trangThai == null) {
        return false;
    }

    String trangThaiChuanHoa = trangThai.trim().toUpperCase();

    return trangThaiChuanHoa.equals("HOẠT ĐỘNG")
            || trangThaiChuanHoa.equals("HOAT_DONG");
}

    private static String layStyleTrangThaiKhachHang(String trangThai) {
    if (trangThai == null) {
        return "status-inactive";
    }

    String trangThaiChuanHoa = trangThai.trim().toUpperCase();

    return switch (trangThaiChuanHoa) {
        case "HOẠT ĐỘNG", "HOAT_DONG" -> "status-active";
        case "NGỪNG HOẠT ĐỘNG", "NGUNG_HOAT_DONG" -> "status-inactive";
        default -> "status-inactive";
    };
}

    private static boolean chuaTuKhoa(String giaTri, String tuKhoa) {
        if (giaTri == null) {
            return false;
        }

        return giaTri.toLowerCase().contains(tuKhoa);
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

    private static String layText(TextField textField) {
    if (textField == null || textField.getText() == null) {
        return "";
    }

    return textField.getText().trim();
}

}