package com.football.management.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BangGia {
    private int maBangGia;
    private int maSan;
    private String tenBangGia;
    private Integer thuTrongTuan;
    private String gioBatDau;
    private String gioKetThuc;
    private BigDecimal giaMoiGio;
    private LocalDate ngayApDungTu;
    private LocalDate ngayApDungDen;
    private int dangApDung;
    private String ghiChu;

    public BangGia() {
    }

    public BangGia(int maBangGia, int maSan, String tenBangGia, Integer thuTrongTuan,
                   String gioBatDau, String gioKetThuc, BigDecimal giaMoiGio,
                   LocalDate ngayApDungTu, LocalDate ngayApDungDen, int dangApDung, String ghiChu) {
        this.maBangGia = maBangGia;
        this.maSan = maSan;
        this.tenBangGia = tenBangGia;
        this.thuTrongTuan = thuTrongTuan;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.giaMoiGio = giaMoiGio;
        this.ngayApDungTu = ngayApDungTu;
        this.ngayApDungDen = ngayApDungDen;
        this.dangApDung = dangApDung;
        this.ghiChu = ghiChu;
    }

    public int getMaBangGia() {
        return maBangGia;
    }

    public void setMaBangGia(int maBangGia) {
        this.maBangGia = maBangGia;
    }

    public int getMaSan() {
        return maSan;
    }

    public void setMaSan(int maSan) {
        this.maSan = maSan;
    }

    public String getTenBangGia() {
        return tenBangGia;
    }

    public void setTenBangGia(String tenBangGia) {
        this.tenBangGia = tenBangGia;
    }

    public Integer getThuTrongTuan() {
        return thuTrongTuan;
    }

    public void setThuTrongTuan(Integer thuTrongTuan) {
        this.thuTrongTuan = thuTrongTuan;
    }

    public String getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(String gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public String getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(String gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

    public BigDecimal getGiaMoiGio() {
        return giaMoiGio;
    }

    public void setGiaMoiGio(BigDecimal giaMoiGio) {
        this.giaMoiGio = giaMoiGio;
    }

    public LocalDate getNgayApDungTu() {
        return ngayApDungTu;
    }

    public void setNgayApDungTu(LocalDate ngayApDungTu) {
        this.ngayApDungTu = ngayApDungTu;
    }

    public LocalDate getNgayApDungDen() {
        return ngayApDungDen;
    }

    public void setNgayApDungDen(LocalDate ngayApDungDen) {
        this.ngayApDungDen = ngayApDungDen;
    }

    public int getDangApDung() {
        return dangApDung;
    }

    public void setDangApDung(int dangApDung) {
        this.dangApDung = dangApDung;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}