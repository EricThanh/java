package com.football.management.dao;

import com.football.management.config.DBConnection;
import com.football.management.model.SanBong;
import com.football.management.model.UuDai;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO chuyên phục vụ các chức năng phía khách hàng:
 * - Xem danh sách sân
 * - Đặt sân, tính tiền
 * - Lịch sử đặt sân, hủy đơn
 * - Thống kê trang chủ
 */
public class KhachHangDatSanDAO {

    // ===================== DTOs =====================

    public static class SanKhachHangRow {
        private final int maSan;
        private final String maSanCode;
        private final String tenSan;
        private final String tenLoaiSan;
        private final String viTri;
        private final String gioMoCua;
        private final String gioDongCua;
        private final String trangThaiSan;
        private final BigDecimal giaMoiGioHienTai; // null nếu chưa có bảng giá

        public SanKhachHangRow(int maSan, String maSanCode, String tenSan,
                               String tenLoaiSan, String viTri,
                               String gioMoCua, String gioDongCua,
                               String trangThaiSan, BigDecimal giaMoiGioHienTai) {
            this.maSan = maSan;
            this.maSanCode = maSanCode;
            this.tenSan = tenSan;
            this.tenLoaiSan = tenLoaiSan;
            this.viTri = viTri;
            this.gioMoCua = gioMoCua;
            this.gioDongCua = gioDongCua;
            this.trangThaiSan = trangThaiSan;
            this.giaMoiGioHienTai = giaMoiGioHienTai;
        }

        public int getMaSan() { return maSan; }
        public String getMaSanCode() { return maSanCode; }
        public String getTenSan() { return tenSan; }
        public String getTenLoaiSan() { return tenLoaiSan; }
        public String getViTri() { return viTri == null ? "" : viTri; }
        public String getGioMoCua() { return gioMoCua == null ? "" : gioMoCua; }
        public String getGioDongCua() { return gioDongCua == null ? "" : gioDongCua; }
        public String getTrangThaiSan() { return trangThaiSan; }
        public BigDecimal getGiaMoiGioHienTai() { return giaMoiGioHienTai; }
        public String getGiaHienThi() {
            if (giaMoiGioHienTai == null) return "Liên hệ";
            return String.format("%,.0f đ/giờ", giaMoiGioHienTai);
        }
        @Override public String toString() { return maSanCode + " - " + tenSan; }
    }

    public static class LichSuDatSanKHRow {
        private final int maDatSan;
        private final String maDatSanCode;
        private final String tenSan;
        private final String ngayDat;
        private final String khungGio;
        private String trangThaiDatSan;
        private final String trangThaiThanhToan;
        private final BigDecimal tongTienThanhToan;

        public LichSuDatSanKHRow(int maDatSan, String maDatSanCode, String tenSan,
                                 String ngayDat, String khungGio,
                                 String trangThaiDatSan, String trangThaiThanhToan,
                                 BigDecimal tongTienThanhToan) {
            this.maDatSan = maDatSan;
            this.maDatSanCode = maDatSanCode;
            this.tenSan = tenSan;
            this.ngayDat = ngayDat;
            this.khungGio = khungGio;
            this.trangThaiDatSan = trangThaiDatSan;
            this.trangThaiThanhToan = trangThaiThanhToan;
            this.tongTienThanhToan = tongTienThanhToan;
        }

        public int getMaDatSan() { return maDatSan; }
        public String getMaDatSanCode() { return maDatSanCode; }
        public String getTenSan() { return tenSan; }
        public String getNgayDat() { return ngayDat; }
        public String getKhungGio() { return khungGio; }
        public String getTrangThaiDatSan() { return trangThaiDatSan; }
        public String getTrangThaiThanhToan() { return trangThaiThanhToan; }
        public BigDecimal getTongTienThanhToan() { return tongTienThanhToan; }
        public String getTongTienHienThi() {
            if (tongTienThanhToan == null) return "0 đ";
            return String.format("%,.0f đ", tongTienThanhToan);
        }
        public boolean coTheHuy() {
            return "CHO_XU_LY".equals(trangThaiDatSan);
        }

        public void setTrangThaiDatSan(String string) {
            this.trangThaiDatSan = string;
        }
    }

    public static class TinhTienResult {
        private final BigDecimal tongTienGoc;
        private final BigDecimal tienGiam;
        private final BigDecimal tongTienThanhToan;
        private final Integer maUuDai;
        private final String ghiChuGia;

        public TinhTienResult(BigDecimal tongTienGoc, BigDecimal tienGiam,
                              BigDecimal tongTienThanhToan, Integer maUuDai, String ghiChuGia) {
            this.tongTienGoc = tongTienGoc;
            this.tienGiam = tienGiam;
            this.tongTienThanhToan = tongTienThanhToan;
            this.maUuDai = maUuDai;
            this.ghiChuGia = ghiChuGia;
        }

        public BigDecimal getTongTienGoc() { return tongTienGoc; }
        public BigDecimal getTienGiam() { return tienGiam; }
        public BigDecimal getTongTienThanhToan() { return tongTienThanhToan; }
        public Integer getMaUuDai() { return maUuDai; }
        public String getGhiChuGia() { return ghiChuGia; }
    }

    public static class ThongKeKhachHang {
        public int sanTrongHomNay;
        public int soUuDaiDangCo;
        public String lanDatGanNhat;    // null nếu chưa có
        public int soSanYeuThich;
        public int tongDonDatSan;
    }

    // ===================== DANH SÁCH SÂN =====================

    /** Lấy tất cả sân đang hoạt động (SAN_SANG + DANG_SU_DUNG) kèm giá hiện tại */
    public List<SanKhachHangRow> layDanhSachSanChoKhachHang() throws SQLException {
        List<SanKhachHangRow> ds = new ArrayList<>();

        String sql = """
                SELECT sb.ma_san,
                       sb.ma_san_code,
                       sb.ten_san,
                       ls.ten_loai_san,
                       NVL(sb.vi_tri, '') AS vi_tri,
                       NVL(sb.gio_mo_cua, '') AS gio_mo_cua,
                       NVL(sb.gio_dong_cua, '') AS gio_dong_cua,
                       sb.trang_thai_san,
                       (
                           SELECT MIN(bg.gia_moi_gio)
                           FROM bang_gia bg
                           WHERE bg.ma_san = sb.ma_san
                             AND bg.dang_ap_dung = 1
                             AND bg.ngay_ap_dung_tu <= TRUNC(SYSDATE)
                             AND (bg.ngay_ap_dung_den IS NULL OR bg.ngay_ap_dung_den >= TRUNC(SYSDATE))
                       ) AS gia_hien_tai
                FROM san_bong sb
                JOIN loai_san ls ON sb.ma_loai_san = ls.ma_loai_san
                WHERE sb.trang_thai_san NOT IN ('NGUNG_HOAT_DONG', 'BAO_TRI')
                ORDER BY sb.ma_san
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                BigDecimal gia = rs.getBigDecimal("gia_hien_tai");
                ds.add(new SanKhachHangRow(
                        rs.getInt("ma_san"),
                        rs.getString("ma_san_code"),
                        rs.getString("ten_san"),
                        rs.getString("ten_loai_san"),
                        rs.getString("vi_tri"),
                        rs.getString("gio_mo_cua"),
                        rs.getString("gio_dong_cua"),
                        rs.getString("trang_thai_san"),
                        rs.wasNull() ? null : gia
                ));
            }
        }
        return ds;
    }

    /** Lấy chi tiết một sân theo maSan */
    public SanKhachHangRow layChiTietSan(int maSan) throws SQLException {
        String sql = """
                SELECT sb.ma_san,
                       sb.ma_san_code,
                       sb.ten_san,
                       ls.ten_loai_san,
                       NVL(sb.vi_tri, '') AS vi_tri,
                       NVL(sb.gio_mo_cua, '') AS gio_mo_cua,
                       NVL(sb.gio_dong_cua, '') AS gio_dong_cua,
                       sb.trang_thai_san,
                       (
                           SELECT MIN(bg.gia_moi_gio)
                           FROM bang_gia bg
                           WHERE bg.ma_san = sb.ma_san
                             AND bg.dang_ap_dung = 1
                             AND bg.ngay_ap_dung_tu <= TRUNC(SYSDATE)
                             AND (bg.ngay_ap_dung_den IS NULL OR bg.ngay_ap_dung_den >= TRUNC(SYSDATE))
                       ) AS gia_hien_tai
                FROM san_bong sb
                JOIN loai_san ls ON sb.ma_loai_san = ls.ma_loai_san
                WHERE sb.ma_san = ?
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BigDecimal gia = rs.getBigDecimal("gia_hien_tai");
                    return new SanKhachHangRow(
                            rs.getInt("ma_san"),
                            rs.getString("ma_san_code"),
                            rs.getString("ten_san"),
                            rs.getString("ten_loai_san"),
                            rs.getString("vi_tri"),
                            rs.getString("gio_mo_cua"),
                            rs.getString("gio_dong_cua"),
                            rs.getString("trang_thai_san"),
                            rs.wasNull() ? null : gia
                    );
                }
            }
        }
        return null;
    }

    /** Đếm sân trống (SAN_SANG) hôm nay */
    public int demSanTrongHomNay() throws SQLException {
        String sql = "SELECT COUNT(*) AS so_luong FROM san_bong WHERE trang_thai_san = 'SAN_SANG'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("so_luong");
        }
        return 0;
    }

    // ===================== TÍNH TIỀN =====================

    /**
     * Tính tiền đặt sân:
     * 1. Tìm bảng giá phù hợp với sân + ngày + khung giờ
     * 2. Tính số giờ * giá mỗi giờ
     * 3. Áp dụng mã ưu đãi nếu có
     */
    public TinhTienResult tinhTienDatSan(int maSan, LocalDate ngayDat,
                                         String gioBatDau, String gioKetThuc,
                                         String maGiamGia) throws SQLException {
        // Lấy giá mỗi giờ theo bảng giá phù hợp nhất
        String sqlGia = """
                SELECT gia_moi_gio
                FROM bang_gia
                WHERE ma_san = ?
                  AND dang_ap_dung = 1
                  AND ngay_ap_dung_tu <= ?
                  AND (ngay_ap_dung_den IS NULL OR ngay_ap_dung_den >= ?)
                  AND (
                      thu_trong_tuan IS NULL
                      OR thu_trong_tuan = TO_NUMBER(TO_CHAR(?, 'D'))
                  )
                AND TO_DATE(gio_bat_dau, 'HH24:MI') <= TO_DATE(?, 'HH24:MI')
                AND TO_DATE(gio_ket_thuc, 'HH24:MI') >= TO_DATE(?, 'HH24:MI')
                ORDER BY thu_trong_tuan DESC NULLS LAST, gia_moi_gio ASC
                FETCH FIRST 1 ROWS ONLY
                """;

        BigDecimal giaMoiGio = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlGia)) {
            Date sqlNgay = Date.valueOf(ngayDat);
            ps.setInt(1, maSan);
            ps.setDate(2, sqlNgay);
            ps.setDate(3, sqlNgay);
            ps.setDate(4, sqlNgay);
            ps.setString(5, gioBatDau);
            ps.setString(6, gioKetThuc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) giaMoiGio = rs.getBigDecimal("gia_moi_gio");
            }
        }

        if (giaMoiGio == null) {
            throw new SQLException("Không tìm thấy bảng giá cho sân và khung giờ này.");
        }

        // Tính số giờ
        double soGio = tinhSoGio(gioBatDau, gioKetThuc);
        if (soGio <= 0) throw new SQLException("Giờ kết thúc phải sau giờ bắt đầu.");

        BigDecimal tongTienGoc = giaMoiGio.multiply(BigDecimal.valueOf(soGio))
                .setScale(0, RoundingMode.HALF_UP);
        BigDecimal tienGiam = BigDecimal.ZERO;
        Integer maUuDai = null;
        String ghiChu = String.format("%.1f giờ x %,.0f đ/giờ", soGio, giaMoiGio);

        // Áp dụng mã giảm giá nếu có
        if (maGiamGia != null && !maGiamGia.trim().isEmpty()) {
            UuDai uuDai = timUuDaiTheoMa(maGiamGia.trim(), ngayDat, gioBatDau, tongTienGoc);
            if (uuDai == null) {
                throw new SQLException("Mã giảm giá không hợp lệ, đã hết hạn hoặc không đủ điều kiện.");
            }
            maUuDai = uuDai.getMaUuDai();
            if ("PHAN_TRAM".equals(uuDai.getLoaiGiamGia())) {
                tienGiam = tongTienGoc.multiply(uuDai.getGiaTriGiam())
                        .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
            } else {
                tienGiam = uuDai.getGiaTriGiam().min(tongTienGoc);
            }
            ghiChu += " | Giảm: " + uuDai.getTenUuDai();
        }

        BigDecimal tongTienThanhToan = tongTienGoc.subtract(tienGiam)
                .max(BigDecimal.ZERO);

        return new TinhTienResult(tongTienGoc, tienGiam, tongTienThanhToan, maUuDai, ghiChu);
    }

    private UuDai timUuDaiTheoMa(String maGiamGia, LocalDate ngayDat,
                                 String gioBatDau, BigDecimal tongTienGoc) throws SQLException {
        String sql = """
                SELECT ma_uu_dai, ma_giam_gia, ten_uu_dai, loai_giam_gia,
                       gia_tri_giam, gia_tri_dat_toi_thieu, ngay_bat_dau,
                       ngay_ket_thuc, gio_bat_dau, gio_ket_thuc, dang_hoat_dong
                FROM uu_dai
                WHERE UPPER(ma_giam_gia) = UPPER(?)
                  AND dang_hoat_dong = 1
                  AND ngay_bat_dau <= ?
                  AND ngay_ket_thuc >= ?
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maGiamGia);
            ps.setDate(2, Date.valueOf(ngayDat));
            ps.setDate(3, Date.valueOf(ngayDat));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UuDai ud = new UuDai();
                    ud.setMaUuDai(rs.getInt("ma_uu_dai"));
                    ud.setMaGiamGia(rs.getString("ma_giam_gia"));
                    ud.setTenUuDai(rs.getString("ten_uu_dai"));
                    ud.setLoaiGiamGia(rs.getString("loai_giam_gia"));
                    ud.setGiaTriGiam(rs.getBigDecimal("gia_tri_giam"));
                    ud.setGiaTriDatToiThieu(rs.getBigDecimal("gia_tri_dat_toi_thieu"));

                    // Kiểm tra giá trị tối thiểu
                    if (ud.getGiaTriDatToiThieu() != null
                            && tongTienGoc.compareTo(ud.getGiaTriDatToiThieu()) < 0) {
                        return null; // không đủ điều kiện
                    }

                    // Kiểm tra khung giờ ưu đãi nếu có
                    String gioUuDaiBD = rs.getString("gio_bat_dau");
                    String gioUuDaiKT = rs.getString("gio_ket_thuc");

                    if (gioUuDaiBD != null && gioUuDaiKT != null) {
                        // Chuẩn hóa độ dài chuỗi để compareTo() hoạt động đúng
                        // Ví dụ: "8:00" -> "08:00", "10:00" giữ nguyên
                        String hDat = gioBatDau.length() == 4 ? "0" + gioBatDau : gioBatDau;
                        String hUuDaiBD = gioUuDaiBD.length() == 4 ? "0" + gioUuDaiBD : gioUuDaiBD;
                        String hUuDaiKT = gioUuDaiKT.length() == 4 ? "0" + gioUuDaiKT : gioUuDaiKT;

                        if (hDat.compareTo(hUuDaiBD) < 0 || hDat.compareTo(hUuDaiKT) >= 0) {
                            return null; // ngoài khung giờ ưu đãi
                        }
                    }
                    return ud;
                }
            }
        }
        return null;
    }

    private double tinhSoGio(String gioBatDau, String gioKetThuc) {
        try {
            String[] batDau = gioBatDau.split(":");
            String[] ketThuc = gioKetThuc.split(":");
            int phutBD = Integer.parseInt(batDau[0]) * 60 + Integer.parseInt(batDau[1]);
            int phutKT = Integer.parseInt(ketThuc[0]) * 60 + Integer.parseInt(ketThuc[1]);
            return (phutKT - phutBD) / 60.0;
        } catch (Exception e) {
            return -1;
        }
    }

    // ===================== ĐẶT SÂN =====================

    /** Kiểm tra slot giờ có bị trùng với đơn khác không */
    public boolean kiemTraTrungLich(int maSan, LocalDate ngayDat,
                                    String gioBatDau, String gioKetThuc) throws SQLException {
        String sql = """
                SELECT COUNT(*) AS so_luong
                FROM dat_san
                WHERE ma_san = ?
                  AND ngay_dat = ?
                  AND trang_thai_dat_san NOT IN ('DA_HUY')
                  AND TO_DATE(?, 'HH24:MI') < TO_DATE(gio_ket_thuc, 'HH24:MI')
                  AND TO_DATE(?, 'HH24:MI') > TO_DATE(gio_bat_dau, 'HH24:MI')
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maSan);
            ps.setDate(2, Date.valueOf(ngayDat));

            // Tham số 3: Kiểm tra giờ bắt đầu nhập vào có trước giờ kết thúc trong DB không
            ps.setString(3, gioBatDau.trim());

            // Tham số 4: Kiểm tra giờ kết thúc nhập vào có sau giờ bắt đầu trong DB không
            ps.setString(4, gioKetThuc.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("so_luong") > 0;
            }
        }
        return false;
    }

    /** Tạo đơn đặt sân mới */
    public String taoMoiDatSan(int maKhachHang, int maSan,
                               LocalDate ngayDat, String gioBatDau, String gioKetThuc,
                               String ghiChu, TinhTienResult tinhTien) throws SQLException {

        // Sinh mã đặt sân tự động
        String maDatSanCode = taoMaDatSanTuDong();

        String sql = """
                INSERT INTO dat_san (
                    ma_dat_san_code,
                    ma_khach_hang,
                    ma_san,
                    ngay_dat,
                    gio_bat_dau,
                    gio_ket_thuc,
                    trang_thai_dat_san,
                    ghi_chu,
                    tong_tien_goc,
                    tien_giam,
                    tong_tien_thanh_toan,
                    ma_uu_dai,
                    trang_thai_thanh_toan,
                    ngay_tao,
                    ngay_cap_nhat
                ) VALUES (?, ?, ?, ?, ?, ?, 'CHO_XU_LY', ?, ?, ?, ?, ?, 'CHUA_THANH_TOAN', SYSDATE, SYSDATE)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDatSanCode);
            ps.setInt(2, maKhachHang);
            ps.setInt(3, maSan);
            ps.setDate(4, Date.valueOf(ngayDat));
            ps.setString(5, gioBatDau.trim());
            ps.setString(6, gioKetThuc.trim());
            ps.setString(7, ghiChu == null ? "" : ghiChu.trim());
            ps.setBigDecimal(8, tinhTien.getTongTienGoc());
            ps.setBigDecimal(9, tinhTien.getTienGiam());
            ps.setBigDecimal(10, tinhTien.getTongTienThanhToan());
            if (tinhTien.getMaUuDai() != null) {
                ps.setInt(11, tinhTien.getMaUuDai());
            } else {
                ps.setNull(11, Types.INTEGER);
            }
            ps.executeUpdate();
        }
        return maDatSanCode;
    }

    private String taoMaDatSanTuDong() throws SQLException {
        String sql = """
                SELECT NVL(MAX(TO_NUMBER(REGEXP_SUBSTR(ma_dat_san_code, '[0-9]+'))), 0) + 1 AS ma_tiep_theo
                FROM dat_san
                WHERE REGEXP_LIKE(ma_dat_san_code, '^DS[0-9]+$')
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return String.format("DS%04d", rs.getInt("ma_tiep_theo"));
            }
        }
        return "DS0001";
    }

    // ===================== LỊCH SỬ ĐẶT SÂN =====================

    /** Lấy lịch sử đặt sân của khách, có thể lọc theo trạng thái */
    public List<LichSuDatSanKHRow> layLichSuDatSan(int maKhachHang,
                                                   String trangThaiFilter) throws SQLException {
        List<LichSuDatSanKHRow> ds = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                SELECT ds.ma_dat_san,
                       ds.ma_dat_san_code,
                       sb.ten_san,
                       TO_CHAR(ds.ngay_dat, 'DD/MM/YYYY') AS ngay_dat,
                       ds.gio_bat_dau || ' - ' || ds.gio_ket_thuc AS khung_gio,
                       ds.trang_thai_dat_san,
                       ds.trang_thai_thanh_toan,
                       ds.tong_tien_thanh_toan
                FROM dat_san ds
                JOIN san_bong sb ON ds.ma_san = sb.ma_san
                WHERE ds.ma_khach_hang = ?
                """);

        if (trangThaiFilter != null && !trangThaiFilter.isEmpty()) {
            sql.append(" AND ds.trang_thai_dat_san = ?");
        }
        sql.append(" ORDER BY ds.ngay_tao DESC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            ps.setInt(1, maKhachHang);
            if (trangThaiFilter != null && !trangThaiFilter.isEmpty()) {
                ps.setString(2, trangThaiFilter);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(new LichSuDatSanKHRow(
                            rs.getInt("ma_dat_san"),
                            rs.getString("ma_dat_san_code"),
                            rs.getString("ten_san"),
                            rs.getString("ngay_dat"),
                            rs.getString("khung_gio"),
                            rs.getString("trang_thai_dat_san"),
                            rs.getString("trang_thai_thanh_toan"),
                            rs.getBigDecimal("tong_tien_thanh_toan")
                    ));
                }
            }
        }
        return ds;
    }

    /** Hủy đơn đặt sân (chỉ cho phép hủy đơn CHO_XU_LY) */
    public boolean huyDatSanCuaKhach(int maKhachHang, String maDatSanCode) throws SQLException {
        String sql = """
                UPDATE dat_san
                SET trang_thai_dat_san = 'DA_HUY',
                    ngay_cap_nhat = SYSDATE
                WHERE ma_dat_san_code = ?
                  AND ma_khach_hang = ?
                  AND trang_thai_dat_san = 'CHO_XU_LY'
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDatSanCode);
            ps.setInt(2, maKhachHang);
            return ps.executeUpdate() > 0;
        }
    }

    // ===================== THỐNG KÊ TRANG CHỦ =====================

    public ThongKeKhachHang layThongKeKhachHang(int maKhachHang) throws SQLException {
        ThongKeKhachHang tk = new ThongKeKhachHang();

        // Sân trống hôm nay
        tk.sanTrongHomNay = demSanTrongHomNay();

        // Số ưu đãi đang hoạt động
        String sqlUuDai = """
                SELECT COUNT(*) AS so_luong FROM uu_dai
                WHERE dang_hoat_dong = 1
                  AND ngay_bat_dau <= TRUNC(SYSDATE)
                  AND ngay_ket_thuc >= TRUNC(SYSDATE)
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlUuDai);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) tk.soUuDaiDangCo = rs.getInt("so_luong");
        }

        // Lần đặt gần nhất
        String sqlGanNhat = """
                SELECT TO_CHAR(ds.ngay_dat, 'DD/MM/YYYY') || ' | '
                       || sb.ten_san AS lan_dat_gan_nhat
                FROM dat_san ds
                JOIN san_bong sb ON ds.ma_san = sb.ma_san
                WHERE ds.ma_khach_hang = ?
                ORDER BY ds.ngay_tao DESC
                FETCH FIRST 1 ROWS ONLY
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlGanNhat)) {
            ps.setInt(1, maKhachHang);
            try (ResultSet rs = ps.executeQuery()) {
                tk.lanDatGanNhat = rs.next() ? rs.getString("lan_dat_gan_nhat") : "Chưa có";
            }
        }

        // Số sân yêu thích
        String sqlYeuThich = "SELECT COUNT(*) AS so_luong FROM san_yeu_thich WHERE ma_khach_hang = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlYeuThich)) {
            ps.setInt(1, maKhachHang);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) tk.soSanYeuThich = rs.getInt("so_luong");
            }
        }

        // Tổng số đơn đặt sân
        String sqlTongDon = "SELECT COUNT(*) AS so_luong FROM dat_san WHERE ma_khach_hang = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlTongDon)) {
            ps.setInt(1, maKhachHang);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) tk.tongDonDatSan = rs.getInt("so_luong");
            }
        }

        return tk;
    }

    // ===================== ƯU ĐÃI =====================

    public List<UuDai> layUuDaiHienTai() throws SQLException {
        List<UuDai> ds = new ArrayList<>();
        String sql = """
                SELECT ma_uu_dai, ma_giam_gia, ten_uu_dai, mo_ta,
                       loai_giam_gia, gia_tri_giam, gia_tri_dat_toi_thieu,
                       ngay_bat_dau, ngay_ket_thuc, gio_bat_dau, gio_ket_thuc
                FROM uu_dai
                WHERE dang_hoat_dong = 1
                  AND ngay_bat_dau <= TRUNC(SYSDATE)
                  AND ngay_ket_thuc >= TRUNC(SYSDATE)
                ORDER BY ngay_ket_thuc ASC
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                UuDai ud = new UuDai();
                ud.setMaUuDai(rs.getInt("ma_uu_dai"));
                ud.setMaGiamGia(rs.getString("ma_giam_gia"));
                ud.setTenUuDai(rs.getString("ten_uu_dai"));
                ud.setMoTa(rs.getString("mo_ta"));
                ud.setLoaiGiamGia(rs.getString("loai_giam_gia"));
                ud.setGiaTriGiam(rs.getBigDecimal("gia_tri_giam"));
                ud.setGiaTriDatToiThieu(rs.getBigDecimal("gia_tri_dat_toi_thieu"));
                Date bd = rs.getDate("ngay_bat_dau");
                Date kt = rs.getDate("ngay_ket_thuc");
                if (bd != null) ud.setNgayBatDau(bd.toLocalDate());
                if (kt != null) ud.setNgayKetThuc(kt.toLocalDate());
                ud.setGioBatDau(rs.getString("gio_bat_dau"));
                ud.setGioKetThuc(rs.getString("gio_ket_thuc"));
                ds.add(ud);
            }
        }
        return ds;
    }
}