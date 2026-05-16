package com.football.management.dao;

import com.football.management.config.DBConnection;
import com.football.management.ui.NhanVien.ThanhToanRow;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ThanhToanDAO {

    private static class KhoangGia {
        private final String gioBatDau;
        private final String gioKetThuc;
        private final double giaMoiGio;

        public KhoangGia(String gioBatDau, String gioKetThuc, double giaMoiGio) {
            this.gioBatDau = gioBatDau;
            this.gioKetThuc = gioKetThuc;
            this.giaMoiGio = giaMoiGio;
        }
    }

    public List<ThanhToanRow> layDanhSachDonCanThanhToan() throws SQLException {
        List<ThanhToanRow> danhSachThanhToan = new ArrayList<>();

        String sql = """
                SELECT
                    ds.ma_dat_san_code,
                    kh.ma_khach_hang_code || ' - ' || NVL(tk.ho_ten, 'Chưa có tên') AS ten_khach_hang,
                    sb.ma_san_code || ' - ' || sb.ten_san AS ten_san,
                    TO_CHAR(ds.ngay_dat, 'DD/MM/YYYY') AS ngay_dat,
                    ds.gio_bat_dau,
                    ds.gio_ket_thuc,
                    ds.trang_thai_dat_san,
                    ds.trang_thai_thanh_toan,
                    ds.tong_tien_thanh_toan
                FROM dat_san ds
                JOIN khach_hang kh ON ds.ma_khach_hang = kh.ma_khach_hang
                LEFT JOIN tai_khoan tk ON kh.ma_tai_khoan = tk.ma_tai_khoan
                JOIN san_bong sb ON ds.ma_san = sb.ma_san
                WHERE ds.trang_thai_dat_san IN ('DA_CHECK_IN', 'DA_HOAN_THANH')
                  AND ds.trang_thai_thanh_toan = 'CHUA_THANH_TOAN'
                ORDER BY ds.ngay_dat DESC, ds.gio_bat_dau ASC
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String maDon = rs.getString("ma_dat_san_code");
                String tenKhachHang = rs.getString("ten_khach_hang");
                String tenSan = rs.getString("ten_san");

                String ngayDat = rs.getString("ngay_dat");
                String gioBatDau = rs.getString("gio_bat_dau");
                String gioKetThuc = rs.getString("gio_ket_thuc");
                String thoiGian = ngayDat + " | " + gioBatDau + " - " + gioKetThuc;

                String trangThaiDon = chuyenTrangThaiDonSangTiengViet(
                        rs.getString("trang_thai_dat_san")
                );

                String trangThaiThanhToan = chuyenTrangThaiThanhToanSangTiengViet(
                        rs.getString("trang_thai_thanh_toan")
                );

                double tongTien = rs.getDouble("tong_tien_thanh_toan");
                String tongTienHienThi = tongTien <= 0 ? "Chưa tính" : dinhDangTien(tongTien);

                danhSachThanhToan.add(new ThanhToanRow(
                        maDon,
                        tenKhachHang,
                        tenSan,
                        thoiGian,
                        trangThaiDon,
                        trangThaiThanhToan,
                        tongTienHienThi
                ));
            }
        }

        return danhSachThanhToan;
    }

    public double tinhVaCapNhatTongTienTheoMaDon(String maDon) throws SQLException {
        if (maDon == null || maDon.trim().isEmpty()) {
            throw new SQLException("Mã đơn không hợp lệ.");
        }

        String maDonChuan = maDon.trim();

        String sqlLayDon = """
                SELECT
                    ma_dat_san,
                    ma_san,
                    ngay_dat,
                    gio_bat_dau,
                    gio_ket_thuc,
                    NVL(tien_giam, 0) AS tien_giam
                FROM dat_san
                WHERE ma_dat_san_code = ?
                  AND trang_thai_dat_san IN ('DA_CHECK_IN', 'DA_HOAN_THANH')
                  AND trang_thai_thanh_toan = 'CHUA_THANH_TOAN'
                """;

        try (Connection conn = DBConnection.getConnection()) {
            Integer maDatSan = null;
            Integer maSan = null;
            LocalDate ngayDat = null;
            String gioBatDau = null;
            String gioKetThuc = null;
            double tienGiam = 0;

            try (PreparedStatement ps = conn.prepareStatement(sqlLayDon)) {
                ps.setString(1, maDonChuan);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        maDatSan = rs.getInt("ma_dat_san");
                        maSan = rs.getInt("ma_san");

                        Date ngayDatSql = rs.getDate("ngay_dat");
                        ngayDat = ngayDatSql == null ? null : ngayDatSql.toLocalDate();

                        gioBatDau = rs.getString("gio_bat_dau");
                        gioKetThuc = rs.getString("gio_ket_thuc");
                        tienGiam = rs.getDouble("tien_giam");
                    }
                }
            }

            if (maDatSan == null || maSan == null || ngayDat == null) {
                throw new SQLException("Không tìm thấy đơn cần tính tiền hoặc đơn không hợp lệ để thanh toán.");
            }

            double tongTienGoc = tinhTienThueSan(conn, maSan, ngayDat, gioBatDau, gioKetThuc);
            double tongTienThanhToan = Math.max(tongTienGoc - tienGiam, 0);

            capNhatTongTienDatSan(conn, maDatSan, tongTienGoc, tongTienThanhToan);

            return tongTienThanhToan;
        }
    }

    private double tinhTienThueSan(Connection conn,
                                   int maSan,
                                   LocalDate ngayDat,
                                   String gioBatDauDat,
                                   String gioKetThucDat) throws SQLException {
        List<KhoangGia> danhSachGia = layBangGiaApDung(conn, maSan, ngayDat);

        if (danhSachGia.isEmpty()) {
            throw new SQLException("Chưa có bảng giá áp dụng cho sân này.");
        }

        LocalTime gioBatDau = LocalTime.parse(gioBatDauDat);
        LocalTime gioKetThuc = LocalTime.parse(gioKetThucDat);

        if (!gioKetThuc.isAfter(gioBatDau)) {
            throw new SQLException("Giờ kết thúc phải lớn hơn giờ bắt đầu.");
        }

        double tongTien = 0;

        for (KhoangGia khoangGia : danhSachGia) {
            LocalTime gioBatDauGia = LocalTime.parse(khoangGia.gioBatDau);
            LocalTime gioKetThucGia = LocalTime.parse(khoangGia.gioKetThuc);

            LocalTime batDauTinh = gioBatDau.isAfter(gioBatDauGia) ? gioBatDau : gioBatDauGia;
            LocalTime ketThucTinh = gioKetThuc.isBefore(gioKetThucGia) ? gioKetThuc : gioKetThucGia;

            if (ketThucTinh.isAfter(batDauTinh)) {
                long soPhut = ChronoUnit.MINUTES.between(batDauTinh, ketThucTinh);
                double soGio = soPhut / 60.0;
                tongTien += soGio * khoangGia.giaMoiGio;
            }
        }

        if (tongTien <= 0) {
            throw new SQLException("Không tìm thấy khung giá phù hợp với giờ đặt sân.");
        }

        return tongTien;
    }

    private List<KhoangGia> layBangGiaApDung(Connection conn,
                                             int maSan,
                                             LocalDate ngayDat) throws SQLException {
        List<KhoangGia> danhSachGia = new ArrayList<>();

        int thuTrongTuan = ngayDat.getDayOfWeek().getValue();

        String sql = """
                SELECT
                    gio_bat_dau,
                    gio_ket_thuc,
                    gia_moi_gio
                FROM bang_gia
                WHERE ma_san = ?
                  AND dang_ap_dung = 1
                  AND ngay_ap_dung_tu <= ?
                  AND (ngay_ap_dung_den IS NULL OR ngay_ap_dung_den >= ?)
                  AND (thu_trong_tuan IS NULL OR thu_trong_tuan = ?)
                ORDER BY gio_bat_dau ASC
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSan);
            ps.setDate(2, Date.valueOf(ngayDat));
            ps.setDate(3, Date.valueOf(ngayDat));
            ps.setInt(4, thuTrongTuan);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    danhSachGia.add(new KhoangGia(
                            rs.getString("gio_bat_dau"),
                            rs.getString("gio_ket_thuc"),
                            rs.getDouble("gia_moi_gio")
                    ));
                }
            }
        }

        return danhSachGia;
    }

    private void capNhatTongTienDatSan(Connection conn,
                                       int maDatSan,
                                       double tongTienGoc,
                                       double tongTienThanhToan) throws SQLException {
        String sql = """
                UPDATE dat_san
                SET tong_tien_goc = ?,
                    tong_tien_thanh_toan = ?,
                    ngay_cap_nhat = SYSDATE
                WHERE ma_dat_san = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, tongTienGoc);
            ps.setDouble(2, tongTienThanhToan);
            ps.setInt(3, maDatSan);
            ps.executeUpdate();
        }
    }

    private String chuyenTrangThaiDonSangTiengViet(String trangThai) {
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

    private String chuyenTrangThaiThanhToanSangTiengViet(String trangThai) {
        if (trangThai == null) {
            return "";
        }

        return switch (trangThai) {
            case "CHUA_THANH_TOAN" -> "CHƯA THANH TOÁN";
            case "THANH_TOAN_MOT_PHAN" -> "THANH TOÁN MỘT PHẦN";
            case "DA_THANH_TOAN" -> "ĐÃ THANH TOÁN";
            case "DA_HOAN_TIEN" -> "ĐÃ HOÀN TIỀN";
            default -> trangThai;
        };
    }

    private String dinhDangTien(double soTien) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(soTien) + " VND";
    }

    public boolean xacNhanThanhToanTheoMaDon(String maDon) throws SQLException {
    if (maDon == null || maDon.trim().isEmpty()) {
        throw new SQLException("Mã đơn không hợp lệ.");
    }

    String sql = """
            UPDATE dat_san
            SET trang_thai_thanh_toan = 'DA_THANH_TOAN',
                ngay_cap_nhat = SYSDATE
            WHERE ma_dat_san_code = ?
              AND trang_thai_dat_san IN ('DA_CHECK_IN', 'DA_HOAN_THANH')
              AND trang_thai_thanh_toan = 'CHUA_THANH_TOAN'
              AND NVL(tong_tien_thanh_toan, 0) > 0
            """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, maDon.trim());

        return ps.executeUpdate() > 0;
    }
}

    public List<ThanhToanRow> layDanhSachDonTheoBoLocThanhToan(String boLocThanhToan) throws SQLException {
    List<ThanhToanRow> danhSachThanhToan = new ArrayList<>();

    String boLoc = boLocThanhToan == null ? "CHUA_THANH_TOAN" : boLocThanhToan.trim();

    StringBuilder sql = new StringBuilder("""
            SELECT
                ds.ma_dat_san_code,
                kh.ma_khach_hang_code || ' - ' || NVL(tk.ho_ten, 'Chưa có tên') AS ten_khach_hang,
                sb.ma_san_code || ' - ' || sb.ten_san AS ten_san,
                TO_CHAR(ds.ngay_dat, 'DD/MM/YYYY') AS ngay_dat,
                ds.gio_bat_dau,
                ds.gio_ket_thuc,
                ds.trang_thai_dat_san,
                ds.trang_thai_thanh_toan,
                ds.tong_tien_thanh_toan
            FROM dat_san ds
            JOIN khach_hang kh ON ds.ma_khach_hang = kh.ma_khach_hang
            LEFT JOIN tai_khoan tk ON kh.ma_tai_khoan = tk.ma_tai_khoan
            JOIN san_bong sb ON ds.ma_san = sb.ma_san
            WHERE ds.trang_thai_dat_san IN ('DA_CHECK_IN', 'DA_HOAN_THANH')
            """);

    if (boLoc.equals("CHUA_THANH_TOAN")) {
        sql.append(" AND ds.trang_thai_thanh_toan = 'CHUA_THANH_TOAN' ");
    } else if (boLoc.equals("DA_THANH_TOAN")) {
        sql.append(" AND ds.trang_thai_thanh_toan = 'DA_THANH_TOAN' ");
    }

    sql.append(" ORDER BY ds.ngay_cap_nhat DESC, ds.ngay_dat DESC, ds.gio_bat_dau ASC ");

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql.toString());
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            String maDon = rs.getString("ma_dat_san_code");
            String tenKhachHang = rs.getString("ten_khach_hang");
            String tenSan = rs.getString("ten_san");

            String ngayDat = rs.getString("ngay_dat");
            String gioBatDau = rs.getString("gio_bat_dau");
            String gioKetThuc = rs.getString("gio_ket_thuc");
            String thoiGian = ngayDat + " | " + gioBatDau + " - " + gioKetThuc;

            String trangThaiDon = chuyenTrangThaiDonSangTiengViet(
                    rs.getString("trang_thai_dat_san")
            );

            String trangThaiThanhToan = chuyenTrangThaiThanhToanSangTiengViet(
                    rs.getString("trang_thai_thanh_toan")
            );

            double tongTien = rs.getDouble("tong_tien_thanh_toan");
            String tongTienHienThi = tongTien <= 0 ? "Chưa tính" : dinhDangTien(tongTien);

            danhSachThanhToan.add(new ThanhToanRow(
                    maDon,
                    tenKhachHang,
                    tenSan,
                    thoiGian,
                    trangThaiDon,
                    trangThaiThanhToan,
                    tongTienHienThi
            ));
        }
    }

    return danhSachThanhToan;
}

}