package com.football.management.dao;

import com.football.management.config.DBConnection;
import com.football.management.model.KhachHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KhachHangDAO {

    public boolean themKhachHang(KhachHang khachHang) {
        String sql = """
                INSERT INTO khach_hang (
                    ma_tai_khoan,
                    ma_khach_hang_code,
                    gioi_tinh,
                    dia_chi,
                    trang_thai_khach_hang
                ) VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (khachHang.getMaTaiKhoan() != null) {
                ps.setInt(1, khachHang.getMaTaiKhoan());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }

            ps.setString(2, khachHang.getMaKhachHangCode());
            ps.setString(3, khachHang.getGioiTinh());
            ps.setString(4, khachHang.getDiaChi());
            ps.setString(5, khachHang.getTrangThaiKhachHang());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Loi khi them khach hang", e);
        }
    }

    public String taoMaKhachHangTuDong() {
        String sql = "SELECT COUNT(*) + 1 AS so_luong FROM khach_hang";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int so = rs.getInt("so_luong");
                return String.format("KH%03d", so);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Loi khi tao ma khach hang", e);
        }

        return "KH001";
    }
}