package com.football.management.ui.ChuSan;

import com.football.management.dao.LoaiSanDAO;
import com.football.management.model.LoaiSan;
import com.football.management.model.SanBong;
import com.football.management.service.SanBongService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class QuanLySanPage {

    private static String selectedTrangThai = null;

    public static Node createView() {
        SanBongService sanBongService = new SanBongService();
        LoaiSanDAO loaiSanDAO = new LoaiSanDAO();

        // ══════════════════════════════════════════
        //  ROOT  (VBox toàn trang, không padding ngang — topbar + statrow kéo full)
        // ══════════════════════════════════════════
        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: #f0f2f5;");

        // ── Topbar ──
        Label[] badgeHolder = new Label[1];
        HBox topbar = buildTopbar(badgeHolder);

        // ── Stat strip ──
        Label[] statValues = new Label[4];
        HBox statRow = buildStatRow(statValues);

        // ── Two-column body ──
        HBox body = new HBox(16);
        body.setPadding(new Insets(0, 24, 24, 24));
        VBox.setVgrow(body, Priority.ALWAYS);

        // ══════════════════════════════════════════
        //  LEFT: FORM PANEL
        // ══════════════════════════════════════════
        VBox formPanel = new VBox(14);
        formPanel.getStyleClass().add("form-card");
        formPanel.setPrefWidth(340);
        formPanel.setMinWidth(300);
        formPanel.setMaxWidth(360);

        Label lblPanelTitle = new Label("✏  Thông tin sân bóng");
        lblPanelTitle.getStyleClass().add("section-title");

        // Mã sân + Loại sân
        TextField txtMaSan = inputField("VD: 5A1");
        ComboBox<LoaiSan> cbLoaiSan = new ComboBox<>();
        cbLoaiSan.setItems(FXCollections.observableArrayList(loaiSanDAO.layTatCaLoaiSan()));
        cbLoaiSan.setPromptText("Chọn loại sân");
        cbLoaiSan.setMaxWidth(Double.MAX_VALUE);
        cbLoaiSan.getStyleClass().add("combo-box");

        HBox row1 = twoFieldRow(fieldBox("Mã sân", txtMaSan), fieldBox("Loại sân", cbLoaiSan));

        // Tên sân
        TextField txtTenSan = inputField("Nhập tên sân...");

        // Vị trí
        TextField txtViTri = inputField("Nhập vị trí...");

        // Sức chứa + Giờ mở cửa
        TextField txtSucChua = inputField("VD: 200");
        TextField txtGioMoCua = inputField("06:00");
        HBox row4 = twoFieldRow(fieldBox("Sức chứa", txtSucChua), fieldBox("Giờ mở cửa", txtGioMoCua));

        // Giờ đóng cửa
        TextField txtGioDongCua = inputField("22:00");

        // ── Trạng thái chips ──
        Label lblTT = new Label("Trạng thái");
        lblTT.getStyleClass().add("field-label");

        String[] ttVals   = {"SAN_SANG",   "DANG_SU_DUNG", "BAO_TRI",    "NGUNG_HOAT_DONG"};
        String[] ttLabels = {"Sẵn sàng",   "Đang dùng",    "Bảo trì",    "Ngừng HĐ"};
        String[] ttCss    = {"chip-green", "chip-blue",    "chip-amber", "chip-red"};

        ToggleGroup tgTT = new ToggleGroup();
        GridPane chipGrid = new GridPane();
        chipGrid.setHgap(8);
        chipGrid.setVgap(8);
        ToggleButton[] chips = new ToggleButton[4];

        for (int i = 0; i < 4; i++) {
            ToggleButton chip = new ToggleButton(ttLabels[i]);
            chip.setToggleGroup(tgTT);
            chip.setUserData(ttVals[i]);
            chip.getStyleClass().addAll("status-chip", ttCss[i]);
            chip.setMaxWidth(Double.MAX_VALUE);
            chips[i] = chip;
            chipGrid.add(chip, i % 2, i / 2);
            GridPane.setHgrow(chip, Priority.ALWAYS);
        }

        tgTT.selectedToggleProperty().addListener((obs, o, nw) ->
                selectedTrangThai = nw != null ? (String) nw.getUserData() : null);

        VBox ttBox = new VBox(6, lblTT, chipGrid);

        // ── Buttons ──
        Button btnThem = new Button("＋  Thêm sân");
        btnThem.getStyleClass().add("primary-button");
        btnThem.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnThem, Priority.ALWAYS);

        Button btnCapNhat = new Button("Cập nhật");
        btnCapNhat.getStyleClass().add("light-button");

        Button btnLamMoi = new Button("Làm mới");
        btnLamMoi.getStyleClass().add("secondary-button");

        HBox actionBar = new HBox(8, btnThem, btnCapNhat, btnLamMoi);
        actionBar.setAlignment(Pos.CENTER_LEFT);

        formPanel.getChildren().addAll(
                lblPanelTitle, row1,
                fieldBox("Tên sân", txtTenSan),
                fieldBox("Vị trí / Địa chỉ", txtViTri),
                row4,
                fieldBox("Giờ đóng cửa", txtGioDongCua),
                ttBox, actionBar
        );

        // ══════════════════════════════════════════
        //  RIGHT: TABLE PANEL
        // ══════════════════════════════════════════
        VBox tablePanel = new VBox(12);
        tablePanel.getStyleClass().add("card");
        HBox.setHgrow(tablePanel, Priority.ALWAYS);

        // Search + filter chips
        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("🔍  Tìm kiếm sân...");
        txtTimKiem.getStyleClass().add("search-field");
        txtTimKiem.setPrefWidth(220);

        ToggleGroup tgFilter = new ToggleGroup();
        String[] fLabels = {"Tất cả", "Sẵn sàng", "Bảo trì", "Ngừng HĐ"};
        String[] fVals   = {null, "SAN_SANG", "BAO_TRI", "NGUNG_HOAT_DONG"};
        HBox filterChips = new HBox(6);
        filterChips.setAlignment(Pos.CENTER_LEFT);

        for (int i = 0; i < fLabels.length; i++) {
            ToggleButton fc = new ToggleButton(fLabels[i]);
            fc.setToggleGroup(tgFilter);
            fc.getStyleClass().add("filter-chip");
            fc.setUserData(fVals[i]);
            if (i == 0) fc.setSelected(true);
            filterChips.getChildren().add(fc);
        }

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        HBox tableTop = new HBox(10, txtTimKiem, sp, filterChips);
        tableTop.setAlignment(Pos.CENTER_LEFT);

        // ── TableView ──
        TableView<SanBong> table = new TableView<>();
        table.getStyleClass().add("table-view");
        VBox.setVgrow(table, Priority.ALWAYS);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<SanBong, String> colMa = col("Mã sân",
                d -> new SimpleStringProperty(d.getValue().getMaSanCode()));

        TableColumn<SanBong, String> colTen = col("Tên sân",
                d -> new SimpleStringProperty(d.getValue().getTenSan()));

        TableColumn<SanBong, String> colLoai = col("Loại sân",
                d -> new SimpleStringProperty(String.valueOf(d.getValue().getMaLoaiSan())));
        colLoai.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String v, boolean empty) {
                super.updateItem(v, empty);
                if (empty || v == null) { setGraphic(null); return; }
                Label tag = new Label(v + " người");
                tag.getStyleClass().add("type-tag");
                setGraphic(tag); setText(null);
            }
        });

        TableColumn<SanBong, String> colViTri = col("Vị trí",
                d -> new SimpleStringProperty(d.getValue().getViTri()));

        TableColumn<SanBong, String> colTT = col("Trạng thái",
                d -> new SimpleStringProperty(d.getValue().getTrangThaiSan()));
        colTT.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String v, boolean empty) {
                super.updateItem(v, empty);
                if (empty || v == null) { setGraphic(null); setText(null); return; }
                Label badge = new Label();
                badge.getStyleClass().add("status-badge");
                switch (v) {
                    case "SAN_SANG"        -> { badge.setText("● Sẵn sàng");  badge.getStyleClass().add("badge-green"); }
                    case "DANG_SU_DUNG"    -> { badge.setText("● Đang dùng"); badge.getStyleClass().add("badge-blue"); }
                    case "BAO_TRI"         -> { badge.setText("● Bảo trì");   badge.getStyleClass().add("badge-amber"); }
                    case "NGUNG_HOAT_DONG" -> { badge.setText("● Ngừng HĐ"); badge.getStyleClass().add("badge-red"); }
                    default                -> badge.setText(v);
                }
                setGraphic(badge); setText(null);
            }
        });

        TableColumn<SanBong, String> colAct = new TableColumn<>("Thao tác");
        colAct.setSortable(false);
        colAct.setMinWidth(80); colAct.setMaxWidth(90);
        colAct.setCellFactory(c -> new TableCell<>() {
            final Button bEdit = new Button("✏");
            final Button bDel  = new Button("🗑");
            {
                bEdit.getStyleClass().add("icon-button");
                bDel.getStyleClass().addAll("icon-button", "icon-button-danger");
            }
            @Override protected void updateItem(String v, boolean empty) {
                super.updateItem(v, empty);
                if (empty) { setGraphic(null); return; }
                HBox box = new HBox(6, bEdit, bDel);
                box.setAlignment(Pos.CENTER_LEFT);
                bEdit.setOnAction(e ->
                        table.getSelectionModel().select(getTableView().getItems().get(getIndex())));
                bDel.setOnAction(e -> {
                    SanBong san = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Xác nhận xóa");
                    confirm.setHeaderText(null);
                    confirm.setContentText("Bạn có chắc muốn xóa sân \"" + san.getTenSan() + "\"?");
                    confirm.showAndWait().ifPresent(r -> {
                        if (r == ButtonType.OK) {
                            try {
                                sanBongService.xoaSan(san.getMaSan());
                                reload(table, sanBongService, badgeHolder[0], statValues);
                                hienThongBao("Xóa sân thành công");
                                clear(txtMaSan, txtTenSan, txtViTri, txtSucChua,
                                        txtGioMoCua, txtGioDongCua, cbLoaiSan, tgTT);
                            } catch (Exception ex) { hienLoi(ex.getMessage()); }
                        }
                    });
                });
                setGraphic(box); setText(null);
            }
        });

        table.getColumns().addAll(colMa, colTen, colLoai, colViTri, colTT, colAct);
        tablePanel.getChildren().addAll(tableTop, table);

        // ══════════════════════════════════════════
        //  WIRE UP EVENTS
        // ══════════════════════════════════════════

        // Chọn row → fill form
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, san) -> {
            if (san == null) return;
            txtMaSan.setText(san.getMaSanCode());
            txtTenSan.setText(san.getTenSan());
            txtViTri.setText(san.getViTri());
            txtSucChua.setText(san.getSucChua() != null ? String.valueOf(san.getSucChua()) : "");
            txtGioMoCua.setText(san.getGioMoCua() != null ? san.getGioMoCua() : "");
            txtGioDongCua.setText(san.getGioDongCua() != null ? san.getGioDongCua() : "");
            cbLoaiSan.getItems().stream()
                    .filter(ls -> ls.getMaLoaiSan() == san.getMaLoaiSan())
                    .findFirst().ifPresent(cbLoaiSan::setValue);
            for (ToggleButton chip : chips)
                chip.setSelected(chip.getUserData().equals(san.getTrangThaiSan()));
        });

        btnThem.setOnAction(e -> {
            try {
                SanBong san = collectForm(txtMaSan, txtTenSan, cbLoaiSan, txtViTri,
                        txtSucChua, tgTT, txtGioMoCua, txtGioDongCua);
                sanBongService.themSan(san);
                reload(table, sanBongService, badgeHolder[0], statValues);
                hienThongBao("Thêm sân thành công");
                clear(txtMaSan, txtTenSan, txtViTri, txtSucChua,
                        txtGioMoCua, txtGioDongCua, cbLoaiSan, tgTT);
            } catch (Exception ex) { hienLoi(ex.getMessage()); }
        });

        btnCapNhat.setOnAction(e -> {
            SanBong sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { hienLoi("Vui lòng chọn sân cần cập nhật"); return; }
            try {
                sel.setMaSanCode(txtMaSan.getText().trim());
                sel.setTenSan(txtTenSan.getText().trim());
                sel.setMaLoaiSan(cbLoaiSan.getValue().getMaLoaiSan());
                sel.setViTri(txtViTri.getText().trim());
                sel.setSucChua(txtSucChua.getText().trim().isEmpty() ? null
                        : Integer.parseInt(txtSucChua.getText().trim()));
                sel.setTrangThaiSan(selectedTrangThai);
                sel.setGioMoCua(txtGioMoCua.getText().trim());
                sel.setGioDongCua(txtGioDongCua.getText().trim());
                sanBongService.capNhatSan(sel);
                reload(table, sanBongService, badgeHolder[0], statValues);
                hienThongBao("Cập nhật sân thành công");
            } catch (Exception ex) { hienLoi(ex.getMessage()); }
        });

        btnLamMoi.setOnAction(e ->
                clear(txtMaSan, txtTenSan, txtViTri, txtSucChua,
                        txtGioMoCua, txtGioDongCua, cbLoaiSan, tgTT));

        txtTimKiem.textProperty().addListener((obs, o, kw) ->
                applyFilter(table, sanBongService.timKiemSan(kw), tgFilter));

        tgFilter.selectedToggleProperty().addListener((obs, o, nw) ->
                applyFilter(table, sanBongService.timKiemSan(txtTimKiem.getText()), tgFilter));

        // Load lần đầu
        reload(table, sanBongService, badgeHolder[0], statValues);

        body.getChildren().addAll(formPanel, tablePanel);
        root.getChildren().addAll(topbar, statRow, body);
        return root;
    }

    // ══════════════════════════════════════════════
    //  UI BUILDERS
    // ══════════════════════════════════════════════

    private static HBox buildTopbar(Label[] badgeHolder) {
        HBox bar = new HBox(10);
        bar.getStyleClass().add("topbar");
        bar.setAlignment(Pos.CENTER_LEFT);

        Label icon = new Label("⚽");
        icon.setStyle("-fx-font-size: 18px;");

        Label title = new Label("Quản lý sân");
        title.getStyleClass().add("topbar-title");

        Label badge = new Label("...");
        badge.getStyleClass().add("count-badge");
        badgeHolder[0] = badge;

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Button btnAdd = new Button("＋  Thêm sân mới");
        btnAdd.getStyleClass().add("primary-button");

        bar.getChildren().addAll(icon, title, badge, sp, btnAdd);
        return bar;
    }

    private static HBox buildStatRow(Label[] statValues) {
        HBox row = new HBox(12);
        row.setPadding(new Insets(16, 24, 16, 24));

        String[] labels = {"Tổng sân", "Sẵn sàng", "Bảo trì", "Ngừng HĐ"};
        String[] icons  = {"🏟", "✅", "🔧", "🚫"};
        String[] css    = {"stat-icon-blue", "stat-icon-green", "stat-icon-amber", "stat-icon-red"};

        for (int i = 0; i < 4; i++) {
            Label ico = new Label(icons[i]);
            ico.setStyle("-fx-font-size: 16px;");
            ico.getStyleClass().add(css[i]);
            ico.setMinWidth(38); ico.setMinHeight(38);
            ico.setAlignment(Pos.CENTER);

            Label val = new Label("0");
            val.getStyleClass().add("stat-value");
            statValues[i] = val;

            Label lbl = new Label(labels[i]);
            lbl.getStyleClass().add("stat-label");

            VBox info = new VBox(2, val, lbl);
            info.setAlignment(Pos.CENTER_LEFT);

            HBox card = new HBox(12, ico, info);
            card.getStyleClass().add("stat-card");
            card.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(card, Priority.ALWAYS);
            row.getChildren().add(card);
        }
        return row;
    }

    private static TextField inputField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.getStyleClass().add("text-field");
        tf.setMaxWidth(Double.MAX_VALUE);
        return tf;
    }

    private static HBox twoFieldRow(VBox left, VBox right) {
        HBox row = new HBox(10, left, right);
        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);
        return row;
    }

    private static VBox fieldBox(String labelText, Control ctrl) {
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("field-label");
        ctrl.setMaxWidth(Double.MAX_VALUE);
        return new VBox(5, lbl, ctrl);
    }

    private static <S> TableColumn<S, String> col(String title,
                                                  javafx.util.Callback<TableColumn.CellDataFeatures<S, String>,
                                                          javafx.beans.value.ObservableValue<String>> factory) {
        TableColumn<S, String> c = new TableColumn<>(title);
        c.setCellValueFactory(factory);
        return c;
    }

    // ══════════════════════════════════════════════
    //  LOGIC HELPERS
    // ══════════════════════════════════════════════

    private static void reload(TableView<SanBong> table, SanBongService svc,
                               Label badge, Label[] stats) {
        List<SanBong> all = svc.layTatCaSan();
        table.setItems(FXCollections.observableArrayList(all));
        if (badge != null) badge.setText(all.size() + " sân");

        long sanSang = all.stream().filter(s -> "SAN_SANG".equals(s.getTrangThaiSan())).count();
        long baoTri  = all.stream().filter(s -> "BAO_TRI".equals(s.getTrangThaiSan())).count();
        long ngung   = all.stream().filter(s -> "NGUNG_HOAT_DONG".equals(s.getTrangThaiSan())).count();

        if (stats[0] != null) stats[0].setText(String.valueOf(all.size()));
        if (stats[1] != null) stats[1].setText(String.valueOf(sanSang));
        if (stats[2] != null) stats[2].setText(String.valueOf(baoTri));
        if (stats[3] != null) stats[3].setText(String.valueOf(ngung));
    }

    private static void applyFilter(TableView<SanBong> table, List<SanBong> src, ToggleGroup tg) {
        String fv = tg.getSelectedToggle() != null
                ? (String) tg.getSelectedToggle().getUserData() : null;
        table.setItems(FXCollections.observableArrayList(fv == null ? src
                : src.stream().filter(s -> fv.equals(s.getTrangThaiSan())).toList()));
    }

    private static SanBong collectForm(TextField ma, TextField ten, ComboBox<LoaiSan> loai,
                                       TextField viTri, TextField sucChua, ToggleGroup tgTT,
                                       TextField gioMo, TextField gioDong) {
        SanBong s = new SanBong();
        s.setMaSanCode(ma.getText().trim());
        s.setTenSan(ten.getText().trim());
        s.setMaLoaiSan(loai.getValue().getMaLoaiSan());
        s.setViTri(viTri.getText().trim());
        s.setSucChua(sucChua.getText().trim().isEmpty() ? null
                : Integer.parseInt(sucChua.getText().trim()));
        Toggle t = tgTT.getSelectedToggle();
        s.setTrangThaiSan(t != null ? (String) t.getUserData() : null);
        s.setGioMoCua(gioMo.getText().trim());
        s.setGioDongCua(gioDong.getText().trim());
        return s;
    }

    private static void clear(TextField ma, TextField ten, TextField viTri, TextField sucChua,
                              TextField gioMo, TextField gioDong,
                              ComboBox<?> loai, ToggleGroup tgTT) {
        ma.clear(); ten.clear(); viTri.clear();
        sucChua.clear(); gioMo.clear(); gioDong.clear();
        loai.setValue(null);
        if (tgTT.getSelectedToggle() != null)
            tgTT.getSelectedToggle().setSelected(false);
        selectedTrangThai = null;
    }

    private static void hienThongBao(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null); a.setTitle("Thông báo"); a.setContentText(msg);
        a.showAndWait();
    }

    private static void hienLoi(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null); a.setTitle("Lỗi"); a.setContentText(msg);
        a.showAndWait();
    }
}