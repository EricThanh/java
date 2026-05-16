package com.football.management.ui.NhanVien;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class KhachHangRow {
    private final StringProperty maKhachHang;
    private final StringProperty hoTen;
    private final StringProperty soDienThoai;
    private final StringProperty email;
    private final StringProperty diaChi;
    private final StringProperty trangThai;

    public KhachHangRow(String maKhachHang,
                        String hoTen,
                        String soDienThoai,
                        String email,
                        String diaChi,
                        String trangThai) {
        this.maKhachHang = new SimpleStringProperty(maKhachHang);
        this.hoTen = new SimpleStringProperty(hoTen);
        this.soDienThoai = new SimpleStringProperty(soDienThoai);
        this.email = new SimpleStringProperty(email);
        this.diaChi = new SimpleStringProperty(diaChi);
        this.trangThai = new SimpleStringProperty(trangThai);
    }

    public KhachHangRow(String maKhachHang,
                        String hoTen,
                        String soDienThoai,
                        String email) {
        this(maKhachHang, hoTen, soDienThoai, email, "", "HOẠT ĐỘNG");
    }

    public StringProperty maKhachHangProperty() {
        return maKhachHang;
    }

    public StringProperty hoTenProperty() {
        return hoTen;
    }

    public StringProperty soDienThoaiProperty() {
        return soDienThoai;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty diaChiProperty() {
        return diaChi;
    }

    public StringProperty trangThaiProperty() {
        return trangThai;
    }
}