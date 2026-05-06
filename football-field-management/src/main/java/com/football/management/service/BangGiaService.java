package com.football.management.service;

import com.football.management.dao.BangGiaDAO;
import com.football.management.model.BangGia;

import java.util.List;

public class BangGiaService {
    private final BangGiaDAO bangGiaDAO = new BangGiaDAO();

    public List<BangGia> layTatCaBangGia() {
        return bangGiaDAO.layTatCaBangGia();
    }

    public List<BangGia> timKiemBangGia(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            return bangGiaDAO.layTatCaBangGia();
        }
        return bangGiaDAO.timKiemBangGia(tuKhoa.trim());
    }

    public boolean themBangGia(BangGia bangGia) {
        validateBangGia(bangGia);
        return bangGiaDAO.themBangGia(bangGia);
    }

    public boolean capNhatBangGia(BangGia bangGia) {
        validateBangGia(bangGia);
        return bangGiaDAO.capNhatBangGia(bangGia);
    }

    public boolean xoaBangGia(int maBangGia) {
        return bangGiaDAO.xoaBangGia(maBangGia);
    }

    private void validateBangGia(BangGia bangGia) {
        if (bangGia.getMaSan() <= 0) {
            throw new IllegalArgumentException("San khong hop le");
        }
        if (bangGia.getTenBangGia() == null || bangGia.getTenBangGia().isBlank()) {
            throw new IllegalArgumentException("Ten bang gia khong duoc de trong");
        }
        if (bangGia.getGioBatDau() == null || bangGia.getGioBatDau().isBlank()) {
            throw new IllegalArgumentException("Gio bat dau khong duoc de trong");
        }
        if (bangGia.getGioKetThuc() == null || bangGia.getGioKetThuc().isBlank()) {
            throw new IllegalArgumentException("Gio ket thuc khong duoc de trong");
        }
        if (bangGia.getGiaMoiGio() == null) {
            throw new IllegalArgumentException("Gia moi gio khong duoc de trong");
        }
        if (bangGia.getNgayApDungTu() == null) {
            throw new IllegalArgumentException("Ngay ap dung tu khong duoc de trong");
        }
    }
}