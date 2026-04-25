package com.football.management.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ThanhToan {
    private int maThanhToan;
    private int maDatSan;
    private String hinhThucThanhToan;
    private String trangThaiThanhToan;
    private BigDecimal soTienThanhToan;
    private LocalDateTime thoiGianThanhToan;
    private String maGiaoDich;
    private Integer maNhanVienXacNhan;
    private String ghiChu;

    public ThanhToan() {
    }

    public ThanhToan(int maThanhToan, int maDatSan, String hinhThucThanhToan,
                     String trangThaiThanhToan, BigDecimal soTienThanhToan,
                     LocalDateTime thoiGianThanhToan, String maGiaoDich,
                     Integer maNhanVienXacNhan, String ghiChu) {
        this.maThanhToan = maThanhToan;
        this.maDatSan = maDatSan;
        this.hinhThucThanhToan = hinhThucThanhToan;
        this.trangThaiThanhToan = trangThaiThanhToan;
        this.soTienThanhToan = soTienThanhToan;
        this.thoiGianThanhToan = thoiGianThanhToan;
        this.maGiaoDich = maGiaoDich;
        this.maNhanVienXacNhan = maNhanVienXacNhan;
        this.ghiChu = ghiChu;
    }

    public int getMaThanhToan() {
        return maThanhToan;
    }

    public void setMaThanhToan(int maThanhToan) {
        this.maThanhToan = maThanhToan;
    }

    public int getMaDatSan() {
        return maDatSan;
    }

    public void setMaDatSan(int maDatSan) {
        this.maDatSan = maDatSan;
    }

    public String getHinhThucThanhToan() {
        return hinhThucThanhToan;
    }

    public void setHinhThucThanhToan(String hinhThucThanhToan) {
        this.hinhThucThanhToan = hinhThucThanhToan;
    }

    public String getTrangThaiThanhToan() {
        return trangThaiThanhToan;
    }

    public void setTrangThaiThanhToan(String trangThaiThanhToan) {
        this.trangThaiThanhToan = trangThaiThanhToan;
    }

    public BigDecimal getSoTienThanhToan() {
        return soTienThanhToan;
    }

    public void setSoTienThanhToan(BigDecimal soTienThanhToan) {
        this.soTienThanhToan = soTienThanhToan;
    }

    public LocalDateTime getThoiGianThanhToan() {
        return thoiGianThanhToan;
    }

    public void setThoiGianThanhToan(LocalDateTime thoiGianThanhToan) {
        this.thoiGianThanhToan = thoiGianThanhToan;
    }

    public String getMaGiaoDich() {
        return maGiaoDich;
    }

    public void setMaGiaoDich(String maGiaoDich) {
        this.maGiaoDich = maGiaoDich;
    }

    public Integer getMaNhanVienXacNhan() {
        return maNhanVienXacNhan;
    }

    public void setMaNhanVienXacNhan(Integer maNhanVienXacNhan) {
        this.maNhanVienXacNhan = maNhanVienXacNhan;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}