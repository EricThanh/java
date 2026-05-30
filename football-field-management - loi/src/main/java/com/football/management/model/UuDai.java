package com.football.management.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UuDai {
    private int maUuDai;
    private String maGiamGia;
    private String tenUuDai;
    private String moTa;
    private String loaiGiamGia;
    private BigDecimal giaTriGiam;
    private BigDecimal giaTriDatToiThieu;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String gioBatDau;
    private String gioKetThuc;
    private int dangHoatDong;

    public UuDai() {
    }

    public UuDai(int maUuDai, String maGiamGia, String tenUuDai, String moTa,
                 String loaiGiamGia, BigDecimal giaTriGiam, BigDecimal giaTriDatToiThieu,
                 LocalDate ngayBatDau, LocalDate ngayKetThuc, String gioBatDau,
                 String gioKetThuc, int dangHoatDong) {
        this.maUuDai = maUuDai;
        this.maGiamGia = maGiamGia;
        this.tenUuDai = tenUuDai;
        this.moTa = moTa;
        this.loaiGiamGia = loaiGiamGia;
        this.giaTriGiam = giaTriGiam;
        this.giaTriDatToiThieu = giaTriDatToiThieu;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.dangHoatDong = dangHoatDong;
    }

    public int getMaUuDai() {
        return maUuDai;
    }

    public void setMaUuDai(int maUuDai) {
        this.maUuDai = maUuDai;
    }

    public String getMaGiamGia() {
        return maGiamGia;
    }

    public void setMaGiamGia(String maGiamGia) {
        this.maGiamGia = maGiamGia;
    }

    public String getTenUuDai() {
        return tenUuDai;
    }

    public void setTenUuDai(String tenUuDai) {
        this.tenUuDai = tenUuDai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getLoaiGiamGia() {
        return loaiGiamGia;
    }

    public void setLoaiGiamGia(String loaiGiamGia) {
        this.loaiGiamGia = loaiGiamGia;
    }

    public BigDecimal getGiaTriGiam() {
        return giaTriGiam;
    }

    public void setGiaTriGiam(BigDecimal giaTriGiam) {
        this.giaTriGiam = giaTriGiam;
    }

    public BigDecimal getGiaTriDatToiThieu() {
        return giaTriDatToiThieu;
    }

    public void setGiaTriDatToiThieu(BigDecimal giaTriDatToiThieu) {
        this.giaTriDatToiThieu = giaTriDatToiThieu;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
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

    public int getDangHoatDong() {
        return dangHoatDong;
    }

    public void setDangHoatDong(int dangHoatDong) {
        this.dangHoatDong = dangHoatDong;
    }
}