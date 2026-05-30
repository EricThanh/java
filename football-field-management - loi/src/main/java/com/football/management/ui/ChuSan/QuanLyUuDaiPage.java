package com.football.management.ui.ChuSan;

import com.football.management.model.UuDai;
import com.football.management.service.UuDaiService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.util.List;

public class QuanLyUuDaiPage {

    public static Node createView() {
        UuDaiService uuDaiService = new UuDaiService();

        VBox root = new VBox(20);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        Label lblTitle = new Label("Quản lý ưu đãi");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Thêm mới, cập nhật và xóa chương trình ưu đãi");
        lblSubtitle.getStyleClass().add("section-subtitle");

        VBox formCard = new VBox(18);
        formCard.getStyleClass().add("form-card");

        Label lblFormTitle = new Label("Thông tin ưu đãi");
        lblFormTitle.getStyleClass().add("section-title");

        GridPane form = new GridPane();
        form.getStyleClass().add("form-grid");

        TextField txtMaGiamGia = new TextField();
        txtMaGiamGia.setPromptText("Mã giảm giá");

        TextField txtTenUuDai = new TextField();
        txtTenUuDai.setPromptText("Tên ưu đãi");

        TextField txtMoTa = new TextField();
        txtMoTa.setPromptText("Mô tả");

        ComboBox<String> cbLoaiGiamGia = new ComboBox<>();
        cbLoaiGiamGia.getItems().addAll("PHAN_TRAM", "SO_TIEN_CO_DINH");
        cbLoaiGiamGia.setPromptText("Loại giảm giá");

        TextField txtGiaTriGiam = new TextField();
        txtGiaTriGiam.setPromptText("Giá trị giảm");

        TextField txtGiaTriToiThieu = new TextField();
        txtGiaTriToiThieu.setPromptText("Giá trị đạt tối thiểu");

        DatePicker dpNgayBatDau = new DatePicker();
        DatePicker dpNgayKetThuc = new DatePicker();

        TextField txtGioBatDau = new TextField();
        txtGioBatDau.setPromptText("VD: 08:00");

        TextField txtGioKetThuc = new TextField();
        txtGioKetThuc.setPromptText("VD: 22:00");

        ComboBox<String> cbDangHoatDong = new ComboBox<>();
        cbDangHoatDong.getItems().addAll("Có", "Không");
        cbDangHoatDong.setPromptText("Đang hoạt động");

        form.add(new Label("Mã giảm giá"), 0, 0);
        form.add(txtMaGiamGia, 1, 0);

        form.add(new Label("Tên ưu đãi"), 0, 1);
        form.add(txtTenUuDai, 1, 1);

        form.add(new Label("Mô tả"), 0, 2);
        form.add(txtMoTa, 1, 2);

        form.add(new Label("Loại giảm giá"), 0, 3);
        form.add(cbLoaiGiamGia, 1, 3);

        form.add(new Label("Giá trị giảm"), 0, 4);
        form.add(txtGiaTriGiam, 1, 4);

        form.add(new Label("Giá trị tối thiểu"), 0, 5);
        form.add(txtGiaTriToiThieu, 1, 5);

        form.add(new Label("Ngày bắt đầu"), 0, 6);
        form.add(dpNgayBatDau, 1, 6);

        form.add(new Label("Ngày kết thúc"), 0, 7);
        form.add(dpNgayKetThuc, 1, 7);

        form.add(new Label("Giờ bắt đầu"), 0, 8);
        form.add(txtGioBatDau, 1, 8);

        form.add(new Label("Giờ kết thúc"), 0, 9);
        form.add(txtGioKetThuc, 1, 9);

        form.add(new Label("Đang hoạt động"), 0, 10);
        form.add(cbDangHoatDong, 1, 10);

        HBox actionBox = new HBox(10);

        Button btnThem = new Button("Thêm ưu đãi");
        btnThem.getStyleClass().add("primary-button");

        Button btnCapNhat = new Button("Cập nhật");
        btnCapNhat.getStyleClass().add("light-button");

        Button btnLamMoi = new Button("Làm mới");
        btnLamMoi.getStyleClass().add("secondary-button");

        actionBox.getChildren().addAll(btnThem, btnCapNhat, btnLamMoi);
        formCard.getChildren().addAll(lblFormTitle, form, actionBox);

        VBox tableCard = new VBox(16);
        tableCard.getStyleClass().add("card");

        Label lblTableTitle = new Label("Danh sách ưu đãi");
        lblTableTitle.getStyleClass().add("section-title");

        HBox filterBar = new HBox(10);
        filterBar.getStyleClass().add("filter-bar");

        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tìm kiếm ưu đãi...");
        txtTimKiem.getStyleClass().add("search-field");
        txtTimKiem.setPrefWidth(280);

        Button btnTim = new Button("Tìm");
        btnTim.getStyleClass().add("light-button");

        Button btnXoa = new Button("Xóa");
        btnXoa.getStyleClass().add("secondary-button");

        filterBar.getChildren().addAll(txtTimKiem, btnTim, btnXoa);

        TableView<UuDai> table = new TableView<>();

        TableColumn<UuDai, String> colMa = new TableColumn<>("Mã giảm giá");
        colMa.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getMaGiamGia())
        );

        TableColumn<UuDai, String> colTen = new TableColumn<>("Tên ưu đãi");
        colTen.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTenUuDai())
        );

        TableColumn<UuDai, String> colLoai = new TableColumn<>("Loại giảm");
        colLoai.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getLoaiGiamGia())
        );

        TableColumn<UuDai, String> colGiaTri = new TableColumn<>("Giá trị giảm");
        colGiaTri.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getGiaTriGiam().toPlainString())
        );

        TableColumn<UuDai, String> colHoatDong = new TableColumn<>("Đang hoạt động");
        colHoatDong.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDangHoatDong() == 1 ? "Có" : "Không")
        );

        table.getColumns().addAll(colMa, colTen, colLoai, colGiaTri, colHoatDong);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        Runnable reloadTable = () -> {
            List<UuDai> danhSach = uuDaiService.layTatCaUuDai();
            table.setItems(FXCollections.observableArrayList(danhSach));
        };

        reloadTable.run();

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, ud) -> {
            if (ud != null) {
                txtMaGiamGia.setText(ud.getMaGiamGia());
                txtTenUuDai.setText(ud.getTenUuDai());
                txtMoTa.setText(ud.getMoTa());
                cbLoaiGiamGia.setValue(ud.getLoaiGiamGia());
                txtGiaTriGiam.setText(ud.getGiaTriGiam().toPlainString());
                txtGiaTriToiThieu.setText(
                        ud.getGiaTriDatToiThieu() != null ? ud.getGiaTriDatToiThieu().toPlainString() : ""
                );
                dpNgayBatDau.setValue(ud.getNgayBatDau());
                dpNgayKetThuc.setValue(ud.getNgayKetThuc());
                txtGioBatDau.setText(ud.getGioBatDau() != null ? ud.getGioBatDau() : "");
                txtGioKetThuc.setText(ud.getGioKetThuc() != null ? ud.getGioKetThuc() : "");
                cbDangHoatDong.setValue(ud.getDangHoatDong() == 1 ? "Có" : "Không");
            }
        });

        btnThem.setOnAction(e -> {
            try {
                if (cbLoaiGiamGia.getValue() == null) {
                    hienLoi("Vui lòng chọn loại giảm giá");
                    return;
                }
                if (cbDangHoatDong.getValue() == null) {
                    hienLoi("Vui lòng chọn trạng thái hoạt động");
                    return;
                }

                UuDai uuDai = new UuDai();
                uuDai.setMaGiamGia(txtMaGiamGia.getText().trim());
                uuDai.setTenUuDai(txtTenUuDai.getText().trim());
                uuDai.setMoTa(txtMoTa.getText().trim());
                uuDai.setLoaiGiamGia(cbLoaiGiamGia.getValue());
                uuDai.setGiaTriGiam(new BigDecimal(txtGiaTriGiam.getText().trim()));
                uuDai.setGiaTriDatToiThieu(
                        txtGiaTriToiThieu.getText().trim().isEmpty()
                                ? BigDecimal.ZERO
                                : new BigDecimal(txtGiaTriToiThieu.getText().trim())
                );
                uuDai.setNgayBatDau(dpNgayBatDau.getValue());
                uuDai.setNgayKetThuc(dpNgayKetThuc.getValue());
                uuDai.setGioBatDau(txtGioBatDau.getText().trim());
                uuDai.setGioKetThuc(txtGioKetThuc.getText().trim());
                uuDai.setDangHoatDong("Có".equals(cbDangHoatDong.getValue()) ? 1 : 0);

                uuDaiService.themUuDai(uuDai);
                reloadTable.run();
                hienThongBao("Thêm ưu đãi thành công");
                lamMoi(
                        txtMaGiamGia, txtTenUuDai, txtMoTa, cbLoaiGiamGia, txtGiaTriGiam,
                        txtGiaTriToiThieu, dpNgayBatDau, dpNgayKetThuc,
                        txtGioBatDau, txtGioKetThuc, cbDangHoatDong
                );
            } catch (Exception ex) {
                hienLoi(ex.getMessage());
            }
        });

        btnCapNhat.setOnAction(e -> {
            UuDai selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                hienLoi("Vui lòng chọn ưu đãi cần cập nhật");
                return;
            }

            try {
                if (cbLoaiGiamGia.getValue() == null) {
                    hienLoi("Vui lòng chọn loại giảm giá");
                    return;
                }
                if (cbDangHoatDong.getValue() == null) {
                    hienLoi("Vui lòng chọn trạng thái hoạt động");
                    return;
                }

                selected.setMaGiamGia(txtMaGiamGia.getText().trim());
                selected.setTenUuDai(txtTenUuDai.getText().trim());
                selected.setMoTa(txtMoTa.getText().trim());
                selected.setLoaiGiamGia(cbLoaiGiamGia.getValue());
                selected.setGiaTriGiam(new BigDecimal(txtGiaTriGiam.getText().trim()));
                selected.setGiaTriDatToiThieu(
                        txtGiaTriToiThieu.getText().trim().isEmpty()
                                ? BigDecimal.ZERO
                                : new BigDecimal(txtGiaTriToiThieu.getText().trim())
                );
                selected.setNgayBatDau(dpNgayBatDau.getValue());
                selected.setNgayKetThuc(dpNgayKetThuc.getValue());
                selected.setGioBatDau(txtGioBatDau.getText().trim());
                selected.setGioKetThuc(txtGioKetThuc.getText().trim());
                selected.setDangHoatDong("Có".equals(cbDangHoatDong.getValue()) ? 1 : 0);

                uuDaiService.capNhatUuDai(selected);
                reloadTable.run();
                hienThongBao("Cập nhật ưu đãi thành công");
            } catch (Exception ex) {
                hienLoi(ex.getMessage());
            }
        });

        btnXoa.setOnAction(e -> {
            UuDai selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                hienLoi("Vui lòng chọn ưu đãi cần xóa");
                return;
            }

            try {
                uuDaiService.xoaUuDai(selected.getMaUuDai());
                reloadTable.run();
                hienThongBao("Xóa ưu đãi thành công");
                lamMoi(
                        txtMaGiamGia, txtTenUuDai, txtMoTa, cbLoaiGiamGia, txtGiaTriGiam,
                        txtGiaTriToiThieu, dpNgayBatDau, dpNgayKetThuc,
                        txtGioBatDau, txtGioKetThuc, cbDangHoatDong
                );
            } catch (Exception ex) {
                hienLoi(ex.getMessage());
            }
        });

        btnTim.setOnAction(e -> {
            List<UuDai> danhSach = uuDaiService.timKiemUuDai(txtTimKiem.getText());
            table.setItems(FXCollections.observableArrayList(danhSach));
        });

        btnLamMoi.setOnAction(e -> lamMoi(
                txtMaGiamGia, txtTenUuDai, txtMoTa, cbLoaiGiamGia, txtGiaTriGiam,
                txtGiaTriToiThieu, dpNgayBatDau, dpNgayKetThuc,
                txtGioBatDau, txtGioKetThuc, cbDangHoatDong
        ));

        tableCard.getChildren().addAll(lblTableTitle, filterBar, table);
        root.getChildren().addAll(lblTitle, lblSubtitle, formCard, tableCard);

        return root;
    }

    private static void lamMoi(
            TextField txtMaGiamGia,
            TextField txtTenUuDai,
            TextField txtMoTa,
            ComboBox<?> cbLoaiGiamGia,
            TextField txtGiaTriGiam,
            TextField txtGiaTriToiThieu,
            DatePicker dpNgayBatDau,
            DatePicker dpNgayKetThuc,
            TextField txtGioBatDau,
            TextField txtGioKetThuc,
            ComboBox<?> cbDangHoatDong
    ) {
        txtMaGiamGia.clear();
        txtTenUuDai.clear();
        txtMoTa.clear();
        cbLoaiGiamGia.setValue(null);
        txtGiaTriGiam.clear();
        txtGiaTriToiThieu.clear();
        dpNgayBatDau.setValue(null);
        dpNgayKetThuc.setValue(null);
        txtGioBatDau.clear();
        txtGioKetThuc.clear();
        cbDangHoatDong.setValue(null);
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