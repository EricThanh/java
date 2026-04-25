package com.football.management.model;

import java.time.LocalDateTime;

public class NhanVien {
    private int maNhanVien;
    private int maTaiKhoan;
    private String maNhanVienCode;
    private LocalDateTime ngayVaoLam;
    private String trangThaiNhanVien;
    private String ghiChu;

    public NhanVien() {
    }

    public NhanVien(int maNhanVien, int maTaiKhoan, String maNhanVienCode,
                    LocalDateTime ngayVaoLam, String trangThaiNhanVien, String ghiChu) {
        this.maNhanVien = maNhanVien;
        this.maTaiKhoan = maTaiKhoan;
        this.maNhanVienCode = maNhanVienCode;
        this.ngayVaoLam = ngayVaoLam;
        this.trangThaiNhanVien = trangThaiNhanVien;
        this.ghiChu = ghiChu;
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public int getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(int maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public String getMaNhanVienCode() {
        return maNhanVienCode;
    }

    public void setMaNhanVienCode(String maNhanVienCode) {
        this.maNhanVienCode = maNhanVienCode;
    }

    public LocalDateTime getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(LocalDateTime ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    public String getTrangThaiNhanVien() {
        return trangThaiNhanVien;
    }

    public void setTrangThaiNhanVien(String trangThaiNhanVien) {
        this.trangThaiNhanVien = trangThaiNhanVien;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}