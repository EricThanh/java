package com.football.management.dao;

import com.football.management.config.DBConnection;
import com.football.management.ui.NhanVien.LichSanRow;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LichSanDAO {

    public List<LichSanRow> layLichSanTheoNgay(LocalDate ngayCanXem) throws SQLException {
        List<LichSanRow> danhSachLichSan = new ArrayList<>();

        if (ngayCanXem == null) {
            ngayCanXem = LocalDate.now();
        }

        String sql = """
                SELECT
                    ds.ma_dat_san_code,
                    sb.ma_san_code || ' - ' || sb.ten_san AS ten_san,
                    kh.ma_khach_hang_code || ' - ' || NVL(tk.ho_ten, 'Chưa có tên') AS ten_khach_hang,
                    TO_CHAR(ds.ngay_dat, 'DD/MM/YYYY') AS ngay_dat,
                    ds.gio_bat_dau,
                    ds.gio_ket_thuc,
                    ds.trang_thai_dat_san
                FROM dat_san ds
                JOIN san_bong sb ON ds.ma_san = sb.ma_san
                JOIN khach_hang kh ON ds.ma_khach_hang = kh.ma_khach_hang
                LEFT JOIN tai_khoan tk ON kh.ma_tai_khoan = tk.ma_tai_khoan
                WHERE ds.ngay_dat = ?
                ORDER BY sb.ten_san ASC, ds.gio_bat_dau ASC
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(ngayCanXem));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String maDon = rs.getString("ma_dat_san_code");
                    String tenSan = rs.getString("ten_san");
                    String tenKhachHang = rs.getString("ten_khach_hang");
                    String ngayDat = rs.getString("ngay_dat");
                    String gioBatDau = rs.getString("gio_bat_dau");
                    String gioKetThuc = rs.getString("gio_ket_thuc");
                    String trangThai = chuyenTrangThaiSangTiengViet(
                            rs.getString("trang_thai_dat_san")
                    );

                    danhSachLichSan.add(new LichSanRow(
                            maDon,
                            tenSan,
                            tenKhachHang,
                            ngayDat,
                            gioBatDau,
                            gioKetThuc,
                            trangThai
                    ));
                }
            }
        }

        return danhSachLichSan;
    }

    private String chuyenTrangThaiSangTiengViet(String trangThai) {
        if (trangThai == null) {
            return "";
        }

        return switch (trangThai) {
            case "CHO_XU_LY" -> "CHỜ XỬ LÝ";
            case "DA_XAC_NHAN" -> "ĐÃ XÁC NHẬN";
            case "DA_HUY" -> "ĐÃ HỦY";
            case "DA_CHECK_IN" -> "ĐÃ CHECK-IN";
            case "DA_HOAN_THANH" -> "ĐÃ HOÀN THÀNH";
            default -> trangThai;
        };
    }

    public List<String> layDanhSachSanChoBoLoc() throws SQLException {
    List<String> danhSachSan = new ArrayList<>();

    String sql = """
            SELECT ma_san_code || ' - ' || ten_san AS ten_san
            FROM san_bong
            ORDER BY ten_san ASC
            """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            danhSachSan.add(rs.getString("ten_san"));
        }
    }

    return danhSachSan;
}
}