package com.football.management;

import com.football.management.config.DBConnection;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try (Connection connection = DBConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Ket noi Oracle thanh cong!");
                System.out.println("DB Name: " + DBConnection.getDbName());
                System.out.println("URL: " + DBConnection.getUrl());
                System.out.println("Username: " + DBConnection.getUsername());
                System.out.println("Database: " + connection.getMetaData().getDatabaseProductName());
                System.out.println("Version: " + connection.getMetaData().getDatabaseProductVersion());
            }
        } catch (Exception e) {
            System.out.println("Ket noi that bai!");
            e.printStackTrace();
        }
    }
}