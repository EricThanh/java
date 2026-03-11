package com.football.management.dao;

import com.football.management.config.DBConnection;
import com.football.management.model.Field;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FieldDAO {

    public List<Field> getAllFields() {
        List<Field> fields = new ArrayList<>();
        String sql = "SELECT FIELD_ID, FIELD_NAME, FIELD_TYPE, DESCRIPTION, STATUS FROM FIELDS ORDER BY FIELD_ID";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Field field = new Field();
                field.setFieldId(rs.getInt("FIELD_ID"));
                field.setFieldName(rs.getString("FIELD_NAME"));
                field.setFieldType(rs.getString("FIELD_TYPE"));
                field.setDescription(rs.getString("DESCRIPTION"));
                field.setStatus(rs.getString("STATUS"));

                fields.add(field);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fields;
    }

    public boolean insertField(Field field) {
        String sql = "INSERT INTO FIELDS (FIELD_NAME, FIELD_TYPE, DESCRIPTION, STATUS) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, field.getFieldName());
            ps.setString(2, field.getFieldType());
            ps.setString(3, field.getDescription());
            ps.setString(4, field.getStatus());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateField(Field field) {
        String sql = "UPDATE FIELDS SET FIELD_NAME = ?, FIELD_TYPE = ?, DESCRIPTION = ?, STATUS = ? WHERE FIELD_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, field.getFieldName());
            ps.setString(2, field.getFieldType());
            ps.setString(3, field.getDescription());
            ps.setString(4, field.getStatus());
            ps.setInt(5, field.getFieldId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteField(int fieldId) {
        String sql = "DELETE FROM FIELDS WHERE FIELD_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, fieldId);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public Field getFieldById(int fieldId) {
        String sql = "SELECT FIELD_ID, FIELD_NAME, FIELD_TYPE, DESCRIPTION, STATUS FROM FIELDS WHERE FIELD_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, fieldId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Field field = new Field();
                    field.setFieldId(rs.getInt("FIELD_ID"));
                    field.setFieldName(rs.getString("FIELD_NAME"));
                    field.setFieldType(rs.getString("FIELD_TYPE"));
                    field.setDescription(rs.getString("DESCRIPTION"));
                    field.setStatus(rs.getString("STATUS"));
                    return field;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
