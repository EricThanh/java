package com.football.management.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HoaDon {
    private int maHoaDon;
    private String maHoaDonCode;
    private int maThanhToan;
    private LocalDateTime ngayXuat;
    private BigDecimal tongTien;
    private String ghiChu;

    public HoaDon() {
    }

    public HoaDon(int maHoaDon, String maHoaDonCode, int maThanhToan,
                  LocalDateTime ngayXuat, BigDecimal tongTien, String ghiChu) {
        this.maHoaDon = maHoaDon;
        this.maHoaDonCode = maHoaDonCode;
        this.maThanhToan = maThanhToan;
        this.ngayXuat = ngayXuat;
        this.tongTien = tongTien;
        this.ghiChu = ghiChu;
    }

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public String getMaHoaDonCode() {
        return maHoaDonCode;
    }

    public void setMaHoaDonCode(String maHoaDonCode) {
        this.maHoaDonCode = maHoaDonCode;
    }

    public int getMaThanhToan() {
        return maThanhToan;
    }

    public void setMaThanhToan(int maThanhToan) {
        this.maThanhToan = maThanhToan;
    }

    public LocalDateTime getNgayXuat() {
        return ngayXuat;
    }

    public void setNgayXuat(LocalDateTime ngayXuat) {
        this.ngayXuat = ngayXuat;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}