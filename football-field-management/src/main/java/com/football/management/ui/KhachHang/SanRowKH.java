package com.football.management.ui.KhachHang;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SanRowKH {
    private final int maSan;
    private final StringProperty maSanCode;
    private final StringProperty tenSan;
    private final StringProperty tenLoaiSan;
    private final StringProperty viTri;
    private final StringProperty gioMoCua;
    private final StringProperty giaDienThi;
    private final StringProperty trangThai;

    public SanRowKH(int maSan, String maSanCode, String tenSan,
                    String tenLoaiSan, String viTri,
                    String gioMoCua, String giaDienThi, String trangThai) {
        this.maSan = maSan;
        this.maSanCode = new SimpleStringProperty(maSanCode);
        this.tenSan = new SimpleStringProperty(tenSan);
        this.tenLoaiSan = new SimpleStringProperty(tenLoaiSan);
        this.viTri = new SimpleStringProperty(viTri);
        this.gioMoCua = new SimpleStringProperty(gioMoCua);
        this.giaDienThi = new SimpleStringProperty(giaDienThi);
        this.trangThai = new SimpleStringProperty(trangThai);
    }

    public int getMaSan() { return maSan; }
    public StringProperty maSanCodeProperty() { return maSanCode; }
    public StringProperty tenSanProperty() { return tenSan; }
    public StringProperty tenLoaiSanProperty() { return tenLoaiSan; }
    public StringProperty viTriProperty() { return viTri; }
    public StringProperty gioMoCuaProperty() { return gioMoCua; }
    public StringProperty giaDienThiProperty() { return giaDienThi; }
    public StringProperty trangThaiProperty() { return trangThai; }
    public String getMaSanCodeValue() { return maSanCode.get(); }
    public String getTenSanValue() { return tenSan.get(); }
}