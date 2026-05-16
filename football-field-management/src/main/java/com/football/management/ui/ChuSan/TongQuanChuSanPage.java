package com.football.management.ui.ChuSan;

import com.football.management.dao.BaoCaoDAO;
import com.football.management.model.BaoCaoTongQuan;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TongQuanChuSanPage {

    public static Node createView() {
        BaoCaoDAO baoCaoDAO = new BaoCaoDAO();
        BaoCaoTongQuan bc = baoCaoDAO.layBaoCaoTheoNgay(LocalDate.now());

        // ══════════════════════════════════════════
        //  ROOT
        // ══════════════════════════════════════════
        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: #f0f2f5;");

        // ── Topbar ──
        HBox topbar = buildTopbar();

        // ── Stat strip (4 cards) ──
        HBox statRow = buildStatRow(bc);

        // ── Body (padding) ──
        VBox body = new VBox(16);
        body.setPadding(new Insets(0, 24, 24, 24));

        // ── Row 1: Tình hình + Biểu đồ placeholder ──
        HBox row1 = new HBox(16);

        VBox tinhHinhCard = buildTinhHinhCard(bc);
        HBox.setHgrow(tinhHinhCard, Priority.ALWAYS);

        VBox goiYCard = buildGoiYCard(bc);
        goiYCard.setPrefWidth(320);
        goiYCard.setMinWidth(280);

        row1.getChildren().addAll(tinhHinhCard, goiYCard);

        // ── Row 2: Tác vụ nhanh ──
        VBox tacVuCard = buildTacVuCard();

        body.getChildren().addAll(row1, tacVuCard);
        root.getChildren().addAll(topbar, statRow, body);
        return root;
    }

    // ══════════════════════════════════════════════
    //  TOPBAR
    // ══════════════════════════════════════════════
    private static HBox buildTopbar() {
        HBox bar = new HBox(10);
        bar.getStyleClass().add("topbar");
        bar.setAlignment(Pos.CENTER_LEFT);

        Label icon = new Label("⊞");
        icon.setStyle("-fx-font-size: 18px; -fx-text-fill: #3b82f6;");

        Label title = new Label("Tổng quan");
        title.getStyleClass().add("topbar-title");

        String today = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Label badge = new Label("Hôm nay, " + today);
        badge.getStyleClass().add("count-badge");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        bar.getChildren().addAll(icon, title, badge, sp);
        return bar;
    }

    // ══════════════════════════════════════════════
    //  STAT CARDS ROW (4 thẻ)
    // ══════════════════════════════════════════════
    private static HBox buildStatRow(BaoCaoTongQuan bc) {
        HBox row = new HBox(12);
        row.setPadding(new Insets(16, 24, 16, 24));

        row.getChildren().addAll(
                statCard("💰", "stat-icon-green",
                        dinhDangTien(bc.getDoanhThu()), "Doanh thu hôm nay"),
                statCard("📅", "stat-icon-blue",
                        String.valueOf(bc.getLuotDatSan()), "Lượt đặt sân"),
                statCard("⚽", "stat-icon-amber",
                        bc.getSoSanDangSuDung() + "/" + bc.getTongSoSan(), "Sân đang hoạt động"),
                statCard("👥", "stat-icon-red",
                        String.valueOf(bc.getSoKhachHang()), "Khách hàng")
        );

        return row;
    }

    private static HBox statCard(String icon, String iconCss,
                                 String value, String label) {
        Label ico = new Label(icon);
        ico.setStyle("-fx-font-size: 18px;");
        ico.getStyleClass().add(iconCss);
        ico.setMinWidth(42); ico.setMinHeight(42);
        ico.setAlignment(Pos.CENTER);

        Label lblVal = new Label(value);
        lblVal.getStyleClass().add("stat-value");

        Label lblLbl = new Label(label);
        lblLbl.getStyleClass().add("stat-label");

        VBox info = new VBox(2, lblVal, lblLbl);
        info.setAlignment(Pos.CENTER_LEFT);

        HBox card = new HBox(12, ico, info);
        card.getStyleClass().add("stat-card");
        card.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    // ══════════════════════════════════════════════
    //  TÌNH HÌNH HÔM NAY
    // ══════════════════════════════════════════════
    private static VBox buildTinhHinhCard(BaoCaoTongQuan bc) {
        VBox card = new VBox(16);
        card.getStyleClass().add("card");

        Label title = new Label("📊  Tình hình hôm nay");
        title.getStyleClass().add("section-title");

        // Grid các chỉ số
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(10);

        String[][] rows = {
                {"Ngày báo cáo",    LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))},
                {"Doanh thu",       dinhDangTien(bc.getDoanhThu())},
                {"Lượt đặt sân",    String.valueOf(bc.getLuotDatSan())},
                {"Sân đang dùng",   bc.getSoSanDangSuDung() + " / " + bc.getTongSoSan() + " sân"},
                {"Khách hàng",      String.valueOf(bc.getSoKhachHang()) + " người"},
                {"Tần suất khai thác", dinhDangPhanTram(bc.getTanSuatSuDung())},
        };

        for (int i = 0; i < rows.length; i++) {
            Label k = new Label(rows[i][0]);
            k.getStyleClass().add("field-label");
            k.setMinWidth(140);

            Label v = new Label(rows[i][1]);
            v.getStyleClass().add("form-label");
            v.setStyle("-fx-font-weight: 600; -fx-text-fill: #1a1f2e;");

            // Progress bar cho tần suất
            if (i == rows.length - 1) {
                double pct = Math.min(bc.getTanSuatSuDung(), 100.0) / 100.0;
                HBox barWrap = new HBox();
                barWrap.setStyle("-fx-background-color: #f0f2f5; -fx-background-radius: 999;");
                barWrap.setPrefHeight(6); barWrap.setMinWidth(200);

                Region fill = new Region();
                fill.setPrefHeight(6);
                fill.setPrefWidth(pct * 200);
                String barColor = pct >= 0.7 ? "#16a34a" : pct >= 0.3 ? "#3b82f6" : "#f59e0b";
                fill.setStyle("-fx-background-color: " + barColor + "; -fx-background-radius: 999;");

                barWrap.getChildren().add(fill);

                HBox valRow = new HBox(10, v, barWrap);
                valRow.setAlignment(Pos.CENTER_LEFT);
                grid.add(k, 0, i);
                grid.add(valRow, 1, i);
            } else {
                grid.add(k, 0, i);
                grid.add(v, 1, i);
            }

            // Separator nhẹ
            if (i < rows.length - 1) {
                Region sep = new Region();
                sep.setStyle("-fx-background-color: rgba(0,0,0,0.04);");
                sep.setPrefHeight(1);
                GridPane.setColumnSpan(sep, 2);
                grid.add(sep, 0, i);  // overridden below, just style it
            }
        }

        card.getChildren().addAll(title, grid);
        return card;
    }

    // ══════════════════════════════════════════════
    //  GỢI Ý QUẢN LÝ
    // ══════════════════════════════════════════════
    private static VBox buildGoiYCard(BaoCaoTongQuan bc) {
        VBox card = new VBox(12);
        card.getStyleClass().add("card");

        Label title = new Label("💡  Gợi ý quản lý");
        title.getStyleClass().add("section-title");

        VBox list = new VBox(8);

        for (GoiY gy : taoGoiY(bc)) {
            HBox item = new HBox(10);
            item.setAlignment(Pos.TOP_LEFT);
            item.setPadding(new Insets(8, 10, 8, 10));
            item.setStyle("-fx-background-color: " + gy.bg + ";"
                    + "-fx-background-radius: 10;");

            Label dot = new Label(gy.icon);
            dot.setStyle("-fx-font-size: 14px;");

            Label text = new Label(gy.text);
            text.setStyle("-fx-font-size: 12px; -fx-text-fill: " + gy.color + "; -fx-wrap-text: true;");
            text.setMaxWidth(240);
            HBox.setHgrow(text, Priority.ALWAYS);

            item.getChildren().addAll(dot, text);
            list.getChildren().add(item);
        }

        card.getChildren().addAll(title, list);
        return card;
    }

    private record GoiY(String icon, String text, String bg, String color) {}

    private static java.util.List<GoiY> taoGoiY(BaoCaoTongQuan bc) {
        java.util.List<GoiY> list = new java.util.ArrayList<>();

        if (bc.getTongSoSan() == 0) {
            list.add(new GoiY("⚠", "Chưa có sân nào. Hãy thêm sân trước.",
                    "#fef2f2", "#b91c1c"));
        }

        if (bc.getTanSuatSuDung() < 30) {
            list.add(new GoiY("📉", "Tần suất thấp. Cân nhắc điều chỉnh bảng giá hoặc thêm ưu đãi.",
                    "#fffbeb", "#b45309"));
        } else if (bc.getTanSuatSuDung() < 70) {
            list.add(new GoiY("✅", "Mức sử dụng ổn định. Có thể đẩy ưu đãi giờ vàng.",
                    "#f0fdf4", "#15803d"));
        } else {
            list.add(new GoiY("🔥", "Tần suất cao. Theo dõi sát tình trạng sân.",
                    "#eff6ff", "#1d4ed8"));
        }

        if (bc.getLuotDatSan() == 0) {
            list.add(new GoiY("📭", "Hôm nay chưa có lượt đặt.",
                    "#fef2f2", "#b91c1c"));
        } else if (bc.getLuotDatSan() < 5) {
            list.add(new GoiY("📌", "Số lượt đặt còn ít. Theo dõi hiệu quả đặt sân.",
                    "#fffbeb", "#b45309"));
        } else {
            list.add(new GoiY("🎯", "Lượt đặt hôm nay đang tốt.",
                    "#f0fdf4", "#15803d"));
        }

        if (bc.getSoKhachHang() < 10) {
            list.add(new GoiY("👥", "Khách hàng còn ít. Nên tăng chương trình thu hút.",
                    "#fffbeb", "#b45309"));
        } else {
            list.add(new GoiY("🌟", "Tập khách hàng ở mức ổn định.",
                    "#f0fdf4", "#15803d"));
        }

        return list;
    }

    // ══════════════════════════════════════════════
    //  TÁC VỤ NHANH
    // ══════════════════════════════════════════════
    private static VBox buildTacVuCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("card");

        Label title = new Label("⚡  Tác vụ nhanh");
        title.getStyleClass().add("section-title");

        String[][] tasks = {
                {"🔧", "Kiểm tra sân đang bảo trì",              "#fffbeb", "#b45309"},
                {"💰", "Cập nhật bảng giá nếu có thay đổi",      "#eff6ff", "#1d4ed8"},
                {"🎁", "Kiểm tra ưu đãi đang hoạt động",         "#f0fdf4", "#15803d"},
                {"👤", "Theo dõi nhân viên đang làm việc",        "#f5f3ff", "#6d28d9"},
                {"📊", "Xem báo cáo chi tiết theo ngày / tháng", "#fef2f2", "#b91c1c"},
        };

        // Hiển thị dạng lưới 5 cột
        HBox taskRow = new HBox(10);

        for (String[] t : tasks) {
            HBox item = new HBox(8);
            item.setAlignment(Pos.CENTER_LEFT);
            item.setPadding(new Insets(10, 14, 10, 14));
            item.setStyle("-fx-background-color: " + t[2] + ";"
                    + "-fx-background-radius: 10; -fx-cursor: hand;");
            HBox.setHgrow(item, Priority.ALWAYS);

            Label ico = new Label(t[0]);
            ico.setStyle("-fx-font-size: 16px;");

            Label lbl = new Label(t[1]);
            lbl.setStyle("-fx-font-size: 12px; -fx-font-weight: 500;"
                    + "-fx-text-fill: " + t[3] + "; -fx-wrap-text: true;");
            lbl.setMaxWidth(150);

            item.getChildren().addAll(ico, lbl);
            taskRow.getChildren().add(item);
        }

        card.getChildren().addAll(title, taskRow);
        return card;
    }

    // ══════════════════════════════════════════════
    //  FORMAT HELPERS
    // ══════════════════════════════════════════════
    private static String dinhDangTien(BigDecimal soTien) {
        if (soTien == null) return "0 VND";
        return new DecimalFormat("#,###").format(soTien) + " VND";
    }

    private static String dinhDangPhanTram(double v) {
        return String.format("%.1f%%", v);
    }
}