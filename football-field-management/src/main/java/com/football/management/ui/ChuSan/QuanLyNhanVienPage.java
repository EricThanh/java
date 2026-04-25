package com.football.management.ui.ChuSan;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class QuanLyNhanVienPage {

    public static Node createView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        Label lblTitle = new Label("Quan ly nhan vien");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Them moi, chinh sua, phan quyen va theo doi nhan vien");
        lblSubtitle.getStyleClass().add("section-subtitle");

        VBox formCard = new VBox(18);
        formCard.getStyleClass().add("form-card");

        Label lblFormTitle = new Label("Thong tin nhan vien");
        lblFormTitle.getStyleClass().add("section-title");

        GridPane form = new GridPane();
        form.getStyleClass().add("form-grid");

        Label lblMaNhanVien = new Label("Ma nhan vien");
        lblMaNhanVien.getStyleClass().add("form-label");
        TextField txtMaNhanVien = new TextField();
        txtMaNhanVien.setPromptText("Nhap ma nhan vien");

        Label lblHoTen = new Label("Ho ten");
        lblHoTen.getStyleClass().add("form-label");
        TextField txtHoTen = new TextField();
        txtHoTen.setPromptText("Nhap ho ten");

        Label lblSoDienThoai = new Label("So dien thoai");
        lblSoDienThoai.getStyleClass().add("form-label");
        TextField txtSoDienThoai = new TextField();
        txtSoDienThoai.setPromptText("Nhap so dien thoai");

        Label lblEmail = new Label("Email");
        lblEmail.getStyleClass().add("form-label");
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Nhap email");

        Label lblTenDangNhap = new Label("Ten dang nhap");
        lblTenDangNhap.getStyleClass().add("form-label");
        TextField txtTenDangNhap = new TextField();
        txtTenDangNhap.setPromptText("Nhap ten dang nhap");

        Label lblMatKhau = new Label("Mat khau");
        lblMatKhau.getStyleClass().add("form-label");
        PasswordField txtMatKhau = new PasswordField();
        txtMatKhau.setPromptText("Nhap mat khau");

        Label lblVaiTro = new Label("Vai tro");
        lblVaiTro.getStyleClass().add("form-label");
        ComboBox<String> cbVaiTro = new ComboBox<>();
        cbVaiTro.setItems(FXCollections.observableArrayList("NHAN_VIEN", "QUAN_LY_CA"));
        cbVaiTro.setPromptText("Chon vai tro");

        Label lblTrangThai = new Label("Trang thai");
        lblTrangThai.getStyleClass().add("form-label");
        ComboBox<String> cbTrangThai = new ComboBox<>();
        cbTrangThai.setItems(FXCollections.observableArrayList("DANG_LAM", "NGHI_LAM"));
        cbTrangThai.setPromptText("Chon trang thai");

        form.add(lblMaNhanVien, 0, 0);
        form.add(txtMaNhanVien, 1, 0);

        form.add(lblHoTen, 0, 1);
        form.add(txtHoTen, 1, 1);

        form.add(lblSoDienThoai, 0, 2);
        form.add(txtSoDienThoai, 1, 2);

        form.add(lblEmail, 0, 3);
        form.add(txtEmail, 1, 3);

        form.add(lblTenDangNhap, 0, 4);
        form.add(txtTenDangNhap, 1, 4);

        form.add(lblMatKhau, 0, 5);
        form.add(txtMatKhau, 1, 5);

        form.add(lblVaiTro, 0, 6);
        form.add(cbVaiTro, 1, 6);

        form.add(lblTrangThai, 0, 7);
        form.add(cbTrangThai, 1, 7);

        Button btnThem = new Button("Them nhan vien");
        btnThem.getStyleClass().add("primary-button");

        Button btnCapNhat = new Button("Cap nhat");
        btnCapNhat.getStyleClass().add("light-button");

        Button btnPhanQuyen = new Button("Phan quyen");
        btnPhanQuyen.getStyleClass().add("secondary-button");

        HBox actionBox = new HBox(10, btnThem, btnCapNhat, btnPhanQuyen);

        formCard.getChildren().addAll(lblFormTitle, form, actionBox);

        VBox tableCard = new VBox(16);
        tableCard.getStyleClass().add("card");

        Label lblTableTitle = new Label("Danh sach nhan vien");
        lblTableTitle.getStyleClass().add("section-title");

        HBox filterBar = new HBox(10);
        filterBar.getStyleClass().add("filter-bar");

        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tim kiem nhan vien...");
        txtTimKiem.getStyleClass().add("search-field");
        txtTimKiem.setPrefWidth(320);

        Button btnXoa = new Button("Xoa");
        btnXoa.getStyleClass().add("secondary-button");

        filterBar.getChildren().addAll(txtTimKiem, btnXoa);

        TableView<NhanVienRow> table = new TableView<>();

        TableColumn<NhanVienRow, String> colMa = new TableColumn<>("Ma NV");
        colMa.setCellValueFactory(data -> data.getValue().maNhanVienProperty());

        TableColumn<NhanVienRow, String> colTen = new TableColumn<>("Ho ten");
        colTen.setCellValueFactory(data -> data.getValue().hoTenProperty());

        TableColumn<NhanVienRow, String> colSdt = new TableColumn<>("So dien thoai");
        colSdt.setCellValueFactory(data -> data.getValue().soDienThoaiProperty());

        TableColumn<NhanVienRow, String> colVaiTro = new TableColumn<>("Vai tro");
        colVaiTro.setCellValueFactory(data -> data.getValue().vaiTroProperty());

        table.getColumns().addAll(colMa, colTen, colSdt, colVaiTro);
        table.setItems(FXCollections.observableArrayList(
                new NhanVienRow("NV01", "Nguyen Van A", "0912345678", "NHAN_VIEN"),
                new NhanVienRow("NV02", "Tran Thi B", "0988123456", "QUAN_LY_CA"),
                new NhanVienRow("NV03", "Le Van C", "0909666888", "NHAN_VIEN")
        ));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        tableCard.getChildren().addAll(lblTableTitle, filterBar, table);

        root.getChildren().addAll(lblTitle, lblSubtitle, formCard, tableCard);
        return root;
    }
}