package com.football.management.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DatSan {
    private int maDatSan;
    private String maDatSanCode;
    private int maKhachHang;
    private int maSan;
    private Integer maNhanVienTao;
    private LocalDate ngayDat;
    private String gioBatDau;
    private String gioKetThuc;
    private String trangThaiDatSan;
    private String ghiChu;
    private BigDecimal tongTienGoc;
    private BigDecimal tienGiam;
    private BigDecimal tongTienThanhToan;
    private Integer maUuDai;
    private String trangThaiThanhToan;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
    private LocalDateTime thoiGianCheckIn;

    public DatSan() {
    }

    public DatSan(int maDatSan, String maDatSanCode, int maKhachHang, int maSan,
                  Integer maNhanVienTao, LocalDate ngayDat, String gioBatDau,
                  String gioKetThuc, String trangThaiDatSan, String ghiChu,
                  BigDecimal tongTienGoc, BigDecimal tienGiam, BigDecimal tongTienThanhToan,
                  Integer maUuDai, String trangThaiThanhToan, LocalDateTime ngayTao,
                  LocalDateTime ngayCapNhat, LocalDateTime thoiGianCheckIn) {
        this.maDatSan = maDatSan;
        this.maDatSanCode = maDatSanCode;
        this.maKhachHang = maKhachHang;
        this.maSan = maSan;
        this.maNhanVienTao = maNhanVienTao;
        this.ngayDat = ngayDat;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.trangThaiDatSan = trangThaiDatSan;
        this.ghiChu = ghiChu;
        this.tongTienGoc = tongTienGoc;
        this.tienGiam = tienGiam;
        this.tongTienThanhToan = tongTienThanhToan;
        this.maUuDai = maUuDai;
        this.trangThaiThanhToan = trangThaiThanhToan;
        this.ngayTao = ngayTao;
        this.ngayCapNhat = ngayCapNhat;
        this.thoiGianCheckIn = thoiGianCheckIn;
    }

    public int getMaDatSan() {
        return maDatSan;
    }

    public void setMaDatSan(int maDatSan) {
        this.maDatSan = maDatSan;
    }

    public String getMaDatSanCode() {
        return maDatSanCode;
    }

    public void setMaDatSanCode(String maDatSanCode) {
        this.maDatSanCode = maDatSanCode;
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

    public Integer getMaNhanVienTao() {
        return maNhanVienTao;
    }

    public void setMaNhanVienTao(Integer maNhanVienTao) {
        this.maNhanVienTao = maNhanVienTao;
    }

    public LocalDate getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(LocalDate ngayDat) {
        this.ngayDat = ngayDat;
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

    public String getTrangThaiDatSan() {
        return trangThaiDatSan;
    }

    public void setTrangThaiDatSan(String trangThaiDatSan) {
        this.trangThaiDatSan = trangThaiDatSan;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public BigDecimal getTongTienGoc() {
        return tongTienGoc;
    }

    public void setTongTienGoc(BigDecimal tongTienGoc) {
        this.tongTienGoc = tongTienGoc;
    }

    public BigDecimal getTienGiam() {
        return tienGiam;
    }

    public void setTienGiam(BigDecimal tienGiam) {
        this.tienGiam = tienGiam;
    }

    public BigDecimal getTongTienThanhToan() {
        return tongTienThanhToan;
    }

    public void setTongTienThanhToan(BigDecimal tongTienThanhToan) {
        this.tongTienThanhToan = tongTienThanhToan;
    }

    public Integer getMaUuDai() {
        return maUuDai;
    }

    public void setMaUuDai(Integer maUuDai) {
        this.maUuDai = maUuDai;
    }

    public String getTrangThaiThanhToan() {
        return trangThaiThanhToan;
    }

    public void setTrangThaiThanhToan(String trangThaiThanhToan) {
        this.trangThaiThanhToan = trangThaiThanhToan;
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

    public LocalDateTime getThoiGianCheckIn() {
        return thoiGianCheckIn;
    }

    public void setThoiGianCheckIn(LocalDateTime thoiGianCheckIn) {
        this.thoiGianCheckIn = thoiGianCheckIn;
    }
}