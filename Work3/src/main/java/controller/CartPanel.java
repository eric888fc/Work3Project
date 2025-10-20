package controller;

import po.Member;
import po.Order;
import po.OrderItem;
import po.service.MemberService;
import po.service.OrderService;
import po.service.impl.MemberServiceImpl;
import po.service.impl.OrderServiceImpl;
import util.CartIoUtil;
import util.CartIoUtil.CartItem;
import util.MemberIoUtil;
import util.OrderTempStore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CartPanel：顯示 Cart.txt 內容、刪除商品、付款（現金 / 電子錢包）
 * - 結構：上表格 / 下資訊與操作列
 * - 請將 MainUi 的 switchPanel(String) 設為 public，以便跳轉 PROFILE 或 EMPLOYEE
 */
public class CartPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> cboPayment;
    private JButton btnDelete, btnPay;
    private JLabel lblTotal, lblBalance;
    private JTextField txtCash;
    private JPanel cashPanel;

    private po.Member currentMember;
    private final MemberService memberService = new MemberServiceImpl();
    private final OrderService orderService = new OrderServiceImpl();

    public CartPanel(po.Member member) {
        this.currentMember = member;
        initComponents();     // 建立 UI 元件（不包含載入購物車）
        initListeners();      // 綁定事件
        // 在元件都建立完後再載入購物車資料（避免 null pointer）
        loadCartData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        // ===== 表格 =====
        String[] cols = {"商品名稱", "數量", "小計"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("新細明體", Font.BOLD, 16));
        table.setFont(new Font("新細明體", Font.BOLD, 16));
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // ===== 下方資訊 + 操作列 =====
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // 資訊（總金額 / 電子錢包餘額）
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 6));
        lblTotal = new JLabel("總金額: 0");
        lblTotal.setFont(new Font("新細明體", Font.BOLD, 16));
        lblBalance = new JLabel("電子錢包餘額: " + (currentMember != null ? currentMember.getBalance() : 0));
        lblBalance.setFont(new Font("新細明體", Font.BOLD, 16));
        infoPanel.add(lblTotal);
        infoPanel.add(lblBalance);
        bottomPanel.add(infoPanel, BorderLayout.NORTH);

        // 支付與按鈕區（右邊）
        JPanel opPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        cboPayment = new JComboBox<>(new String[]{"電子錢包","現金" });
        cboPayment.setFont(new Font("新細明體", Font.PLAIN, 16));
        // 現金輸入欄（預設隱藏）
        cashPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        txtCash = new JTextField(8);
        txtCash.setFont(new Font("新細明體", Font.PLAIN, 16));
        JLabel label = new JLabel("輸入現金:");
        label.setFont(new Font("新細明體", Font.PLAIN, 16));
        cashPanel.add(label);
        cashPanel.add(txtCash);
        cashPanel.setVisible(false);

        btnDelete = new JButton("刪除選中商品");
        btnDelete.setFont(new Font("新細明體", Font.PLAIN, 16));
        btnPay = new JButton("確認付款");
        btnPay.setFont(new Font("新細明體", Font.PLAIN, 16));

        opPanel.add(cboPayment);
        opPanel.add(cashPanel);
        opPanel.add(btnDelete);
        opPanel.add(btnPay);

        bottomPanel.add(opPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    

    private void initListeners() {
        cboPayment.addActionListener(e -> {
            String pay = (String) cboPayment.getSelectedItem();
            cashPanel.setVisible("現金".equals(pay));
            revalidate();
            repaint();
        });

        btnDelete.addActionListener(e -> deleteSelectedItem());
        btnPay.addActionListener(e -> processPayment());
    }

    /** 載入購物車到表格（注意：此方法會被放在元件建立完成後呼叫） */
    public void loadCartData() {
        try {
            List<CartIoUtil.CartItem> cartItems = CartIoUtil.readCartSafe();
            model.setRowCount(0);  // 清空表格
            for (CartIoUtil.CartItem item : cartItems) {
                model.addRow(new Object[]{
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getSubtotal()
                });
            }
            // 🔹 即時更新總金額與餘額
            updateTotalAndBalance();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "載入購物車失敗：" + e.getMessage());
        }
    }


    /** 刪除選中的商品（更新檔案與表格） */
    private void deleteSelectedItem() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "請先選擇要刪除的商品");
            return;
        }
        try {
            List<CartItem> items = CartIoUtil.readCartSafe();
            if (row < 0 || row >= items.size()) {
                JOptionPane.showMessageDialog(this, "選擇錯誤，無法刪除");
                return;
            }
            items.remove(row);
            CartIoUtil.saveCart(items);
            loadCartData(); // 已經在 loadCartData 內更新總金額與餘額
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "刪除失敗：" + ex.getMessage());
        }
    }


    /** 更新總金額與電子錢包餘額（有安全檢查 lblTotal / lblBalance） */
    private void updateTotalAndBalance() {
        if (lblTotal == null || lblBalance == null) return;

        try {
            List<CartItem> items = CartIoUtil.readCartSafe();
            int total = items.stream().mapToInt(CartItem::getSubtotal).sum();
            lblTotal.setText("總金額: " + total + " 元");
            lblBalance.setText("電子錢包餘額: " + (currentMember != null ? currentMember.getBalance() : 0) + " 元");
        } catch (Exception e) {
            lblTotal.setText("總金額: 0 元");
        }
    }

    
    public void refreshMember(Member member) {
        this.currentMember = member;
        lblBalance.setText("電子錢包餘額：" + member.getBalance()); // 假設 lblBalance 是顯示餘額的 JLabel
        try {
            loadCartData(); // 重新載入購物車內容與總金額
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "刷新購物車失敗：" + e.getMessage());
        }
    }


    /** 付款流程：含現金與電子錢包（電子錢包不足可跳轉至 Profile） */
    private void processPayment() {
        try {
            List<CartIoUtil.CartItem> items = CartIoUtil.readCartSafe();
            if (items.isEmpty()) {
                JOptionPane.showMessageDialog(this, "購物車為空！");
                return;
            }

            int total = items.stream().mapToInt(CartIoUtil.CartItem::getSubtotal).sum();
            String payMethod = (String) cboPayment.getSelectedItem();

            OrderTempStore.PaymentInfo paymentInfo;

            // 建立訂單物件
            Order order = new Order();
            order.setMemberid(currentMember.getMemberId());
            order.setEmployeeid(null);
            order.setDate(java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            order.setPaymentMethod(payMethod);
            order.setTotal(total);

            List<OrderItem> orderItems = new ArrayList<>();
            for (CartIoUtil.CartItem ci : items) {
                OrderItem oi = new OrderItem();
                oi.setProductid(ci.getProduct().getProductid());
                oi.setAmount(ci.getQuantity());
                oi.setPrice(ci.getProduct().getPrice());
                oi.setSubtotal(ci.getSubtotal());
                orderItems.add(oi);
            }

            // 電子錢包付款
            if ("電子錢包".equals(payMethod)) {
                if (currentMember.getBalance() < total) {
                    int option = JOptionPane.showConfirmDialog(this,
                            "電子錢包餘額不足，是否前往個人資料頁面儲值？",
                            "餘額不足", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        JFrame top = (JFrame) SwingUtilities.getWindowAncestor(this);
                        if (top instanceof MainUi) {
                            ((MainUi) top).switchPanel("PROFILE");
                        }
                    }
                    return;
                }

                int walletBefore = currentMember.getBalance();
                int walletAfter = walletBefore - total;
                currentMember.setBalance(walletAfter);

                // 更新會員資料庫與檔案
                memberService.updateMember(currentMember);
                MemberIoUtil.saveMember(currentMember);

                // 建立訂單，取得 orderId
                String orderId = orderService.addOrder(order, orderItems);

                // 將 wallet_after 寫入訂單資料庫
                orderService.updateWalletAfter(orderId, walletAfter);

                paymentInfo = new OrderTempStore.PaymentInfo(payMethod, null, null, walletBefore, walletAfter);
                OrderTempStore.put(orderId, paymentInfo);

                JOptionPane.showMessageDialog(this, "付款完成，訂單已建立！（編號：" + orderId + "）");

            } else { // 現金付款
                int cash;
                try {
                    cash = Integer.parseInt(txtCash.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "請輸入正確金額！");
                    return;
                }
                if (cash < total) {
                    JOptionPane.showMessageDialog(this, "現金不足！");
                    return;
                }
                int change = cash - total;

                // 將找零視為 wallet_after
                int walletAfter = change;

                // 建立訂單，取得 orderId
                String orderId = orderService.addOrder(order, orderItems);

                // 將 wallet_after 寫入訂單資料庫
                orderService.updateWalletAfter(orderId, walletAfter);

                paymentInfo = new OrderTempStore.PaymentInfo(payMethod, cash, change, null, walletAfter);
                OrderTempStore.put(orderId, paymentInfo);

                JOptionPane.showMessageDialog(this, "付款成功，找零：" + change + " 元\n訂單編號：" + orderId);
            }

            // 清空購物車並刷新
            CartIoUtil.clearCart();
            loadCartData();
            updateTotalAndBalance();

            // 跳轉到外送員面板
            JFrame top = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (top instanceof MainUi) {
                ((MainUi) top).switchPanel("EMPLOYEE");
            }

        } catch (Exception ex) {
            ex.printStackTrace(); // 🔹完整印出 stack trace
            JOptionPane.showMessageDialog(this, "付款失敗：" + ex.getMessage());
        }
    }





}
