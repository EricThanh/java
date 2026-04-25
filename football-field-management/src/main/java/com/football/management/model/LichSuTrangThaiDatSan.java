package com.football.management.model;

import java.time.LocalDateTime;

public class LichSuTrangThaiDatSan {
    private int maLichSu;
    private int maDatSan;
    private String trangThaiCu;
    private String trangThaiMoi;
    private Integer maTaiKhoanThayDoi;
    private LocalDateTime thoiGianThayDoi;
    private String ghiChu;

    public LichSuTrangThaiDatSan() {
    }

    public LichSuTrangThaiDatSan(int maLichSu, int maDatSan, String trangThaiCu,
                                 String trangThaiMoi, Integer maTaiKhoanThayDoi,
                                 LocalDateTime thoiGianThayDoi, String ghiChu) {
        this.maLichSu = maLichSu;
        this.maDatSan = maDatSan;
        this.trangThaiCu = trangThaiCu;
        this.trangThaiMoi = trangThaiMoi;
        this.maTaiKhoanThayDoi = maTaiKhoanThayDoi;
        this.thoiGianThayDoi = thoiGianThayDoi;
        this.ghiChu = ghiChu;
    }

    public int getMaLichSu() {
        return maLichSu;
    }

    public void setMaLichSu(int maLichSu) {
        this.maLichSu = maLichSu;
    }

    public int getMaDatSan() {
        return maDatSan;
    }

    public void setMaDatSan(int maDatSan) {
        this.maDatSan = maDatSan;
    }

    public String getTrangThaiCu() {
        return trangThaiCu;
    }

    public void setTrangThaiCu(String trangThaiCu) {
        this.trangThaiCu = trangThaiCu;
    }

    public String getTrangThaiMoi() {
        return trangThaiMoi;
    }

    public void setTrangThaiMoi(String trangThaiMoi) {
        this.trangThaiMoi = trangThaiMoi;
    }

    public Integer getMaTaiKhoanThayDoi() {
        return maTaiKhoanThayDoi;
    }

    public void setMaTaiKhoanThayDoi(Integer maTaiKhoanThayDoi) {
        this.maTaiKhoanThayDoi = maTaiKhoanThayDoi;
    }

    public LocalDateTime getThoiGianThayDoi() {
        return thoiGianThayDoi;
    }

    public void setThoiGianThayDoi(LocalDateTime thoiGianThayDoi) {
        this.thoiGianThayDoi = thoiGianThayDoi;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}