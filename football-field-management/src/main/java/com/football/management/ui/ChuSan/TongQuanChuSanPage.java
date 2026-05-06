package com.football.management.ui.ChuSan;

import com.football.management.dao.BaoCaoDAO;
import com.football.management.model.BaoCaoTongQuan;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class TongQuanChuSanPage {

    public static Node createView() {
        BaoCaoDAO baoCaoDAO = new BaoCaoDAO();
        BaoCaoTongQuan baoCao = baoCaoDAO.layBaoCaoTheoNgay(LocalDate.now());

        VBox root = new VBox(20);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        Label lblTitle = new Label("Tổng quan chủ sân");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Theo dõi nhanh doanh thu, lịch đặt và tình hình hoạt động hôm nay");
        lblSubtitle.getStyleClass().add("section-subtitle");

        GridPane thongKeGrid = new GridPane();
        thongKeGrid.setHgap(20);
        thongKeGrid.setVgap(20);

        thongKeGrid.add(createStatCard("Doanh thu hôm nay", dinhDangTien(baoCao.getDoanhThu())), 0, 0);
        thongKeGrid.add(createStatCard("Lượt đặt hôm nay", String.valueOf(baoCao.getLuotDatSan())), 1, 0);
        thongKeGrid.add(createStatCard("Tổng số sân", String.valueOf(baoCao.getTongSoSan())), 2, 0);

        thongKeGrid.add(createStatCard("Sân đang sử dụng", String.valueOf(baoCao.getSoSanDangSuDung())), 0, 1);
        thongKeGrid.add(createStatCard("Số khách hàng", String.valueOf(baoCao.getSoKhachHang())), 1, 1);
        thongKeGrid.add(createStatCard("Tần suất sử dụng", dinhDangPhanTram(baoCao.getTanSuatSuDung())), 2, 1);

        VBox tinhHinhCard = new VBox(12);
        tinhHinhCard.getStyleClass().add("card");

        Label lblTinhHinh = new Label("Tình hình hôm nay");
        lblTinhHinh.getStyleClass().add("section-title");

        Label lblTinhHinhText = new Label(
                "- Ngày báo cáo: " + LocalDate.now() + "\n" +
                        "- Doanh thu hiện tại: " + dinhDangTien(baoCao.getDoanhThu()) + "\n" +
                        "- Lượt đặt trong ngày: " + baoCao.getLuotDatSan() + "\n" +
                        "- Số sân đang sử dụng: " + baoCao.getSoSanDangSuDung() + "/" + baoCao.getTongSoSan() + "\n" +
                        "- Mức độ khai thác sân: " + dinhDangPhanTram(baoCao.getTanSuatSuDung())
        );
        lblTinhHinhText.getStyleClass().add("section-subtitle");

        tinhHinhCard.getChildren().addAll(lblTinhHinh, lblTinhHinhText);

        VBox goiYCard = new VBox(12);
        goiYCard.getStyleClass().add("card");

        Label lblGoiY = new Label("Gợi ý quản lý");
        lblGoiY.getStyleClass().add("section-title");

        Label lblGoiYText = new Label(taoNoiDungGoiY(baoCao));
        lblGoiYText.getStyleClass().add("section-subtitle");

        goiYCard.getChildren().addAll(lblGoiY, lblGoiYText);

        VBox tacVuCard = new VBox(12);
        tacVuCard.getStyleClass().add("card");

        Label lblTacVu = new Label("Tác vụ nhanh");
        lblTacVu.getStyleClass().add("section-title");

        Label lblTacVuText = new Label("""
                - Kiểm tra sân đang bảo trì
                - Cập nhật bảng giá nếu có thay đổi khung giờ
                - Kiểm tra ưu đãi đang hoạt động
                - Theo dõi nhân viên đang làm việc
                - Xem báo cáo chi tiết theo ngày/tháng
                """);
        lblTacVuText.getStyleClass().add("section-subtitle");

        tacVuCard.getChildren().addAll(lblTacVu, lblTacVuText);

        root.getChildren().addAll(
                lblTitle,
                lblSubtitle,
                thongKeGrid,
                tinhHinhCard,
                goiYCard,
                tacVuCard
        );

        return root;
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

    private static String taoNoiDungGoiY(BaoCaoTongQuan baoCao) {
        StringBuilder sb = new StringBuilder();

        if (baoCao.getTongSoSan() == 0) {
            sb.append("- Chưa có sân nào trong hệ thống. Nên thêm sân bóng trước.\n");
        }

        if (baoCao.getTanSuatSuDung() < 30) {
            sb.append("- Tần suất sử dụng đang thấp. Nên kiểm tra lại bảng giá hoặc ưu đãi.\n");
        } else if (baoCao.getTanSuatSuDung() < 70) {
            sb.append("- Mức độ sử dụng sân đang ổn định. Có thể đẩy mạnh ưu đãi vào giờ vàng.\n");
        } else {
            sb.append("- Tần suất sử dụng sân cao. Cần theo dõi lịch và tình trạng sân sát hơn.\n");
        }

        if (baoCao.getLuotDatSan() == 0) {
            sb.append("- Hôm nay chưa có lượt đặt sân nào.\n");
        } else if (baoCao.getLuotDatSan() < 5) {
            sb.append("- Số lượt đặt hôm nay còn ít. Nên theo dõi hiệu quả đặt sân.\n");
        } else {
            sb.append("- Số lượt đặt hôm nay đang tốt.\n");
        }

        if (baoCao.getSoKhachHang() < 10) {
            sb.append("- Số khách hàng đang hoạt động còn thấp. Nên tăng chương trình thu hút khách.\n");
        } else {
            sb.append("- Tập khách hàng đang hoạt động ở mức ổn định.\n");
        }

        return sb.toString().trim();
    }
}