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
        Label lblLogo = new Label("Quản lý sân bóng");
        lblLogo.getStyleClass().add("app-logo");

        Label lblTieuDe = new Label("Đăng nhập");
        lblTieuDe.getStyleClass().add("login-title");

        TextField txtTenDangNhap = new TextField();
        txtTenDangNhap.setPromptText("Tên đăng nhập");
        txtTenDangNhap.getStyleClass().add("input-field");

        PasswordField txtMatKhau = new PasswordField();
        txtMatKhau.setPromptText("Mật khẩu");
        txtMatKhau.getStyleClass().add("input-field");

        ComboBox<String> cbVaiTro = new ComboBox<>();
        cbVaiTro.getItems().addAll("CHU_SAN", "NHAN_VIEN", "KHACH_HANG");
        cbVaiTro.setPromptText("Chọn vai trò");
        cbVaiTro.getStyleClass().add("input-field");
        cbVaiTro.setMaxWidth(Double.MAX_VALUE);

        Button btnDangNhap = new Button("Đăng nhập");
        btnDangNhap.getStyleClass().add("primary-button");
        btnDangNhap.setMaxWidth(Double.MAX_VALUE);

        Hyperlink linkDangKy = new Hyperlink("Đăng ký");
        linkDangKy.getStyleClass().add("text-link");

        linkDangKy.setOnAction(e ->
                AppNavigator.goTo(DangKyPage.createScene(), "Dang ky")
        );

        Label lblThongBao = new Label();
        lblThongBao.getStyleClass().add("error-text");

//        btnDangNhap.setOnAction(e -> {
//            String tenDangNhap = txtTenDangNhap.getText().trim();
//            String matKhau = txtMatKhau.getText().trim();
//            String vaiTro = cbVaiTro.getValue();
//
//            lblThongBao.setText("");
//
//            if (tenDangNhap.isEmpty() || matKhau.isEmpty() || vaiTro == null) {
//                lblThongBao.setText("Vui long nhap day du thong tin");
//                return;
//            }
//
//            AppState.setTenNguoiDung(tenDangNhap);
//            AppState.setVaiTro(vaiTro);
//
//            switch (vaiTro) {
//                case "CHU_SAN" ->
//                        AppNavigator.goTo(ChuSanDashboardPage.createScene(), "Chu san");
//                case "NHAN_VIEN" ->
//                        AppNavigator.goTo(NhanVienDashboardPage.createScene(), "Nhan vien");
//                case "KHACH_HANG" ->
//                        AppNavigator.goTo(KhachHangHomePage.createScene(), "Khach hang");
//                default -> lblThongBao.setText("Vai tro khong hop le");
//            }
//        });
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

            if (maVaiTro == 1 && "CHU_SAN".equals(vaiTroTuChon)) {
                AppState.setVaiTro("CHU_SAN");
                AppNavigator.goTo(ChuSanDashboardPage.createScene(), "Chu san");
            } else if (maVaiTro == 2 && "NHAN_VIEN".equals(vaiTroTuChon)) {
                AppState.setVaiTro("NHAN_VIEN");
                AppNavigator.goTo(NhanVienDashboardPage.createScene(), "Nhan vien");
            } else if (maVaiTro == 3 && "KHACH_HANG".equals(vaiTroTuChon)) {
                AppState.setVaiTro("KHACH_HANG");
                AppNavigator.goTo(KhachHangHomePage.createScene(), "Khach hang");
            } else {
                lblThongBao.setText("Vai trò chọn không khớp với tài khoản");
            }
        });

        VBox card = new VBox(
                16,
                lblLogo,
                lblTieuDe,
                txtTenDangNhap,
                txtMatKhau,
                cbVaiTro,
                btnDangNhap,
                linkDangKy,
                lblThongBao
        );
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("login-card");
        card.setMaxWidth(420);

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