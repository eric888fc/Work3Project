package controller;

import po.Product;
import po.service.ProductService;
import po.service.impl.ProductServiceImpl;
import util.CartIoUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class StorePanel extends JPanel {

    private ProductService productService = new ProductServiceImpl();
    private JPanel productListPanel;
    private JLabel lblQuantity, lblSubtotal;
    private JButton btnMinus, btnPlus, btnAddToCart;
    private JComboBox<String> cboSort;

    private int selectedIndex = -1;
    private int quantity = 0;
    private int subtotal = 0;
    private List<Product> products;
    private JPanel selectedCard = null;

    public StorePanel() {
        initComponents();
        initListeners();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // ===== 上方篩選列 =====
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.add(new JLabel("商品分類："));
        String[] sorts = {"所有", "藥品", "工具", "食材", "子彈"};
        cboSort = new JComboBox<>(sorts);
        filterPanel.add(cboSort);
        add(filterPanel, BorderLayout.NORTH);

        // ===== 中間商品清單 =====
        productListPanel = new JPanel();
        productListPanel.setLayout(new BoxLayout(productListPanel, BoxLayout.Y_AXIS));
        productListPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(productListPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // ===== 下方控制區 =====
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        controlPanel.setBackground(new Color(245, 245, 245));

        btnMinus = new JButton("-");
        btnMinus.setFont(new Font("新細明體", Font.BOLD, 16));
        btnPlus = new JButton("+");
        btnPlus.setFont(new Font("新細明體", Font.BOLD, 16));
        lblQuantity = new JLabel("0", SwingConstants.CENTER);
        lblQuantity.setFont(new Font("新細明體", Font.BOLD, 16));
        lblQuantity.setPreferredSize(new Dimension(40, 25));
        lblSubtotal = new JLabel("小計: 0");
        lblSubtotal.setFont(new Font("新細明體", Font.BOLD, 16));
        btnAddToCart = new JButton("加入購物車");
        btnAddToCart.setFont(new Font("新細明體", Font.BOLD, 16));

        controlPanel.add(btnMinus);
        controlPanel.add(lblQuantity);
        controlPanel.add(btnPlus);
        controlPanel.add(lblSubtotal);
        controlPanel.add(btnAddToCart);

        add(controlPanel, BorderLayout.SOUTH);
    }

    private void initListeners() {
        // 避免在設計模式下讀資料報錯
        if (!GraphicsEnvironment.isHeadless()) {
            try {
                loadProducts();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        cboSort.addActionListener(e -> {
            if (products != null)
                filterProducts((String) cboSort.getSelectedItem());
        });

        btnPlus.addActionListener(e -> {
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "請先選擇商品");
                return;
            }
            quantity++;
            updateSubtotal();
        });

        btnMinus.addActionListener(e -> {
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "請先選擇商品");
                return;
            }
            if (quantity > 0) quantity--;
            updateSubtotal();
        });

        btnAddToCart.addActionListener(e -> {
            if (selectedIndex == -1 || quantity <= 0) {
                JOptionPane.showMessageDialog(this, "請先選擇商品並設定數量");
                return;
            }
            Product p = products.get(selectedIndex);
            try {
                CartIoUtil.addToCart(p, quantity);
                JOptionPane.showMessageDialog(this, "已加入購物車！");
                quantity = 0;
                updateSubtotal();
                
             // 🔹 通知 CartPanel 更新
                JFrame top = (JFrame) SwingUtilities.getWindowAncestor(this);
                if (top instanceof MainUi) {
                    ((MainUi) top).getCartPanel().loadCartData();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "加入購物車失敗：" + ex.getMessage());
            }
        });
    }

    /** 🛒 載入商品清單 */
    public void loadProducts() throws Exception {
        products = productService.getAllProducts();
        filterProducts("所有");
    }

    /** 🔍 根據分類過濾商品 */
    private void filterProducts(String sort) {
        productListPanel.removeAll();
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            if ("所有".equals(sort) || p.getSort().equals(sort)) {
                JPanel card = createProductCard(i);
                productListPanel.add(card);
                productListPanel.add(Box.createVerticalStrut(5));
            }
        }
        productListPanel.revalidate();
        productListPanel.repaint();
    }

    /** 📦 單一卡片顯示商品 */
    private JPanel createProductCard(int index) {
        Product p = products.get(index);
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); 
        card.setPreferredSize(new Dimension(0, 80));

        // ===== 左邊圖片（支援JAR） =====
        try {
            java.net.URL imgUrl = getClass().getClassLoader().getResource(p.getImage());
            JLabel imgLabel = new JLabel();
            imgLabel.setPreferredSize(new Dimension(60, 60));
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

            if (imgUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imgUrl);
                Image scaledImage = originalIcon.getImage()
                        .getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                imgLabel.setText("無圖片");
            }
            card.add(imgLabel, BorderLayout.WEST);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("圖片載入失敗", SwingConstants.CENTER);
            errorLabel.setForeground(Color.RED);
            card.add(errorLabel, BorderLayout.WEST);
        }

        // ===== 中間資訊 =====
        JPanel infoPanel = new JPanel(new GridLayout(1, 3));
        infoPanel.setBackground(Color.WHITE);
        Font cardFont = new Font("微軟正黑體", Font.BOLD, 16);
        infoPanel.add(new JLabel(p.getSort(), SwingConstants.CENTER) {{ setFont(cardFont); }});
        infoPanel.add(new JLabel(p.getName(), SwingConstants.CENTER) {{ setFont(cardFont); }});
        infoPanel.add(new JLabel(p.getPrice() + " 元", SwingConstants.CENTER) {{ setFont(cardFont); }});
        card.add(infoPanel, BorderLayout.CENTER);

        // ===== 點選事件 =====
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedCard != null)
                    selectedCard.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                selectedCard = card;
                card.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                selectedIndex = index;
                quantity = 0;
                updateSubtotal();
            }
        });

        return card;
    }


    private void updateSubtotal() {
        lblQuantity.setText(String.valueOf(quantity));
        if (selectedIndex == -1) {
            lblSubtotal.setText("小計: 0");
            return;
        }
        Product p = products.get(selectedIndex);
        subtotal = quantity * p.getPrice();
        lblSubtotal.setText("小計: " + subtotal);
    }
}
