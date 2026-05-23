package com.football.management.ui.auth;

import com.football.management.app.AppNavigator;
import com.football.management.app.AppState;
import com.football.management.config.DBConnection;
import com.football.management.ui.ChuSan.ChuSanDashboardPage;
import com.football.management.ui.KhachHang.KhachHangHomePage;
import com.football.management.ui.NhanVien.NhanVienDashboardPage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import com.football.management.dao.TaiKhoanDAO;
import com.football.management.model.TaiKhoan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DangNhapPage {

    public static Scene createScene() {
        // ── Logo & tiêu đề ──────────────────────────────────────────────────
        Label lblLogo = new Label("QUẢN LÝ SÂN BÓNG");
        lblLogo.getStyleClass().add("app-logo");

        Label lblTieuDe = new Label("ĐĂNG NHẬP");
        lblTieuDe.getStyleClass().add("login-title");

        // Subtitle (branch 2)
        Label lblMoTa = new Label("Đăng nhập để quản lý sân, lịch đặt và thanh toán");
        lblMoTa.getStyleClass().add("login-subtitle");
        lblMoTa.setWrapText(true);

        // ── Các trường nhập liệu ────────────────────────────────────────────
        TextField txtTenDangNhap = new TextField();
        txtTenDangNhap.setPromptText("Tên đăng nhập");
        txtTenDangNhap.getStyleClass().add("input-field");

        PasswordField txtMatKhau = new PasswordField();
        txtMatKhau.setPromptText("Mật khẩu");
        txtMatKhau.getStyleClass().add("input-field");

        // ComboBox hiển thị tiếng Việt (branch 2), giá trị nội bộ map sang
        // hằng số vai trò (branch 1) trong phần xử lý bên dưới.
        ComboBox<String> cbVaiTro = new ComboBox<>();
        cbVaiTro.getItems().addAll("Chủ sân", "Nhân viên", "Khách hàng");
        cbVaiTro.setPromptText("Chọn vai trò");
        cbVaiTro.getStyleClass().add("input-field");
        cbVaiTro.setMaxWidth(Double.MAX_VALUE);

        // ── Nút & liên kết ──────────────────────────────────────────────────
        Button btnDangNhap = new Button("Đăng nhập");
        btnDangNhap.getStyleClass().add("primary-button");
        btnDangNhap.setMaxWidth(Double.MAX_VALUE);

        // "Quên mật khẩu?" (branch 1)
        Hyperlink linkQuenMatKhau = new Hyperlink("Quên mật khẩu?");
        linkQuenMatKhau.getStyleClass().add("text-link");
        linkQuenMatKhau.setOnAction(e ->
                AppNavigator.goTo(QuenMatKhauPage.createScene(), "Quên mật khẩu")
        );

        // "Đăng ký" (cả hai branch)
        Hyperlink linkDangKy = new Hyperlink("Chưa có tài khoản? Đăng ký");
        linkDangKy.getStyleClass().add("text-link");
        linkDangKy.setOnAction(e ->
                AppNavigator.goTo(DangKyPage.createScene(), "Đăng ký")
        );

        Label lblThongBao = new Label();
        lblThongBao.getStyleClass().add("error-text");

        // ── Xử lý đăng nhập ────────────────────────────────────────────────
        btnDangNhap.setOnAction(e -> {
            String tenDangNhap = txtTenDangNhap.getText().trim();
            String matKhau     = txtMatKhau.getText().trim();
            String vaiTroHienThi = cbVaiTro.getValue(); // "Chủ sân" / "Nhân viên" / "Khách hàng"

            lblThongBao.setText("");

            if (tenDangNhap.isEmpty() || matKhau.isEmpty() || vaiTroHienThi == null) {
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
            AppState.setMaTaiKhoan(taiKhoan.getMaTaiKhoan()); // branch 1

            int maVaiTro = taiKhoan.getMaVaiTro();

            if (maVaiTro == 1 && "Chủ sân".equals(vaiTroHienThi)) {
                AppState.setVaiTro("CHU_SAN");
                AppNavigator.goTo(ChuSanDashboardPage.createScene(), "Chủ sân");

            } else if (maVaiTro == 2 && "Nhân viên".equals(vaiTroHienThi)) {
                AppState.setVaiTro("NHAN_VIEN");
                AppNavigator.goTo(NhanVienDashboardPage.createScene(), "Nhân viên");

            } else if (maVaiTro == 3 && "Khách hàng".equals(vaiTroHienThi)) {
                // Lấy maKhachHang từ DB rồi lưu vào AppState (branch 1)
                int maKhachHang = layMaKhachHang(taiKhoan.getMaTaiKhoan());
                if (maKhachHang <= 0) {
                    lblThongBao.setText("Không tìm thấy thông tin khách hàng");
                    return;
                }
                AppState.setMaKhachHang(maKhachHang);
                AppState.setVaiTro("KHACH_HANG");
                AppNavigator.goTo(KhachHangHomePage.createScene(), "Khách hàng");

            } else {
                lblThongBao.setText("Vai trò chọn không khớp với tài khoản");
            }
        });

        // ── Layout ──────────────────────────────────────────────────────────
        VBox card = new VBox(
                16,
                lblLogo,
                lblTieuDe,
                lblMoTa,          // branch 2
                txtTenDangNhap,
                txtMatKhau,
                cbVaiTro,
                btnDangNhap,
                linkQuenMatKhau,  // branch 1
                linkDangKy,
                lblThongBao
        );
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("login-card");
        card.setMaxWidth(650);   // branch 2
        card.setMinWidth(560);   // branch 2

        VBox root = new VBox(card);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("page-root");

        Scene scene = new Scene(root, 1100, 760);
        scene.getStylesheets().add(
                DangNhapPage.class.getResource("/css/login.css").toExternalForm()
        );

        return scene;
    }

    // ── Helper: lấy maKhachHang từ DB (branch 1) ────────────────────────────
    private static int layMaKhachHang(int maTaiKhoan) {
        String sql = "SELECT ma_khach_hang FROM khach_hang WHERE ma_tai_khoan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maTaiKhoan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ma_khach_hang");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }
}