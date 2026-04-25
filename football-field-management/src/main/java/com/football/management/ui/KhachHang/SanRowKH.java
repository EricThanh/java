package com.football.management.ui.KhachHang;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SanRowKH {
    private final StringProperty maSan;
    private final StringProperty tenSan;
    private final StringProperty loaiSan;
    private final StringProperty giaMoiGio;
    private final StringProperty trangThai;

    public SanRowKH(String maSan, String tenSan, String loaiSan, String giaMoiGio, String trangThai) {
        this.maSan = new SimpleStringProperty(maSan);
        this.tenSan = new SimpleStringProperty(tenSan);
        this.loaiSan = new SimpleStringProperty(loaiSan);
        this.giaMoiGio = new SimpleStringProperty(giaMoiGio);
        this.trangThai = new SimpleStringProperty(trangThai);
    }

    public StringProperty maSanProperty() {
        return maSan;
    }

    public StringProperty tenSanProperty() {
        return tenSan;
    }

    public StringProperty loaiSanProperty() {
        return loaiSan;
    }

    public StringProperty giaMoiGioProperty() {
        return giaMoiGio;
    }

    public StringProperty trangThaiProperty() {
        return trangThai;
    }
}