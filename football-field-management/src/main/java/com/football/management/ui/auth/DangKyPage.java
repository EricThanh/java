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
        Label lblLogo = new Label("Quan ly san bong");
        lblLogo.getStyleClass().add("app-logo");

        Label lblTieuDe = new Label("Dang ky tai khoan khach hang");
        lblTieuDe.getStyleClass().add("login-title");

        Label lblMoTa = new Label("Tao tai khoan de dat san, thanh toan va theo doi lich su");
        lblMoTa.getStyleClass().add("login-subtitle");
        lblMoTa.setWrapText(true);

        TextField txtHoTen = new TextField();
        txtHoTen.setPromptText("Ho ten");
        txtHoTen.getStyleClass().add("input-field");

        TextField txtTenDangNhap = new TextField();
        txtTenDangNhap.setPromptText("Ten dang nhap");
        txtTenDangNhap.getStyleClass().add("input-field");

        PasswordField txtMatKhau = new PasswordField();
        txtMatKhau.setPromptText("Mat khau");
        txtMatKhau.getStyleClass().add("input-field");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email");
        txtEmail.getStyleClass().add("input-field");

        TextField txtSoDienThoai = new TextField();
        txtSoDienThoai.setPromptText("So dien thoai");
        txtSoDienThoai.getStyleClass().add("input-field");

        TextField txtDiaChi = new TextField();
        txtDiaChi.setPromptText("Dia chi");
        txtDiaChi.getStyleClass().add("input-field");

        ComboBox<String> cbGioiTinh = new ComboBox<>();
        cbGioiTinh.getItems().addAll("NAM", "NU", "KHAC");
        cbGioiTinh.setPromptText("Gioi tinh");
        cbGioiTinh.getStyleClass().add("input-field");
        cbGioiTinh.setMaxWidth(Double.MAX_VALUE);

        Button btnDangKy = new Button("Dang ky");
        btnDangKy.getStyleClass().add("primary-button");
        btnDangKy.setMaxWidth(Double.MAX_VALUE);

        Hyperlink linkDangNhap = new Hyperlink("Da co tai khoan? Dang nhap");
        linkDangNhap.getStyleClass().add("text-link");

        Label lblThongBao = new Label();
        lblThongBao.getStyleClass().add("error-text");

        linkDangNhap.setOnAction(e ->
                AppNavigator.goTo(DangNhapPage.createScene(), "Dang nhap")
        );

        btnDangKy.setOnAction(e -> {
            lblThongBao.setText("");

            String hoTen = txtHoTen.getText().trim();
            String tenDangNhap = txtTenDangNhap.getText().trim();
            String matKhau = txtMatKhau.getText().trim();
            String email = txtEmail.getText().trim();
            String soDienThoai = txtSoDienThoai.getText().trim();
            String diaChi = txtDiaChi.getText().trim();
            String gioiTinh = cbGioiTinh.getValue();

            if (hoTen.isEmpty() || tenDangNhap.isEmpty() || matKhau.isEmpty()
                    || email.isEmpty() || soDienThoai.isEmpty() || gioiTinh == null) {
                lblThongBao.setText("Vui long nhap day du thong tin");
                return;
            }

            TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
            KhachHangDAO khachHangDAO = new KhachHangDAO();

            if (taiKhoanDAO.tonTaiTenDangNhap(tenDangNhap)) {
                lblThongBao.setText("Ten dang nhap da ton tai");
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
                lblThongBao.setText("Dang ky that bai o buoc tao tai khoan");
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
                lblThongBao.setText("Dang ky that bai o buoc tao khach hang");
                return;
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thong bao");
            alert.setHeaderText(null);
            alert.setContentText("Dang ky thanh cong. Moi ban dang nhap.");
            alert.showAndWait();

            AppNavigator.goTo(DangNhapPage.createScene(), "Dang nhap");
        });

        VBox card = new VBox(
                14,
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
        card.setMaxWidth(440);

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