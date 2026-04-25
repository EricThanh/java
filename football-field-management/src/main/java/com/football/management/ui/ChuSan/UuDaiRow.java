package com.football.management.ui.ChuSan;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UuDaiRow {
    private final StringProperty maGiamGia;
    private final StringProperty tenUuDai;
    private final StringProperty loaiGiam;
    private final StringProperty giaTriGiam;
    private final StringProperty thoiGianApDung;

    public UuDaiRow(String maGiamGia, String tenUuDai, String loaiGiam, String giaTriGiam, String thoiGianApDung) {
        this.maGiamGia = new SimpleStringProperty(maGiamGia);
        this.tenUuDai = new SimpleStringProperty(tenUuDai);
        this.loaiGiam = new SimpleStringProperty(loaiGiam);
        this.giaTriGiam = new SimpleStringProperty(giaTriGiam);
        this.thoiGianApDung = new SimpleStringProperty(thoiGianApDung);
    }

    public StringProperty maGiamGiaProperty() {
        return maGiamGia;
    }

    public StringProperty tenUuDaiProperty() {
        return tenUuDai;
    }

    public StringProperty loaiGiamProperty() {
        return loaiGiam;
    }

    public StringProperty giaTriGiamProperty() {
        return giaTriGiam;
    }

    public StringProperty thoiGianApDungProperty() {
        return thoiGianApDung;
    }
}