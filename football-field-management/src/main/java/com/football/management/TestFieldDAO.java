package com.football.management;

import com.football.management.dao.FieldDAO;
import com.football.management.model.Field;

import java.util.List;

public class TestFieldDAO {
    public static void main(String[] args) {
        FieldDAO fieldDAO = new FieldDAO();

        System.out.println("===== DANH SACH SAN BAN DAU =====");
        printFields(fieldDAO.getAllFields());

        System.out.println("\n===== THEM SAN MOI =====");
        Field newField = new Field("Sân 11A", "11", "Sân 11 người khu A", "ACTIVE");
        boolean insertResult = fieldDAO.insertField(newField);
        System.out.println("Them san: " + (insertResult ? "Thanh cong" : "That bai"));

        System.out.println("\n===== DANH SACH SAU KHI THEM =====");
        printFields(fieldDAO.getAllFields());

        System.out.println("\n===== SUA SAN CO ID = 1 =====");
        Field fieldToUpdate = fieldDAO.getFieldById(1);
        if (fieldToUpdate != null) {
            fieldToUpdate.setFieldName("Sân 5A - Đã cập nhật");
            fieldToUpdate.setDescription("Sân 5 người khu A - cập nhật mô tả");
            boolean updateResult = fieldDAO.updateField(fieldToUpdate);
            System.out.println("Cap nhat san: " + (updateResult ? "Thanh cong" : "That bai"));
        } else {
            System.out.println("Khong tim thay san ID = 1");
        }

        System.out.println("\n===== DANH SACH SAU KHI SUA =====");
        printFields(fieldDAO.getAllFields());

        System.out.println("\n===== XOA SAN CO ID = 4 =====");
        boolean deleteResult = fieldDAO.deleteField(4);
        System.out.println("Xoa san: " + (deleteResult ? "Thanh cong" : "That bai"));

        System.out.println("\n===== DANH SACH CUOI CUNG =====");
        printFields(fieldDAO.getAllFields());
    }

    private static void printFields(List<Field> fields) {
        for (Field field : fields) {
            System.out.println(field);
        }
    }
}
