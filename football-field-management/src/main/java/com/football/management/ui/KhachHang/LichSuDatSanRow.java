package com.football.management.ui.KhachHang;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LichSuDatSanRow {
    private final StringProperty maDatSan;
    private final StringProperty tenSan;
    private final StringProperty ngayDat;
    private final StringProperty khungGio;
    private final StringProperty trangThai;

    public LichSuDatSanRow(String maDatSan, String tenSan, String ngayDat, String khungGio, String trangThai) {
        this.maDatSan = new SimpleStringProperty(maDatSan);
        this.tenSan = new SimpleStringProperty(tenSan);
        this.ngayDat = new SimpleStringProperty(ngayDat);
        this.khungGio = new SimpleStringProperty(khungGio);
        this.trangThai = new SimpleStringProperty(trangThai);
    }

    public StringProperty maDatSanProperty() {
        return maDatSan;
    }

    public StringProperty tenSanProperty() {
        return tenSan;
    }

    public StringProperty ngayDatProperty() {
        return ngayDat;
    }

    public StringProperty khungGioProperty() {
        return khungGio;
    }

    public StringProperty trangThaiProperty() {
        return trangThai;
    }
}