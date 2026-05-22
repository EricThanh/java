package com.football.management.ui.auth;

import com.football.management.app.AppNavigator;
import com.football.management.dao.KhachHangDAO;
import com.football.management.dao.TaiKhoanDAO;
import com.football.management.model.KhachHang;
import com.football.management.model.TaiKhoan;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;

public class DangKyPage {

    public static Scene createScene() {
        Label lblLogo = new Label("QUẢN LÝ SÂN BÓNG");
        lblLogo.getStyleClass().add("app-logo");

        Label lblTieuDe = new Label("ĐĂNG KÝ");
        lblTieuDe.getStyleClass().add("login-title");

        Label lblMoTa = new Label("Tạo tài khoản để đặt sân, thanh toán và theo dõi lịch sử");
        lblMoTa.getStyleClass().add("login-subtitle");
        lblMoTa.setWrapText(true);

        TextField txtHoTen = new TextField();
        txtHoTen.setPromptText("Họ tên");
        txtHoTen.getStyleClass().add("input-field");

        TextField txtTenDangNhap = new TextField();
        txtTenDangNhap.setPromptText("Tên đăng nhập");
        txtTenDangNhap.getStyleClass().add("input-field");

        PasswordField txtMatKhau = new PasswordField();
        txtMatKhau.setPromptText("Mật khẩu");
        txtMatKhau.getStyleClass().add("input-field");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email");
        txtEmail.getStyleClass().add("input-field");

        TextField txtSoDienThoai = new TextField();
        txtSoDienThoai.setPromptText("Số điện thoại");
        txtSoDienThoai.getStyleClass().add("input-field");

        TextField txtDiaChi = new TextField();
        txtDiaChi.setPromptText("Địa chỉ");
        txtDiaChi.getStyleClass().add("input-field");

        ComboBox<String> cbGioiTinh = new ComboBox<>();
        cbGioiTinh.getItems().addAll("Nam", "Nữ", "Khác");
        cbGioiTinh.setPromptText("Giới tính");
        cbGioiTinh.getStyleClass().add("input-field");
        cbGioiTinh.setMaxWidth(Double.MAX_VALUE);

        Button btnDangKy = new Button("Đăng ký");
        btnDangKy.getStyleClass().add("primary-button");
        btnDangKy.setMaxWidth(Double.MAX_VALUE);

        Hyperlink linkDangNhap = new Hyperlink("Đã có tài khoản? Đăng nhập");
        linkDangNhap.getStyleClass().add("text-link");

        Label lblThongBao = new Label();
        lblThongBao.getStyleClass().add("error-text");

        linkDangNhap.setOnAction(e ->
                AppNavigator.goTo(DangNhapPage.createScene(), "Đăng nhập")
        );

        btnDangKy.setOnAction(e -> {
            lblThongBao.setText("");

            String hoTen = txtHoTen.getText().trim();
            String tenDangNhap = txtTenDangNhap.getText().trim();
            String matKhau = txtMatKhau.getText().trim();
            String email = txtEmail.getText().trim();
            String soDienThoai = txtSoDienThoai.getText().trim();
            String diaChi = txtDiaChi.getText().trim();
            String gioiTinhChon = cbGioiTinh.getValue();

            if (hoTen.isEmpty() || tenDangNhap.isEmpty() || matKhau.isEmpty()
                    || email.isEmpty() || soDienThoai.isEmpty() || gioiTinhChon == null) {
                lblThongBao.setText("Vui lòng nhập đầy đủ thông tin");
                return;
            }

            String gioiTinh;
            if ("Nam".equals(gioiTinhChon)) {
                gioiTinh = "NAM";
            } else if ("Nữ".equals(gioiTinhChon)) {
                gioiTinh = "NU";
            } else {
                gioiTinh = "KHAC";
            }

            TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
            KhachHangDAO khachHangDAO = new KhachHangDAO();

            if (taiKhoanDAO.tonTaiTenDangNhap(tenDangNhap)) {
                lblThongBao.setText("Tên đăng nhập đã tồn tại");
                return;
            }

            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setTenDangNhap(tenDangNhap);
            taiKhoan.setMatKhau(matKhau);
            taiKhoan.setHoTen(hoTen);
            taiKhoan.setEmail(email);
            taiKhoan.setSoDienThoai(soDienThoai);
            taiKhoan.setMaVaiTro(3); // KHACH_HANG
            taiKhoan.setTrangThaiTaiKhoan("HOAT_DONG");
            taiKhoan.setNgayTao(LocalDateTime.now());
            taiKhoan.setNgayCapNhat(LocalDateTime.now());

            int maTaiKhoan = taiKhoanDAO.themTaiKhoanVaLayMa(taiKhoan);

            if (maTaiKhoan <= 0) {
                lblThongBao.setText("Đăng ký thất bại ở bước tạo tài khoản");
                return;
            }

            KhachHang khachHang = new KhachHang();
            khachHang.setMaTaiKhoan(maTaiKhoan);
            khachHang.setMaKhachHangCode(khachHangDAO.taoMaKhachHangTuDong());
            khachHang.setGioiTinh(gioiTinh);
            khachHang.setDiaChi(diaChi);
            khachHang.setTrangThaiKhachHang("HOAT_DONG");
            khachHang.setNgayTao(LocalDateTime.now());

            boolean themKhachHang = khachHangDAO.themKhachHang(khachHang);

            if (!themKhachHang) {
                lblThongBao.setText("Đăng ký thất bại ở bước tạo khách hàng");
                return;
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Đăng ký thành công. Mời bạn đăng nhập.");
            alert.showAndWait();

            AppNavigator.goTo(DangNhapPage.createScene(), "Đăng nhập");
        });

        VBox card = new VBox(
                16,
                lblLogo,
                lblTieuDe,
                lblMoTa,
                txtHoTen,
                txtTenDangNhap,
                txtMatKhau,
                txtEmail,
                txtSoDienThoai,
                txtDiaChi,
                cbGioiTinh,
                btnDangKy,
                linkDangNhap,
                lblThongBao
        );

        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("login-card");
        card.setMaxWidth(720);
        card.setMinWidth(680);

        VBox root = new VBox(card);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("page-root");

        Scene scene = new Scene(root, 1100, 760);
        scene.getStylesheets().add(
                DangKyPage.class.getResource("/css/login.css").toExternalForm()
        );

        return scene;
    }
}