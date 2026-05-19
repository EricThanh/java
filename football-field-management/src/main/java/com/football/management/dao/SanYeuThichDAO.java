package com.football.management.dao;

import com.football.management.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SanYeuThichDAO {

    // DTO nội bộ cho danh sách sân yêu thích
    public static class SanYeuThichRow {
        private final int maSan;
        private final String maSanCode;
        private final String tenSan;
        private final String tenLoaiSan;
        private final String viTri;
        private final String gioMoCua;
        private final String gioDongCua;
        private final String trangThaiSan;

        public SanYeuThichRow(int maSan, String maSanCode, String tenSan,
                              String tenLoaiSan, String viTri,
                              String gioMoCua, String gioDongCua,
                              String trangThaiSan) {
            this.maSan = maSan;
            this.maSanCode = maSanCode;
            this.tenSan = tenSan;
            this.tenLoaiSan = tenLoaiSan;
            this.viTri = viTri;
            this.gioMoCua = gioMoCua;
            this.gioDongCua = gioDongCua;
            this.trangThaiSan = trangThaiSan;
        }

        public int getMaSan() { return maSan; }
        public String getMaSanCode() { return maSanCode; }
        public String getTenSan() { return tenSan; }
        public String getTenLoaiSan() { return tenLoaiSan; }
        public String getViTri() { return viTri; }
        public String getGioMoCua() { return gioMoCua; }
        public String getGioDongCua() { return gioDongCua; }
        public String getTrangThaiSan() { return trangThaiSan; }
    }

    /** Lấy danh sách sân yêu thích theo maKhachHang */
    public List<SanYeuThichRow> layDanhSachYeuThich(int maKhachHang) throws SQLException {
        List<SanYeuThichRow> ds = new ArrayList<>();

        String sql = """
                SELECT sb.ma_san,
                       sb.ma_san_code,
                       sb.ten_san,
                       ls.ten_loai_san,
                       NVL(sb.vi_tri, '') AS vi_tri,
                       NVL(sb.gio_mo_cua, '') AS gio_mo_cua,
                       NVL(sb.gio_dong_cua, '') AS gio_dong_cua,
                       sb.trang_thai_san
                FROM san_yeu_thich syt
                JOIN san_bong sb ON syt.ma_san = sb.ma_san
                JOIN loai_san ls ON sb.ma_loai_san = ls.ma_loai_san
                WHERE syt.ma_khach_hang = ?
                ORDER BY syt.ngay_tao DESC
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maKhachHang);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(new SanYeuThichRow(
                            rs.getInt("ma_san"),
                            rs.getString("ma_san_code"),
                            rs.getString("ten_san"),
                            rs.getString("ten_loai_san"),
                            rs.getString("vi_tri"),
                            rs.getString("gio_mo_cua"),
                            rs.getString("gio_dong_cua"),
                            rs.getString("trang_thai_san")
                    ));
                }
            }
        }
        return ds;
    }

    /** Kiểm tra sân có trong danh sách yêu thích của khách không */
    public boolean laSanYeuThich(int maKhachHang, int maSan) throws SQLException {
        String sql = """
                SELECT COUNT(*) AS so_luong
                FROM san_yeu_thich
                WHERE ma_khach_hang = ? AND ma_san = ?
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maKhachHang);
            ps.setInt(2, maSan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("so_luong") > 0;
            }
        }
        return false;
    }

    /** Thêm sân vào danh sách yêu thích */
    public boolean themYeuThich(int maKhachHang, int maSan) throws SQLException {
        if (laSanYeuThich(maKhachHang, maSan)) return false; // đã có rồi

        String sql = """
                INSERT INTO san_yeu_thich (ma_khach_hang, ma_san, ngay_tao)
                VALUES (?, ?, SYSDATE)
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maKhachHang);
            ps.setInt(2, maSan);
            return ps.executeUpdate() > 0;
        }
    }

    /** Xóa sân khỏi danh sách yêu thích */
    public boolean xoaYeuThich(int maKhachHang, int maSan) throws SQLException {
        String sql = """
                DELETE FROM san_yeu_thich
                WHERE ma_khach_hang = ? AND ma_san = ?
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maKhachHang);
            ps.setInt(2, maSan);
            return ps.executeUpdate() > 0;
        }
    }

    /** Toggle: nếu đã thích thì bỏ, nếu chưa thì thêm. Trả về true = đang yêu thích sau khi toggle */
    public boolean toggleYeuThich(int maKhachHang, int maSan) throws SQLException {
        if (laSanYeuThich(maKhachHang, maSan)) {
            xoaYeuThich(maKhachHang, maSan);
            return false;
        } else {
            themYeuThich(maKhachHang, maSan);
            return true;
        }
    }

    /** Đếm số sân yêu thích */
    public int demSanYeuThich(int maKhachHang) throws SQLException {
        String sql = "SELECT COUNT(*) AS so_luong FROM san_yeu_thich WHERE ma_khach_hang = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maKhachHang);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("so_luong");
            }
        }
        return 0;
    }
}
