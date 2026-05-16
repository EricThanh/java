package com.football.management.ui.auth;

import com.football.management.app.AppNavigator;
import com.football.management.dao.QuenMatKhauDAO;
import com.football.management.service.EmailService;
import com.football.management.service.OtpService;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;

public class QuenMatKhauPage {

    public static Scene createScene() {
        QuenMatKhauDAO quenMatKhauDAO = new QuenMatKhauDAO();
        EmailService emailService = new EmailService();
        OtpService otpService = new OtpService();

        Label lblLogo = new Label("Quản lý sân bóng");
        lblLogo.getStyleClass().add("app-logo");

        Label lblTieuDe = new Label("Quên mật khẩu");
        lblTieuDe.getStyleClass().add("login-title");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Nhập email");
        txtEmail.getStyleClass().add("input-field");

        TextField txtOtp = new TextField();
        txtOtp.setPromptText("Nhập mã xác nhận");
        txtOtp.getStyleClass().add("input-field");

        PasswordField txtMatKhauMoi = new PasswordField();
        txtMatKhauMoi.setPromptText("Nhập mật khẩu mới");
        txtMatKhauMoi.getStyleClass().add("input-field");

        Button btnGuiMa = new Button("Gửi mã");
        btnGuiMa.getStyleClass().add("light-button");

        Button btnXacNhan = new Button("Xác nhận đổi mật khẩu");
        btnXacNhan.getStyleClass().add("primary-button");

        Hyperlink linkDangNhap = new Hyperlink("Quay lại đăng nhập");
        linkDangNhap.getStyleClass().add("text-link");

        Label lblThongBao = new Label();
        lblThongBao.getStyleClass().add("error-text");

        btnGuiMa.setOnAction(e -> {
            try {
                String email = txtEmail.getText().trim();
                if (email.isEmpty()) {
                    lblThongBao.setText("Vui lòng nhập email");
                    return;
                }

                Integer maTaiKhoan = quenMatKhauDAO.timMaTaiKhoanTheoEmail(email);
                if (maTaiKhoan == null) {
                    lblThongBao.setText("Email không tồn tại trong hệ thống");
                    return;
                }

                String otp = otpService.taoOtp6So();
                LocalDateTime hetHan = LocalDateTime.now().plusMinutes(5);

                boolean luu = quenMatKhauDAO.luuOtp(maTaiKhoan, email, otp, hetHan);
                if (!luu) {
                    lblThongBao.setText("Không lưu được mã xác nhận");
                    return;
                }

                String subject = "Mã xác nhận đặt lại mật khẩu";
                String content = "Mã OTP của bạn là: " + otp + "\nMã có hiệu lực trong 5 phút.";

                emailService.guiEmail(email, subject, content);
                lblThongBao.setText("Đã gửi mã xác nhận qua email");
            } catch (Exception ex) {
                lblThongBao.setText(ex.getMessage());
            }
        });

        btnXacNhan.setOnAction(e -> {
            try {
                String email = txtEmail.getText().trim();
                String otp = txtOtp.getText().trim();
                String matKhauMoi = txtMatKhauMoi.getText().trim();

                if (email.isEmpty() || otp.isEmpty() || matKhauMoi.isEmpty()) {
                    lblThongBao.setText("Vui lòng nhập đầy đủ thông tin");
                    return;
                }

                boolean hopLe = quenMatKhauDAO.xacThucOtp(email, otp);
                if (!hopLe) {
                    lblThongBao.setText("OTP không đúng hoặc đã hết hạn");
                    return;
                }

                boolean capNhat = quenMatKhauDAO.capNhatMatKhauTheoEmail(email, matKhauMoi);
                if (!capNhat) {
                    lblThongBao.setText("Đổi mật khẩu thất bại");
                    return;
                }

                quenMatKhauDAO.danhDauOtpDaDung(email, otp);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Đổi mật khẩu thành công");
                alert.showAndWait();

                AppNavigator.goTo(DangNhapPage.createScene(), "Đăng nhập");
            } catch (Exception ex) {
                lblThongBao.setText(ex.getMessage());
            }
        });

        linkDangNhap.setOnAction(e ->
                AppNavigator.goTo(DangNhapPage.createScene(), "Đăng nhập")
        );

        VBox card = new VBox(14,
                lblLogo, lblTieuDe,
                txtEmail, btnGuiMa,
                txtOtp, txtMatKhauMoi,
                btnXacNhan,
                linkDangNhap,
                lblThongBao
        );
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("login-card");
        card.setMaxWidth(420);

        VBox root = new VBox(card);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("page-root");

        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(
                QuenMatKhauPage.class.getResource("/css/login.css").toExternalForm()
        );

        return scene;
    }
}