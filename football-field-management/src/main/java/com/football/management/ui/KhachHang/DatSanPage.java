package com.football.management.ui.KhachHang;

import com.football.management.app.AppState;
import com.football.management.dao.KhachHangDatSanDAO;
import com.football.management.dao.KhachHangDatSanDAO.SanKhachHangRow;
import com.football.management.dao.KhachHangDatSanDAO.TinhTienResult;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;

public class DatSanPage {

    /**
     * @param sanMacDinh Sân được chọn sẵn từ ChiTietSanPage
     */
    public static Node createView(SanKhachHangRow sanMacDinh) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        // ===== TITLE =====
        Label lblTitle = new Label("Đặt sân");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Chọn sân, thời gian và xác nhận thông tin đặt");
        lblSubtitle.getStyleClass().add("section-subtitle");

        // ===== FORM CARD =====
        VBox formCard = new VBox(18);
        formCard.getStyleClass().add("form-card");

        Label lblFormTitle = new Label("Thông tin đặt sân");
        lblFormTitle.getStyleClass().add("section-title");

        GridPane form = new GridPane();
        form.getStyleClass().add("form-grid");
        form.setHgap(12);
        form.setVgap(12);

        // ===== SÂN =====
        Label lblSan = new Label("Sân");
        lblSan.getStyleClass().add("form-label");

        ComboBox<SanKhachHangRow> cbSan = new ComboBox<>();
        cbSan.setPromptText("Chọn sân...");
        cbSan.setPrefWidth(280);
        
        cbSan.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(SanKhachHangRow item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTenSan());
            }
        });

        cbSan.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(SanKhachHangRow item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTenSan());
            }
        });

        // ===== NGÀY =====
        Label lblNgay = new Label("Ngày đặt");
        lblNgay.getStyleClass().add("form-label");
        DatePicker dpNgay = new DatePicker(LocalDate.now().plusDays(1));

        // ===== GIỜ BẮT ĐẦU =====
        Label lblGioBD = new Label("Giờ bắt đầu");
        lblGioBD.getStyleClass().add("form-label");
        TextField txtGioBD = new TextField();
        txtGioBD.setPromptText("VD: 08:00");

        // ===== GIỜ KẾT THÚC =====
        Label lblGioKT = new Label("Giờ kết thúc");
        lblGioKT.getStyleClass().add("form-label");
        TextField txtGioKT = new TextField();
        txtGioKT.setPromptText("VD: 10:00");

        // ===== MÃ GIẢM =====
        Label lblMaGiam = new Label("Mã giảm giá");
        lblMaGiam.getStyleClass().add("form-label");
        TextField txtMaGiam = new TextField();
        txtMaGiam.setPromptText("Nếu có");

        // ===== GHI CHÚ =====
        Label lblGhiChu = new Label("Ghi chú");
        lblGhiChu.getStyleClass().add("form-label");
        TextField txtGhiChu = new TextField();
        txtGhiChu.setPromptText("Nhập ghi chú");

        // ===== FORM ADD =====
        form.add(lblSan, 0, 0);
        form.add(cbSan, 1, 0);

        form.add(lblNgay, 0, 1);
        form.add(dpNgay, 1, 1);

        form.add(lblGioBD, 0, 2);
        form.add(txtGioBD, 1, 2);

        form.add(lblGioKT, 0, 3);
        form.add(txtGioKT, 1, 3);

        form.add(lblMaGiam, 0, 4);
        form.add(txtMaGiam, 1, 4);

        form.add(lblGhiChu, 0, 5);
        form.add(txtGhiChu, 1, 5);

        // ===== LABEL KẾT QUẢ =====
        Label lblKetQua = new Label("");
        lblKetQua.getStyleClass().add("section-subtitle");
        lblKetQua.setWrapText(true);

        // ===== GIỮ KẾT QUẢ TÍNH TIỀN =====
        final TinhTienResult[] tinhTienResult = { null };

        // ===== ACTIONS =====
        HBox actions = new HBox(10);
        Button btnTinhTien = new Button("Tính tiền");
        btnTinhTien.getStyleClass().add("light-button");

        Button btnXacNhan = new Button("Xác nhận đặt sân");
        btnXacNhan.getStyleClass().add("primary-button");
        btnXacNhan.setDisable(true);

        actions.getChildren().addAll(btnTinhTien, btnXacNhan);

        // ===== ADD FORM =====
        formCard.getChildren().addAll(lblFormTitle, form, lblKetQua, actions);
        root.getChildren().addAll(lblTitle, lblSubtitle, formCard);

        // ===== LOAD DANH SÁCH SÂN =====
        Thread tLoad = new Thread(() -> {
            try {
                KhachHangDatSanDAO dao = new KhachHangDatSanDAO();
                List<SanKhachHangRow> ds = dao.layDanhSachSanChoKhachHang();

                Platform.runLater(() -> {
                    cbSan.getItems().setAll(ds);

                    // ===== AUTO CHỌN SÂN =====
                    if (sanMacDinh != null) {
                        ds.stream()
                          .filter(s -> s.getMaSan() == sanMacDinh.getMaSan())
                          .findFirst()
                          .ifPresent(cbSan::setValue);
                    }
                });
            } catch (Exception ex) {
                Platform.runLater(() -> lblKetQua.setText("Lỗi tải danh sách sân: " + ex.getMessage()));
            }
        });
        tLoad.setDaemon(true);
        tLoad.start();

        // ===== TÍNH TIỀN =====
        btnTinhTien.setOnAction(e -> {
            SanKhachHangRow sanChon = cbSan.getValue();
            LocalDate ngay = dpNgay.getValue();
            String gioBD = txtGioBD.getText().trim();
            String gioKT = txtGioKT.getText().trim();
            String maGiam = txtMaGiam.getText().trim();

            // ===== VALIDATE =====
            if (sanChon == null || ngay == null || gioBD.isEmpty() || gioKT.isEmpty()) {
                lblKetQua.setText("⚠ Vui lòng điền đầy đủ sân, ngày và khung giờ.");
                btnXacNhan.setDisable(true);
                return;
            }

            // ===== VALIDATE NGÀY =====
            if (ngay.isBefore(LocalDate.now())) {
                lblKetQua.setText("⚠ Không thể đặt ngày trong quá khứ.");
                btnXacNhan.setDisable(true);
                return;
            }

            lblKetQua.setText("Đang tính tiền...");
            btnTinhTien.setDisable(true);
            btnXacNhan.setDisable(true);
            tinhTienResult[0] = null;

            Thread t = new Thread(() -> {
                try {
                    KhachHangDatSanDAO dao = new KhachHangDatSanDAO();

                    // ===== CHECK TRÙNG =====
                    boolean trung = dao.kiemTraTrungLich(sanChon.getMaSan(), ngay, gioBD, gioKT);

                    if (trung) {
                        Platform.runLater(() -> {
                            lblKetQua.setText("⚠ Khung giờ này đã có người đặt.");
                            btnTinhTien.setDisable(false);
                        });
                        return;
                    }

                    // ===== TÍNH TIỀN =====
                    TinhTienResult kq = dao.tinhTienDatSan(sanChon.getMaSan(), ngay, gioBD, gioKT, maGiam.isEmpty() ? null : maGiam);

                    Platform.runLater(() -> {
                        tinhTienResult[0] = kq;
                        String text = String.format(
                                """
                                ✔ %s
                                Tiền gốc: %,.0f đ
                                Giảm giá: %,.0f đ
                                Thanh toán: %,.0f đ
                                """,
                                kq.getGhiChuGia(),
                                kq.getTongTienGoc(),
                                kq.getTienGiam(),
                                kq.getTongTienThanhToan()
                        );
                        lblKetQua.setText(text);
                        btnXacNhan.setDisable(false);
                        btnTinhTien.setDisable(false);
                    });
                } catch (Exception ex) {
                    Platform.runLater(() -> {
                        lblKetQua.setText("⚠ " + ex.getMessage());
                        btnTinhTien.setDisable(false);
                    });
                }
            });
            t.setDaemon(true);
            t.start();
        });

        // ===== XÁC NHẬN ĐẶT =====
        btnXacNhan.setOnAction(e -> {
            SanKhachHangRow sanChon = cbSan.getValue();
            LocalDate ngay = dpNgay.getValue();
            String gioBD = txtGioBD.getText().trim();
            String gioKT = txtGioKT.getText().trim();
            String ghiChu = txtGhiChu.getText().trim();

            if (tinhTienResult[0] == null) {
                lblKetQua.setText("⚠ Vui lòng tính tiền trước.");
                return;
            }

            // ===== CONFIRM =====
            Alert confirm = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    String.format(
                            """
                            Xác nhận đặt sân:
                            
                            %s
                            Ngày: %s
                            Giờ: %s - %s
                            
                            Tổng tiền: %,.0f đ
                            """,
                            sanChon.getTenSan(),
                            ngay,
                            gioBD,
                            gioKT,
                            tinhTienResult[0].getTongTienThanhToan()
                    ),
                    ButtonType.YES,
                    ButtonType.NO
            );
            confirm.setTitle("Xác nhận đặt sân");

            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.YES) {
                    btnXacNhan.setDisable(true);
                    Thread t = new Thread(() -> {
                        try {
                            KhachHangDatSanDAO dao = new KhachHangDatSanDAO();
                            String maDatSan = dao.taoMoiDatSan(AppState.getMaKhachHang(), sanChon.getMaSan(), ngay, gioBD, gioKT, ghiChu, tinhTienResult[0]);

                            Platform.runLater(() -> {
                                new Alert(Alert.AlertType.INFORMATION, "Đặt sân thành công!\nMã đặt: " + maDatSan).showAndWait();

                                // ===== RESET =====
                                cbSan.setValue(null);
                                if (sanMacDinh != null) {
                                    cbSan.setValue(sanMacDinh);
                                }
                                dpNgay.setValue(LocalDate.now().plusDays(1));
                                txtGioBD.clear();
                                txtGioKT.clear();
                                txtMaGiam.clear();
                                txtGhiChu.clear();
                                lblKetQua.setText("");
                                tinhTienResult[0] = null;
                                btnXacNhan.setDisable(true);
                            });
                        } catch (Exception ex) {
                            Platform.runLater(() -> {
                                new Alert(Alert.AlertType.ERROR, "Đặt sân thất bại:\n" + ex.getMessage()).showAndWait();
                                btnXacNhan.setDisable(false);
                            });
                        }
                    });
                    t.setDaemon(true);
                    t.start();
                }
            });
        });

        // Tự động khóa nút Xác nhận nếu khách hàng cố tình sửa thông tin sau khi đã tính tiền
        cbSan.valueProperty().addListener((obs, oldV, newV) -> btnXacNhan.setDisable(true));
        dpNgay.valueProperty().addListener((obs, oldV, newV) -> btnXacNhan.setDisable(true));
        txtGioBD.textProperty().addListener((obs, oldV, newV) -> btnXacNhan.setDisable(true));
        txtGioKT.textProperty().addListener((obs, oldV, newV) -> btnXacNhan.setDisable(true));
        txtMaGiam.textProperty().addListener((obs, oldV, newV) -> btnXacNhan.setDisable(true));

        return root;
    }

    /**
     * Overload không có sân mặc định
     */
    public static Node createView() {
        return createView(null);
    }
}