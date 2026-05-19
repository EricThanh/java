package com.football.management.app;

public class AppState {

    private static String vaiTro;
    private static String tenNguoiDung;

    private static int maKhachHang;
    private static int maTaiKhoan;

    public static String getVaiTro() {
        return vaiTro;
    }

    public static void setVaiTro(String vaiTro) {
        AppState.vaiTro = vaiTro;
    }

    public static String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public static void setTenNguoiDung(String tenNguoiDung) {
        AppState.tenNguoiDung = tenNguoiDung;
    }

    public static int getMaKhachHang() {
        return maKhachHang;
    }

    public static void setMaKhachHang(int maKhachHang) {
        AppState.maKhachHang = maKhachHang;
    }

    public static int getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public static void setMaTaiKhoan(int maTaiKhoan) {
        AppState.maTaiKhoan = maTaiKhoan;
    }

    public static void clear() {
        vaiTro = null;
        tenNguoiDung = null;
        maKhachHang = 0;
        maTaiKhoan = 0;
    }
    
}