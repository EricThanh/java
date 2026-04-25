package com.football.management.ui.ChuSan;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SanRow {
    private final StringProperty maSan;
    private final StringProperty tenSan;
    private final StringProperty loaiSan;
    private final StringProperty trangThai;

    public SanRow(String maSan, String tenSan, String loaiSan, String trangThai) {
        this.maSan = new SimpleStringProperty(maSan);
        this.tenSan = new SimpleStringProperty(tenSan);
        this.loaiSan = new SimpleStringProperty(loaiSan);
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

    public StringProperty trangThaiProperty() {
        return trangThai;
    }
}