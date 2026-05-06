package com.football.management.dao;

import com.football.management.config.DBConnection;
import com.football.management.model.LoaiSan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LoaiSanDAO {

    public List<LoaiSan> layTatCaLoaiSan() {
        List<LoaiSan> danhSach = new ArrayList<>();

        String sql = """
                SELECT ma_loai_san, ten_loai_san, mo_ta
                FROM loai_san
                ORDER BY ma_loai_san
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LoaiSan loaiSan = new LoaiSan();
                loaiSan.setMaLoaiSan(rs.getInt("ma_loai_san"));
                loaiSan.setTenLoaiSan(rs.getString("ten_loai_san"));
                loaiSan.setMoTa(rs.getString("mo_ta"));
                danhSach.add(loaiSan);
            }

        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay danh sach loai san", e);
        }

        return danhSach;
    }
}