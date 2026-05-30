package com.football.management.ui.NhanVien;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LichSanRow {
    private final StringProperty maDon;
    private final StringProperty tenSan;
    private final StringProperty tenKhachHang;
    private final StringProperty ngayDat;
    private final StringProperty gioBatDau;
    private final StringProperty gioKetThuc;
    private final StringProperty trangThai;

    public LichSanRow(String maDon,
                      String tenSan,
                      String tenKhachHang,
                      String ngayDat,
                      String gioBatDau,
                      String gioKetThuc,
                      String trangThai) {
        this.maDon = new SimpleStringProperty(maDon);
        this.tenSan = new SimpleStringProperty(tenSan);
        this.tenKhachHang = new SimpleStringProperty(tenKhachHang);
        this.ngayDat = new SimpleStringProperty(ngayDat);
        this.gioBatDau = new SimpleStringProperty(gioBatDau);
        this.gioKetThuc = new SimpleStringProperty(gioKetThuc);
        this.trangThai = new SimpleStringProperty(trangThai);
    }

    public StringProperty maDonProperty() {
        return maDon;
    }

    public StringProperty tenSanProperty() {
        return tenSan;
    }

    public StringProperty tenKhachHangProperty() {
        return tenKhachHang;
    }

    public StringProperty ngayDatProperty() {
        return ngayDat;
    }

    public StringProperty gioBatDauProperty() {
        return gioBatDau;
    }

    public StringProperty gioKetThucProperty() {
        return gioKetThuc;
    }

    public StringProperty trangThaiProperty() {
        return trangThai;
    }
}