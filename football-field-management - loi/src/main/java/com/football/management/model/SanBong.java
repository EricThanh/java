package com.football.management.model;

import java.time.LocalDateTime;

public class SanBong {
    private int maSan;
    private String maSanCode;
    private String tenSan;
    private int maLoaiSan;
    private String viTri;
    private Integer sucChua;
    private String trangThaiSan;
    private String gioMoCua;
    private String gioDongCua;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;

    public SanBong() {
    }

    public SanBong(int maSan, String maSanCode, String tenSan, int maLoaiSan, String viTri,
                   Integer sucChua, String trangThaiSan, String gioMoCua,
                   String gioDongCua, LocalDateTime ngayTao, LocalDateTime ngayCapNhat) {
        this.maSan = maSan;
        this.maSanCode = maSanCode;
        this.tenSan = tenSan;
        this.maLoaiSan = maLoaiSan;
        this.viTri = viTri;
        this.sucChua = sucChua;
        this.trangThaiSan = trangThaiSan;
        this.gioMoCua = gioMoCua;
        this.gioDongCua = gioDongCua;
        this.ngayTao = ngayTao;
        this.ngayCapNhat = ngayCapNhat;
    }

    public int getMaSan() {
        return maSan;
    }

    public void setMaSan(int maSan) {
        this.maSan = maSan;
    }

    public String getMaSanCode() {
        return maSanCode;
    }

    public void setMaSanCode(String maSanCode) {
        this.maSanCode = maSanCode;
    }

    public String getTenSan() {
        return tenSan;
    }

    public void setTenSan(String tenSan) {
        this.tenSan = tenSan;
    }

    public int getMaLoaiSan() {
        return maLoaiSan;
    }

    public void setMaLoaiSan(int maLoaiSan) {
        this.maLoaiSan = maLoaiSan;
    }

    public String getViTri() {
        return viTri;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }

    public Integer getSucChua() {
        return sucChua;
    }

    public void setSucChua(Integer sucChua) {
        this.sucChua = sucChua;
    }

    public String getTrangThaiSan() {
        return trangThaiSan;
    }

    public void setTrangThaiSan(String trangThaiSan) {
        this.trangThaiSan = trangThaiSan;
    }

    public String getGioMoCua() {
        return gioMoCua;
    }

    public void setGioMoCua(String gioMoCua) {
        this.gioMoCua = gioMoCua;
    }

    public String getGioDongCua() {
        return gioDongCua;
    }

    public void setGioDongCua(String gioDongCua) {
        this.gioDongCua = gioDongCua;
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
        return tenSan;
    }
}