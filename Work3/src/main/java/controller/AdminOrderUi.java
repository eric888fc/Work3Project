package controller;

import util.OrderIoUtil;
import vo.OrderReportVo;
import vo.service.OrderReportService;
import vo.service.impl.OrderReportServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class AdminOrderUi extends JPanel {

    private final OrderReportService orderReportService = new OrderReportServiceImpl();
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtSearch;
    private List<OrderReportVo> allOrders = new ArrayList<>();
    private boolean checkboxMode = false;
    private JButton btnCloseSelection;

    public AdminOrderUi() {
        setLayout(new BorderLayout());

        // ===== 搜尋與工具列 =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(15);
        txtSearch.setFont(new Font("新細明體", Font.BOLD, 16));
        JButton btnExportExcel = new JButton("匯出 Excel");
        btnExportExcel.setFont(new Font("新細明體", Font.BOLD, 16));
        JButton btnViewDetail = new JButton("查看訂單細項");
        btnViewDetail.setFont(new Font("新細明體", Font.BOLD, 16));
        btnCloseSelection = new JButton("關閉選取");
        btnCloseSelection.setFont(new Font("新細明體", Font.BOLD, 16));
        btnCloseSelection.setVisible(false);

        JLabel label = new JLabel("搜尋會員 / 外送員:");
        label.setFont(new Font("新細明體", Font.BOLD, 16));
        topPanel.add(label);
        topPanel.add(txtSearch);
        topPanel.add(btnExportExcel);
        topPanel.add(btnViewDetail);
        topPanel.add(btnCloseSelection);
        add(topPanel, BorderLayout.NORTH);

        // ===== 表格設定 =====
        tableModel = new DefaultTableModel(new String[]{
                "選取", "訂單編號", "會員名稱", "Gmail", "總金額", "外送員", "日期", "付款方式", "電子錢包/找零"
        }, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Boolean.class;
                return String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // 只有 checkbox 可編輯
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        table.getColumnModel().getColumn(0).setCellRenderer(table.getDefaultRenderer(Boolean.class));
        hideCheckboxColumn(); // 初始隱藏 checkbox

        table.getTableHeader().setFont(new Font("Microsoft JhengHei", Font.BOLD, 14));
        table.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 13));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== 載入資料 =====
        loadOrders(null);

        // ===== 事件綁定 =====
        txtSearch.addActionListener(e -> loadOrders(txtSearch.getText().trim().isEmpty() ? null : txtSearch.getText().trim()));

        btnExportExcel.addActionListener(e -> {
            if (!checkboxMode) {
                showCheckboxColumn();
                checkboxMode = true;
                btnExportExcel.setText("匯出選中訂單");
                btnCloseSelection.setVisible(true);
            } else {
                exportSelectedOrders();
            }
        });

        btnCloseSelection.addActionListener(e -> {
            hideCheckboxColumn();
            checkboxMode = false;
            btnExportExcel.setText("匯出 Excel");
            btnCloseSelection.setVisible(false);
        });

        btnViewDetail.addActionListener(e -> viewSelectedOrderDetail());

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    viewSelectedOrderDetail();
                }
            }
        });
    }

    /** 載入訂單資料 */
    private void loadOrders(String keyword) {
        try {
            tableModel.setRowCount(0);
            allOrders = orderReportService.getAllOrderReports();
            for (OrderReportVo r : allOrders) {
                if (keyword == null ||
                        r.getMemberName().contains(keyword) ||
                        (r.getEmployeeName() != null && r.getEmployeeName().contains(keyword))) {

                    String walletOrChange;
                    if ("電子錢包".equals(r.getPaymentMethod())) {
                        walletOrChange = "餘額：" + r.getWalletBalance() + " 元";
                    } else {
                        walletOrChange = "找零：" + r.getWalletBalance() + " 元";
                    }

                    tableModel.addRow(new Object[]{
                            false, r.getOrderid(), r.getMemberName(), r.getGmail(), r.getTotal(),
                            r.getEmployeeName() == null ? "尚未指派" : r.getEmployeeName(),
                            r.getDate(), r.getPaymentMethod(), walletOrChange
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "載入訂單失敗：" + ex.getMessage());
        }
    }

    /** 查看選取訂單明細 */
    private void viewSelectedOrderDetail() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "請先選擇要查看的訂單！");
            return;
        }

        OrderReportVo r = allOrders.get(row);
        StringBuilder sb = new StringBuilder();
        sb.append("===== 訂單明細 =====\n\n");
        sb.append("訂單編號：").append(r.getOrderid()).append("\n");
        sb.append("會員：").append(r.getMemberName()).append(" (").append(r.getGmail()).append(")\n");
        sb.append("外送員：").append(r.getEmployeeName() == null ? "尚未指派" : r.getEmployeeName()).append("\n");
        sb.append("日期：").append(r.getDate()).append("\n");
        sb.append("付款方式：").append(r.getPaymentMethod()).append("\n\n");
        sb.append("=== 商品明細 ===\n");
        sb.append(r.getProductsDetail()).append("\n\n");
        sb.append("總金額：").append(r.getTotal()).append(" 元\n");
        if ("電子錢包".equals(r.getPaymentMethod())) {
            sb.append("電子錢包餘額：").append(r.getWalletBalance()).append(" 元\n");
        } else {
            sb.append("找零：").append(r.getWalletBalance()).append(" 元\n");
        }
        sb.append("\n=======================");

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "訂單細項", true);
        dialog.setLayout(new BorderLayout());
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14));
        textArea.setMargin(new Insets(10, 10, 10, 10));
        dialog.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnPrint = new JButton("列印訂單明細");
        JButton btnClose = new JButton("關閉");

        btnPrint.addActionListener(e -> {
            try {
                boolean complete = textArea.print();
                JOptionPane.showMessageDialog(dialog, complete ? "列印成功！" : "列印取消！");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "列印失敗：" + ex.getMessage());
            }
        });

        btnClose.addActionListener(e -> dialog.dispose());
        buttonPanel.add(btnPrint);
        buttonPanel.add(btnClose);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /** 匯出選取訂單 */
    private void exportSelectedOrders() {
        try {
            List<OrderReportVo> selectedOrders = new ArrayList<>();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                Boolean checked = (Boolean) tableModel.getValueAt(i, 0);
                if (checked != null && checked) {
                    selectedOrders.add(allOrders.get(i));
                }
            }

            if (selectedOrders.isEmpty()) {
                JOptionPane.showMessageDialog(this, "請先勾選要匯出的訂單！");
                return;
            }

            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("選擇匯出位置");
            int result = chooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String filePath = chooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".xlsx")) filePath += ".xlsx";
                OrderIoUtil.exportToExcel(selectedOrders, filePath);
                JOptionPane.showMessageDialog(this, "匯出成功！\n檔案位置: " + filePath);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "匯出失敗：" + ex.getMessage());
        }
    }

    private void showCheckboxColumn() {
        TableColumn col = table.getColumnModel().getColumn(0);
        col.setMinWidth(30);
        col.setMaxWidth(30);
        col.setPreferredWidth(30);
        col.setResizable(true);
        col.setHeaderValue("選");
        table.revalidate();
        table.repaint();
        table.getTableHeader().repaint();
    }

    private void hideCheckboxColumn() {
        TableColumn col = table.getColumnModel().getColumn(0);
        col.setMinWidth(0);
        col.setMaxWidth(0);
        col.setPreferredWidth(0);
        col.setResizable(false);
        col.setHeaderValue("");
        table.revalidate();
        table.repaint();
        table.getTableHeader().repaint();
    }
}
