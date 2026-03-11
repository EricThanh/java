package com.football.management;

import com.formdev.flatlaf.FlatLightLaf;
import com.football.management.ui.field.FieldManagementFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            FieldManagementFrame frame = new FieldManagementFrame();
            frame.setVisible(true);
        });
    }
}