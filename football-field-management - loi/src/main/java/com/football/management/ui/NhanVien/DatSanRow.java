package com.football.management.ui.NhanVien;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DatSanRow {
    private final StringProperty maDon;
    private final StringProperty maKhachHang;
    private final StringProperty tenKhachHang;
    private final StringProperty tenSan;
    private final StringProperty thoiGian;
    private final StringProperty trangThai;

    public DatSanRow(
            String maDon,
            String maKhachHang,
            String tenKhachHang,
            String tenSan,
            String thoiGian,
            String trangThai
    ) {
        this.maDon = new SimpleStringProperty(maDon);
        this.maKhachHang = new SimpleStringProperty(maKhachHang);
        this.tenKhachHang = new SimpleStringProperty(tenKhachHang);
        this.tenSan = new SimpleStringProperty(tenSan);
        this.thoiGian = new SimpleStringProperty(thoiGian);
        this.trangThai = new SimpleStringProperty(trangThai);
    }

    public DatSanRow(
            String maDon,
            String tenKhachHang,
            String tenSan,
            String thoiGian,
            String trangThai
    ) {
        this(maDon, "", tenKhachHang, tenSan, thoiGian, trangThai);
    }

    public StringProperty maDonProperty() {
        return maDon;
    }

    public StringProperty maKhachHangProperty() {
        return maKhachHang;
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