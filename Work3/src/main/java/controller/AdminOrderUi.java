package controller;

import util.OrderIoUtil;
import vo.OrderReportVo;
import vo.service.OrderReportService;
import vo.service.impl.OrderReportServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

    public AdminOrderUi() {
        setLayout(new BorderLayout());

        // ===== 搜尋與工具列 =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(15);
        JButton btnSearch = new JButton("搜尋");
        JButton btnExportExcel = new JButton("匯出報表");
        JButton btnViewDetail = new JButton("查看訂單細項");

        topPanel.add(new JLabel("搜尋會員 / 外送員:"));
        topPanel.add(txtSearch);
        topPanel.add(btnSearch);
        topPanel.add(btnExportExcel);
        topPanel.add(btnViewDetail);
        add(topPanel, BorderLayout.NORTH);

        // ===== 表格設定 =====
        tableModel = new DefaultTableModel(new String[]{
                "訂單編號", "會員名稱", "Gmail", "總金額", "外送員", "日期", "付款方式", "電子錢包/找零"
        }, 0);

        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Microsoft JhengHei", Font.BOLD, 14));
        table.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 13));

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== 載入資料 =====
        loadOrders(null);

        // ===== 事件綁定 =====
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            loadOrders(keyword.isEmpty() ? null : keyword);
        });

        btnExportExcel.addActionListener(e -> exportSelectedOrders());
        btnViewDetail.addActionListener(e -> viewSelectedOrderDetail());

        // 🔹 雙擊列查看細項
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    viewSelectedOrderDetail();
                }
            }
        });
    }

    /** 載入所有訂單並支援搜尋 */
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
                        walletOrChange = "現金付款";
                    }

                    tableModel.addRow(new Object[]{
                            r.getOrderid(),
                            r.getMemberName(),
                            r.getGmail(),
                            r.getTotal(),
                            r.getEmployeeName() == null ? "尚未指派" : r.getEmployeeName(),
                            r.getDate(),
                            r.getPaymentMethod(),
                            walletOrChange
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "載入訂單失敗：" + ex.getMessage());
        }
    }

    /** 查看選取訂單的商品明細 */
    private void viewSelectedOrderDetail() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "請先選擇要查看的訂單！");
            return;
        }

        OrderReportVo selectedOrder = allOrders.get(row);
        String orderId = selectedOrder.getOrderid();

        try {
            List<OrderReportVo> details = orderReportService.getReportsByOrderId(orderId);
            if (details.isEmpty()) {
                JOptionPane.showMessageDialog(this, "查無此訂單細項！");
                return;
            }

            OrderReportVo r = details.get(0);
            StringBuilder sb = new StringBuilder();
            sb.append("===== 訂單明細 =====\n\n");
            sb.append("訂單編號：").append(r.getOrderid()).append("\n");
            sb.append("會員：").append(r.getMemberName()).append(" (").append(r.getGmail()).append(")\n");
            sb.append("外送員：").append(r.getEmployeeName() == null ? "尚未指派" : r.getEmployeeName()).append("\n");
            sb.append("日期：").append(r.getDate()).append("\n");
            sb.append("付款方式：").append(r.getPaymentMethod()).append("\n\n");
            sb.append("=== 商品明細 ===\n");
            sb.append(r.getProductsDetail()).append("\n");
            sb.append("\n總金額：").append(r.getTotal()).append(" 元\n");

            if ("電子錢包".equals(r.getPaymentMethod())) {
                sb.append("電子錢包餘額：").append(r.getWalletBalance()).append(" 元\n");
            }

            sb.append("\n=======================");

            // 使用 JDialog 自訂視窗
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "訂單細項", true);
            dialog.setLayout(new BorderLayout());

            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14));
            textArea.setMargin(new Insets(10, 10, 10, 10));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 350));

            // 按鈕列
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnPrint = new JButton("列印訂單明細");
            JButton btnClose = new JButton("關閉");

            btnPrint.addActionListener(e -> {
                try {
                    boolean complete = textArea.print(); // 使用 JTextArea 內建列印功能
                    if (complete) {
                        JOptionPane.showMessageDialog(dialog, "列印成功！");
                    } else {
                        JOptionPane.showMessageDialog(dialog, "列印取消！");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "列印失敗：" + ex.getMessage());
                }
            });

            btnClose.addActionListener(e -> dialog.dispose());

            buttonPanel.add(btnPrint);
            buttonPanel.add(btnClose);

            dialog.add(scrollPane, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "查看訂單細項失敗：" + ex.getMessage());
        }
    }


    /** 匯出選取的訂單報表 (支援多筆) */
    private void exportSelectedOrders() {
        try {
            int[] selectedRows = table.getSelectedRows();
            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(this, "請先選擇要匯出的訂單！");
                return;
            }

            List<OrderReportVo> selectedOrders = new ArrayList<>();
            for (int row : selectedRows) {
                selectedOrders.add(allOrders.get(row));
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
}
