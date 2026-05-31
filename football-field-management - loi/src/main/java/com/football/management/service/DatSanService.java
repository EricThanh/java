package com.football.management.service;

import com.football.management.dao.DatSanDAO;

import java.time.LocalDate;

public class DatSanService {

    private final DatSanDAO datSanDAO = new DatSanDAO();

    public String kiemTraDuLieuDatSan(int maSan,
                                      int maKhachHang,
                                      LocalDate ngayDat,
                                      String gioBatDau,
                                      String gioKetThuc,
                                      Integer maDatSanBoQua) {

        if (maSan <= 0) {
            return "Vui lòng chọn sân.";
        }

        if (maKhachHang <= 0) {
            return "Vui lòng chọn khách hàng.";
        }

        if (ngayDat == null) {
            return "Vui lòng chọn ngày đặt sân.";
        }

        if (ngayDat.isBefore(LocalDate.now())) {
            return "Ngày đặt sân không được nhỏ hơn ngày hiện tại.";
        }

        if (gioBatDau == null || gioBatDau.isBlank()) {
            return "Vui lòng nhập giờ bắt đầu.";
        }

        if (gioKetThuc == null || gioKetThuc.isBlank()) {
            return "Vui lòng nhập giờ kết thúc.";
        }

        gioBatDau = gioBatDau.trim();
        gioKetThuc = gioKetThuc.trim();

        if (!gioBatDau.matches("\\d{2}:\\d{2}")) {
            return "Giờ bắt đầu phải có dạng HH:mm, ví dụ 08:00.";
        }

        if (!gioKetThuc.matches("\\d{2}:\\d{2}")) {
            return "Giờ kết thúc phải có dạng HH:mm, ví dụ 10:00.";
        }

        if (gioBatDau.compareTo(gioKetThuc) >= 0) {
            return "Giờ bắt đầu phải nhỏ hơn giờ kết thúc.";
        }

        boolean biTrungLich = datSanDAO.kiemTraTrungLich(
                maSan,
                ngayDat,
                gioBatDau,
                gioKetThuc,
                maDatSanBoQua
        );

        if (biTrungLich) {
            return "Khung giờ này đã có người đặt. Vui lòng chọn thời gian khác.";
        }

        return null;
    }
}