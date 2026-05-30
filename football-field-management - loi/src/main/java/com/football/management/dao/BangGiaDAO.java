package com.football.management.dao;

import com.football.management.config.DBConnection;
import com.football.management.model.BangGia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class BangGiaDAO {

    public List<BangGia> layTatCaBangGia() {
        List<BangGia> danhSach = new ArrayList<>();

        String sql = """
                SELECT ma_bang_gia,
                       ma_san,
                       ten_bang_gia,
                       thu_trong_tuan,
                       gio_bat_dau,
                       gio_ket_thuc,
                       gia_moi_gio,
                       ngay_ap_dung_tu,
                       ngay_ap_dung_den,
                       dang_ap_dung,
                       ghi_chu
                FROM bang_gia
                ORDER BY ma_bang_gia
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                danhSach.add(mapRow(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay danh sach bang gia", e);
        }

        return danhSach;
    }

    public List<BangGia> timKiemBangGia(String tuKhoa) {
        List<BangGia> danhSach = new ArrayList<>();

        String sql = """
                SELECT ma_bang_gia,
                       ma_san,
                       ten_bang_gia,
                       thu_trong_tuan,
                       gio_bat_dau,
                       gio_ket_thuc,
                       gia_moi_gio,
                       ngay_ap_dung_tu,
                       ngay_ap_dung_den,
                       dang_ap_dung,
                       ghi_chu
                FROM bang_gia
                WHERE LOWER(ten_bang_gia) LIKE ?
                   OR LOWER(gio_bat_dau) LIKE ?
                   OR LOWER(gio_ket_thuc) LIKE ?
                ORDER BY ma_bang_gia
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
            throw new RuntimeException("Loi khi tim kiem bang gia", e);
        }

        return danhSach;
    }

    public boolean themBangGia(BangGia bangGia) {
        String sql = """
                INSERT INTO bang_gia (
                    ma_san,
                    ten_bang_gia,
                    thu_trong_tuan,
                    gio_bat_dau,
                    gio_ket_thuc,
                    gia_moi_gio,
                    ngay_ap_dung_tu,
                    ngay_ap_dung_den,
                    dang_ap_dung,
                    ghi_chu
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bangGia.getMaSan());
            ps.setString(2, bangGia.getTenBangGia());

            if (bangGia.getThuTrongTuan() != null) {
                ps.setInt(3, bangGia.getThuTrongTuan());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.setString(4, bangGia.getGioBatDau());
            ps.setString(5, bangGia.getGioKetThuc());
            ps.setBigDecimal(6, bangGia.getGiaMoiGio());
            ps.setDate(7, Date.valueOf(bangGia.getNgayApDungTu()));

            if (bangGia.getNgayApDungDen() != null) {
                ps.setDate(8, Date.valueOf(bangGia.getNgayApDungDen()));
            } else {
                ps.setNull(8, Types.DATE);
            }

            ps.setInt(9, bangGia.getDangApDung());
            ps.setString(10, bangGia.getGhiChu());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Loi khi them bang gia", e);
        }
    }

    public boolean capNhatBangGia(BangGia bangGia) {
        String sql = """
                UPDATE bang_gia
                SET ma_san = ?,
                    ten_bang_gia = ?,
                    thu_trong_tuan = ?,
                    gio_bat_dau = ?,
                    gio_ket_thuc = ?,
                    gia_moi_gio = ?,
                    ngay_ap_dung_tu = ?,
                    ngay_ap_dung_den = ?,
                    dang_ap_dung = ?,
                    ghi_chu = ?
                WHERE ma_bang_gia = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bangGia.getMaSan());
            ps.setString(2, bangGia.getTenBangGia());

            if (bangGia.getThuTrongTuan() != null) {
                ps.setInt(3, bangGia.getThuTrongTuan());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.setString(4, bangGia.getGioBatDau());
            ps.setString(5, bangGia.getGioKetThuc());
            ps.setBigDecimal(6, bangGia.getGiaMoiGio());
            ps.setDate(7, Date.valueOf(bangGia.getNgayApDungTu()));

            if (bangGia.getNgayApDungDen() != null) {
                ps.setDate(8, Date.valueOf(bangGia.getNgayApDungDen()));
            } else {
                ps.setNull(8, Types.DATE);
            }

            ps.setInt(9, bangGia.getDangApDung());
            ps.setString(10, bangGia.getGhiChu());
            ps.setInt(11, bangGia.getMaBangGia());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Loi khi cap nhat bang gia", e);
        }
    }

    public boolean xoaBangGia(int maBangGia) {
        String sql = "DELETE FROM bang_gia WHERE ma_bang_gia = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maBangGia);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Loi khi xoa bang gia", e);
        }
    }

    private BangGia mapRow(ResultSet rs) throws Exception {
        BangGia bangGia = new BangGia();
        bangGia.setMaBangGia(rs.getInt("ma_bang_gia"));
        bangGia.setMaSan(rs.getInt("ma_san"));
        bangGia.setTenBangGia(rs.getString("ten_bang_gia"));

        int thu = rs.getInt("thu_trong_tuan");
        if (!rs.wasNull()) {
            bangGia.setThuTrongTuan(thu);
        }

        bangGia.setGioBatDau(rs.getString("gio_bat_dau"));
        bangGia.setGioKetThuc(rs.getString("gio_ket_thuc"));
        bangGia.setGiaMoiGio(rs.getBigDecimal("gia_moi_gio"));

        Date tuNgay = rs.getDate("ngay_ap_dung_tu");
        if (tuNgay != null) {
            bangGia.setNgayApDungTu(tuNgay.toLocalDate());
        }

        Date denNgay = rs.getDate("ngay_ap_dung_den");
        if (denNgay != null) {
            bangGia.setNgayApDungDen(denNgay.toLocalDate());
        }

        bangGia.setDangApDung(rs.getInt("dang_ap_dung"));
        bangGia.setGhiChu(rs.getString("ghi_chu"));

        return bangGia;
    }
}