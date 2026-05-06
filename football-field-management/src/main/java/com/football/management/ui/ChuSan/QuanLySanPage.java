package com.football.management.ui.ChuSan;

import com.football.management.dao.LoaiSanDAO;
import com.football.management.model.LoaiSan;
import com.football.management.model.SanBong;
import com.football.management.service.SanBongService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class QuanLySanPage {

    public static Node createView() {
        SanBongService sanBongService = new SanBongService();
        LoaiSanDAO loaiSanDAO = new LoaiSanDAO();

        VBox root = new VBox(18);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        Label lblTitle = new Label("Quản lý sân");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Thêm mới, cập nhật và tìm kiếm sân bóng");
        lblSubtitle.getStyleClass().add("section-subtitle");

        VBox formCard = new VBox(18);
        formCard.getStyleClass().add("form-card");

        Label lblFormTitle = new Label("Thông tin sân bóng");
        lblFormTitle.getStyleClass().add("section-title");

        GridPane form = new GridPane();
        form.getStyleClass().add("form-grid");

        TextField txtMaSan = new TextField();
        txtMaSan.setPromptText("Mã sân");

        TextField txtTenSan = new TextField();
        txtTenSan.setPromptText("Tên sân");

        ComboBox<LoaiSan> cbLoaiSan = new ComboBox<>();
        cbLoaiSan.setItems(FXCollections.observableArrayList(loaiSanDAO.layTatCaLoaiSan()));
        cbLoaiSan.setPromptText("Chọn loại sân");

        TextField txtViTri = new TextField();
        txtViTri.setPromptText("Vị trí");

        TextField txtSucChua = new TextField();
        txtSucChua.setPromptText("Sức chứa");

        ComboBox<String> cbTrangThai = new ComboBox<>();
        cbTrangThai.getItems().addAll("SAN_SANG", "BAO_TRI", "DANG_SU_DUNG", "NGUNG_HOAT_DONG");
        cbTrangThai.setPromptText("Trạng thái");

        TextField txtGioMoCua = new TextField();
        txtGioMoCua.setPromptText("VD: 06:00");

        TextField txtGioDongCua = new TextField();
        txtGioDongCua.setPromptText("VD: 22:00");

        form.add(new Label("Mã sân"), 0, 0);
        form.add(txtMaSan, 1, 0);

        form.add(new Label("Tên sân"), 0, 1);
        form.add(txtTenSan, 1, 1);

        form.add(new Label("Loại sân"), 0, 2);
        form.add(cbLoaiSan, 1, 2);

        form.add(new Label("Vị trí"), 0, 3);
        form.add(txtViTri, 1, 3);

        form.add(new Label("Sức chứa"), 0, 4);
        form.add(txtSucChua, 1, 4);

        form.add(new Label("Trạng thái"), 0, 5);
        form.add(cbTrangThai, 1, 5);

        form.add(new Label("Giờ mở cửa"), 0, 6);
        form.add(txtGioMoCua, 1, 6);

        form.add(new Label("Giờ đóng cửa"), 0, 7);
        form.add(txtGioDongCua, 1, 7);

        HBox actionBar = new HBox(10);

        Button btnThem = new Button("Thêm");
        btnThem.getStyleClass().add("primary-button");

        Button btnCapNhat = new Button("Cập nhật");
        btnCapNhat.getStyleClass().add("light-button");

        Button btnLamMoi = new Button("Làm mới");
        btnLamMoi.getStyleClass().add("secondary-button");

        actionBar.getChildren().addAll(btnThem, btnCapNhat, btnLamMoi);

        formCard.getChildren().addAll(lblFormTitle, form, actionBar);

        VBox tableCard = new VBox(16);
        tableCard.getStyleClass().add("card");

        Label lblTableTitle = new Label("Danh sách sân");
        lblTableTitle.getStyleClass().add("section-title");

        HBox filterBar = new HBox(10);
        filterBar.getStyleClass().add("filter-bar");

        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tìm kiếm sân...");
        txtTimKiem.getStyleClass().add("search-field");
        txtTimKiem.setPrefWidth(260);

        Button btnTim = new Button("Tìm");
        btnTim.getStyleClass().add("light-button");

        Button btnXoa = new Button("Xóa");
        btnXoa.getStyleClass().add("secondary-button");

        filterBar.getChildren().addAll(txtTimKiem, btnTim, btnXoa);

        TableView<SanBong> table = new TableView<>();

        TableColumn<SanBong, String> colMa = new TableColumn<>("Mã sân");
        colMa.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMaSanCode()));

        TableColumn<SanBong, String> colTen = new TableColumn<>("Tên sân");
        colTen.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTenSan()));

        TableColumn<SanBong, String> colLoai = new TableColumn<>("Mã loại");
        colLoai.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getMaLoaiSan())));

        TableColumn<SanBong, String> colViTri = new TableColumn<>("Vị trí");
        colViTri.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getViTri()));

        TableColumn<SanBong, String> colTrangThai = new TableColumn<>("Trạng thái");
        colTrangThai.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTrangThaiSan()));

        table.getColumns().addAll(colMa, colTen, colLoai, colViTri, colTrangThai);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        Runnable reloadTable = () -> {
            List<SanBong> danhSach = sanBongService.layTatCaSan();
            table.setItems(FXCollections.observableArrayList(danhSach));
        };

        reloadTable.run();

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, san) -> {
            if (san != null) {
                txtMaSan.setText(san.getMaSanCode());
                txtTenSan.setText(san.getTenSan());
                txtViTri.setText(san.getViTri());
                txtSucChua.setText(san.getSucChua() != null ? String.valueOf(san.getSucChua()) : "");
                cbTrangThai.setValue(san.getTrangThaiSan());
                txtGioMoCua.setText(san.getGioMoCua());
                txtGioDongCua.setText(san.getGioDongCua());

                for (LoaiSan ls : cbLoaiSan.getItems()) {
                    if (ls.getMaLoaiSan() == san.getMaLoaiSan()) {
                        cbLoaiSan.setValue(ls);
                        break;
                    }
                }
            }
        });

        btnThem.setOnAction(e -> {
            try {
                SanBong san = new SanBong();
                san.setMaSanCode(txtMaSan.getText().trim());
                san.setTenSan(txtTenSan.getText().trim());
                san.setMaLoaiSan(cbLoaiSan.getValue().getMaLoaiSan());
                san.setViTri(txtViTri.getText().trim());
                san.setSucChua(txtSucChua.getText().trim().isEmpty() ? null : Integer.parseInt(txtSucChua.getText().trim()));
                san.setTrangThaiSan(cbTrangThai.getValue());
                san.setGioMoCua(txtGioMoCua.getText().trim());
                san.setGioDongCua(txtGioDongCua.getText().trim());

                sanBongService.themSan(san);
                reloadTable.run();
                hienThongBao("Thêm sân thành công");
                lamMoi(txtMaSan, txtTenSan, txtViTri, txtSucChua, txtGioMoCua, txtGioDongCua, cbLoaiSan, cbTrangThai);
            } catch (Exception ex) {
                hienLoi(ex.getMessage());
            }
        });

        btnCapNhat.setOnAction(e -> {
            SanBong selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                hienLoi("Vui lòng chọn sân cần cập nhật");
                return;
            }

            try {
                selected.setMaSanCode(txtMaSan.getText().trim());
                selected.setTenSan(txtTenSan.getText().trim());
                selected.setMaLoaiSan(cbLoaiSan.getValue().getMaLoaiSan());
                selected.setViTri(txtViTri.getText().trim());
                selected.setSucChua(txtSucChua.getText().trim().isEmpty() ? null : Integer.parseInt(txtSucChua.getText().trim()));
                selected.setTrangThaiSan(cbTrangThai.getValue());
                selected.setGioMoCua(txtGioMoCua.getText().trim());
                selected.setGioDongCua(txtGioDongCua.getText().trim());

                sanBongService.capNhatSan(selected);
                reloadTable.run();
                hienThongBao("Cập nhật sân thành công");
            } catch (Exception ex) {
                hienLoi(ex.getMessage());
            }
        });

        btnXoa.setOnAction(e -> {
            SanBong selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                hienLoi("Vui lòng chọn sân cần xóa");
                return;
            }

            try {
                sanBongService.xoaSan(selected.getMaSan());
                reloadTable.run();
                hienThongBao("Xóa sân thành công");
                lamMoi(txtMaSan, txtTenSan, txtViTri, txtSucChua, txtGioMoCua, txtGioDongCua, cbLoaiSan, cbTrangThai);
            } catch (Exception ex) {
                hienLoi(ex.getMessage());
            }
        });

        btnTim.setOnAction(e -> {
            List<SanBong> danhSach = sanBongService.timKiemSan(txtTimKiem.getText());
            table.setItems(FXCollections.observableArrayList(danhSach));
        });

        btnLamMoi.setOnAction(e -> lamMoi(txtMaSan, txtTenSan, txtViTri, txtSucChua, txtGioMoCua, txtGioDongCua, cbLoaiSan, cbTrangThai));

        tableCard.getChildren().addAll(lblTableTitle, filterBar, table);

        root.getChildren().addAll(lblTitle, lblSubtitle, formCard, tableCard);
        return root;
    }

    private static void lamMoi(TextField txtMaSan, TextField txtTenSan, TextField txtViTri,
                               TextField txtSucChua, TextField txtGioMoCua, TextField txtGioDongCua,
                               ComboBox<?> cbLoaiSan, ComboBox<?> cbTrangThai) {
        txtMaSan.clear();
        txtTenSan.clear();
        txtViTri.clear();
        txtSucChua.clear();
        txtGioMoCua.clear();
        txtGioDongCua.clear();
        cbLoaiSan.setValue(null);
        cbTrangThai.setValue(null);
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