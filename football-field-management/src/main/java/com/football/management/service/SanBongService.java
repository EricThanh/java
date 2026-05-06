package com.football.management.service;

import com.football.management.dao.SanBongDAO;
import com.football.management.model.SanBong;

import java.util.List;

public class SanBongService {
    private final SanBongDAO sanBongDAO = new SanBongDAO();

    public List<SanBong> layTatCaSan() {
        return sanBongDAO.layTatCaSan();
    }

    public List<SanBong> timKiemSan(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            return sanBongDAO.layTatCaSan();
        }
        return sanBongDAO.timKiemSan(tuKhoa.trim());
    }

    public boolean themSan(SanBong sanBong) {
        validateSan(sanBong);
        return sanBongDAO.themSan(sanBong);
    }

    public boolean capNhatSan(SanBong sanBong) {
        validateSan(sanBong);
        return sanBongDAO.capNhatSan(sanBong);
    }

    public boolean xoaSan(int maSan) {
        return sanBongDAO.xoaSan(maSan);
    }

    private void validateSan(SanBong sanBong) {
        if (sanBong.getMaSanCode() == null || sanBong.getMaSanCode().isBlank()) {
            throw new IllegalArgumentException("Ma san khong duoc de trong");
        }
        if (sanBong.getTenSan() == null || sanBong.getTenSan().isBlank()) {
            throw new IllegalArgumentException("Ten san khong duoc de trong");
        }
        if (sanBong.getMaLoaiSan() <= 0) {
            throw new IllegalArgumentException("Loai san khong hop le");
        }
        if (sanBong.getTrangThaiSan() == null || sanBong.getTrangThaiSan().isBlank()) {
            throw new IllegalArgumentException("Trang thai san khong duoc de trong");
        }
    }
}