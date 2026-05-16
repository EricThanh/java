package com.football.management.dao;

import com.football.management.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ThongKeNhanVienDAO {

    public int demDonDatSanHomNay() throws SQLException {
        String sql = """
                SELECT COUNT(*) AS so_luong
                FROM dat_san
                WHERE ngay_dat = TRUNC(SYSDATE)
                  AND trang_thai_dat_san <> 'DA_HUY'
                """;

        return demSoLuong(sql);
    }

    public int demDonDangSuDung() throws SQLException {
        String sql = """
                SELECT COUNT(*) AS so_luong
                FROM dat_san
                WHERE trang_thai_dat_san = 'DA_CHECK_IN'
                """;

        return demSoLuong(sql);
    }

    public int demDonCanThanhToan() throws SQLException {
        String sql = """
                SELECT COUNT(*) AS so_luong
                FROM dat_san
                WHERE trang_thai_dat_san IN ('DA_CHECK_IN', 'DA_HOAN_THANH')
                  AND trang_thai_thanh_toan = 'CHUA_THANH_TOAN'
                """;

        return demSoLuong(sql);
    }

    public int demKhachHangHoatDong() throws SQLException {
        String sql = """
                SELECT COUNT(*) AS so_luong
                FROM khach_hang
                WHERE trang_thai_khach_hang = 'HOAT_DONG'
                """;

        return demSoLuong(sql);
    }

    public int demSanDangSuDung() throws SQLException {
        String sql = """
                SELECT COUNT(*) AS so_luong
                FROM san_bong
                WHERE trang_thai_san = 'DANG_SU_DUNG'
                """;

        return demSoLuong(sql);
    }

    public int demTongSan() throws SQLException {
        String sql = """
                SELECT COUNT(*) AS so_luong
                FROM san_bong
                """;

        return demSoLuong(sql);
    }

    private int demSoLuong(String sql) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("so_luong");
            }
        }

        return 0;
    }
}