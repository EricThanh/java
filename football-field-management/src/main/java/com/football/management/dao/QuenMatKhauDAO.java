package com.football.management.dao;

import com.football.management.config.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;

public class QuenMatKhauDAO {

    public Integer timMaTaiKhoanTheoEmail(String email) {
        String sql = """
                SELECT ma_tai_khoan
                FROM tai_khoan
                WHERE email = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ma_tai_khoan");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi tim tai khoan theo email", e);
        }

        return null;
    }

    public boolean luuOtp(int maTaiKhoan, String email, String otp, LocalDateTime hetHan) {
        String sql = """
                INSERT INTO otp_dat_lai_mat_khau (
                    ma_tai_khoan,
                    email,
                    ma_xac_nhan,
                    thoi_gian_het_han,
                    da_su_dung
                ) VALUES (?, ?, ?, ?, 0)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maTaiKhoan);
            ps.setString(2, email);
            ps.setString(3, otp);
            ps.setTimestamp(4, Timestamp.valueOf(hetHan));

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Loi khi luu OTP", e);
        }
    }

    public boolean xacThucOtp(String email, String otp) {
        String sql = """
                SELECT COUNT(*) AS tong
                FROM otp_dat_lai_mat_khau
                WHERE email = ?
                  AND ma_xac_nhan = ?
                  AND da_su_dung = 0
                  AND thoi_gian_het_han >= SYSTIMESTAMP
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, otp);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("tong") > 0;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi xac thuc OTP", e);
        }

        return false;
    }

    public boolean danhDauOtpDaDung(String email, String otp) {
        String sql = """
                UPDATE otp_dat_lai_mat_khau
                SET da_su_dung = 1
                WHERE email = ?
                  AND ma_xac_nhan = ?
                  AND da_su_dung = 0
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, otp);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Loi khi danh dau OTP da dung", e);
        }
    }

    public boolean capNhatMatKhauTheoEmail(String email, String matKhauMoi) {
        String sql = """
                UPDATE tai_khoan
                SET mat_khau = ?,
                    ngay_cap_nhat = SYSTIMESTAMP
                WHERE email = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, matKhauMoi);
            ps.setString(2, email);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Loi khi cap nhat mat khau", e);
        }
    }
}