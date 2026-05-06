package com.football.management.service;

import com.football.management.dao.NhanVienDAO;
import com.football.management.model.NhanVien;

import java.util.List;

public class NhanVienService {
    private final NhanVienDAO nhanVienDAO = new NhanVienDAO();

    public List<NhanVien> layTatCaNhanVien() {
        return nhanVienDAO.layTatCaNhanVien();
    }

    public List<NhanVien> timKiemNhanVien(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            return nhanVienDAO.layTatCaNhanVien();
        }
        return nhanVienDAO.timKiemNhanVien(tuKhoa.trim());
    }

    public boolean themNhanVien(NhanVien nhanVien) {
        validateNhanVien(nhanVien);

        if (nhanVienDAO.tonTaiTenDangNhap(nhanVien.getTenDangNhap())) {
            throw new IllegalArgumentException("Ten dang nhap da ton tai");
        }

        return nhanVienDAO.themNhanVien(nhanVien);
    }

    public boolean capNhatNhanVien(NhanVien nhanVien) {
        validateNhanVien(nhanVien);
        return nhanVienDAO.capNhatNhanVien(nhanVien);
    }

    public boolean xoaNhanVien(int maNhanVien, int maTaiKhoan) {
        return nhanVienDAO.xoaNhanVien(maNhanVien, maTaiKhoan);
    }

    private void validateNhanVien(NhanVien nhanVien) {
        if (nhanVien.getMaNhanVienCode() == null || nhanVien.getMaNhanVienCode().isBlank()) {
            throw new IllegalArgumentException("Ma nhan vien khong duoc de trong");
        }
        if (nhanVien.getHoTen() == null || nhanVien.getHoTen().isBlank()) {
            throw new IllegalArgumentException("Ho ten khong duoc de trong");
        }
        if (nhanVien.getSoDienThoai() == null || nhanVien.getSoDienThoai().isBlank()) {
            throw new IllegalArgumentException("So dien thoai khong duoc de trong");
        }
        if (nhanVien.getTenDangNhap() == null || nhanVien.getTenDangNhap().isBlank()) {
            throw new IllegalArgumentException("Ten dang nhap khong duoc de trong");
        }
        if (nhanVien.getMatKhau() == null || nhanVien.getMatKhau().isBlank()) {
            throw new IllegalArgumentException("Mat khau khong duoc de trong");
        }
    }
}