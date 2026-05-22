package com.football.management.ui.auth;

import com.football.management.app.AppNavigator;
import com.football.management.app.AppState;
import com.football.management.ui.ChuSan.ChuSanDashboardPage;
import com.football.management.ui.KhachHang.KhachHangHomePage;
import com.football.management.ui.NhanVien.NhanVienDashboardPage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import com.football.management.dao.TaiKhoanDAO;
import com.football.management.model.TaiKhoan;

public class DangNhapPage {

    public static Scene createScene() {
        Label lblLogo = new Label("QUẢN LÝ SÂN BÓNG");
        lblLogo.getStyleClass().add("app-logo");

        Label lblTieuDe = new Label("ĐĂNG NHẬP");
        lblTieuDe.getStyleClass().add("login-title");

        Label lblMoTa = new Label("Đăng nhập để quản lý sân, lịch đặt và thanh toán");
        lblMoTa.getStyleClass().add("login-subtitle");
        lblMoTa.setWrapText(true);

        TextField txtTenDangNhap = new TextField();
        txtTenDangNhap.setPromptText("Tên đăng nhập");
        txtTenDangNhap.getStyleClass().add("input-field");

        PasswordField txtMatKhau = new PasswordField();
        txtMatKhau.setPromptText("Mật khẩu");
        txtMatKhau.getStyleClass().add("input-field");

        ComboBox<String> cbVaiTro = new ComboBox<>();
        cbVaiTro.getItems().addAll("Chủ sân", "Nhân viên", "Khách hàng");
        cbVaiTro.setPromptText("Chọn vai trò");
        cbVaiTro.getStyleClass().add("input-field");
        cbVaiTro.setMaxWidth(Double.MAX_VALUE);

        Button btnDangNhap = new Button("Đăng nhập");
        btnDangNhap.getStyleClass().add("primary-button");
        btnDangNhap.setMaxWidth(Double.MAX_VALUE);

        Hyperlink linkDangKy = new Hyperlink("Chưa có tài khoản? Đăng ký");
        linkDangKy.getStyleClass().add("text-link");

        linkDangKy.setOnAction(e ->
                AppNavigator.goTo(DangKyPage.createScene(), "Đăng ký")
        );

        Label lblThongBao = new Label();
        lblThongBao.getStyleClass().add("error-text");

        btnDangNhap.setOnAction(e -> {
            String tenDangNhap = txtTenDangNhap.getText().trim();
            String matKhau = txtMatKhau.getText().trim();
            String vaiTro = cbVaiTro.getValue();

            lblThongBao.setText("");

            if (tenDangNhap.isEmpty() || matKhau.isEmpty() || vaiTro == null) {
                lblThongBao.setText("Vui lòng nhập đầy đủ thông tin");
                return;
            }

            TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
            TaiKhoan taiKhoan = taiKhoanDAO.dangNhap(tenDangNhap, matKhau);

            if (taiKhoan == null) {
                lblThongBao.setText("Sai tên đăng nhập hoặc mật khẩu");
                return;
            }

            AppState.setTenNguoiDung(taiKhoan.getHoTen());

            String vaiTroTuChon = vaiTro;
            int maVaiTro = taiKhoan.getMaVaiTro();

            if (maVaiTro == 1 && "Chủ sân".equals(vaiTroTuChon)) {
                AppState.setVaiTro("CHU_SAN");
                AppNavigator.goTo(ChuSanDashboardPage.createScene(), "Chủ sân");
            } else if (maVaiTro == 2 && "Nhân viên".equals(vaiTroTuChon)) {
                AppState.setVaiTro("NHAN_VIEN");
                AppNavigator.goTo(NhanVienDashboardPage.createScene(), "Nhân viên");
            } else if (maVaiTro == 3 && "Khách hàng".equals(vaiTroTuChon)) {
                AppState.setVaiTro("KHACH_HANG");
                AppNavigator.goTo(KhachHangHomePage.createScene(), "Khách hàng");
            } else {
                lblThongBao.setText("Vai trò chọn không khớp với tài khoản");
            }
        });

        VBox card = new VBox(
                16,
                lblLogo,
                lblTieuDe,
                lblMoTa,
                txtTenDangNhap,
                txtMatKhau,
                cbVaiTro,
                btnDangNhap,
                linkDangKy,
                lblThongBao
        );

        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("login-card");
        card.setMaxWidth(650);
        card.setMinWidth(560);

        VBox root = new VBox(card);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("page-root");

        Scene scene = new Scene(root, 1100, 760);
        scene.getStylesheets().add(
                DangNhapPage.class.getResource("/css/login.css").toExternalForm()
        );

        return scene;
    }
}