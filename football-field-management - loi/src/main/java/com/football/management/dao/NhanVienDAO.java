package com.football.management.dao;

import com.football.management.config.DBConnection;
import com.football.management.model.NhanVien;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {

    public List<NhanVien> layTatCaNhanVien() {
        List<NhanVien> danhSach = new ArrayList<>();

        String sql = """
                SELECT nv.ma_nhan_vien,
                       nv.ma_tai_khoan,
                       nv.ma_nhan_vien_code,
                       nv.ngay_vao_lam,
                       nv.trang_thai_nhan_vien,
                       nv.ghi_chu,
                       tk.ho_ten,
                       tk.email,
                       tk.so_dien_thoai,
                       tk.ten_dang_nhap,
                       tk.mat_khau,
                       tk.ma_vai_tro,
                       tk.trang_thai_tai_khoan
                FROM nhan_vien nv
                JOIN tai_khoan tk ON nv.ma_tai_khoan = tk.ma_tai_khoan
                ORDER BY nv.ma_nhan_vien
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                danhSach.add(mapRow(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay danh sach nhan vien", e);
        }

        return danhSach;
    }

    public List<NhanVien> timKiemNhanVien(String tuKhoa) {
        List<NhanVien> danhSach = new ArrayList<>();

        String sql = """
                SELECT nv.ma_nhan_vien,
                       nv.ma_tai_khoan,
                       nv.ma_nhan_vien_code,
                       nv.ngay_vao_lam,
                       nv.trang_thai_nhan_vien,
                       nv.ghi_chu,
                       tk.ho_ten,
                       tk.email,
                       tk.so_dien_thoai,
                       tk.ten_dang_nhap,
                       tk.mat_khau,
                       tk.ma_vai_tro,
                       tk.trang_thai_tai_khoan
                FROM nhan_vien nv
                JOIN tai_khoan tk ON nv.ma_tai_khoan = tk.ma_tai_khoan
                WHERE LOWER(nv.ma_nhan_vien_code) LIKE ?
                   OR LOWER(tk.ho_ten) LIKE ?
                   OR LOWER(tk.so_dien_thoai) LIKE ?
                ORDER BY nv.ma_nhan_vien
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
            throw new RuntimeException("Loi khi tim kiem nhan vien", e);
        }

        return danhSach;
    }

    public boolean tonTaiTenDangNhap(String tenDangNhap) {
        String sql = "SELECT COUNT(*) FROM tai_khoan WHERE ten_dang_nhap = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tenDangNhap);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Loi khi kiem tra ten dang nhap nhan vien", e);
        }

        return false;
    }

    public boolean themNhanVien(NhanVien nhanVien) {
        String sqlTaiKhoan = """
                INSERT INTO tai_khoan (
                    ten_dang_nhap,
                    mat_khau,
                    ho_ten,
                    email,
                    so_dien_thoai,
                    ma_vai_tro,
                    trang_thai_tai_khoan
                ) VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        String sqlNhanVien = """
                INSERT INTO nhan_vien (
                    ma_tai_khoan,
                    ma_nhan_vien_code,
                    trang_thai_nhan_vien,
                    ghi_chu
                ) VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement psTaiKhoan = conn.prepareStatement(sqlTaiKhoan, new String[]{"ma_tai_khoan"});
                    PreparedStatement psNhanVien = conn.prepareStatement(sqlNhanVien)
            ) {
                psTaiKhoan.setString(1, nhanVien.getTenDangNhap());
                psTaiKhoan.setString(2, nhanVien.getMatKhau());
                psTaiKhoan.setString(3, nhanVien.getHoTen());
                psTaiKhoan.setString(4, nhanVien.getEmail());
                psTaiKhoan.setString(5, nhanVien.getSoDienThoai());
                psTaiKhoan.setInt(6, nhanVien.getMaVaiTro());
                psTaiKhoan.setString(7, nhanVien.getTrangThaiTaiKhoan());

                int rowsTaiKhoan = psTaiKhoan.executeUpdate();
                if (rowsTaiKhoan == 0) {
                    conn.rollback();
                    return false;
                }

                int maTaiKhoan;
                try (ResultSet rs = psTaiKhoan.getGeneratedKeys()) {
                    if (rs.next()) {
                        maTaiKhoan = rs.getInt(1);
                    } else {
                        conn.rollback();
                        return false;
                    }
                }

                psNhanVien.setInt(1, maTaiKhoan);
                psNhanVien.setString(2, nhanVien.getMaNhanVienCode());
                psNhanVien.setString(3, nhanVien.getTrangThaiNhanVien());
                psNhanVien.setString(4, nhanVien.getGhiChu());

                int rowsNhanVien = psNhanVien.executeUpdate();
                if (rowsNhanVien == 0) {
                    conn.rollback();
                    return false;
                }

                conn.commit();
                return true;

            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (Exception e) {
            throw new RuntimeException("Loi khi them nhan vien", e);
        }
    }

    public boolean capNhatNhanVien(NhanVien nhanVien) {
        String sqlTaiKhoan = """
                UPDATE tai_khoan
                SET ho_ten = ?,
                    email = ?,
                    so_dien_thoai = ?,
                    ten_dang_nhap = ?,
                    mat_khau = ?,
                    ma_vai_tro = ?,
                    trang_thai_tai_khoan = ?,
                    ngay_cap_nhat = SYSDATE
                WHERE ma_tai_khoan = ?
                """;

        String sqlNhanVien = """
                UPDATE nhan_vien
                SET ma_nhan_vien_code = ?,
                    trang_thai_nhan_vien = ?,
                    ghi_chu = ?
                WHERE ma_nhan_vien = ?
                """;

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement psTaiKhoan = conn.prepareStatement(sqlTaiKhoan);
                    PreparedStatement psNhanVien = conn.prepareStatement(sqlNhanVien)
            ) {
                psTaiKhoan.setString(1, nhanVien.getHoTen());
                psTaiKhoan.setString(2, nhanVien.getEmail());
                psTaiKhoan.setString(3, nhanVien.getSoDienThoai());
                psTaiKhoan.setString(4, nhanVien.getTenDangNhap());
                psTaiKhoan.setString(5, nhanVien.getMatKhau());
                psTaiKhoan.setInt(6, nhanVien.getMaVaiTro());
                psTaiKhoan.setString(7, nhanVien.getTrangThaiTaiKhoan());
                psTaiKhoan.setInt(8, nhanVien.getMaTaiKhoan());

                psNhanVien.setString(1, nhanVien.getMaNhanVienCode());
                psNhanVien.setString(2, nhanVien.getTrangThaiNhanVien());
                psNhanVien.setString(3, nhanVien.getGhiChu());
                psNhanVien.setInt(4, nhanVien.getMaNhanVien());

                psTaiKhoan.executeUpdate();
                psNhanVien.executeUpdate();

                conn.commit();
                return true;

            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (Exception e) {
            throw new RuntimeException("Loi khi cap nhat nhan vien", e);
        }
    }

    public boolean xoaNhanVien(int maNhanVien, int maTaiKhoan) {
        String sqlNhanVien = "DELETE FROM nhan_vien WHERE ma_nhan_vien = ?";
        String sqlTaiKhoan = "DELETE FROM tai_khoan WHERE ma_tai_khoan = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement psNhanVien = conn.prepareStatement(sqlNhanVien);
                    PreparedStatement psTaiKhoan = conn.prepareStatement(sqlTaiKhoan)
            ) {
                psNhanVien.setInt(1, maNhanVien);
                psNhanVien.executeUpdate();

                psTaiKhoan.setInt(1, maTaiKhoan);
                psTaiKhoan.executeUpdate();

                conn.commit();
                return true;

            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (Exception e) {
            throw new RuntimeException("Loi khi xoa nhan vien", e);
        }
    }

    private NhanVien mapRow(ResultSet rs) throws SQLException {
        NhanVien nv = new NhanVien();

        nv.setMaNhanVien(rs.getInt("ma_nhan_vien"));
        nv.setMaTaiKhoan(rs.getInt("ma_tai_khoan"));
        nv.setMaNhanVienCode(rs.getString("ma_nhan_vien_code"));
        nv.setTrangThaiNhanVien(rs.getString("trang_thai_nhan_vien"));
        nv.setGhiChu(rs.getString("ghi_chu"));

        Timestamp ngayVaoLam = rs.getTimestamp("ngay_vao_lam");
        if (ngayVaoLam != null) {
            nv.setNgayVaoLam(ngayVaoLam.toLocalDateTime());
        }

        nv.setHoTen(rs.getString("ho_ten"));
        nv.setEmail(rs.getString("email"));
        nv.setSoDienThoai(rs.getString("so_dien_thoai"));
        nv.setTenDangNhap(rs.getString("ten_dang_nhap"));
        nv.setMatKhau(rs.getString("mat_khau"));
        nv.setMaVaiTro(rs.getInt("ma_vai_tro"));
        nv.setTrangThaiTaiKhoan(rs.getString("trang_thai_tai_khoan"));

        return nv;
    }
}