package com.football.management.ui.KhachHang;

import com.football.management.app.AppState;
import com.football.management.dao.KhachHangDatSanDAO.SanKhachHangRow;
import com.football.management.dao.SanYeuThichDAO;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class ChiTietSanPage {

    /**
     * @param san       Sân được chọn từ DanhSachSanPage
     * @param onDatSan  Callback để navigate sang DatSanPage
     */
    public static Node createView(SanKhachHangRow san, Consumer<SanKhachHangRow> onDatSan) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        // ===== NULL CHECK =====
        if (san == null) {
            root.getChildren().add(new Label("Vui lòng chọn sân từ danh sách."));
            return root;
        }

        // ===== TITLE =====
        Label lblTitle = new Label("Chi tiết sân");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Thông tin chi tiết của sân được chọn");
        lblSubtitle.getStyleClass().add("section-subtitle");

        // ===== CARD =====
        VBox card = new VBox(12);
        card.getStyleClass().add("card");

        // ===== TÊN SÂN =====
        Label lblTenSan = new Label(san.getMaSanCode() + " – " + san.getTenSan());
        lblTenSan.getStyleClass().add("section-title");

        // ===== TRẠNG THÁI =====
        String trangThaiHienThi = switch (san.getTrangThaiSan() == null ? "" : san.getTrangThaiSan()) {
            case "SAN_SANG" -> "Sẵn sàng";
            case "DANG_SU_DUNG" -> "Đang sử dụng";
            case "BAO_TRI" -> "Bảo trì";
            default -> san.getTrangThaiSan();
        };

        // ===== GIỜ MỞ CỬA =====
        String gioMo = san.getGioMoCua() == null ? "" : san.getGioMoCua();
        String gioDong = san.getGioDongCua() == null ? "" : san.getGioDongCua();
        String gioCua = (gioMo.isEmpty() && gioDong.isEmpty()) ? "Chưa cập nhật" : gioMo + " - " + gioDong;

        // ===== VỊ TRÍ =====
        String viTri = san.getViTri() == null || san.getViTri().isBlank() ? "Chưa cập nhật" : san.getViTri();

        // ===== NỘI DUNG =====
        Label lblNoiDung = new Label(
                "Loại sân    : " + san.getTenLoaiSan() + "\n" +
                "Giá/giờ     : " + san.getGiaHienThi() + "\n" +
                "Vị trí      : " + viTri + "\n" +
                "Trạng thái  : " + trangThaiHienThi + "\n" +
                "Mở cửa      : " + gioCua
        );
        lblNoiDung.getStyleClass().add("section-subtitle");
        lblNoiDung.setStyle("-fx-font-family: monospace;");

        // ===== ACTIONS =====
        HBox actions = new HBox(10);

        // ===== BUTTON ĐẶT SÂN =====
        Button btnDatSan = new Button("Đặt sân");
        btnDatSan.getStyleClass().add("primary-button");
        btnDatSan.setDisable(!"SAN_SANG".equals(san.getTrangThaiSan()));

        // ===== BUTTON YÊU THÍCH =====
        Button btnYeuThich = new Button("Thêm yêu thích");
        btnYeuThich.getStyleClass().add("light-button");

        // ===== BUTTON QUAY LẠI =====
        Button btnQuayLai = new Button("← Quay lại");
        btnQuayLai.getStyleClass().add("light-button");

        // ===== ACTION ĐẶT SÂN =====
        btnDatSan.setOnAction(e -> {
            if (!"SAN_SANG".equals(san.getTrangThaiSan())) {
                new Alert(Alert.AlertType.WARNING, "Sân hiện không khả dụng.").showAndWait();
                return;
            }
            if (onDatSan != null) {
                onDatSan.accept(san);
            }
        });

        // ===== ACTION YÊU THÍCH =====
        btnYeuThich.setOnAction(e -> {
            btnYeuThich.setDisable(true);
            Thread t = new Thread(() -> {
                try {
                    SanYeuThichDAO dao = new SanYeuThichDAO();
                    boolean ok = dao.themYeuThich(AppState.getMaKhachHang(), san.getMaSan());

                    Platform.runLater(() -> {
                        if (ok) {
                            btnYeuThich.setText("Đã yêu thích ♥");
                            btnYeuThich.setDisable(true);
                        } else {
                            new Alert(Alert.AlertType.INFORMATION, "Sân đã có trong danh sách yêu thích.").showAndWait();
                            btnYeuThich.setDisable(false);
                        }
                    });
                } catch (Exception ex) {
                    Platform.runLater(() -> {
                        new Alert(Alert.AlertType.ERROR, "Lỗi thêm yêu thích: " + ex.getMessage()).showAndWait();
                        btnYeuThich.setDisable(false);
                    });
                }
            });
            t.setDaemon(true);
            t.start();
        });

        // ===== ACTION QUAY LẠI =====
        btnQuayLai.setOnAction(e -> {
            // Nếu dùng HomePage dynamic content thì callback navigate ngoài xử lý.
            root.getScene().getWindow().hide();
        });

        // ===== ADD UI ELEMENTS =====
        actions.getChildren().addAll(btnDatSan, btnYeuThich, btnQuayLai);
        card.getChildren().addAll(lblTenSan, lblNoiDung, actions);
        root.getChildren().addAll(lblTitle, lblSubtitle, card);

        return root;
    }

    /**
     * Overload không callback
     */
    public static Node createView(SanKhachHangRow san) {
        return createView(san, null);
    }
}