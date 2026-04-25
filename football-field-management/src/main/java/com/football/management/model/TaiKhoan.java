package com.football.management.model;

import java.time.LocalDateTime;

public class TaiKhoan {
    private int maTaiKhoan;
    private String tenDangNhap;
    private String matKhau;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private int maVaiTro;
    private String trangThaiTaiKhoan;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;

    public TaiKhoan() {
    }

    public TaiKhoan(int maTaiKhoan, String tenDangNhap, String matKhau, String hoTen,
                    String email, String soDienThoai, int maVaiTro,
                    String trangThaiTaiKhoan, LocalDateTime ngayTao, LocalDateTime ngayCapNhat) {
        this.maTaiKhoan = maTaiKhoan;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.hoTen = hoTen;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.maVaiTro = maVaiTro;
        this.trangThaiTaiKhoan = trangThaiTaiKhoan;
        this.ngayTao = ngayTao;
        this.ngayCapNhat = ngayCapNhat;
    }

    public int getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(int maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public int getMaVaiTro() {
        return maVaiTro;
    }

    public void setMaVaiTro(int maVaiTro) {
        this.maVaiTro = maVaiTro;
    }

    public String getTrangThaiTaiKhoan() {
        return trangThaiTaiKhoan;
    }

    public void setTrangThaiTaiKhoan(String trangThaiTaiKhoan) {
        this.trangThaiTaiKhoan = trangThaiTaiKhoan;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public LocalDateTime getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(LocalDateTime ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }

    @Override
    public String toString() {
        return hoTen + " (" + tenDangNhap + ")";
    }
}