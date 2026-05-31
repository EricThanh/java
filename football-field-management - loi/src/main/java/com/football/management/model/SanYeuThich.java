package com.football.management.model;

import java.time.LocalDateTime;

public class SanYeuThich {
    private int maKhachHang;
    private int maSan;
    private LocalDateTime ngayTao;

    public SanYeuThich() {
    }

    public SanYeuThich(int maKhachHang, int maSan, LocalDateTime ngayTao) {
        this.maKhachHang = maKhachHang;
        this.maSan = maSan;
        this.ngayTao = ngayTao;
    }

    public int getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(int maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public int getMaSan() {
        return maSan;
    }

    public void setMaSan(int maSan) {
        this.maSan = maSan;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }
}