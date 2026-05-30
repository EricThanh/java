package com.football.management.dao;

import com.football.management.config.DBConnection;
import com.football.management.model.TaiKhoan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TaiKhoanDAO {

    public TaiKhoan timTheoTenDangNhap(String tenDangNhap) {
        String sql = """
                SELECT ma_tai_khoan,
                       ten_dang_nhap,
                       mat_khau,
                       ho_ten,
                       email,
                       so_dien_thoai,
                       ma_vai_tro,
                       trang_thai_tai_khoan,
                       ngay_tao,
                       ngay_cap_nhat
                FROM tai_khoan
                WHERE ten_dang_nhap = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tenDangNhap);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTaiKhoan(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Loi khi tim tai khoan theo ten dang nhap", e);
        }

        return null;
    }
    public int themTaiKhoanVaLayMa(TaiKhoan taiKhoan) {
        String sql = """
            INSERT INTO tai_khoan (
                ten_dang_nhap,
                mat_khau,
                ho_ten,
                email,
                so_dien_thoai,
                ma_vai_tro,
                trang_thai_tai_khoan
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ma_tai_khoan"})) {

            ps.setString(1, taiKhoan.getTenDangNhap());
            ps.setString(2, taiKhoan.getMatKhau());
            ps.setString(3, taiKhoan.getHoTen());
            ps.setString(4, taiKhoan.getEmail());
            ps.setString(5, taiKhoan.getSoDienThoai());
            ps.setInt(6, taiKhoan.getMaVaiTro());
            ps.setString(7, taiKhoan.getTrangThaiTaiKhoan());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Loi khi them tai khoan va lay ma", e);
        }

        return -1;
    }

    public TaiKhoan dangNhap(String tenDangNhap, String matKhau) {
        String sql = """
                SELECT ma_tai_khoan,
                       ten_dang_nhap,
                       mat_khau,
                       ho_ten,
                       email,
                       so_dien_thoai,
                       ma_vai_tro,
                       trang_thai_tai_khoan,
                       ngay_tao,
                       ngay_cap_nhat
                FROM tai_khoan
                WHERE ten_dang_nhap = ?
                  AND mat_khau = ?
                  AND trang_thai_tai_khoan = 'HOAT_DONG'
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tenDangNhap);
            ps.setString(2, matKhau);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTaiKhoan(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Loi khi dang nhap", e);
        }

        return null;
    }

    public boolean tonTaiTenDangNhap(String tenDangNhap) {
        String sql = "SELECT COUNT(*) FROM tai_khoan WHERE ten_dang_nhap = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tenDangNhap);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Loi khi kiem tra ten dang nhap", e);
        }

        return false;
    }

    public boolean themTaiKhoan(TaiKhoan taiKhoan) {
        String sql = """
                INSERT INTO tai_khoan (
                    ten_dang_nhap,
                    mat_khau,
                    ho_ten,
                    email,
                    so_dien_thoai,
                    ma_vai_tro,
                    trang_thai_tai_khoan
                ) VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, taiKhoan.getTenDangNhap());
            ps.setString(2, taiKhoan.getMatKhau());
            ps.setString(3, taiKhoan.getHoTen());
            ps.setString(4, taiKhoan.getEmail());
            ps.setString(5, taiKhoan.getSoDienThoai());
            ps.setInt(6, taiKhoan.getMaVaiTro());
            ps.setString(7, taiKhoan.getTrangThaiTaiKhoan());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Loi khi them tai khoan", e);
        }
    }

    private TaiKhoan mapResultSetToTaiKhoan(ResultSet rs) throws SQLException {
        TaiKhoan taiKhoan = new TaiKhoan();

        taiKhoan.setMaTaiKhoan(rs.getInt("ma_tai_khoan"));
        taiKhoan.setTenDangNhap(rs.getString("ten_dang_nhap"));
        taiKhoan.setMatKhau(rs.getString("mat_khau"));
        taiKhoan.setHoTen(rs.getString("ho_ten"));
        taiKhoan.setEmail(rs.getString("email"));
        taiKhoan.setSoDienThoai(rs.getString("so_dien_thoai"));
        taiKhoan.setMaVaiTro(rs.getInt("ma_vai_tro"));
        taiKhoan.setTrangThaiTaiKhoan(rs.getString("trang_thai_tai_khoan"));

        Timestamp ngayTaoTs = rs.getTimestamp("ngay_tao");
        if (ngayTaoTs != null) {
            taiKhoan.setNgayTao(ngayTaoTs.toLocalDateTime());
        }

        Timestamp ngayCapNhatTs = rs.getTimestamp("ngay_cap_nhat");
        if (ngayCapNhatTs != null) {
            taiKhoan.setNgayCapNhat(ngayCapNhatTs.toLocalDateTime());
        }

        return taiKhoan;
    }
}