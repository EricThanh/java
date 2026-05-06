package com.football.management.ui.ChuSan;

import com.football.management.dao.SanBongDAO;
import com.football.management.model.BangGia;
import com.football.management.model.SanBong;
import com.football.management.service.BangGiaService;
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

public class QuanLyGiaPage {

    public static Node createView() {
        BangGiaService bangGiaService = new BangGiaService();
        SanBongDAO sanBongDAO = new SanBongDAO();

        List<SanBong> danhSachSan = sanBongDAO.layTatCaSan();

        VBox root = new VBox(20);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        Label lblTitle = new Label("Quản lý giá");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Thêm mới, cập nhật và xóa bảng giá của từng sân");
        lblSubtitle.getStyleClass().add("section-subtitle");

        VBox formCard = new VBox(18);
        formCard.getStyleClass().add("form-card");

        Label lblFormTitle = new Label("Thông tin bảng giá");
        lblFormTitle.getStyleClass().add("section-title");

        GridPane form = new GridPane();
        form.getStyleClass().add("form-grid");

        ComboBox<SanBong> cbSan = new ComboBox<>();
        cbSan.setItems(FXCollections.observableArrayList(danhSachSan));
        cbSan.setPromptText("Chọn sân");

        TextField txtTenBangGia = new TextField();
        txtTenBangGia.setPromptText("Tên bảng giá");

        ComboBox<Integer> cbThuTrongTuan = new ComboBox<>();
        cbThuTrongTuan.getItems().addAll(1, 2, 3, 4, 5, 6, 7);
        cbThuTrongTuan.setPromptText("Thứ trong tuần");

        TextField txtGioBatDau = new TextField();
        txtGioBatDau.setPromptText("VD: 08:00");

        TextField txtGioKetThuc = new TextField();
        txtGioKetThuc.setPromptText("VD: 10:00");

        TextField txtGiaMoiGio = new TextField();
        txtGiaMoiGio.setPromptText("Nhập giá mỗi giờ");

        DatePicker dpTuNgay = new DatePicker();
        DatePicker dpDenNgay = new DatePicker();

        ComboBox<String> cbDangApDung = new ComboBox<>();
        cbDangApDung.getItems().addAll("Có", "Không");
        cbDangApDung.setPromptText("Đang áp dụng");

        TextField txtGhiChu = new TextField();
        txtGhiChu.setPromptText("Ghi chú");

        form.add(new Label("Sân"), 0, 0);
        form.add(cbSan, 1, 0);

        form.add(new Label("Tên bảng giá"), 0, 1);
        form.add(txtTenBangGia, 1, 1);

        form.add(new Label("Thứ trong tuần"), 0, 2);
        form.add(cbThuTrongTuan, 1, 2);

        form.add(new Label("Giờ bắt đầu"), 0, 3);
        form.add(txtGioBatDau, 1, 3);

        form.add(new Label("Giờ kết thúc"), 0, 4);
        form.add(txtGioKetThuc, 1, 4);

        form.add(new Label("Giá mỗi giờ"), 0, 5);
        form.add(txtGiaMoiGio, 1, 5);

        form.add(new Label("Từ ngày"), 0, 6);
        form.add(dpTuNgay, 1, 6);

        form.add(new Label("Đến ngày"), 0, 7);
        form.add(dpDenNgay, 1, 7);

        form.add(new Label("Đang áp dụng"), 0, 8);
        form.add(cbDangApDung, 1, 8);

        form.add(new Label("Ghi chú"), 0, 9);
        form.add(txtGhiChu, 1, 9);

        HBox actionBox = new HBox(10);

        Button btnThem = new Button("Thêm bảng giá");
        btnThem.getStyleClass().add("primary-button");

        Button btnCapNhat = new Button("Cập nhật");
        btnCapNhat.getStyleClass().add("light-button");

        Button btnLamMoi = new Button("Làm mới");
        btnLamMoi.getStyleClass().add("secondary-button");

        actionBox.getChildren().addAll(btnThem, btnCapNhat, btnLamMoi);
        formCard.getChildren().addAll(lblFormTitle, form, actionBox);

        VBox tableCard = new VBox(16);
        tableCard.getStyleClass().add("card");

        Label lblTableTitle = new Label("Danh sách bảng giá");
        lblTableTitle.getStyleClass().add("section-title");

        HBox filterBar = new HBox(10);
        filterBar.getStyleClass().add("filter-bar");

        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tìm kiếm bảng giá...");
        txtTimKiem.getStyleClass().add("search-field");
        txtTimKiem.setPrefWidth(280);

        Button btnTim = new Button("Tìm");
        btnTim.getStyleClass().add("light-button");

        Button btnXoa = new Button("Xóa");
        btnXoa.getStyleClass().add("secondary-button");

        filterBar.getChildren().addAll(txtTimKiem, btnTim, btnXoa);

        TableView<BangGia> table = new TableView<>();

        TableColumn<BangGia, String> colTenBangGia = new TableColumn<>("Tên bảng giá");
        colTenBangGia.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTenBangGia())
        );

        TableColumn<BangGia, String> colMaSan = new TableColumn<>("Mã sân");
        colMaSan.setCellValueFactory(data ->
                new SimpleStringProperty(layMaSanCodeTheoId(danhSachSan, data.getValue().getMaSan()))
        );

        TableColumn<BangGia, String> colTenSan = new TableColumn<>("Tên sân");
        colTenSan.setCellValueFactory(data ->
                new SimpleStringProperty(layTenSanTheoId(danhSachSan, data.getValue().getMaSan()))
        );

        TableColumn<BangGia, String> colThu = new TableColumn<>("Thứ");
        colThu.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getThuTrongTuan() != null
                                ? String.valueOf(data.getValue().getThuTrongTuan())
                                : ""
                )
        );

        TableColumn<BangGia, String> colKhungGio = new TableColumn<>("Khung giờ");
        colKhungGio.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getGioBatDau() + " - " + data.getValue().getGioKetThuc())
        );

        TableColumn<BangGia, String> colGiaMoiGio = new TableColumn<>("Giá mỗi giờ");
        colGiaMoiGio.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getGiaMoiGio().toPlainString())
        );

        TableColumn<BangGia, String> colDangApDung = new TableColumn<>("Đang áp dụng");
        colDangApDung.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDangApDung() == 1 ? "Có" : "Không")
        );

        table.getColumns().addAll(
                colTenBangGia,
                colMaSan,
                colTenSan,
                colThu,
                colKhungGio,
                colGiaMoiGio,
                colDangApDung
        );
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        Runnable reloadTable = () -> {
            List<BangGia> danhSach = bangGiaService.layTatCaBangGia();
            table.setItems(FXCollections.observableArrayList(danhSach));
        };

        reloadTable.run();

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, bg) -> {
            if (bg != null) {
                txtTenBangGia.setText(bg.getTenBangGia());
                cbThuTrongTuan.setValue(bg.getThuTrongTuan());
                txtGioBatDau.setText(bg.getGioBatDau());
                txtGioKetThuc.setText(bg.getGioKetThuc());
                txtGiaMoiGio.setText(bg.getGiaMoiGio().toPlainString());
                dpTuNgay.setValue(bg.getNgayApDungTu());
                dpDenNgay.setValue(bg.getNgayApDungDen());
                cbDangApDung.setValue(bg.getDangApDung() == 1 ? "Có" : "Không");
                txtGhiChu.setText(bg.getGhiChu());

                for (SanBong san : cbSan.getItems()) {
                    if (san.getMaSan() == bg.getMaSan()) {
                        cbSan.setValue(san);
                        break;
                    }
                }
            }
        });

        btnThem.setOnAction(e -> {
            try {
                if (cbSan.getValue() == null) {
                    hienLoi("Vui lòng chọn sân");
                    return;
                }
                if (cbDangApDung.getValue() == null) {
                    hienLoi("Vui lòng chọn trạng thái đang áp dụng");
                    return;
                }

                BangGia bangGia = new BangGia();
                bangGia.setMaSan(cbSan.getValue().getMaSan());
                bangGia.setTenBangGia(txtTenBangGia.getText().trim());
                bangGia.setThuTrongTuan(cbThuTrongTuan.getValue());
                bangGia.setGioBatDau(txtGioBatDau.getText().trim());
                bangGia.setGioKetThuc(txtGioKetThuc.getText().trim());
                bangGia.setGiaMoiGio(new BigDecimal(txtGiaMoiGio.getText().trim()));
                bangGia.setNgayApDungTu(dpTuNgay.getValue());
                bangGia.setNgayApDungDen(dpDenNgay.getValue());
                bangGia.setDangApDung("Có".equals(cbDangApDung.getValue()) ? 1 : 0);
                bangGia.setGhiChu(txtGhiChu.getText().trim());

                bangGiaService.themBangGia(bangGia);
                reloadTable.run();
                hienThongBao("Thêm bảng giá thành công");
                lamMoi(
                        cbSan, txtTenBangGia, cbThuTrongTuan, txtGioBatDau, txtGioKetThuc,
                        txtGiaMoiGio, dpTuNgay, dpDenNgay, cbDangApDung, txtGhiChu
                );
            } catch (Exception ex) {
                hienLoi(ex.getMessage());
            }
        });

        btnCapNhat.setOnAction(e -> {
            BangGia selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                hienLoi("Vui lòng chọn bảng giá cần cập nhật");
                return;
            }

            try {
                if (cbSan.getValue() == null) {
                    hienLoi("Vui lòng chọn sân");
                    return;
                }
                if (cbDangApDung.getValue() == null) {
                    hienLoi("Vui lòng chọn trạng thái đang áp dụng");
                    return;
                }

                selected.setMaSan(cbSan.getValue().getMaSan());
                selected.setTenBangGia(txtTenBangGia.getText().trim());
                selected.setThuTrongTuan(cbThuTrongTuan.getValue());
                selected.setGioBatDau(txtGioBatDau.getText().trim());
                selected.setGioKetThuc(txtGioKetThuc.getText().trim());
                selected.setGiaMoiGio(new BigDecimal(txtGiaMoiGio.getText().trim()));
                selected.setNgayApDungTu(dpTuNgay.getValue());
                selected.setNgayApDungDen(dpDenNgay.getValue());
                selected.setDangApDung("Có".equals(cbDangApDung.getValue()) ? 1 : 0);
                selected.setGhiChu(txtGhiChu.getText().trim());

                bangGiaService.capNhatBangGia(selected);
                reloadTable.run();
                hienThongBao("Cập nhật bảng giá thành công");
            } catch (Exception ex) {
                hienLoi(ex.getMessage());
            }
        });

        btnXoa.setOnAction(e -> {
            BangGia selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                hienLoi("Vui lòng chọn bảng giá cần xóa");
                return;
            }

            try {
                bangGiaService.xoaBangGia(selected.getMaBangGia());
                reloadTable.run();
                hienThongBao("Xóa bảng giá thành công");
                lamMoi(
                        cbSan, txtTenBangGia, cbThuTrongTuan, txtGioBatDau, txtGioKetThuc,
                        txtGiaMoiGio, dpTuNgay, dpDenNgay, cbDangApDung, txtGhiChu
                );
            } catch (Exception ex) {
                hienLoi(ex.getMessage());
            }
        });

        btnTim.setOnAction(e -> {
            List<BangGia> danhSach = bangGiaService.timKiemBangGia(txtTimKiem.getText());
            table.setItems(FXCollections.observableArrayList(danhSach));
        });

        btnLamMoi.setOnAction(e -> lamMoi(
                cbSan, txtTenBangGia, cbThuTrongTuan, txtGioBatDau, txtGioKetThuc,
                txtGiaMoiGio, dpTuNgay, dpDenNgay, cbDangApDung, txtGhiChu
        ));

        tableCard.getChildren().addAll(lblTableTitle, filterBar, table);
        root.getChildren().addAll(lblTitle, lblSubtitle, formCard, tableCard);

        return root;
    }

    private static String layMaSanCodeTheoId(List<SanBong> danhSachSan, int maSan) {
        for (SanBong san : danhSachSan) {
            if (san.getMaSan() == maSan) {
                return san.getMaSanCode();
            }
        }
        return "";
    }

    private static String layTenSanTheoId(List<SanBong> danhSachSan, int maSan) {
        for (SanBong san : danhSachSan) {
            if (san.getMaSan() == maSan) {
                return san.getTenSan();
            }
        }
        return "";
    }

    private static void lamMoi(
            ComboBox<?> cbSan,
            TextField txtTenBangGia,
            ComboBox<?> cbThuTrongTuan,
            TextField txtGioBatDau,
            TextField txtGioKetThuc,
            TextField txtGiaMoiGio,
            DatePicker dpTuNgay,
            DatePicker dpDenNgay,
            ComboBox<?> cbDangApDung,
            TextField txtGhiChu
    ) {
        cbSan.setValue(null);
        txtTenBangGia.clear();
        cbThuTrongTuan.setValue(null);
        txtGioBatDau.clear();
        txtGioKetThuc.clear();
        txtGiaMoiGio.clear();
        dpTuNgay.setValue(null);
        dpDenNgay.setValue(null);
        cbDangApDung.setValue(null);
        txtGhiChu.clear();
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