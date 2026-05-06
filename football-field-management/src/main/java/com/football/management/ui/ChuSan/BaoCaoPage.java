package com.football.management.ui.ChuSan;

import com.football.management.dao.BaoCaoDAO;
import com.football.management.model.BaoCaoTongQuan;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Year;

public class BaoCaoPage {

    public static Node createView() {
        BaoCaoDAO baoCaoDAO = new BaoCaoDAO();

        VBox root = new VBox(20);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        Label lblTitle = new Label("Báo cáo thống kê");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Báo cáo theo ngày hoặc theo tháng");
        lblSubtitle.getStyleClass().add("section-subtitle");

        VBox filterCard = new VBox(16);
        filterCard.getStyleClass().add("card");

        Label lblFilterTitle = new Label("Bộ lọc báo cáo");
        lblFilterTitle.getStyleClass().add("section-title");

        HBox filterBar = new HBox(10);

        ComboBox<String> cbLoaiBaoCao = new ComboBox<>();
        cbLoaiBaoCao.getItems().addAll("Theo ngày", "Theo tháng");
        cbLoaiBaoCao.setValue("Theo ngày");

        DatePicker dpNgay = new DatePicker(LocalDate.now());

        ComboBox<Integer> cbThang = new ComboBox<>();
        for (int i = 1; i <= 12; i++) {
            cbThang.getItems().add(i);
        }
        cbThang.setValue(LocalDate.now().getMonthValue());

        ComboBox<Integer> cbNam = new ComboBox<>();
        int namHienTai = Year.now().getValue();
        for (int i = namHienTai - 5; i <= namHienTai + 2; i++) {
            cbNam.getItems().add(i);
        }
        cbNam.setValue(namHienTai);

        Button btnXemBaoCao = new Button("Xem báo cáo");
        btnXemBaoCao.getStyleClass().add("primary-button");

        filterBar.getChildren().addAll(cbLoaiBaoCao, dpNgay, cbThang, cbNam, btnXemBaoCao);
        filterCard.getChildren().addAll(lblFilterTitle, filterBar);

        VBox contentBox = new VBox(20);

        Runnable hienThiBaoCaoTheoNgay = () -> {
            BaoCaoTongQuan baoCao = baoCaoDAO.layBaoCaoTheoNgay(dpNgay.getValue());
            contentBox.getChildren().setAll(taoNoiDungBaoCao(baoCao, "Báo cáo theo ngày: " + dpNgay.getValue()));
        };

        Runnable hienThiBaoCaoTheoThang = () -> {
            BaoCaoTongQuan baoCao = baoCaoDAO.layBaoCaoTheoThang(cbThang.getValue(), cbNam.getValue());
            contentBox.getChildren().setAll(
                    taoNoiDungBaoCao(baoCao, "Báo cáo theo tháng " + cbThang.getValue() + "/" + cbNam.getValue())
            );
        };

        cbLoaiBaoCao.setOnAction(e -> {
            boolean theoNgay = "Theo ngày".equals(cbLoaiBaoCao.getValue());
            dpNgay.setDisable(!theoNgay);
            cbThang.setDisable(theoNgay);
            cbNam.setDisable(theoNgay);
        });

        btnXemBaoCao.setOnAction(e -> {
            try {
                if ("Theo ngày".equals(cbLoaiBaoCao.getValue())) {
                    if (dpNgay.getValue() == null) {
                        hienLoi("Vui lòng chọn ngày");
                        return;
                    }
                    hienThiBaoCaoTheoNgay.run();
                } else {
                    if (cbThang.getValue() == null || cbNam.getValue() == null) {
                        hienLoi("Vui lòng chọn tháng và năm");
                        return;
                    }
                    hienThiBaoCaoTheoThang.run();
                }
            } catch (Exception ex) {
                hienLoi(ex.getMessage());
            }
        });

        dpNgay.setDisable(false);
        cbThang.setDisable(true);
        cbNam.setDisable(true);

        hienThiBaoCaoTheoNgay.run();

        root.getChildren().addAll(lblTitle, lblSubtitle, filterCard, contentBox);
        return root;
    }

    private static VBox taoNoiDungBaoCao(BaoCaoTongQuan baoCao, String tieuDePhu) {
        VBox wrapper = new VBox(20);

        Label lblPhu = new Label(tieuDePhu);
        lblPhu.getStyleClass().add("section-title");

        GridPane thongKeGrid = new GridPane();
        thongKeGrid.setHgap(20);
        thongKeGrid.setVgap(20);

        thongKeGrid.add(createStatCard("Doanh thu", dinhDangTien(baoCao.getDoanhThu())), 0, 0);
        thongKeGrid.add(createStatCard("Lượt đặt sân", String.valueOf(baoCao.getLuotDatSan())), 1, 0);
        thongKeGrid.add(createStatCard("Số khách hàng", String.valueOf(baoCao.getSoKhachHang())), 0, 1);
        thongKeGrid.add(createStatCard("Tổng số sân", String.valueOf(baoCao.getTongSoSan())), 1, 1);

        GridPane thongKeThemGrid = new GridPane();
        thongKeThemGrid.setHgap(20);
        thongKeThemGrid.setVgap(20);

        thongKeThemGrid.add(createStatCard("Sân đang sử dụng", String.valueOf(baoCao.getSoSanDangSuDung())), 0, 0);
        thongKeThemGrid.add(createStatCard("Tần suất sử dụng", dinhDangPhanTram(baoCao.getTanSuatSuDung())), 1, 0);

        VBox doanhThuCard = new VBox(10);
        doanhThuCard.getStyleClass().add("card");

        Label lblDoanhThu = new Label("Thống kê doanh thu");
        lblDoanhThu.getStyleClass().add("section-title");

        Label txtDoanhThu = new Label(
                "- Khoảng thời gian: " + baoCao.getTuNgay() + " đến " + baoCao.getDenNgay() + "\n" +
                        "- Tổng doanh thu: " + dinhDangTien(baoCao.getDoanhThu())
        );
        txtDoanhThu.getStyleClass().add("section-subtitle");
        doanhThuCard.getChildren().addAll(lblDoanhThu, txtDoanhThu);

        VBox suDungCard = new VBox(10);
        suDungCard.getStyleClass().add("card");

        Label lblSuDung = new Label("Tình trạng sử dụng sân");
        lblSuDung.getStyleClass().add("section-title");

        Label txtSuDung = new Label(
                "- Tổng số sân: " + baoCao.getTongSoSan() + "\n" +
                        "- Sân đang sử dụng: " + baoCao.getSoSanDangSuDung() + "\n" +
                        "- Tần suất sử dụng: " + dinhDangPhanTram(baoCao.getTanSuatSuDung())
        );
        txtSuDung.getStyleClass().add("section-subtitle");
        suDungCard.getChildren().addAll(lblSuDung, txtSuDung);

        VBox khachHangCard = new VBox(10);
        khachHangCard.getStyleClass().add("card");

        Label lblKhachHang = new Label("Thống kê đặt sân và khách hàng");
        lblKhachHang.getStyleClass().add("section-title");

        Label txtKhachHang = new Label(
                "- Số khách hàng đang hoạt động: " + baoCao.getSoKhachHang() + "\n" +
                        "- Lượt đặt sân trong kỳ: " + baoCao.getLuotDatSan()
        );
        txtKhachHang.getStyleClass().add("section-subtitle");
        khachHangCard.getChildren().addAll(lblKhachHang, txtKhachHang);

        wrapper.getChildren().addAll(
                lblPhu,
                thongKeGrid,
                thongKeThemGrid,
                doanhThuCard,
                suDungCard,
                khachHangCard
        );

        return wrapper;
    }

    private static VBox createStatCard(String title, String value) {
        VBox card = new VBox(10);
        card.getStyleClass().add("stat-card");

        Label lblTitle = new Label(title);
        lblTitle.getStyleClass().add("stat-label");

        Label lblValue = new Label(value);
        lblValue.getStyleClass().add("stat-value");

        card.getChildren().addAll(lblTitle, lblValue);
        return card;
    }

    private static String dinhDangTien(BigDecimal soTien) {
        if (soTien == null) {
            return "0 VND";
        }
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(soTien) + " VND";
    }

    private static String dinhDangPhanTram(double giaTri) {
        return String.format("%.2f%%", giaTri);
    }

    private static void hienLoi(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Lỗi");
        alert.setContentText(message);
        alert.showAndWait();
    }
}