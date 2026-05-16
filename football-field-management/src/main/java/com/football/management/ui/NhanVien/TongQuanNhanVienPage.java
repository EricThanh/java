package com.football.management.ui.NhanVien;

import com.football.management.dao.ThongKeNhanVienDAO;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class TongQuanNhanVienPage {

    private static final ThongKeNhanVienDAO thongKeDAO = new ThongKeNhanVienDAO();

    public static Node createView() {
        VBox khungChinh = new VBox(18);
        khungChinh.setPadding(new Insets(24));
        khungChinh.getStyleClass().add("content-root");

        Label tieuDe = new Label("Tổng quan nhân viên");
        tieuDe.getStyleClass().add("page-title");

        Label moTa = new Label("Theo dõi nhanh tình hình đặt sân, thanh toán, khách hàng và trạng thái sân");
        moTa.getStyleClass().add("section-subtitle");

        GridPane luoiThongKe = new GridPane();
        luoiThongKe.setHgap(16);
        luoiThongKe.setVgap(16);

        int donHomNay = 0;
        int donDangSuDung = 0;
        int donCanThanhToan = 0;
        int khachHangHoatDong = 0;
        int sanDangSuDung = 0;
        int tongSan = 0;

        try {
            donHomNay = thongKeDAO.demDonDatSanHomNay();
            donDangSuDung = thongKeDAO.demDonDangSuDung();
            donCanThanhToan = thongKeDAO.demDonCanThanhToan();
            khachHangHoatDong = thongKeDAO.demKhachHangHoatDong();
            sanDangSuDung = thongKeDAO.demSanDangSuDung();
            tongSan = thongKeDAO.demTongSan();

        } catch (SQLException e) {
            e.printStackTrace();

            Label loi = new Label("Không thể tải số liệu tổng quan từ cơ sở dữ liệu.");
            loi.getStyleClass().add("section-subtitle");

            khungChinh.getChildren().addAll(tieuDe, moTa, loi);
            return khungChinh;
        }

        luoiThongKe.add(
                taoTheThongKe("Đơn đặt sân hôm nay", String.valueOf(donHomNay), "Đơn chưa hủy trong ngày hiện tại"),
                0,
                0
        );

        luoiThongKe.add(
                taoTheThongKe("Đơn đang sử dụng", String.valueOf(donDangSuDung), "Các đơn đã check-in"),
                1,
                0
        );

        luoiThongKe.add(
                taoTheThongKe("Đơn cần thanh toán", String.valueOf(donCanThanhToan), "Đơn chưa thanh toán"),
                2,
                0
        );

        luoiThongKe.add(
                taoTheThongKe("Khách hàng hoạt động", String.valueOf(khachHangHoatDong), "Khách hàng còn hoạt động"),
                0,
                1
        );

        luoiThongKe.add(
                taoTheThongKe("Sân đang sử dụng", sanDangSuDung + " / " + tongSan, "Số sân đang có khách sử dụng"),
                1,
                1
        );

        luoiThongKe.add(
                taoTheThongKe("Tổng số sân", String.valueOf(tongSan), "Tất cả sân trong hệ thống"),
                2,
                1
        );

        Label goiY = new Label(
                "Gợi ý: Nhân viên nên kiểm tra lịch sân, xử lý đơn đang sử dụng và xác nhận thanh toán cho các đơn còn tồn."
        );
        goiY.getStyleClass().add("section-subtitle");
        goiY.setWrapText(true);

        khungChinh.getChildren().addAll(
                tieuDe,
                moTa,
                luoiThongKe,
                goiY
        );

        return khungChinh;
    }

    private static VBox taoTheThongKe(String tieuDe, String giaTri, String moTa) {
        VBox the = new VBox(8);
        the.setPadding(new Insets(18));
        the.setPrefWidth(250);
        the.setMinHeight(130);
        the.getStyleClass().add("booking-stat-card");

        Label lblTieuDe = new Label(tieuDe);
        lblTieuDe.getStyleClass().add("stat-title");
        lblTieuDe.setWrapText(true);

        Label lblGiaTri = new Label(giaTri);
        lblGiaTri.getStyleClass().add("stat-value");

        Label lblMoTa = new Label(moTa);
        lblMoTa.getStyleClass().add("section-subtitle");
        lblMoTa.setWrapText(true);

        the.getChildren().addAll(lblTieuDe, lblGiaTri, lblMoTa);

        return the;
    }
}