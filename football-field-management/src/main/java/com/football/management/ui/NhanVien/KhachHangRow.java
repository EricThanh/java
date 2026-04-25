package com.football.management.ui.NhanVien;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class KhachHangRow {
    private final StringProperty maKhachHang;
    private final StringProperty hoTen;
    private final StringProperty soDienThoai;
    private final StringProperty email;

    public KhachHangRow(String maKhachHang, String hoTen, String soDienThoai, String email) {
        this.maKhachHang = new SimpleStringProperty(maKhachHang);
        this.hoTen = new SimpleStringProperty(hoTen);
        this.soDienThoai = new SimpleStringProperty(soDienThoai);
        this.email = new SimpleStringProperty(email);
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
}