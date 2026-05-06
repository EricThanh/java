package com.football.management.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BaoCaoTongQuan {
    private BigDecimal doanhThu;
    private int luotDatSan;
    private int soKhachHang;
    private int tongSoSan;
    private int soSanDangSuDung;
    private double tanSuatSuDung;
    private LocalDate tuNgay;
    private LocalDate denNgay;

    public BaoCaoTongQuan() {
    }

    public BigDecimal getDoanhThu() {
        return doanhThu;
    }

    public void setDoanhThu(BigDecimal doanhThu) {
        this.doanhThu = doanhThu;
    }

    public int getLuotDatSan() {
        return luotDatSan;
    }

    public void setLuotDatSan(int luotDatSan) {
        this.luotDatSan = luotDatSan;
    }

    public int getSoKhachHang() {
        return soKhachHang;
    }

    public void setSoKhachHang(int soKhachHang) {
        this.soKhachHang = soKhachHang;
    }

    public int getTongSoSan() {
        return tongSoSan;
    }

    public void setTongSoSan(int tongSoSan) {
        this.tongSoSan = tongSoSan;
    }

    public int getSoSanDangSuDung() {
        return soSanDangSuDung;
    }

    public void setSoSanDangSuDung(int soSanDangSuDung) {
        this.soSanDangSuDung = soSanDangSuDung;
    }

    public double getTanSuatSuDung() {
        return tanSuatSuDung;
    }

    public void setTanSuatSuDung(double tanSuatSuDung) {
        this.tanSuatSuDung = tanSuatSuDung;
    }

    public LocalDate getTuNgay() {
        return tuNgay;
    }

    public void setTuNgay(LocalDate tuNgay) {
        this.tuNgay = tuNgay;
    }

    public LocalDate getDenNgay() {
        return denNgay;
    }

    public void setDenNgay(LocalDate denNgay) {
        this.denNgay = denNgay;
    }
}