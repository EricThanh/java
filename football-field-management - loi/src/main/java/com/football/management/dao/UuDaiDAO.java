package com.football.management.dao;

import com.football.management.config.DBConnection;
import com.football.management.model.UuDai;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class UuDaiDAO {

    public List<UuDai> layTatCaUuDai() {
        List<UuDai> danhSach = new ArrayList<>();

        String sql = """
                SELECT ma_uu_dai,
                       ma_giam_gia,
                       ten_uu_dai,
                       mo_ta,
                       loai_giam_gia,
                       gia_tri_giam,
                       gia_tri_dat_toi_thieu,
                       ngay_bat_dau,
                       ngay_ket_thuc,
                       gio_bat_dau,
                       gio_ket_thuc,
                       dang_hoat_dong
                FROM uu_dai
                ORDER BY ma_uu_dai
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                danhSach.add(mapRow(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay danh sach uu dai", e);
        }

        return danhSach;
    }

    public List<UuDai> timKiemUuDai(String tuKhoa) {
        List<UuDai> danhSach = new ArrayList<>();

        String sql = """
                SELECT ma_uu_dai,
                       ma_giam_gia,
                       ten_uu_dai,
                       mo_ta,
                       loai_giam_gia,
                       gia_tri_giam,
                       gia_tri_dat_toi_thieu,
                       ngay_bat_dau,
                       ngay_ket_thuc,
                       gio_bat_dau,
                       gio_ket_thuc,
                       dang_hoat_dong
                FROM uu_dai
                WHERE LOWER(ma_giam_gia) LIKE ?
                   OR LOWER(ten_uu_dai) LIKE ?
                ORDER BY ma_uu_dai
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String keyword = "%" + tuKhoa.toLowerCase() + "%";
            ps.setString(1, keyword);
            ps.setString(2, keyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    danhSach.add(mapRow(rs));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Loi khi tim kiem uu dai", e);
        }

        return danhSach;
    }

    public boolean themUuDai(UuDai uuDai) {
        String sql = """
                INSERT INTO uu_dai (
                    ma_giam_gia,
                    ten_uu_dai,
                    mo_ta,
                    loai_giam_gia,
                    gia_tri_giam,
                    gia_tri_dat_toi_thieu,
                    ngay_bat_dau,
                    ngay_ket_thuc,
                    gio_bat_dau,
                    gio_ket_thuc,
                    dang_hoat_dong
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, uuDai.getMaGiamGia());
            ps.setString(2, uuDai.getTenUuDai());
            ps.setString(3, uuDai.getMoTa());
            ps.setString(4, uuDai.getLoaiGiamGia());
            ps.setBigDecimal(5, uuDai.getGiaTriGiam());
            ps.setBigDecimal(6, uuDai.getGiaTriDatToiThieu());
            ps.setDate(7, Date.valueOf(uuDai.getNgayBatDau()));
            ps.setDate(8, Date.valueOf(uuDai.getNgayKetThuc()));

            if (uuDai.getGioBatDau() != null && !uuDai.getGioBatDau().isBlank()) {
                ps.setString(9, uuDai.getGioBatDau());
            } else {
                ps.setNull(9, Types.VARCHAR);
            }

            if (uuDai.getGioKetThuc() != null && !uuDai.getGioKetThuc().isBlank()) {
                ps.setString(10, uuDai.getGioKetThuc());
            } else {
                ps.setNull(10, Types.VARCHAR);
            }

            ps.setInt(11, uuDai.getDangHoatDong());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Loi khi them uu dai", e);
        }
    }

    public boolean capNhatUuDai(UuDai uuDai) {
        String sql = """
                UPDATE uu_dai
                SET ma_giam_gia = ?,
                    ten_uu_dai = ?,
                    mo_ta = ?,
                    loai_giam_gia = ?,
                    gia_tri_giam = ?,
                    gia_tri_dat_toi_thieu = ?,
                    ngay_bat_dau = ?,
                    ngay_ket_thuc = ?,
                    gio_bat_dau = ?,
                    gio_ket_thuc = ?,
                    dang_hoat_dong = ?
                WHERE ma_uu_dai = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, uuDai.getMaGiamGia());
            ps.setString(2, uuDai.getTenUuDai());
            ps.setString(3, uuDai.getMoTa());
            ps.setString(4, uuDai.getLoaiGiamGia());
            ps.setBigDecimal(5, uuDai.getGiaTriGiam());
            ps.setBigDecimal(6, uuDai.getGiaTriDatToiThieu());
            ps.setDate(7, Date.valueOf(uuDai.getNgayBatDau()));
            ps.setDate(8, Date.valueOf(uuDai.getNgayKetThuc()));

            if (uuDai.getGioBatDau() != null && !uuDai.getGioBatDau().isBlank()) {
                ps.setString(9, uuDai.getGioBatDau());
            } else {
                ps.setNull(9, Types.VARCHAR);
            }

            if (uuDai.getGioKetThuc() != null && !uuDai.getGioKetThuc().isBlank()) {
                ps.setString(10, uuDai.getGioKetThuc());
            } else {
                ps.setNull(10, Types.VARCHAR);
            }

            ps.setInt(11, uuDai.getDangHoatDong());
            ps.setInt(12, uuDai.getMaUuDai());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Loi khi cap nhat uu dai", e);
        }
    }

    public boolean xoaUuDai(int maUuDai) {
        String sql = "DELETE FROM uu_dai WHERE ma_uu_dai = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maUuDai);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Loi khi xoa uu dai", e);
        }
    }

    private UuDai mapRow(ResultSet rs) throws Exception {
        UuDai uuDai = new UuDai();

        uuDai.setMaUuDai(rs.getInt("ma_uu_dai"));
        uuDai.setMaGiamGia(rs.getString("ma_giam_gia"));
        uuDai.setTenUuDai(rs.getString("ten_uu_dai"));
        uuDai.setMoTa(rs.getString("mo_ta"));
        uuDai.setLoaiGiamGia(rs.getString("loai_giam_gia"));
        uuDai.setGiaTriGiam(rs.getBigDecimal("gia_tri_giam"));
        uuDai.setGiaTriDatToiThieu(rs.getBigDecimal("gia_tri_dat_toi_thieu"));

        Date ngayBatDau = rs.getDate("ngay_bat_dau");
        if (ngayBatDau != null) {
            uuDai.setNgayBatDau(ngayBatDau.toLocalDate());
        }

        Date ngayKetThuc = rs.getDate("ngay_ket_thuc");
        if (ngayKetThuc != null) {
            uuDai.setNgayKetThuc(ngayKetThuc.toLocalDate());
        }

        uuDai.setGioBatDau(rs.getString("gio_bat_dau"));
        uuDai.setGioKetThuc(rs.getString("gio_ket_thuc"));
        uuDai.setDangHoatDong(rs.getInt("dang_hoat_dong"));

        return uuDai;
    }
}