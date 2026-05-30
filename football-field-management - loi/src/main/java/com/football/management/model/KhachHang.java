package com.football.management.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class KhachHang {
    private int maKhachHang;
    private Integer maTaiKhoan;
    private String maKhachHangCode;
    private LocalDate ngaySinh;
    private String gioiTinh;
    private String diaChi;
    private String trangThaiKhachHang;
    private LocalDateTime ngayTao;

    public KhachHang() {
    }

    public KhachHang(int maKhachHang, Integer maTaiKhoan, String maKhachHangCode,
                     LocalDate ngaySinh, String gioiTinh, String diaChi,
                     String trangThaiKhachHang, LocalDateTime ngayTao) {
        this.maKhachHang = maKhachHang;
        this.maTaiKhoan = maTaiKhoan;
        this.maKhachHangCode = maKhachHangCode;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
        this.trangThaiKhachHang = trangThaiKhachHang;
        this.ngayTao = ngayTao;
    }

    public int getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(int maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public Integer getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(Integer maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public String getMaKhachHangCode() {
        return maKhachHangCode;
    }

    public void setMaKhachHangCode(String maKhachHangCode) {
        this.maKhachHangCode = maKhachHangCode;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getTrangThaiKhachHang() {
        return trangThaiKhachHang;
    }

    public void setTrangThaiKhachHang(String trangThaiKhachHang) {
        this.trangThaiKhachHang = trangThaiKhachHang;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }
}