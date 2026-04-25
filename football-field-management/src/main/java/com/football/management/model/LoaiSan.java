package com.football.management.model;

public class LoaiSan {
    private int maLoaiSan;
    private String tenLoaiSan;
    private String moTa;

    public LoaiSan() {
    }

    public LoaiSan(int maLoaiSan, String tenLoaiSan, String moTa) {
        this.maLoaiSan = maLoaiSan;
        this.tenLoaiSan = tenLoaiSan;
        this.moTa = moTa;
    }

    public int getMaLoaiSan() {
        return maLoaiSan;
    }

    public void setMaLoaiSan(int maLoaiSan) {
        this.maLoaiSan = maLoaiSan;
    }

    public String getTenLoaiSan() {
        return tenLoaiSan;
    }

    public void setTenLoaiSan(String tenLoaiSan) {
        this.tenLoaiSan = tenLoaiSan;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return tenLoaiSan;
    }
}