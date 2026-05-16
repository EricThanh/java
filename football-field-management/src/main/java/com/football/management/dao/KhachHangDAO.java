package com.football.management.dao;

import com.football.management.config.DBConnection;
import com.football.management.model.KhachHang;
import com.football.management.ui.NhanVien.KhachHangRow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

    public List<KhachHangRow> layDanhSachKhachHangChoNhanVien() throws SQLException {
        List<KhachHangRow> danhSachKhachHang = new ArrayList<>();

        String sql = """
                SELECT
                    kh.ma_khach_hang_code,
                    NVL(tk.ho_ten, 'Chưa có tên') AS ho_ten,
                    NVL(tk.so_dien_thoai, '') AS so_dien_thoai,
                    NVL(tk.email, '') AS email,
                    NVL(kh.dia_chi, '') AS dia_chi,
                    kh.trang_thai_khach_hang
                FROM khach_hang kh
                LEFT JOIN tai_khoan tk ON kh.ma_tai_khoan = tk.ma_tai_khoan
                ORDER BY kh.ma_khach_hang DESC
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String maKhachHang = rs.getString("ma_khach_hang_code");
                String hoTen = rs.getString("ho_ten");
                String soDienThoai = rs.getString("so_dien_thoai");
                String email = rs.getString("email");
                String diaChi = rs.getString("dia_chi");
                String trangThai = chuyenTrangThaiSangTiengViet(
                        rs.getString("trang_thai_khach_hang")
                );

                danhSachKhachHang.add(new KhachHangRow(
                        maKhachHang,
                        hoTen,
                        soDienThoai,
                        email,
                        diaChi,
                        trangThai
                ));
            }
        }

        return danhSachKhachHang;
    }

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
            throw new RuntimeException("Lỗi khi thêm khách hàng", e);
        }
    }

    public String taoMaKhachHangTuDong() {
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

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tạo mã khách hàng", e);
        }

        return "KH001";
    }

    private String chuyenTrangThaiSangTiengViet(String trangThai) {
        if (trangThai == null) {
            return "";
        }

        return switch (trangThai) {
            case "HOAT_DONG" -> "HOẠT ĐỘNG";
            case "NGUNG_HOAT_DONG" -> "NGỪNG HOẠT ĐỘNG";
            default -> trangThai;
        };
    }

    public boolean tonTaiSoDienThoai(String soDienThoai) throws SQLException {
    if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
        return false;
    }

    String sql = """
            SELECT COUNT(*) AS so_luong
            FROM tai_khoan
            WHERE so_dien_thoai = ?
            """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, soDienThoai.trim());

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("so_luong") > 0;
            }
        }
    }

    return false;
}

    public boolean tonTaiEmail(String email) throws SQLException {
    if (email == null || email.trim().isEmpty()) {
        return false;
    }

    String sql = """
            SELECT COUNT(*) AS so_luong
            FROM tai_khoan
            WHERE LOWER(email) = LOWER(?)
            """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, email.trim());

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("so_luong") > 0;
            }
        }
    }

    return false;
}

    public boolean themKhachHangChoNhanVien(String hoTen,
                                        String soDienThoai,
                                        String email,
                                        String diaChi) throws SQLException {
    if (hoTen == null || hoTen.trim().isEmpty()) {
        throw new SQLException("Họ tên khách hàng không được để trống.");
    }

    if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
        throw new SQLException("Số điện thoại khách hàng không được để trống.");
    }

    String hoTenMoi = hoTen.trim();
    String soDienThoaiMoi = soDienThoai.trim();
    String emailMoi = email == null ? "" : email.trim();
    String diaChiMoi = diaChi == null ? "" : diaChi.trim();

    if (tonTaiSoDienThoai(soDienThoaiMoi)) {
        throw new SQLException("Số điện thoại đã tồn tại trong hệ thống.");
    }

    if (!emailMoi.isEmpty() && tonTaiEmail(emailMoi)) {
        throw new SQLException("Email đã tồn tại trong hệ thống.");
    }

    String maKhachHangCode = taoMaKhachHangTuDong();
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
                ?,
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
                dia_chi,
                trang_thai_khach_hang
            ) VALUES (?, ?, ?, 'HOAT_DONG')
            """;

    try (Connection conn = DBConnection.getConnection()) {
        conn.setAutoCommit(false);

        try {
            try (PreparedStatement ps = conn.prepareStatement(sqlThemTaiKhoan)) {
                ps.setString(1, tenDangNhap);
                ps.setString(2, "123");
                ps.setString(3, hoTenMoi);

                if (emailMoi.isEmpty()) {
                    ps.setNull(4, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(4, emailMoi);
                }

                ps.setString(5, soDienThoaiMoi);
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
                ps.setString(3, diaChiMoi);
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

    public boolean tonTaiSoDienThoaiCuaKhachHangKhac(String soDienThoai,
                                                 String maKhachHangCode) throws SQLException {
    if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
        return false;
    }

    String sql = """
            SELECT COUNT(*) AS so_luong
            FROM khach_hang kh
            JOIN tai_khoan tk ON kh.ma_tai_khoan = tk.ma_tai_khoan
            WHERE tk.so_dien_thoai = ?
              AND UPPER(kh.ma_khach_hang_code) <> UPPER(?)
            """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, soDienThoai.trim());
        ps.setString(2, maKhachHangCode.trim());

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("so_luong") > 0;
            }
        }
    }

    return false;
}

    public boolean tonTaiEmailCuaKhachHangKhac(String email,
                                           String maKhachHangCode) throws SQLException {
    if (email == null || email.trim().isEmpty()) {
        return false;
    }

    String sql = """
            SELECT COUNT(*) AS so_luong
            FROM khach_hang kh
            JOIN tai_khoan tk ON kh.ma_tai_khoan = tk.ma_tai_khoan
            WHERE LOWER(tk.email) = LOWER(?)
              AND UPPER(kh.ma_khach_hang_code) <> UPPER(?)
            """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, email.trim());
        ps.setString(2, maKhachHangCode.trim());

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("so_luong") > 0;
            }
        }
    }

    return false;
}

    public boolean capNhatKhachHangChoNhanVien(String maKhachHangCode,
                                           String hoTen,
                                           String soDienThoai,
                                           String email,
                                           String diaChi) throws SQLException {
    if (maKhachHangCode == null || maKhachHangCode.trim().isEmpty()) {
        throw new SQLException("Mã khách hàng không hợp lệ.");
    }

    if (hoTen == null || hoTen.trim().isEmpty()) {
        throw new SQLException("Họ tên khách hàng không được để trống.");
    }

    if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
        throw new SQLException("Số điện thoại khách hàng không được để trống.");
    }

    String maCode = maKhachHangCode.trim();
    String hoTenMoi = hoTen.trim();
    String soDienThoaiMoi = soDienThoai.trim();
    String emailMoi = email == null ? "" : email.trim();
    String diaChiMoi = diaChi == null ? "" : diaChi.trim();

    if (tonTaiSoDienThoaiCuaKhachHangKhac(soDienThoaiMoi, maCode)) {
        throw new SQLException("Số điện thoại đã thuộc về khách hàng khác.");
    }

    if (!emailMoi.isEmpty() && tonTaiEmailCuaKhachHangKhac(emailMoi, maCode)) {
        throw new SQLException("Email đã thuộc về khách hàng khác.");
    }

    String sqlCapNhatTaiKhoan = """
            UPDATE tai_khoan
            SET ho_ten = ?,
                so_dien_thoai = ?,
                email = ?
            WHERE ma_tai_khoan = (
                SELECT ma_tai_khoan
                FROM khach_hang
                WHERE UPPER(ma_khach_hang_code) = UPPER(?)
            )
            """;

    String sqlCapNhatKhachHang = """
            UPDATE khach_hang
            SET dia_chi = ?
            WHERE UPPER(ma_khach_hang_code) = UPPER(?)
            """;

    try (Connection conn = DBConnection.getConnection()) {
        conn.setAutoCommit(false);

        try {
            int soDongTaiKhoan;

            try (PreparedStatement ps = conn.prepareStatement(sqlCapNhatTaiKhoan)) {
                ps.setString(1, hoTenMoi);
                ps.setString(2, soDienThoaiMoi);

                if (emailMoi.isEmpty()) {
                    ps.setNull(3, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(3, emailMoi);
                }

                ps.setString(4, maCode);

                soDongTaiKhoan = ps.executeUpdate();
            }

            if (soDongTaiKhoan <= 0) {
                conn.rollback();
                throw new SQLException("Không tìm thấy tài khoản của khách hàng cần cập nhật.");
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlCapNhatKhachHang)) {
                ps.setString(1, diaChiMoi);
                ps.setString(2, maCode);
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

    public boolean khachHangCoDonChuaKetThuc(String maKhachHangCode) throws SQLException {
    if (maKhachHangCode == null || maKhachHangCode.trim().isEmpty()) {
        return false;
    }

    String sql = """
            SELECT COUNT(*) AS so_luong
            FROM dat_san ds
            JOIN khach_hang kh ON ds.ma_khach_hang = kh.ma_khach_hang
            WHERE UPPER(kh.ma_khach_hang_code) = UPPER(?)
              AND ds.trang_thai_dat_san IN ('CHO_XU_LY', 'DA_XAC_NHAN', 'DA_CHECK_IN')
            """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, maKhachHangCode.trim());

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("so_luong") > 0;
            }
        }
    }

    return false;
}

    public boolean ngungHoatDongKhachHangChoNhanVien(String maKhachHangCode) throws SQLException {
    if (maKhachHangCode == null || maKhachHangCode.trim().isEmpty()) {
        throw new SQLException("Mã khách hàng không hợp lệ.");
    }

    String maCode = maKhachHangCode.trim();

    if (khachHangCoDonChuaKetThuc(maCode)) {
        throw new SQLException("Khách hàng còn đơn đặt sân chưa kết thúc, không thể ngưng hoạt động.");
    }

    String sqlCapNhatKhachHang = """
            UPDATE khach_hang
            SET trang_thai_khach_hang = 'NGUNG_HOAT_DONG'
            WHERE UPPER(ma_khach_hang_code) = UPPER(?)
              AND trang_thai_khach_hang = 'HOAT_DONG'
            """;

    String sqlCapNhatTaiKhoan = """
            UPDATE tai_khoan
            SET trang_thai_tai_khoan = 'NGUNG_HOAT_DONG',
                ngay_cap_nhat = SYSDATE
            WHERE ma_tai_khoan = (
                SELECT ma_tai_khoan
                FROM khach_hang
                WHERE UPPER(ma_khach_hang_code) = UPPER(?)
            )
            """;

    try (Connection conn = DBConnection.getConnection()) {
        conn.setAutoCommit(false);

        try {
            int soDongKhachHang;

            try (PreparedStatement ps = conn.prepareStatement(sqlCapNhatKhachHang)) {
                ps.setString(1, maCode);
                soDongKhachHang = ps.executeUpdate();
            }

            if (soDongKhachHang <= 0) {
                conn.rollback();
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlCapNhatTaiKhoan)) {
                ps.setString(1, maCode);
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