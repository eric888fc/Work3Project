package controller;

import po.Product;
import po.service.ProductService;
import po.service.impl.ProductServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminProductUi extends JPanel {

    private ProductService productService = new ProductServiceImpl();
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtProductId, txtName, txtPrice, txtImage;
    private JComboBox<String> cboSort;

    public AdminProductUi() {
        setLayout(new BorderLayout());

        // ===== 上方表格 =====
        tableModel = new DefaultTableModel(new String[]{"ProductID", "分類", "名稱", "價格", "圖片"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== 下方操作面板 =====
        JPanel bottomPanel = new JPanel(new GridLayout(2,1));

        JPanel inputPanel = new JPanel(new FlowLayout());
        txtProductId = new JTextField(8);
        cboSort = new JComboBox<>(new String[]{"藥品","工具","食材","子彈"});
        txtName = new JTextField(10);
        txtPrice = new JTextField(5);
        txtImage = new JTextField(15);

        inputPanel.add(new JLabel("ProductID:")); inputPanel.add(txtProductId);
        inputPanel.add(new JLabel("分類:")); inputPanel.add(cboSort);
        inputPanel.add(new JLabel("名稱:")); inputPanel.add(txtName);
        inputPanel.add(new JLabel("價格:")); inputPanel.add(txtPrice);
        inputPanel.add(new JLabel("圖片路徑:")); inputPanel.add(txtImage);

        bottomPanel.add(inputPanel);

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("新增");
        JButton btnUpdate = new JButton("修改");
        JButton btnDelete = new JButton("刪除");

        btnPanel.add(btnAdd); btnPanel.add(btnUpdate); btnPanel.add(btnDelete);
        bottomPanel.add(btnPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        loadProducts();

        // ===== 事件 =====
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if(row >= 0){
                txtProductId.setText(tableModel.getValueAt(row,0).toString());
                cboSort.setSelectedItem(tableModel.getValueAt(row,1));
                txtName.setText(tableModel.getValueAt(row,2).toString());
                txtPrice.setText(tableModel.getValueAt(row,3).toString());
                txtImage.setText(tableModel.getValueAt(row,4).toString());
            }
        });

        btnAdd.addActionListener(e -> {
            try {
                Product p = new Product(
                        txtProductId.getText().trim(),
                        cboSort.getSelectedItem().toString(),
                        txtName.getText().trim(),
                        Integer.parseInt(txtPrice.getText().trim()),
                        txtImage.getText().trim()
                );
                productService.addProduct(p);
                JOptionPane.showMessageDialog(this,"新增成功");
                loadProducts();
            } catch (Exception ex){
                JOptionPane.showMessageDialog(this,"新增失敗："+ex.getMessage());
            }
        });

        btnUpdate.addActionListener(e -> {
            try {
                Product p = new Product(
                        txtProductId.getText().trim(),
                        cboSort.getSelectedItem().toString(),
                        txtName.getText().trim(),
                        Integer.parseInt(txtPrice.getText().trim()),
                        txtImage.getText().trim()
                );
                productService.updateProduct(p);
                JOptionPane.showMessageDialog(this,"修改成功");
                loadProducts();
            } catch (Exception ex){
                JOptionPane.showMessageDialog(this,"修改失敗："+ex.getMessage());
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row < 0){ JOptionPane.showMessageDialog(this,"請先選擇要刪除的商品"); return; }
            String id = tableModel.getValueAt(row,0).toString();
            try {
                productService.deleteProduct(id);
                JOptionPane.showMessageDialog(this,"刪除成功");
                loadProducts();
            } catch (Exception ex){
                JOptionPane.showMessageDialog(this,"刪除失敗："+ex.getMessage());
            }
        });
    }

    private void loadProducts(){
        try {
            tableModel.setRowCount(0);
            List<Product> products = productService.getAllProducts();
            for(Product p : products){
                tableModel.addRow(new Object[]{
                        p.getProductid(), p.getSort(), p.getName(), p.getPrice(), p.getImage()
                });
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(this,"載入商品失敗："+e.getMessage());
        }
    }
}
