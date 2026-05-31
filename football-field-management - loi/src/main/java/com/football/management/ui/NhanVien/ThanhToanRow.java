package com.football.management.ui.NhanVien;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ThanhToanRow {
    private final StringProperty maDon;
    private final StringProperty tenKhachHang;
    private final StringProperty tenSan;
    private final StringProperty thoiGian;
    private final StringProperty trangThaiDon;
    private final StringProperty trangThaiThanhToan;
    private final StringProperty tongTien;

    public ThanhToanRow(String maDon,
                        String tenKhachHang,
                        String tenSan,
                        String thoiGian,
                        String trangThaiDon,
                        String trangThaiThanhToan,
                        String tongTien) {
        this.maDon = new SimpleStringProperty(maDon);
        this.tenKhachHang = new SimpleStringProperty(tenKhachHang);
        this.tenSan = new SimpleStringProperty(tenSan);
        this.thoiGian = new SimpleStringProperty(thoiGian);
        this.trangThaiDon = new SimpleStringProperty(trangThaiDon);
        this.trangThaiThanhToan = new SimpleStringProperty(trangThaiThanhToan);
        this.tongTien = new SimpleStringProperty(tongTien);
    }

    public StringProperty maDonProperty() {
        return maDon;
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

    public StringProperty trangThaiDonProperty() {
        return trangThaiDon;
    }

    public StringProperty trangThaiThanhToanProperty() {
        return trangThaiThanhToan;
    }

    public StringProperty tongTienProperty() {
        return tongTien;
    }
}