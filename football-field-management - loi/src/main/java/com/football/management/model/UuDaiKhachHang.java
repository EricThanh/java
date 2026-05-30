package com.football.management.model;

public class UuDaiKhachHang {
    private int maUuDai;
    private int maKhachHang;

    public UuDaiKhachHang() {
    }

    public UuDaiKhachHang(int maUuDai, int maKhachHang) {
        this.maUuDai = maUuDai;
        this.maKhachHang = maKhachHang;
    }

    public int getMaUuDai() {
        return maUuDai;
    }

    public void setMaUuDai(int maUuDai) {
        this.maUuDai = maUuDai;
    }

    public int getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(int maKhachHang) {
        this.maKhachHang = maKhachHang;
    }
}