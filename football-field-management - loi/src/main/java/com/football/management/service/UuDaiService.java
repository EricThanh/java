package com.football.management.service;

import com.football.management.dao.UuDaiDAO;
import com.football.management.model.UuDai;

import java.util.List;

public class UuDaiService {
    private final UuDaiDAO uuDaiDAO = new UuDaiDAO();

    public List<UuDai> layTatCaUuDai() {
        return uuDaiDAO.layTatCaUuDai();
    }

    public List<UuDai> timKiemUuDai(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            return uuDaiDAO.layTatCaUuDai();
        }
        return uuDaiDAO.timKiemUuDai(tuKhoa.trim());
    }

    public boolean themUuDai(UuDai uuDai) {
        validateUuDai(uuDai);
        return uuDaiDAO.themUuDai(uuDai);
    }

    public boolean capNhatUuDai(UuDai uuDai) {
        validateUuDai(uuDai);
        return uuDaiDAO.capNhatUuDai(uuDai);
    }

    public boolean xoaUuDai(int maUuDai) {
        return uuDaiDAO.xoaUuDai(maUuDai);
    }

    private void validateUuDai(UuDai uuDai) {
        if (uuDai.getMaGiamGia() == null || uuDai.getMaGiamGia().isBlank()) {
            throw new IllegalArgumentException("Ma giam gia khong duoc de trong");
        }
        if (uuDai.getTenUuDai() == null || uuDai.getTenUuDai().isBlank()) {
            throw new IllegalArgumentException("Ten uu dai khong duoc de trong");
        }
        if (uuDai.getLoaiGiamGia() == null || uuDai.getLoaiGiamGia().isBlank()) {
            throw new IllegalArgumentException("Loai giam gia khong duoc de trong");
        }
        if (uuDai.getGiaTriGiam() == null) {
            throw new IllegalArgumentException("Gia tri giam khong duoc de trong");
        }
        if (uuDai.getNgayBatDau() == null) {
            throw new IllegalArgumentException("Ngay bat dau khong duoc de trong");
        }
        if (uuDai.getNgayKetThuc() == null) {
            throw new IllegalArgumentException("Ngay ket thuc khong duoc de trong");
        }
    }
}