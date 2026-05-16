package com.football.management.dao;

import com.football.management.config.DBConnection;
import com.football.management.ui.NhanVien.DatSanRow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.time.LocalDate;

public class DatSanDAO {

    public static class ThongTinKhachHang {
    private final Integer maKhachHang;
    private final String maKhachHangCode;
    private final String tenKhachHang;
    private final String soDienThoai;

    public ThongTinKhachHang(Integer maKhachHang,
                             String maKhachHangCode,
                             String tenKhachHang,
                             String soDienThoai) {
        this.maKhachHang = maKhachHang;
        this.maKhachHangCode = maKhachHangCode;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
    }

    public Integer getMaKhachHang() {
        return maKhachHang;
    }

    public String getMaKhachHangCode() {
        return maKhachHangCode;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }
}
    public static class ChiTietDatSan {
    private final String maDon;
    private final String maSanCode;
    private final String maKhachHangCode;
    private final LocalDate ngayDat;
    private final String gioBatDau;
    private final String gioKetThuc;
    private final String ghiChu;

    public ChiTietDatSan(String maDon,
                         String maSanCode,
                         String maKhachHangCode,
                         LocalDate ngayDat,
                         String gioBatDau,
                         String gioKetThuc,
                         String ghiChu) {
        this.maDon = maDon;
        this.maSanCode = maSanCode;
        this.maKhachHangCode = maKhachHangCode;
        this.ngayDat = ngayDat;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.ghiChu = ghiChu;
    }

    public String getMaDon() {
        return maDon;
    }

    public String getMaSanCode() {
        return maSanCode;
    }

    public String getMaKhachHangCode() {
        return maKhachHangCode;
    }

    public LocalDate getNgayDat() {
        return ngayDat;
    }

    public String getGioBatDau() {
        return gioBatDau;
    }

    public String getGioKetThuc() {
        return gioKetThuc;
    }

    public String getGhiChu() {
        return ghiChu;
    }
}

    public List<DatSanRow> layDanhSachDatSanChoNhanVien() throws SQLException {
        List<DatSanRow> danhSachDatSan = new ArrayList<>();

        String sql = """
        SELECT
            ds.ma_dat_san_code,
            kh.ma_khach_hang_code,
            NVL(tk.ho_ten, 'Chưa có tên') AS ten_khach_hang,
            sb.ma_san_code || ' - ' || sb.ten_san AS ten_san,
            TO_CHAR(ds.ngay_dat, 'DD/MM/YYYY') AS ngay_dat,
            ds.gio_bat_dau,
            ds.gio_ket_thuc,
            ds.trang_thai_dat_san
        FROM dat_san ds
        JOIN khach_hang kh ON ds.ma_khach_hang = kh.ma_khach_hang
        LEFT JOIN tai_khoan tk ON kh.ma_tai_khoan = tk.ma_tai_khoan
        JOIN san_bong sb ON ds.ma_san = sb.ma_san
        ORDER BY ds.ma_dat_san DESC
        """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                String maDon = resultSet.getString("ma_dat_san_code");
                String maKhachHang = resultSet.getString("ma_khach_hang_code");
                String tenKhachHang = resultSet.getString("ten_khach_hang");
                String tenSan = resultSet.getString("ten_san");

                String ngayDat = resultSet.getString("ngay_dat");
                String gioBatDau = resultSet.getString("gio_bat_dau");
                String gioKetThuc = resultSet.getString("gio_ket_thuc");
                String thoiGian = ngayDat + " | " + gioBatDau + " - " + gioKetThuc;

                String trangThai = chuyenTrangThaiSangTiengViet(
                        resultSet.getString("trang_thai_dat_san")
                );

                danhSachDatSan.add(new DatSanRow(
                    maDon,
                    maKhachHang,
                    tenKhachHang,
                    tenSan,
                    thoiGian,
                    trangThai
));
            }
        }

        return danhSachDatSan;
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
    public boolean kiemTraTrungLich(int maSan,
                                LocalDate ngayDat,
                                String gioBatDau,
                                String gioKetThuc,
                                Integer maDatSanBoQua) {
    String sql =
            "SELECT COUNT(*) AS so_luong " +
            "FROM dat_san " +
            "WHERE ma_san = ? " +
            "AND ngay_dat = ? " +
            "AND trang_thai_dat_san NOT IN ('DA_HUY', 'DA_HOAN_THANH') " +
            "AND (? < gio_ket_thuc AND ? > gio_bat_dau) ";

    if (maDatSanBoQua != null) {
        sql += "AND ma_dat_san <> ? ";
    }

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, maSan);
        ps.setDate(2, Date.valueOf(ngayDat));
        ps.setString(3, gioBatDau);
        ps.setString(4, gioKetThuc);

        if (maDatSanBoQua != null) {
            ps.setInt(5, maDatSanBoQua);
        }

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("so_luong") > 0;
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return true;
}
public Integer timMaKhachHangTheoMaNhap(String maKhachHangNhap) throws SQLException {
    if (maKhachHangNhap == null || maKhachHangNhap.trim().isEmpty()) {
        return null;
    }

    String giaTriNhap = maKhachHangNhap.trim();

    String sql =
            "SELECT ma_khach_hang " +
            "FROM khach_hang " +
            "WHERE TO_CHAR(ma_khach_hang) = ? " +
            "OR UPPER(ma_khach_hang_code) = UPPER(?)";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, giaTriNhap);
        ps.setString(2, giaTriNhap);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("ma_khach_hang");
            }
        }
    }

    return null;
}

public Integer timMaSanTheoMaNhap(String maSanNhap) throws SQLException {
    if (maSanNhap == null || maSanNhap.trim().isEmpty()) {
        return null;
    }

    String giaTriNhap = maSanNhap.trim();

    String sql =
            "SELECT ma_san " +
            "FROM san_bong " +
            "WHERE TO_CHAR(ma_san) = ? " +
            "OR UPPER(ma_san_code) = UPPER(?)";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, giaTriNhap);
        ps.setString(2, giaTriNhap);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("ma_san");
            }
        }
    }

    return null;
}
public String taoMaDatSanCodeTuDong() throws SQLException {
    String sql = "SELECT NVL(MAX(ma_dat_san), 0) + 1 AS ma_tiep_theo FROM dat_san";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            int maTiepTheo = rs.getInt("ma_tiep_theo");
            return String.format("DS%03d", maTiepTheo);
        }
    }

    return "DS001";
}

public boolean themDonDatSanNhanVien(int maSan,
                                     int maKhachHang,
                                     LocalDate ngayDat,
                                     String gioBatDau,
                                     String gioKetThuc,
                                     String ghiChu) throws SQLException {
    String maDatSanCode = taoMaDatSanCodeTuDong();

    String sql =
            "INSERT INTO dat_san (" +
                    "ma_dat_san_code, " +
                    "ma_khach_hang, " +
                    "ma_san, " +
                    "ma_nhan_vien_tao, " +
                    "ngay_dat, " +
                    "gio_bat_dau, " +
                    "gio_ket_thuc, " +
                    "trang_thai_dat_san, " +
                    "ghi_chu, " +
                    "trang_thai_thanh_toan" +
            ") VALUES (?, ?, ?, NULL, ?, ?, ?, 'DA_XAC_NHAN', ?, 'CHUA_THANH_TOAN')";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, maDatSanCode);
        ps.setInt(2, maKhachHang);
        ps.setInt(3, maSan);
        ps.setDate(4, Date.valueOf(ngayDat));
        ps.setString(5, gioBatDau.trim());
        ps.setString(6, gioKetThuc.trim());
        ps.setString(7, ghiChu == null ? "" : ghiChu.trim());

        return ps.executeUpdate() > 0;
    }
}
    public ThongTinKhachHang timThongTinKhachHangTheoMaHoacSdt(String giaTriNhap) throws SQLException {
    if (giaTriNhap == null || giaTriNhap.trim().isEmpty()) {
        return null;
    }

    String tuKhoa = giaTriNhap.trim();

    String sql = """
            SELECT
                kh.ma_khach_hang,
                kh.ma_khach_hang_code,
                tk.ho_ten,
                tk.so_dien_thoai
            FROM khach_hang kh
            LEFT JOIN tai_khoan tk ON kh.ma_tai_khoan = tk.ma_tai_khoan
            WHERE UPPER(kh.ma_khach_hang_code) = UPPER(?)
               OR TO_CHAR(kh.ma_khach_hang) = ?
               OR tk.so_dien_thoai = ?
            FETCH FIRST 1 ROWS ONLY
            """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, tuKhoa);
        ps.setString(2, tuKhoa);
        ps.setString(3, tuKhoa);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new ThongTinKhachHang(
                        rs.getInt("ma_khach_hang"),
                        rs.getString("ma_khach_hang_code"),
                        rs.getString("ho_ten"),
                        rs.getString("so_dien_thoai")
                );
            }
        }
    }

    return null;
}
    public ThongTinKhachHang taoKhachHangMoiNhanh(String tenKhachHang,
                                               String soDienThoai) throws SQLException {
    if (tenKhachHang == null || tenKhachHang.trim().isEmpty()) {
        throw new SQLException("Tên khách hàng không được để trống.");
    }

    if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
        throw new SQLException("Số điện thoại khách hàng không được để trống.");
    }

    String ten = tenKhachHang.trim();
    String sdt = soDienThoai.trim();
    String maKhachHangCode = taoMaKhachHangCodeTuDong();
    String tenDangNhap = maKhachHangCode.toLowerCase();

    String sqlThemTaiKhoan = """
            INSERT INTO tai_khoan (
                ten_dang_nhap,
                mat_khau,
                ho_ten,
                email,
                so_dien_thoai,
                ma_vai_tro,
                trang_thai_tai_khoan
            ) VALUES (
                ?,
                ?,
                ?,
                NULL,
                ?,
                (SELECT ma_vai_tro FROM vai_tro WHERE ten_vai_tro = 'KHACH_HANG'),
                'HOAT_DONG'
            )
            """;

    String sqlLayMaTaiKhoan = """
            SELECT ma_tai_khoan
            FROM tai_khoan
            WHERE ten_dang_nhap = ?
            """;

    String sqlThemKhachHang = """
            INSERT INTO khach_hang (
                ma_tai_khoan,
                ma_khach_hang_code,
                trang_thai_khach_hang
            ) VALUES (?, ?, 'HOAT_DONG')
            """;

    String sqlLayMaKhachHang = """
            SELECT ma_khach_hang
            FROM khach_hang
            WHERE ma_khach_hang_code = ?
            """;

    try (Connection conn = DBConnection.getConnection()) {
        conn.setAutoCommit(false);

        try {
            try (PreparedStatement ps = conn.prepareStatement(sqlThemTaiKhoan)) {
                ps.setString(1, tenDangNhap);
                ps.setString(2, "123");
                ps.setString(3, ten);
                ps.setString(4, sdt);
                ps.executeUpdate();
            }

            int maTaiKhoan;

            try (PreparedStatement ps = conn.prepareStatement(sqlLayMaTaiKhoan)) {
                ps.setString(1, tenDangNhap);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Không lấy được mã tài khoản vừa tạo.");
                    }

                    maTaiKhoan = rs.getInt("ma_tai_khoan");
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlThemKhachHang)) {
                ps.setInt(1, maTaiKhoan);
                ps.setString(2, maKhachHangCode);
                ps.executeUpdate();
            }

            int maKhachHang;

            try (PreparedStatement ps = conn.prepareStatement(sqlLayMaKhachHang)) {
                ps.setString(1, maKhachHangCode);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Không lấy được mã khách hàng vừa tạo.");
                    }

                    maKhachHang = rs.getInt("ma_khach_hang");
                }
            }

            conn.commit();

            return new ThongTinKhachHang(
                    maKhachHang,
                    maKhachHangCode,
                    ten,
                    sdt
            );

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}
    public String taoMaKhachHangCodeTuDong() throws SQLException {
    String sql = """
            SELECT NVL(MAX(TO_NUMBER(REGEXP_SUBSTR(ma_khach_hang_code, '[0-9]+'))), 0) + 1 AS ma_tiep_theo
            FROM khach_hang
            WHERE REGEXP_LIKE(ma_khach_hang_code, '^KH[0-9]+$')
            """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            int maTiepTheo = rs.getInt("ma_tiep_theo");
            return String.format("KH%03d", maTiepTheo);
        }
    }

    return "KH001";
}
    public boolean huyDonDatSanTheoMaDon(String maDon) throws SQLException {
    if (maDon == null || maDon.trim().isEmpty()) {
        return false;
    }

    String sql = """
            UPDATE dat_san
            SET trang_thai_dat_san = 'DA_HUY',
                ngay_cap_nhat = SYSDATE
            WHERE ma_dat_san_code = ?
              AND trang_thai_dat_san IN ('CHO_XU_LY', 'DA_XAC_NHAN')
            """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, maDon.trim());

        return ps.executeUpdate() > 0;
    }
}
    
    public boolean xacNhanKhachDenSanTheoMaDon(String maDon) throws SQLException {
    if (maDon == null || maDon.trim().isEmpty()) {
        return false;
    }

    String sqlLayMaSan = """
            SELECT ma_san
            FROM dat_san
            WHERE ma_dat_san_code = ?
              AND trang_thai_dat_san IN ('CHO_XU_LY', 'DA_XAC_NHAN')
            """;

    String sqlCapNhatDon = """
            UPDATE dat_san
            SET trang_thai_dat_san = 'DA_CHECK_IN',
                thoi_gian_check_in = SYSDATE,
                ngay_cap_nhat = SYSDATE
            WHERE ma_dat_san_code = ?
              AND trang_thai_dat_san IN ('CHO_XU_LY', 'DA_XAC_NHAN')
            """;

    String sqlCapNhatSan = """
            UPDATE san_bong
            SET trang_thai_san = 'DANG_SU_DUNG',
                ngay_cap_nhat = SYSDATE
            WHERE ma_san = ?
            """;

    try (Connection conn = DBConnection.getConnection()) {
        conn.setAutoCommit(false);

        try {
            Integer maSan = null;

            try (PreparedStatement ps = conn.prepareStatement(sqlLayMaSan)) {
                ps.setString(1, maDon.trim());

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        maSan = rs.getInt("ma_san");
                    }
                }
            }

            if (maSan == null) {
                conn.rollback();
                return false;
            }

            int soDongCapNhatDon;

            try (PreparedStatement ps = conn.prepareStatement(sqlCapNhatDon)) {
                ps.setString(1, maDon.trim());
                soDongCapNhatDon = ps.executeUpdate();
            }

            if (soDongCapNhatDon <= 0) {
                conn.rollback();
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlCapNhatSan)) {
                ps.setInt(1, maSan);
                ps.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}

    public ChiTietDatSan layChiTietDatSanTheoMaDon(String maDon) throws SQLException {
    if (maDon == null || maDon.trim().isEmpty()) {
        return null;
    }

    String sql = """
            SELECT
                ds.ma_dat_san_code,
                sb.ma_san_code,
                kh.ma_khach_hang_code,
                ds.ngay_dat,
                ds.gio_bat_dau,
                ds.gio_ket_thuc,
                ds.ghi_chu
            FROM dat_san ds
            JOIN san_bong sb ON ds.ma_san = sb.ma_san
            JOIN khach_hang kh ON ds.ma_khach_hang = kh.ma_khach_hang
            WHERE ds.ma_dat_san_code = ?
            """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, maDon.trim());

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Date ngayDatSql = rs.getDate("ngay_dat");

                return new ChiTietDatSan(
                        rs.getString("ma_dat_san_code"),
                        rs.getString("ma_san_code"),
                        rs.getString("ma_khach_hang_code"),
                        ngayDatSql == null ? null : ngayDatSql.toLocalDate(),
                        rs.getString("gio_bat_dau"),
                        rs.getString("gio_ket_thuc"),
                        rs.getString("ghi_chu")
                );
            }
        }
    }

    return null;
}
    public Integer timMaDatSanNoiBoTheoMaDon(String maDon) throws SQLException {
    if (maDon == null || maDon.trim().isEmpty()) {
        return null;
    }

    String sql = """
            SELECT ma_dat_san
            FROM dat_san
            WHERE ma_dat_san_code = ?
            """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, maDon.trim());

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("ma_dat_san");
            }
        }
    }

    return null;
}
    
    public boolean capNhatDonDatSanTheoMaDon(String maDon,
                                         int maSan,
                                         int maKhachHang,
                                         LocalDate ngayDat,
                                         String gioBatDau,
                                         String gioKetThuc,
                                         String ghiChu) throws SQLException {
    if (maDon == null || maDon.trim().isEmpty()) {
        return false;
    }

    String sql = """
        UPDATE dat_san
        SET ma_san = ?,
            ma_khach_hang = ?,
            ngay_dat = ?,
            gio_bat_dau = ?,
            gio_ket_thuc = ?,
            ghi_chu = ?,
            ngay_cap_nhat = SYSDATE
        WHERE ma_dat_san_code = ?
          AND trang_thai_dat_san IN ('CHO_XU_LY', 'DA_XAC_NHAN')
        """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, maSan);
        ps.setInt(2, maKhachHang);
        ps.setDate(3, Date.valueOf(ngayDat));
        ps.setString(4, gioBatDau.trim());
        ps.setString(5, gioKetThuc.trim());
        ps.setString(6, ghiChu == null ? "" : ghiChu.trim());
        ps.setString(7, maDon.trim());

        return ps.executeUpdate() > 0;
    }
}
    public boolean hoanThanhDonDatSanTheoMaDon(String maDon) throws SQLException {
    if (maDon == null || maDon.trim().isEmpty()) {
        return false;
    }

    String sqlLayMaSan = """
            SELECT ma_san
            FROM dat_san
            WHERE ma_dat_san_code = ?
              AND trang_thai_dat_san = 'DA_CHECK_IN'
            """;

    String sqlCapNhatDon = """
            UPDATE dat_san
            SET trang_thai_dat_san = 'DA_HOAN_THANH',
                ngay_cap_nhat = SYSDATE
            WHERE ma_dat_san_code = ?
              AND trang_thai_dat_san = 'DA_CHECK_IN'
            """;

    String sqlCapNhatSan = """
            UPDATE san_bong
            SET trang_thai_san = 'SAN_SANG',
                ngay_cap_nhat = SYSDATE
            WHERE ma_san = ?
            """;

    try (Connection conn = DBConnection.getConnection()) {
        conn.setAutoCommit(false);

        try {
            Integer maSan = null;

            try (PreparedStatement ps = conn.prepareStatement(sqlLayMaSan)) {
                ps.setString(1, maDon.trim());

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        maSan = rs.getInt("ma_san");
                    }
                }
            }

            if (maSan == null) {
                conn.rollback();
                return false;
            }

            int soDongCapNhatDon;

            try (PreparedStatement ps = conn.prepareStatement(sqlCapNhatDon)) {
                ps.setString(1, maDon.trim());
                soDongCapNhatDon = ps.executeUpdate();
            }

            if (soDongCapNhatDon <= 0) {
                conn.rollback();
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlCapNhatSan)) {
                ps.setInt(1, maSan);
                ps.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}
}