package com.football.management.ui.NhanVien;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DatSanRow {
    private final StringProperty maDatSan;
    private final StringProperty tenKhachHang;
    private final StringProperty tenSan;
    private final StringProperty thoiGian;
    private final StringProperty trangThai;

    public DatSanRow(String maDatSan, String tenKhachHang, String tenSan, String thoiGian, String trangThai) {
        this.maDatSan = new SimpleStringProperty(maDatSan);
        this.tenKhachHang = new SimpleStringProperty(tenKhachHang);
        this.tenSan = new SimpleStringProperty(tenSan);
        this.thoiGian = new SimpleStringProperty(thoiGian);
        this.trangThai = new SimpleStringProperty(trangThai);
    }

    public StringProperty maDatSanProperty() {
        return maDatSan;
    }

    public StringProperty tenKhachHangProperty() {
        return tenKhachHang;
    }

    public StringProperty tenSanProperty() {
        return tenSan;
    }

    public StringProperty thoiGianProperty() {
        return thoiGian;
    }

    public StringProperty trangThaiProperty() {
        return trangThai;
    }
}