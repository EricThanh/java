package com.football.management.dao;

import com.football.management.config.DBConnection;
import com.football.management.model.BaoCaoTongQuan;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.YearMonth;

public class BaoCaoDAO {

    public BaoCaoTongQuan layBaoCaoTheoNgay(LocalDate ngay) {
        BaoCaoTongQuan baoCao = new BaoCaoTongQuan();
        baoCao.setTuNgay(ngay);
        baoCao.setDenNgay(ngay);

        try (Connection conn = DBConnection.getConnection()) {
            baoCao.setDoanhThu(layDoanhThuTheoKhoang(conn, ngay, ngay));
            baoCao.setLuotDatSan(layLuotDatSanTheoKhoang(conn, ngay, ngay));
            baoCao.setSoKhachHang(laySoKhachHang(conn));
            baoCao.setTongSoSan(layTongSoSan(conn));
            baoCao.setSoSanDangSuDung(laySoSanDangSuDung(conn));
            baoCao.setTanSuatSuDung(tinhTanSuatSuDung(
                    baoCao.getSoSanDangSuDung(),
                    baoCao.getTongSoSan()
            ));
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay bao cao theo ngay", e);
        }

        return baoCao;
    }

    public BaoCaoTongQuan layBaoCaoTheoThang(int thang, int nam) {
        YearMonth yearMonth = YearMonth.of(nam, thang);
        LocalDate tuNgay = yearMonth.atDay(1);
        LocalDate denNgay = yearMonth.atEndOfMonth();

        BaoCaoTongQuan baoCao = new BaoCaoTongQuan();
        baoCao.setTuNgay(tuNgay);
        baoCao.setDenNgay(denNgay);

        try (Connection conn = DBConnection.getConnection()) {
            baoCao.setDoanhThu(layDoanhThuTheoKhoang(conn, tuNgay, denNgay));
            baoCao.setLuotDatSan(layLuotDatSanTheoKhoang(conn, tuNgay, denNgay));
            baoCao.setSoKhachHang(laySoKhachHang(conn));
            baoCao.setTongSoSan(layTongSoSan(conn));
            baoCao.setSoSanDangSuDung(laySoSanDangSuDung(conn));
            baoCao.setTanSuatSuDung(tinhTanSuatSuDung(
                    baoCao.getSoSanDangSuDung(),
                    baoCao.getTongSoSan()
            ));
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay bao cao theo thang", e);
        }

        return baoCao;
    }

    private BigDecimal layDoanhThuTheoKhoang(Connection conn, LocalDate tuNgay, LocalDate denNgay) throws Exception {
        String sql = """
                SELECT NVL(SUM(so_tien_thanh_toan), 0) AS tong
                FROM thanh_toan
                WHERE trang_thai_thanh_toan = 'THANH_CONG'
                  AND TRUNC(thoi_gian_thanh_toan) BETWEEN ? AND ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(tuNgay));
            ps.setDate(2, Date.valueOf(denNgay));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("tong");
                }
            }
        }
        return BigDecimal.ZERO;
    }

    private int layLuotDatSanTheoKhoang(Connection conn, LocalDate tuNgay, LocalDate denNgay) throws Exception {
        String sql = """
                SELECT COUNT(*) AS tong
                FROM dat_san
                WHERE TRUNC(ngay_dat) BETWEEN ? AND ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(tuNgay));
            ps.setDate(2, Date.valueOf(denNgay));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("tong");
                }
            }
        }
        return 0;
    }

    private int laySoKhachHang(Connection conn) throws Exception {
        String sql = """
                SELECT COUNT(*) AS tong
                FROM khach_hang
                WHERE trang_thai_khach_hang = 'HOAT_DONG'
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("tong");
            }
        }
        return 0;
    }

    private int layTongSoSan(Connection conn) throws Exception {
        String sql = """
                SELECT COUNT(*) AS tong
                FROM san_bong
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("tong");
            }
        }
        return 0;
    }

    private int laySoSanDangSuDung(Connection conn) throws Exception {
        String sql = """
                SELECT COUNT(*) AS tong
                FROM san_bong
                WHERE trang_thai_san = 'DANG_SU_DUNG'
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("tong");
            }
        }
        return 0;
    }

    private double tinhTanSuatSuDung(int soSanDangSuDung, int tongSoSan) {
        if (tongSoSan == 0) {
            return 0;
        }
        return (soSanDangSuDung * 100.0) / tongSoSan;
    }
}