
CREATE OR REPLACE TRIGGER TRG_GIOBATDAU_GIOKETTHUC_DATSAN_in_up
BEFORE INSERT OR UPDATE OF gio_bat_dau, gio_ket_thuc ON dat_san
    FOR EACH ROW
BEGIN
    IF (:new.gio_bat_dau >= :new.gio_ket_thuc) THEN
        RAISE_APPLICATION_ERROR(-20001,
            'Gio bat dau phai nho hon gio ket thuc trong dat_san');
END IF;
END;
/

-- ------------------------------------------------------------
-- 2.2 TRG_GIOBATDAU_GIOKETTHUC_BANGGIA_in_up
--     Kiểm tra gio_bat_dau < gio_ket_thuc khi INSERT hoặc UPDATE bang_gia
-- ------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_GIOBATDAU_GIOKETTHUC_BANGGIA_in_up
BEFORE INSERT OR UPDATE OF gio_bat_dau, gio_ket_thuc ON bang_gia
    FOR EACH ROW
BEGIN
    IF (:new.gio_bat_dau >= :new.gio_ket_thuc) THEN
        RAISE_APPLICATION_ERROR(-20002,
            'Gio bat dau phai nho hon gio ket thuc trong bang_gia');
END IF;
END;
/

-- ------------------------------------------------------------
-- 2.3 TRG_TRANGTHAISAN_DATSAN_in
--     Kiểm tra sân có trạng thái SAN_SANG trước khi đặt
-- ------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_TRANGTHAISAN_DATSAN_in
BEFORE INSERT ON dat_san
FOR EACH ROW
DECLARE
v_trang_thai_san san_bong.trang_thai_san%TYPE;
BEGIN
SELECT trang_thai_san INTO v_trang_thai_san
FROM san_bong
WHERE ma_san = :new.ma_san;

IF (v_trang_thai_san != 'SAN_SANG') THEN
        RAISE_APPLICATION_ERROR(-20003,
            'San bong khong o trang thai SAN_SANG, khong the dat san');
END IF;
END;
/

-- ------------------------------------------------------------
-- 2.4 TRG_TRANGTHAISAN_DATSAN_up
--     Tự động cập nhật trang_thai_san khi trạng thái đặt sân thay đổi
-- ------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_TRANGTHAISAN_DATSAN_up
AFTER UPDATE OF trang_thai_dat_san ON dat_san
    FOR EACH ROW
BEGIN
    IF (:new.trang_thai_dat_san = 'DA_XAC_NHAN'
        AND :old.trang_thai_dat_san != 'DA_XAC_NHAN') THEN
UPDATE san_bong
SET trang_thai_san = 'DANG_SU_DUNG',
    ngay_cap_nhat  = SYSDATE
WHERE ma_san = :new.ma_san;

ELSIF (:new.trang_thai_dat_san IN ('DA_HOAN_THANH', 'DA_HUY')) THEN
UPDATE san_bong
SET trang_thai_san = 'SAN_SANG',
    ngay_cap_nhat  = SYSDATE
WHERE ma_san = :new.ma_san;
END IF;
END;
/

-- ------------------------------------------------------------
-- 2.5 TRG_TONGTIEN_DATSAN_in_up
--     Tự động tính tong_tien_goc, tien_giam, tong_tien_thanh_toan
--     khi INSERT hoặc UPDATE dat_san
-- ------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_TONGTIEN_DATSAN_in_up
BEFORE INSERT OR UPDATE OF gio_bat_dau, gio_ket_thuc, ma_uu_dai ON dat_san
    FOR EACH ROW
DECLARE
v_gia_moi_gio       bang_gia.gia_moi_gio%TYPE := 0;
    v_so_gio            NUMBER := 0;
    v_tong_tien_goc     NUMBER := 0;
    v_tien_giam         NUMBER := 0;
    v_loai_giam_gia     uu_dai.loai_giam_gia%TYPE;
    v_gia_tri_giam      uu_dai.gia_tri_giam%TYPE;
    v_gia_tri_toi_thieu uu_dai.gia_tri_dat_toi_thieu%TYPE;
BEGIN
BEGIN
SELECT gia_moi_gio INTO v_gia_moi_gio
FROM bang_gia
WHERE ma_san       = :new.ma_san
  AND dang_ap_dung = 1
  AND gio_bat_dau  <= :new.gio_bat_dau
  AND gio_ket_thuc >= :new.gio_ket_thuc
  AND ROWNUM = 1;
EXCEPTION
        WHEN NO_DATA_FOUND THEN v_gia_moi_gio := 0;
END;

    v_so_gio := ( TO_NUMBER(SUBSTR(:new.gio_ket_thuc, 1, 2)) * 60
                + TO_NUMBER(SUBSTR(:new.gio_ket_thuc, 4, 2))
                - TO_NUMBER(SUBSTR(:new.gio_bat_dau,  1, 2)) * 60
                - TO_NUMBER(SUBSTR(:new.gio_bat_dau,  4, 2)) ) / 60;

    v_tong_tien_goc := v_so_gio * v_gia_moi_gio;

    IF (:new.ma_uu_dai IS NOT NULL) THEN
SELECT loai_giam_gia, gia_tri_giam, gia_tri_dat_toi_thieu
INTO   v_loai_giam_gia, v_gia_tri_giam, v_gia_tri_toi_thieu
FROM   uu_dai
WHERE  ma_uu_dai      = :new.ma_uu_dai
  AND  dang_hoat_dong = 1;

IF (v_tong_tien_goc >= v_gia_tri_toi_thieu) THEN
            IF (v_loai_giam_gia = 'PHAN_TRAM') THEN
                v_tien_giam := v_tong_tien_goc * v_gia_tri_giam / 100;
            ELSIF (v_loai_giam_gia = 'SO_TIEN_CO_DINH') THEN
                v_tien_giam := v_gia_tri_giam;
END IF;
END IF;
END IF;

    :new.tong_tien_goc        := v_tong_tien_goc;
    :new.tien_giam            := v_tien_giam;
    :new.tong_tien_thanh_toan := v_tong_tien_goc - v_tien_giam;
    :new.ngay_cap_nhat        := SYSDATE;
END;
/

-- ------------------------------------------------------------
-- 2.6 TRG_TONGTIEN_BANGGIA_up
--     Tự động tính lại tong_tien trong dat_san khi gia_moi_gio thay đổi
-- ------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_TONGTIEN_BANGGIA_up
AFTER UPDATE OF gia_moi_gio ON bang_gia
    FOR EACH ROW
DECLARE
CURSOR cur_dat_san IS
SELECT ma_dat_san, gio_bat_dau, gio_ket_thuc, ma_uu_dai
FROM   dat_san
WHERE  ma_san             = :new.ma_san
  AND  trang_thai_dat_san NOT IN ('DA_HUY', 'DA_HOAN_THANH')
  AND  gio_bat_dau        >= :new.gio_bat_dau
  AND  gio_ket_thuc       <= :new.gio_ket_thuc;

v_so_gio            NUMBER;
    v_tong_tien_goc     NUMBER;
    v_tien_giam         NUMBER := 0;
    v_loai_giam_gia     uu_dai.loai_giam_gia%TYPE;
    v_gia_tri_giam      uu_dai.gia_tri_giam%TYPE;
    v_gia_tri_toi_thieu uu_dai.gia_tri_dat_toi_thieu%TYPE;
BEGIN
FOR rec IN cur_dat_san LOOP
        v_so_gio := ( TO_NUMBER(SUBSTR(rec.gio_ket_thuc, 1, 2)) * 60
                    + TO_NUMBER(SUBSTR(rec.gio_ket_thuc, 4, 2))
                    - TO_NUMBER(SUBSTR(rec.gio_bat_dau,  1, 2)) * 60
                    - TO_NUMBER(SUBSTR(rec.gio_bat_dau,  4, 2)) ) / 60;

        v_tong_tien_goc := v_so_gio * :new.gia_moi_gio;
        v_tien_giam     := 0;

        IF (rec.ma_uu_dai IS NOT NULL) THEN
BEGIN
SELECT loai_giam_gia, gia_tri_giam, gia_tri_dat_toi_thieu
INTO   v_loai_giam_gia, v_gia_tri_giam, v_gia_tri_toi_thieu
FROM   uu_dai
WHERE  ma_uu_dai      = rec.ma_uu_dai
  AND  dang_hoat_dong = 1;

IF (v_tong_tien_goc >= v_gia_tri_toi_thieu) THEN
                    IF (v_loai_giam_gia = 'PHAN_TRAM') THEN
                        v_tien_giam := v_tong_tien_goc * v_gia_tri_giam / 100;
                    ELSIF (v_loai_giam_gia = 'SO_TIEN_CO_DINH') THEN
                        v_tien_giam := v_gia_tri_giam;
END IF;
END IF;
EXCEPTION
                WHEN NO_DATA_FOUND THEN v_tien_giam := 0;
END;
END IF;

UPDATE dat_san
SET tong_tien_goc        = v_tong_tien_goc,
    tien_giam            = v_tien_giam,
    tong_tien_thanh_toan = v_tong_tien_goc - v_tien_giam,
    ngay_cap_nhat        = SYSDATE
WHERE ma_dat_san = rec.ma_dat_san;
END LOOP;
END;
/

-- ------------------------------------------------------------
-- 2.7 TRG_LICHSU_TRANGTHAI_DATSAN_up
--     Tự động ghi lịch sử thay đổi trạng thái đặt sân
-- ------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_LICHSU_TRANGTHAI_DATSAN_up
AFTER UPDATE OF trang_thai_dat_san ON dat_san
    FOR EACH ROW
BEGIN
    IF (:old.trang_thai_dat_san != :new.trang_thai_dat_san) THEN
        INSERT INTO lich_su_trang_thai_dat_san (
            ma_dat_san,
            trang_thai_cu,
            trang_thai_moi,
            thoi_gian_thay_doi,
            ghi_chu
        ) VALUES (
            :new.ma_dat_san,
            :old.trang_thai_dat_san,
            :new.trang_thai_dat_san,
            SYSDATE,
            'He thong tu dong ghi nhan thay doi trang thai'
        );
END IF;
END;
/

-- ------------------------------------------------------------
-- 2.8 TRG_KIEMTRA_UUDAI_DATSAN_in_up
--     Kiểm tra ưu đãi còn hiệu lực trước khi áp dụng vào đặt sân
-- ------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_KIEMTRA_UUDAI_DATSAN_in_up
BEFORE INSERT OR UPDATE OF ma_uu_dai ON dat_san
    FOR EACH ROW
DECLARE
v_dang_hoat_dong uu_dai.dang_hoat_dong%TYPE;
    v_ngay_bat_dau   uu_dai.ngay_bat_dau%TYPE;
    v_ngay_ket_thuc  uu_dai.ngay_ket_thuc%TYPE;
BEGIN
    IF (:new.ma_uu_dai IS NOT NULL) THEN
SELECT dang_hoat_dong, ngay_bat_dau, ngay_ket_thuc
INTO   v_dang_hoat_dong, v_ngay_bat_dau, v_ngay_ket_thuc
FROM   uu_dai
WHERE  ma_uu_dai = :new.ma_uu_dai;

IF (v_dang_hoat_dong = 0) THEN
            RAISE_APPLICATION_ERROR(-20004,
                'Uu dai nay da ngung hoat dong, khong the ap dung');
END IF;

        IF (:new.ngay_dat < v_ngay_bat_dau OR :new.ngay_dat > v_ngay_ket_thuc) THEN
            RAISE_APPLICATION_ERROR(-20005,
                'Ngay dat san nam ngoai thoi gian hieu luc cua uu dai');
END IF;
END IF;
END;
/

-- ------------------------------------------------------------
-- 2.9 TRG_TRANGTHAITT_DATSAN_THANHTOAN_in_up
--     Tự động cập nhật trang_thai_thanh_toan trong dat_san
--     sau khi INSERT hoặc UPDATE bảng thanh_toan
-- ------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_TRANGTHAITT_DATSAN_THANHTOAN_in_up
AFTER INSERT OR UPDATE OF trang_thai_thanh_toan ON thanh_toan
    FOR EACH ROW
DECLARE
v_trang_thai_moi VARCHAR2(20);
BEGIN
    IF (:new.trang_thai_thanh_toan = 'THANH_CONG') THEN
        v_trang_thai_moi := 'DA_THANH_TOAN';
    ELSIF (:new.trang_thai_thanh_toan IN ('CHO_XU_LY', 'CHO_XAC_NHAN')) THEN
        v_trang_thai_moi := 'THANH_TOAN_MOT_PHAN';
    ELSIF (:new.trang_thai_thanh_toan = 'THAT_BAI') THEN
        v_trang_thai_moi := 'CHUA_THANH_TOAN';
END IF;

UPDATE dat_san
SET trang_thai_thanh_toan = v_trang_thai_moi,
    ngay_cap_nhat         = SYSDATE
WHERE ma_dat_san = :new.ma_dat_san;
END;
/

-- ------------------------------------------------------------
-- 2.10 TRG_TAOHODON_THANHTOAN_in_up
--      Tự động tạo hóa đơn khi thanh toán thành công
-- ------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_TAOHODON_THANHTOAN_in_up
AFTER INSERT OR UPDATE OF trang_thai_thanh_toan ON thanh_toan
    FOR EACH ROW
DECLARE
v_so_hoa_don      NUMBER;
    v_ma_hoa_don_code hoa_don.ma_hoa_don_code%TYPE;
BEGIN
    IF (:new.trang_thai_thanh_toan = 'THANH_CONG') THEN
SELECT NVL(MAX(ma_hoa_don), 0) + 1 INTO v_so_hoa_don FROM hoa_don;
v_ma_hoa_don_code := 'HD' || LPAD(v_so_hoa_don, 6, '0');

INSERT INTO hoa_don (
    ma_hoa_don_code,
    ma_thanh_toan,
    ngay_xuat,
    tong_tien,
    ghi_chu
) VALUES (
             v_ma_hoa_don_code,
             :new.ma_thanh_toan,
             SYSDATE,
             :new.so_tien_thanh_toan,
             'Hoa don duoc tao tu dong sau khi thanh toan thanh cong'
         );
END IF;
END;
/


-- 3.1.1 Tên Procedure: sp_ThemDatSan
-- ------------------------------------------------------------
CREATE OR REPLACE PROCEDURE sp_ThemDatSan(
    v_ma_khach_hang_in  IN dat_san.ma_khach_hang%TYPE,
    v_ma_san_in         IN dat_san.ma_san%TYPE,
    v_ngay_dat_in       IN dat_san.ngay_dat%TYPE,
    v_gio_bat_dau_in    IN dat_san.gio_bat_dau%TYPE,
    v_gio_ket_thuc_in   IN dat_san.gio_ket_thuc%TYPE,
    v_ma_uu_dai_in      IN dat_san.ma_uu_dai%TYPE DEFAULT NULL,
    v_ma_nhan_vien_in   IN dat_san.ma_nhan_vien_tao%TYPE DEFAULT NULL,
    v_ghi_chu_in        IN dat_san.ghi_chu%TYPE DEFAULT NULL
)
AS
    v_trang_thai_san     san_bong.trang_thai_san%TYPE;
    v_gia_moi_gio        bang_gia.gia_moi_gio%TYPE := 0;
    v_so_gio             NUMBER := 0;
    v_tong_tien_goc      NUMBER := 0;
    v_tien_giam          NUMBER := 0;
    v_loai_giam_gia      uu_dai.loai_giam_gia%TYPE;
    v_gia_tri_giam       uu_dai.gia_tri_giam%TYPE;
    v_gia_tri_toi_thieu  uu_dai.gia_tri_dat_toi_thieu%TYPE;
    v_so_dat_san         NUMBER;
    v_ma_dat_san_code    dat_san.ma_dat_san_code%TYPE;
BEGIN
SELECT trang_thai_san INTO v_trang_thai_san
FROM san_bong WHERE ma_san = v_ma_san_in;

IF (v_trang_thai_san != 'SAN_SANG') THEN
        RAISE_APPLICATION_ERROR(-20001,
            'San bong khong o trang thai SAN_SANG, khong the dat san');
END IF;

    IF (v_gio_bat_dau_in >= v_gio_ket_thuc_in) THEN
        RAISE_APPLICATION_ERROR(-20002,
            'Gio bat dau phai nho hon gio ket thuc');
END IF;

BEGIN
SELECT gia_moi_gio INTO v_gia_moi_gio
FROM bang_gia
WHERE ma_san = v_ma_san_in AND dang_ap_dung = 1
  AND gio_bat_dau <= v_gio_bat_dau_in
  AND gio_ket_thuc >= v_gio_ket_thuc_in
  AND ROWNUM = 1;
EXCEPTION
        WHEN NO_DATA_FOUND THEN v_gia_moi_gio := 0;
END;

    v_so_gio := ( TO_NUMBER(SUBSTR(v_gio_ket_thuc_in, 1, 2)) * 60
                + TO_NUMBER(SUBSTR(v_gio_ket_thuc_in, 4, 2))
                - TO_NUMBER(SUBSTR(v_gio_bat_dau_in,  1, 2)) * 60
                - TO_NUMBER(SUBSTR(v_gio_bat_dau_in,  4, 2)) ) / 60;

    v_tong_tien_goc := v_so_gio * v_gia_moi_gio;

    IF (v_ma_uu_dai_in IS NOT NULL) THEN
SELECT loai_giam_gia, gia_tri_giam, gia_tri_dat_toi_thieu
INTO   v_loai_giam_gia, v_gia_tri_giam, v_gia_tri_toi_thieu
FROM   uu_dai
WHERE  ma_uu_dai = v_ma_uu_dai_in AND dang_hoat_dong = 1;

IF (v_tong_tien_goc >= v_gia_tri_toi_thieu) THEN
            IF (v_loai_giam_gia = 'PHAN_TRAM') THEN
                v_tien_giam := v_tong_tien_goc * v_gia_tri_giam / 100;
            ELSIF (v_loai_giam_gia = 'SO_TIEN_CO_DINH') THEN
                v_tien_giam := v_gia_tri_giam;
END IF;
END IF;
END IF;

SELECT NVL(MAX(ma_dat_san), 0) + 1 INTO v_so_dat_san FROM dat_san;
v_ma_dat_san_code := 'DS' || LPAD(v_so_dat_san, 8, '0');

INSERT INTO dat_san (
    ma_dat_san_code, ma_khach_hang, ma_san, ma_nhan_vien_tao,
    ngay_dat, gio_bat_dau, gio_ket_thuc,
    trang_thai_dat_san, trang_thai_thanh_toan,
    tong_tien_goc, tien_giam, tong_tien_thanh_toan,
    ma_uu_dai, ghi_chu, ngay_tao, ngay_cap_nhat
) VALUES (
             v_ma_dat_san_code, v_ma_khach_hang_in, v_ma_san_in, v_ma_nhan_vien_in,
             v_ngay_dat_in, v_gio_bat_dau_in, v_gio_ket_thuc_in,
             'CHO_XU_LY', 'CHUA_THANH_TOAN',
             v_tong_tien_goc, v_tien_giam, v_tong_tien_goc - v_tien_giam,
             v_ma_uu_dai_in, v_ghi_chu_in, SYSDATE, SYSDATE
         );

COMMIT;
END;
/

-- ------------------------------------------------------------
-- 3.1.2 Tên Procedure: sp_XacNhanDatSan
-- ------------------------------------------------------------
CREATE OR REPLACE PROCEDURE sp_XacNhanDatSan(
    v_ma_dat_san_in   IN dat_san.ma_dat_san%TYPE,
    v_ma_tai_khoan_in IN tai_khoan.ma_tai_khoan%TYPE DEFAULT NULL
)
AS
    v_trang_thai_hien_tai dat_san.trang_thai_dat_san%TYPE;
    v_ma_san              dat_san.ma_san%TYPE;
BEGIN
SELECT trang_thai_dat_san, ma_san
INTO   v_trang_thai_hien_tai, v_ma_san
FROM   dat_san WHERE ma_dat_san = v_ma_dat_san_in;

IF (v_trang_thai_hien_tai != 'CHO_XU_LY') THEN
        RAISE_APPLICATION_ERROR(-20003,
            'Chi co the xac nhan dat san o trang thai CHO_XU_LY');
END IF;

UPDATE dat_san
SET trang_thai_dat_san = 'DA_XAC_NHAN', ngay_cap_nhat = SYSDATE
WHERE ma_dat_san = v_ma_dat_san_in;

INSERT INTO lich_su_trang_thai_dat_san (
    ma_dat_san, trang_thai_cu, trang_thai_moi,
    ma_tai_khoan_thay_doi, thoi_gian_thay_doi, ghi_chu
) VALUES (
             v_ma_dat_san_in, 'CHO_XU_LY', 'DA_XAC_NHAN',
             v_ma_tai_khoan_in, SYSDATE, 'Nhan vien xac nhan dat san'
         );

UPDATE san_bong
SET trang_thai_san = 'DANG_SU_DUNG', ngay_cap_nhat = SYSDATE
WHERE ma_san = v_ma_san;

COMMIT;
END;
/

-- ------------------------------------------------------------
-- 3.1.3 Tên Procedure: sp_HuyDatSan
-- ------------------------------------------------------------
CREATE OR REPLACE PROCEDURE sp_HuyDatSan(
    v_ma_dat_san_in   IN dat_san.ma_dat_san%TYPE,
    v_ma_tai_khoan_in IN tai_khoan.ma_tai_khoan%TYPE DEFAULT NULL,
    v_ghi_chu_in      IN VARCHAR2 DEFAULT 'Huy dat san'
)
AS
    v_trang_thai_hien_tai dat_san.trang_thai_dat_san%TYPE;
    v_ma_san              dat_san.ma_san%TYPE;
    v_trang_thai_tt       dat_san.trang_thai_thanh_toan%TYPE;
BEGIN
SELECT trang_thai_dat_san, ma_san, trang_thai_thanh_toan
INTO   v_trang_thai_hien_tai, v_ma_san, v_trang_thai_tt
FROM   dat_san WHERE ma_dat_san = v_ma_dat_san_in;

IF (v_trang_thai_hien_tai IN ('DA_HOAN_THANH', 'DA_HUY')) THEN
        RAISE_APPLICATION_ERROR(-20004,
            'Khong the huy dat san da hoan thanh hoac da bi huy truoc do');
END IF;

UPDATE dat_san
SET trang_thai_dat_san    = 'DA_HUY',
    trang_thai_thanh_toan = CASE
                                WHEN v_trang_thai_tt = 'DA_THANH_TOAN'
                                    THEN 'DA_HOAN_TIEN'
                                ELSE trang_thai_thanh_toan
        END,
    ngay_cap_nhat = SYSDATE
WHERE ma_dat_san = v_ma_dat_san_in;

INSERT INTO lich_su_trang_thai_dat_san (
    ma_dat_san, trang_thai_cu, trang_thai_moi,
    ma_tai_khoan_thay_doi, thoi_gian_thay_doi, ghi_chu
) VALUES (
             v_ma_dat_san_in, v_trang_thai_hien_tai, 'DA_HUY',
             v_ma_tai_khoan_in, SYSDATE, v_ghi_chu_in
         );

UPDATE san_bong
SET trang_thai_san = 'SAN_SANG', ngay_cap_nhat = SYSDATE
WHERE ma_san = v_ma_san;

COMMIT;
END;
/

-- ------------------------------------------------------------
-- 3.1.4 Tên Procedure: sp_XoaDatSan
-- ------------------------------------------------------------
CREATE OR REPLACE PROCEDURE sp_XoaDatSan(
    v_ma_dat_san_in IN dat_san.ma_dat_san%TYPE
)
AS
    v_trang_thai dat_san.trang_thai_dat_san%TYPE;
    v_ma_san     dat_san.ma_san%TYPE;
BEGIN
SELECT trang_thai_dat_san, ma_san
INTO   v_trang_thai, v_ma_san
FROM   dat_san WHERE ma_dat_san = v_ma_dat_san_in;

IF (v_trang_thai NOT IN ('DA_HUY', 'CHO_XU_LY')) THEN
        RAISE_APPLICATION_ERROR(-20005,
            'Chi co the xoa dat san o trang thai DA_HUY hoac CHO_XU_LY');
END IF;

DELETE FROM lich_su_trang_thai_dat_san WHERE ma_dat_san = v_ma_dat_san_in;

DELETE FROM hoa_don
WHERE ma_thanh_toan IN (
    SELECT ma_thanh_toan FROM thanh_toan WHERE ma_dat_san = v_ma_dat_san_in
);

DELETE FROM thanh_toan WHERE ma_dat_san = v_ma_dat_san_in;

DELETE FROM dat_san WHERE ma_dat_san = v_ma_dat_san_in;

UPDATE san_bong
SET trang_thai_san = 'SAN_SANG', ngay_cap_nhat = SYSDATE
WHERE ma_san = v_ma_san AND trang_thai_san = 'DANG_SU_DUNG';

COMMIT;
END;
/

-- ------------------------------------------------------------
-- 3.1.5 Tên Procedure: sp_ThemThanhToan
-- ------------------------------------------------------------
CREATE OR REPLACE PROCEDURE sp_ThemThanhToan(
    v_ma_dat_san_in   IN thanh_toan.ma_dat_san%TYPE,
    v_hinh_thuc_tt_in IN thanh_toan.hinh_thuc_thanh_toan%TYPE,
    v_so_tien_in      IN thanh_toan.so_tien_thanh_toan%TYPE,
    v_ma_giao_dich_in IN thanh_toan.ma_giao_dich%TYPE DEFAULT NULL,
    v_ma_nhan_vien_in IN thanh_toan.ma_nhan_vien_xac_nhan%TYPE DEFAULT NULL,
    v_ghi_chu_in      IN thanh_toan.ghi_chu%TYPE DEFAULT NULL
)
AS
    v_tong_tien_can_tt  dat_san.tong_tien_thanh_toan%TYPE;
    v_trang_thai_ds     dat_san.trang_thai_dat_san%TYPE;
    v_trang_thai_tt_moi thanh_toan.trang_thai_thanh_toan%TYPE;
BEGIN
SELECT tong_tien_thanh_toan, trang_thai_dat_san
INTO   v_tong_tien_can_tt, v_trang_thai_ds
FROM   dat_san WHERE ma_dat_san = v_ma_dat_san_in;

IF (v_trang_thai_ds IN ('DA_HUY', 'DA_HOAN_THANH')) THEN
        RAISE_APPLICATION_ERROR(-20006,
            'Khong the thanh toan cho dat san da huy hoac da hoan thanh');
END IF;

    IF (v_so_tien_in <= 0 OR v_so_tien_in > v_tong_tien_can_tt) THEN
        RAISE_APPLICATION_ERROR(-20007, 'So tien thanh toan khong hop le');
END IF;

    v_trang_thai_tt_moi := CASE
                               WHEN v_so_tien_in = v_tong_tien_can_tt
                               THEN 'THANH_CONG'
                               ELSE 'CHO_XAC_NHAN'
END;

    -- Chỉ cần INSERT — Trigger tự động xử lý phần còn lại
INSERT INTO thanh_toan (
    ma_dat_san, hinh_thuc_thanh_toan, trang_thai_thanh_toan,
    so_tien_thanh_toan, thoi_gian_thanh_toan,
    ma_giao_dich, ma_nhan_vien_xac_nhan, ghi_chu
) VALUES (
             v_ma_dat_san_in, v_hinh_thuc_tt_in, v_trang_thai_tt_moi,
             v_so_tien_in, SYSDATE,
             v_ma_giao_dich_in, v_ma_nhan_vien_in, v_ghi_chu_in
         );

COMMIT;
END;
/

-- ------------------------------------------------------------
-- 3.1.6 Tên Procedure: sp_XoaKhachHang
-- ------------------------------------------------------------
CREATE OR REPLACE PROCEDURE sp_XoaKhachHang(
    v_ma_khach_hang_in IN khach_hang.ma_khach_hang%TYPE
)
AS
    v_ma_tai_khoan tai_khoan.ma_tai_khoan%TYPE;
    v_count        NUMBER;
BEGIN
SELECT ma_tai_khoan INTO v_ma_tai_khoan
FROM   khach_hang WHERE ma_khach_hang = v_ma_khach_hang_in;

SELECT COUNT(*) INTO v_count FROM dat_san
WHERE  ma_khach_hang      = v_ma_khach_hang_in
  AND  trang_thai_dat_san NOT IN ('DA_HUY', 'DA_HOAN_THANH');

IF (v_count > 0) THEN
        RAISE_APPLICATION_ERROR(-20009,
            'Khach hang con dat san chua hoan tat, khong the xoa');
END IF;

DELETE FROM hoa_don
WHERE ma_thanh_toan IN (
    SELECT tt.ma_thanh_toan FROM thanh_toan tt
                                     INNER JOIN dat_san ds ON tt.ma_dat_san = ds.ma_dat_san
    WHERE ds.ma_khach_hang = v_ma_khach_hang_in
);

DELETE FROM thanh_toan
WHERE ma_dat_san IN (
    SELECT ma_dat_san FROM dat_san WHERE ma_khach_hang = v_ma_khach_hang_in
);

DELETE FROM lich_su_trang_thai_dat_san
WHERE ma_dat_san IN (
    SELECT ma_dat_san FROM dat_san WHERE ma_khach_hang = v_ma_khach_hang_in
);

DELETE FROM dat_san         WHERE ma_khach_hang = v_ma_khach_hang_in;
DELETE FROM san_yeu_thich   WHERE ma_khach_hang = v_ma_khach_hang_in;
DELETE FROM uu_dai_khach_hang WHERE ma_khach_hang = v_ma_khach_hang_in;
DELETE FROM khach_hang      WHERE ma_khach_hang = v_ma_khach_hang_in;

IF (v_ma_tai_khoan IS NOT NULL) THEN
DELETE FROM tai_khoan WHERE ma_tai_khoan = v_ma_tai_khoan;
END IF;

COMMIT;
END;
/

-- ============================================================

-- 3.2.1 Tên Function: insert_new_MaDatSan
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION insert_new_MaDatSan
RETURN NUMBER
IS
    v_MaDatSan_new dat_san.ma_dat_san%TYPE;
BEGIN
SELECT MAX(ma_dat_san) INTO v_MaDatSan_new FROM dat_san;
RETURN NVL(v_MaDatSan_new, 0) + 1;
END;
/

-- ------------------------------------------------------------
-- 3.2.2 Tên Function: insert_new_MaThanhToan
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION insert_new_MaThanhToan
RETURN NUMBER
IS
    v_MaThanhToan_new thanh_toan.ma_thanh_toan%TYPE;
BEGIN
SELECT MAX(ma_thanh_toan) INTO v_MaThanhToan_new FROM thanh_toan;
RETURN NVL(v_MaThanhToan_new, 0) + 1;
END;
/

-- ------------------------------------------------------------
-- 3.2.3 Tên Function: insert_new_MaKhachHang
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION insert_new_MaKhachHang
RETURN NUMBER
IS
    v_MaKhachHang_new khach_hang.ma_khach_hang%TYPE;
BEGIN
SELECT MAX(ma_khach_hang) INTO v_MaKhachHang_new FROM khach_hang;
RETURN NVL(v_MaKhachHang_new, 0) + 1;
END;
/

