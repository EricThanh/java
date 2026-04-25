package com.football.management.ui.ChuSan;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NhanVienRow {
    private final StringProperty maNhanVien;
    private final StringProperty hoTen;
    private final StringProperty soDienThoai;
    private final StringProperty vaiTro;

    public NhanVienRow(String maNhanVien, String hoTen, String soDienThoai, String vaiTro) {
        this.maNhanVien = new SimpleStringProperty(maNhanVien);
        this.hoTen = new SimpleStringProperty(hoTen);
        this.soDienThoai = new SimpleStringProperty(soDienThoai);
        this.vaiTro = new SimpleStringProperty(vaiTro);
    }

    public StringProperty maNhanVienProperty() {
        return maNhanVien;
    }

    public StringProperty hoTenProperty() {
        return hoTen;
    }

    public StringProperty soDienThoaiProperty() {
        return soDienThoai;
    }

    public StringProperty vaiTroProperty() {
        return vaiTro;
    }
}