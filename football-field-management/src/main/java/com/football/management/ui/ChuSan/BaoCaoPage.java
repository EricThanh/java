package com.football.management.ui.ChuSan;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BaoCaoPage {

    public static Node createView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("content-root");

        Label lblTitle = new Label("Bao cao thong ke");
        lblTitle.getStyleClass().add("page-title");

        Label lblSubtitle = new Label("Tong hop doanh thu, luot dat san va tinh hinh kinh doanh");
        lblSubtitle.getStyleClass().add("section-subtitle");

        GridPane thongKeGrid = new GridPane();
        thongKeGrid.setHgap(20);
        thongKeGrid.setVgap(20);

        thongKeGrid.add(createStatCard("Doanh thu thang", "120.000.000"), 0, 0);
        thongKeGrid.add(createStatCard("Luot dat san", "356"), 1, 0);
        thongKeGrid.add(createStatCard("So khach hang", "210"), 0, 1);
        thongKeGrid.add(createStatCard("Tan suat su dung", "82%"), 1, 1);

        HBox reportSections = new HBox(20);

        VBox doanhThuCard = new VBox(10);
        doanhThuCard.getStyleClass().add("card");
        Label lblDoanhThu = new Label("Thong ke doanh thu");
        lblDoanhThu.getStyleClass().add("section-title");
        Label txtDoanhThu = new Label("""
                - Doanh thu hom nay: 4.500.000
                - Doanh thu tuan nay: 28.000.000
                - Doanh thu thang nay: 120.000.000
                """);
        txtDoanhThu.getStyleClass().add("section-subtitle");
        doanhThuCard.getChildren().addAll(lblDoanhThu, txtDoanhThu);

        VBox suDungCard = new VBox(10);
        suDungCard.getStyleClass().add("card");
        Label lblSuDung = new Label("Tinh trang su dung san");
        lblSuDung.getStyleClass().add("section-title");
        Label txtSuDung = new Label("""
                - San A: su dung cao
                - San B: on dinh
                - San C: bao tri trong 2 ngay
                """);
        txtSuDung.getStyleClass().add("section-subtitle");
        suDungCard.getChildren().addAll(lblSuDung, txtSuDung);

        reportSections.getChildren().addAll(doanhThuCard, suDungCard);

        VBox exportCard = new VBox(12);
        exportCard.getStyleClass().add("card");

        Label lblExport = new Label("Xuat bao cao");
        lblExport.getStyleClass().add("section-title");

        Label lblExportDesc = new Label("Xuat bao cao doanh thu, lich dat san hoac thong ke khach hang");
        lblExportDesc.getStyleClass().add("section-subtitle");

        javafx.scene.control.Button btnXuat = new javafx.scene.control.Button("Xuat bao cao");
        btnXuat.getStyleClass().add("primary-button");

        exportCard.getChildren().addAll(lblExport, lblExportDesc, btnXuat);

        root.getChildren().addAll(lblTitle, lblSubtitle, thongKeGrid, reportSections, exportCard);
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
}