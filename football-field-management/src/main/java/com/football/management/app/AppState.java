package com.football.management.app;

public class AppState {
    private static String vaiTro;
    private static String tenNguoiDung;

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

    public static void clear() {
        vaiTro = null;
        tenNguoiDung = null;
    }
}