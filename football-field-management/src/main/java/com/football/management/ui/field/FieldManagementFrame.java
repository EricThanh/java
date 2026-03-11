package com.football.management.ui.field;
import com.football.management.dao.FieldDAO;
import com.football.management.model.Field;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FieldManagementFrame extends JFrame {

    private JTextField txtFieldId;
    private JTextField txtFieldName;
    private JComboBox<String> cbFieldType;
    private JTextField txtDescription;
    private JComboBox<String> cbStatus;

    private JTable tblFields;
    private DefaultTableModel tableModel;

    private final FieldDAO fieldDAO = new FieldDAO();

    public FieldManagementFrame() {
        setTitle("Quản lý sân bóng");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        loadFieldData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin sân"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblFieldId = new JLabel("Mã sân:");
        JLabel lblFieldName = new JLabel("Tên sân:");
        JLabel lblFieldType = new JLabel("Loại sân:");
        JLabel lblDescription = new JLabel("Mô tả:");
        JLabel lblStatus = new JLabel("Trạng thái:");

        txtFieldId = new JTextField(20);
        txtFieldId.setEditable(false);

        txtFieldName = new JTextField(20);
        cbFieldType = new JComboBox<>(new String[]{"5", "7", "11"});
        txtDescription = new JTextField(20);
        cbStatus = new JComboBox<>(new String[]{"ACTIVE", "MAINTENANCE"});

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblFieldId, gbc);
        gbc.gridx = 1;
        formPanel.add(txtFieldId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblFieldName, gbc);
        gbc.gridx = 1;
        formPanel.add(txtFieldName, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lblFieldType, gbc);
        gbc.gridx = 1;
        formPanel.add(cbFieldType, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(lblDescription, gbc);
        gbc.gridx = 1;
        formPanel.add(txtDescription, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(lblStatus, gbc);
        gbc.gridx = 1;
        formPanel.add(cbStatus, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnClear = new JButton("Làm mới");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(formPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{"Mã sân", "Tên sân", "Loại sân", "Mô tả", "Trạng thái"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblFields = new JTable(tableModel);
        tblFields.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tblFields);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách sân"));
        add(scrollPane, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> addField());
        btnUpdate.addActionListener(e -> updateField());
        btnDelete.addActionListener(e -> deleteField());
        btnClear.addActionListener(e -> clearForm());

        tblFields.getSelectionModel().addListSelectionListener(this::fillFormFromTable);
    }

    private void loadFieldData() {
        tableModel.setRowCount(0);
        List<Field> fields = fieldDAO.getAllFields();

        for (Field field : fields) {
            tableModel.addRow(new Object[]{
                    field.getFieldId(),
                    field.getFieldName(),
                    field.getFieldType(),
                    field.getDescription(),
                    field.getStatus()
            });
        }
    }

    private void addField() {
        String fieldName = txtFieldName.getText().trim();
        String fieldType = cbFieldType.getSelectedItem().toString();
        String description = txtDescription.getText().trim();
        String status = cbStatus.getSelectedItem().toString();

        if (fieldName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên sân không được để trống.");
            return;
        }

        Field field = new Field(fieldName, fieldType, description, status);
        boolean result = fieldDAO.insertField(field);

        if (result) {
            JOptionPane.showMessageDialog(this, "Thêm sân thành công.");
            loadFieldData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm sân thất bại.");
        }
    }

    private void updateField() {
        if (txtFieldId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sân cần sửa.");
            return;
        }

        int fieldId = Integer.parseInt(txtFieldId.getText().trim());
        String fieldName = txtFieldName.getText().trim();
        String fieldType = cbFieldType.getSelectedItem().toString();
        String description = txtDescription.getText().trim();
        String status = cbStatus.getSelectedItem().toString();

        if (fieldName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên sân không được để trống.");
            return;
        }

        Field field = new Field(fieldId, fieldName, fieldType, description, status);
        boolean result = fieldDAO.updateField(field);

        if (result) {
            JOptionPane.showMessageDialog(this, "Cập nhật sân thành công.");
            loadFieldData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật sân thất bại.");
        }
    }

    private void deleteField() {
        if (txtFieldId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sân cần xóa.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa sân này không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int fieldId = Integer.parseInt(txtFieldId.getText().trim());
        boolean result = fieldDAO.deleteField(fieldId);

        if (result) {
            JOptionPane.showMessageDialog(this, "Xóa sân thành công.");
            loadFieldData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa sân thất bại.");
        }
    }

    private void clearForm() {
        txtFieldId.setText("");
        txtFieldName.setText("");
        cbFieldType.setSelectedIndex(0);
        txtDescription.setText("");
        cbStatus.setSelectedIndex(0);
        tblFields.clearSelection();
    }

    private void fillFormFromTable(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) {
            return;
        }

        int selectedRow = tblFields.getSelectedRow();
        if (selectedRow >= 0) {
            txtFieldId.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtFieldName.setText(tableModel.getValueAt(selectedRow, 1).toString());
            cbFieldType.setSelectedItem(tableModel.getValueAt(selectedRow, 2).toString());
            txtDescription.setText(tableModel.getValueAt(selectedRow, 3).toString());
            cbStatus.setSelectedItem(tableModel.getValueAt(selectedRow, 4).toString());
        }
    }
}
