package controller;

import javax.swing.*;
import java.awt.*;
import po.Admin;

public class AdminUi extends JFrame {
	private JPanel mainPanel;
	private Admin admin;

	public AdminUi(Admin admin) {
		this.admin = admin;

		setTitle("外送平台後台管理系統");
		setSize(900, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JButton btnProduct = new JButton("商品管理");
		JButton btnEmployee = new JButton("外送員管理");
		JButton btnOrder = new JButton("訂單檢視");
		JButton btnLogout = new JButton("登出");

		topPanel.add(btnProduct);
		topPanel.add(btnEmployee);
		topPanel.add(btnOrder);
		topPanel.add(btnLogout);

		mainPanel = new JPanel(new BorderLayout());
		add(topPanel, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);

		// 預設載入商品管理頁
		showPanel(new AdminProductUi());

		btnProduct.addActionListener(e -> showPanel(new AdminProductUi()));
		btnEmployee.addActionListener(e -> showPanel(new AdminEmployeeUi()));
		btnOrder.addActionListener(e -> showPanel(new AdminOrderUi()));
		btnLogout.addActionListener(e -> {
			new LoginUi().setVisible(true);
			dispose();
		});
	}

	private void showPanel(JPanel panel) {
		mainPanel.removeAll();
		mainPanel.add(panel, BorderLayout.CENTER);
		mainPanel.revalidate();
		mainPanel.repaint();
	}
}
