package com.football.management.dao;

import com.football.management.config.DBConnection;
import com.football.management.model.SanBong;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SanBongDAO {

    public List<SanBong> layTatCaSan() {
        List<SanBong> danhSach = new ArrayList<>();

        String sql = """
                SELECT ma_san,
                       ma_san_code,
                       ten_san,
                       ma_loai_san,
                       vi_tri,
                       suc_chua,
                       trang_thai_san,
                       gio_mo_cua,
                       gio_dong_cua,
                       ngay_tao,
                       ngay_cap_nhat
                FROM san_bong
                ORDER BY ma_san
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                danhSach.add(mapRow(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay danh sach san bong", e);
        }

        return danhSach;
    }

    public boolean themSan(SanBong sanBong) {
        String sql = """
                INSERT INTO san_bong (
                    ma_san_code,
                    ten_san,
                    ma_loai_san,
                    vi_tri,
                    suc_chua,
                    trang_thai_san,
                    gio_mo_cua,
                    gio_dong_cua
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sanBong.getMaSanCode());
            ps.setString(2, sanBong.getTenSan());
            ps.setInt(3, sanBong.getMaLoaiSan());
            ps.setString(4, sanBong.getViTri());
            if (sanBong.getSucChua() != null) {
                ps.setInt(5, sanBong.getSucChua());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.setString(6, sanBong.getTrangThaiSan());
            ps.setString(7, sanBong.getGioMoCua());
            ps.setString(8, sanBong.getGioDongCua());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Loi khi them san bong", e);
        }
    }

    public boolean capNhatSan(SanBong sanBong) {
        String sql = """
                UPDATE san_bong
                SET ma_san_code = ?,
                    ten_san = ?,
                    ma_loai_san = ?,
                    vi_tri = ?,
                    suc_chua = ?,
                    trang_thai_san = ?,
                    gio_mo_cua = ?,
                    gio_dong_cua = ?,
                    ngay_cap_nhat = SYSDATE
                WHERE ma_san = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sanBong.getMaSanCode());
            ps.setString(2, sanBong.getTenSan());
            ps.setInt(3, sanBong.getMaLoaiSan());
            ps.setString(4, sanBong.getViTri());
            if (sanBong.getSucChua() != null) {
                ps.setInt(5, sanBong.getSucChua());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.setString(6, sanBong.getTrangThaiSan());
            ps.setString(7, sanBong.getGioMoCua());
            ps.setString(8, sanBong.getGioDongCua());
            ps.setInt(9, sanBong.getMaSan());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Loi khi cap nhat san bong", e);
        }
    }

    public boolean xoaSan(int maSan) {
        String sql = "DELETE FROM san_bong WHERE ma_san = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maSan);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Loi khi xoa san bong", e);
        }
    }

    public List<SanBong> timKiemSan(String tuKhoa) {
        List<SanBong> danhSach = new ArrayList<>();

        String sql = """
                SELECT ma_san,
                       ma_san_code,
                       ten_san,
                       ma_loai_san,
                       vi_tri,
                       suc_chua,
                       trang_thai_san,
                       gio_mo_cua,
                       gio_dong_cua,
                       ngay_tao,
                       ngay_cap_nhat
                FROM san_bong
                WHERE LOWER(ma_san_code) LIKE ?
                   OR LOWER(ten_san) LIKE ?
                   OR LOWER(vi_tri) LIKE ?
                ORDER BY ma_san
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String keyword = "%" + tuKhoa.toLowerCase() + "%";
            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setString(3, keyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    danhSach.add(mapRow(rs));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Loi khi tim kiem san bong", e);
        }

        return danhSach;
    }

    private SanBong mapRow(ResultSet rs) throws SQLException {
        SanBong san = new SanBong();
        san.setMaSan(rs.getInt("ma_san"));
        san.setMaSanCode(rs.getString("ma_san_code"));
        san.setTenSan(rs.getString("ten_san"));
        san.setMaLoaiSan(rs.getInt("ma_loai_san"));
        san.setViTri(rs.getString("vi_tri"));

        int sucChua = rs.getInt("suc_chua");
        if (!rs.wasNull()) {
            san.setSucChua(sucChua);
        }

        san.setTrangThaiSan(rs.getString("trang_thai_san"));
        san.setGioMoCua(rs.getString("gio_mo_cua"));
        san.setGioDongCua(rs.getString("gio_dong_cua"));

        Timestamp ngayTao = rs.getTimestamp("ngay_tao");
        if (ngayTao != null) {
            san.setNgayTao(ngayTao.toLocalDateTime());
        }

        Timestamp ngayCapNhat = rs.getTimestamp("ngay_cap_nhat");
        if (ngayCapNhat != null) {
            san.setNgayCapNhat(ngayCapNhat.toLocalDateTime());
        }

        return san;
    }
}