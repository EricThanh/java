package com.football.management.ui.ChuSan;

import com.football.management.model.NhanVien;
import com.football.management.service.NhanVienService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class QuanLyNhanVienPage {

    public static Node createView() {
        NhanVienService nhanVienService = new NhanVienService();

        VBox root = new VBox(20);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        Label lblTitle = new Label("Quản lý nhân viên");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Thêm mới, cập nhật và phân quyền nhân viên");
        lblSubtitle.getStyleClass().add("section-subtitle");

        VBox formCard = new VBox(18);
        formCard.getStyleClass().add("form-card");

        Label lblFormTitle = new Label("Thông tin nhân viên");
        lblFormTitle.getStyleClass().add("section-title");

        GridPane form = new GridPane();
        form.getStyleClass().add("form-grid");

        TextField txtMaNhanVien = new TextField();
        txtMaNhanVien.setPromptText("Nhập mã nhân viên");

        TextField txtHoTen = new TextField();
        txtHoTen.setPromptText("Nhập họ tên");

        TextField txtSoDienThoai = new TextField();
        txtSoDienThoai.setPromptText("Nhập số điện thoại");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Nhập email");

        TextField txtTenDangNhap = new TextField();
        txtTenDangNhap.setPromptText("Nhập tên đăng nhập");

        PasswordField txtMatKhau = new PasswordField();
        txtMatKhau.setPromptText("Nhập mật khẩu");

        ComboBox<String> cbVaiTro = new ComboBox<>();
        cbVaiTro.getItems().addAll("NHAN_VIEN", "QUAN_LY_CA");
        cbVaiTro.setPromptText("Chọn vai trò");

        ComboBox<String> cbTrangThaiNhanVien = new ComboBox<>();
        cbTrangThaiNhanVien.getItems().addAll("DANG_LAM_VIEC", "DA_NGHI");
        cbTrangThaiNhanVien.setPromptText("Chọn trạng thái nhân viên");

        ComboBox<String> cbTrangThaiTaiKhoan = new ComboBox<>();
        cbTrangThaiTaiKhoan.getItems().addAll("HOAT_DONG", "BI_KHOA", "NGUNG_HOAT_DONG");
        cbTrangThaiTaiKhoan.setPromptText("Chọn trạng thái tài khoản");

        TextField txtGhiChu = new TextField();
        txtGhiChu.setPromptText("Nhập ghi chú");

        form.add(new Label("Mã nhân viên"), 0, 0);
        form.add(txtMaNhanVien, 1, 0);

        form.add(new Label("Họ tên"), 0, 1);
        form.add(txtHoTen, 1, 1);

        form.add(new Label("Số điện thoại"), 0, 2);
        form.add(txtSoDienThoai, 1, 2);

        form.add(new Label("Email"), 0, 3);
        form.add(txtEmail, 1, 3);

        form.add(new Label("Tên đăng nhập"), 0, 4);
        form.add(txtTenDangNhap, 1, 4);

        form.add(new Label("Mật khẩu"), 0, 5);
        form.add(txtMatKhau, 1, 5);

        form.add(new Label("Vai trò"), 0, 6);
        form.add(cbVaiTro, 1, 6);

        form.add(new Label("Trạng thái nhân viên"), 0, 7);
        form.add(cbTrangThaiNhanVien, 1, 7);

        form.add(new Label("Trạng thái tài khoản"), 0, 8);
        form.add(cbTrangThaiTaiKhoan, 1, 8);

        form.add(new Label("Ghi chú"), 0, 9);
        form.add(txtGhiChu, 1, 9);

        HBox actionBox = new HBox(10);

        Button btnThem = new Button("Thêm nhân viên");
        btnThem.getStyleClass().add("primary-button");

        Button btnCapNhat = new Button("Cập nhật");
        btnCapNhat.getStyleClass().add("light-button");

        Button btnLamMoi = new Button("Làm mới");
        btnLamMoi.getStyleClass().add("secondary-button");

        actionBox.getChildren().addAll(btnThem, btnCapNhat, btnLamMoi);

        formCard.getChildren().addAll(lblFormTitle, form, actionBox);

        VBox tableCard = new VBox(16);
        tableCard.getStyleClass().add("card");

        Label lblTableTitle = new Label("Danh sách nhân viên");
        lblTableTitle.getStyleClass().add("section-title");

        HBox filterBar = new HBox(10);
        filterBar.getStyleClass().add("filter-bar");

        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tìm kiếm nhân viên...");
        txtTimKiem.getStyleClass().add("search-field");
        txtTimKiem.setPrefWidth(320);

        Button btnTim = new Button("Tìm");
        btnTim.getStyleClass().add("light-button");

        Button btnXoa = new Button("Xóa");
        btnXoa.getStyleClass().add("secondary-button");

        filterBar.getChildren().addAll(txtTimKiem, btnTim, btnXoa);

        TableView<NhanVien> table = new TableView<>();

        TableColumn<NhanVien, String> colMa = new TableColumn<>("Mã NV");
        colMa.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMaNhanVienCode()));

        TableColumn<NhanVien, String> colTen = new TableColumn<>("Họ tên");
        colTen.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getHoTen()));

        TableColumn<NhanVien, String> colSdt = new TableColumn<>("Số điện thoại");
        colSdt.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSoDienThoai()));

        TableColumn<NhanVien, String> colVaiTro = new TableColumn<>("Vai trò");
        colVaiTro.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getMaVaiTro() == 2 ? "NHAN_VIEN" : String.valueOf(data.getValue().getMaVaiTro())
        ));

        TableColumn<NhanVien, String> colTrangThai = new TableColumn<>("Trạng thái");
        colTrangThai.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTrangThaiNhanVien()));

        table.getColumns().addAll(colMa, colTen, colSdt, colVaiTro, colTrangThai);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        Runnable reloadTable = () -> {
            List<NhanVien> danhSach = nhanVienService.layTatCaNhanVien();
            table.setItems(FXCollections.observableArrayList(danhSach));
        };

        reloadTable.run();

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, nv) -> {
            if (nv != null) {
                txtMaNhanVien.setText(nv.getMaNhanVienCode());
                txtHoTen.setText(nv.getHoTen());
                txtSoDienThoai.setText(nv.getSoDienThoai());
                txtEmail.setText(nv.getEmail());
                txtTenDangNhap.setText(nv.getTenDangNhap());
                txtMatKhau.setText(nv.getMatKhau());
                cbTrangThaiNhanVien.setValue(nv.getTrangThaiNhanVien());
                cbTrangThaiTaiKhoan.setValue(nv.getTrangThaiTaiKhoan());
                txtGhiChu.setText(nv.getGhiChu());

                if (nv.getMaVaiTro() == 2) {
                    cbVaiTro.setValue("NHAN_VIEN");
                } else {
                    cbVaiTro.setValue("QUAN_LY_CA");
                }
            }
        });

        btnThem.setOnAction(e -> {
            try {
                NhanVien nv = new NhanVien();
                nv.setMaNhanVienCode(txtMaNhanVien.getText().trim());
                nv.setHoTen(txtHoTen.getText().trim());
                nv.setSoDienThoai(txtSoDienThoai.getText().trim());
                nv.setEmail(txtEmail.getText().trim());
                nv.setTenDangNhap(txtTenDangNhap.getText().trim());
                nv.setMatKhau(txtMatKhau.getText().trim());
                nv.setMaVaiTro(2);
                nv.setTrangThaiNhanVien(cbTrangThaiNhanVien.getValue());
                nv.setTrangThaiTaiKhoan(cbTrangThaiTaiKhoan.getValue());
                nv.setGhiChu(txtGhiChu.getText().trim());

                nhanVienService.themNhanVien(nv);
                reloadTable.run();
                hienThongBao("Thêm nhân viên thành công");
                lamMoi(txtMaNhanVien, txtHoTen, txtSoDienThoai, txtEmail, txtTenDangNhap, txtMatKhau, txtGhiChu,
                        cbVaiTro, cbTrangThaiNhanVien, cbTrangThaiTaiKhoan);
            } catch (Exception ex) {
                hienLoi(ex.getMessage());
            }
        });

        btnCapNhat.setOnAction(e -> {
            NhanVien selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                hienLoi("Vui lòng chọn nhân viên cần cập nhật");
                return;
            }

            try {
                selected.setMaNhanVienCode(txtMaNhanVien.getText().trim());
                selected.setHoTen(txtHoTen.getText().trim());
                selected.setSoDienThoai(txtSoDienThoai.getText().trim());
                selected.setEmail(txtEmail.getText().trim());
                selected.setTenDangNhap(txtTenDangNhap.getText().trim());
                selected.setMatKhau(txtMatKhau.getText().trim());
                selected.setMaVaiTro(2);
                selected.setTrangThaiNhanVien(cbTrangThaiNhanVien.getValue());
                selected.setTrangThaiTaiKhoan(cbTrangThaiTaiKhoan.getValue());
                selected.setGhiChu(txtGhiChu.getText().trim());

                nhanVienService.capNhatNhanVien(selected);
                reloadTable.run();
                hienThongBao("Cập nhật nhân viên thành công");
            } catch (Exception ex) {
                hienLoi(ex.getMessage());
            }
        });

        btnXoa.setOnAction(e -> {
            NhanVien selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                hienLoi("Vui lòng chọn nhân viên cần xóa");
                return;
            }

            try {
                nhanVienService.xoaNhanVien(selected.getMaNhanVien(), selected.getMaTaiKhoan());
                reloadTable.run();
                hienThongBao("Xóa nhân viên thành công");
                lamMoi(txtMaNhanVien, txtHoTen, txtSoDienThoai, txtEmail, txtTenDangNhap, txtMatKhau, txtGhiChu,
                        cbVaiTro, cbTrangThaiNhanVien, cbTrangThaiTaiKhoan);
            } catch (Exception ex) {
                hienLoi(ex.getMessage());
            }
        });

        btnTim.setOnAction(e -> {
            List<NhanVien> danhSach = nhanVienService.timKiemNhanVien(txtTimKiem.getText());
            table.setItems(FXCollections.observableArrayList(danhSach));
        });

        btnLamMoi.setOnAction(e -> lamMoi(
                txtMaNhanVien, txtHoTen, txtSoDienThoai, txtEmail, txtTenDangNhap, txtMatKhau, txtGhiChu,
                cbVaiTro, cbTrangThaiNhanVien, cbTrangThaiTaiKhoan
        ));

        tableCard.getChildren().addAll(lblTableTitle, filterBar, table);

        root.getChildren().addAll(lblTitle, lblSubtitle, formCard, tableCard);
        return root;
    }

    private static void lamMoi(
            TextField txtMaNhanVien, TextField txtHoTen, TextField txtSoDienThoai,
            TextField txtEmail, TextField txtTenDangNhap, TextField txtMatKhau, TextField txtGhiChu,
            ComboBox<?> cbVaiTro, ComboBox<?> cbTrangThaiNhanVien, ComboBox<?> cbTrangThaiTaiKhoan
    ) {
        txtMaNhanVien.clear();
        txtHoTen.clear();
        txtSoDienThoai.clear();
        txtEmail.clear();
        txtTenDangNhap.clear();
        txtMatKhau.clear();
        txtGhiChu.clear();
        cbVaiTro.setValue(null);
        cbTrangThaiNhanVien.setValue(null);
        cbTrangThaiTaiKhoan.setValue(null);
    }

    private static void hienThongBao(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Thông báo");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static void hienLoi(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Lỗi");
        alert.setContentText(message);
        alert.showAndWait();
    }
}