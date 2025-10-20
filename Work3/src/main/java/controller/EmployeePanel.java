package controller;

import po.Employee;
import po.Member;
import po.service.EmployeeService;
import po.service.OrderService;
import po.service.impl.EmployeeServiceImpl;
import po.service.impl.OrderServiceImpl;
import util.EmailUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class EmployeePanel extends JPanel {

    private EmployeeService employeeService = new EmployeeServiceImpl();
    private OrderService orderService = new OrderServiceImpl();
    private JPanel employeeListPanel;
    private List<Employee> employees;
    private JPanel selectedCard = null;
    private Employee selectedEmployee = null;

    private Member currentMember; // ✅ 加上目前登入會員

    public EmployeePanel(Member member) {
        this.currentMember = member;
        initComponents();
        loadEmployeesSafe();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        employeeListPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JScrollPane scrollPane = new JScrollPane(employeeListPanel);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnAssign = new JButton("確認指派");
        btnAssign.addActionListener(e -> confirmAssign());
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnAssign);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void loadEmployeesSafe() {
        try {
            loadEmployees();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadEmployees() throws Exception {
        employees = employeeService.getAllEmployees();
        employeeListPanel.removeAll();

        for (Employee emp : employees) {
            JPanel card = createEmployeeCard(emp);
            employeeListPanel.add(card);
        }

        employeeListPanel.revalidate();
        employeeListPanel.repaint();
    }

    private JPanel createEmployeeCard(Employee emp) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(150, 180));
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        card.setBackground(Color.WHITE);

        // ===== 圖片載入（支援JAR） =====
        try {
            JLabel imgLabel = new JLabel();
            imgLabel.setPreferredSize(new Dimension(150, 120));
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // 使用 ClassLoader 載入圖片資源
            java.net.URL imgUrl = getClass().getClassLoader().getResource(emp.getImage());
            if (imgUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imgUrl);
                Image scaledImage = originalIcon.getImage()
                        .getScaledInstance(150, 120, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                imgLabel.setText("無圖片");
            }

            card.add(imgLabel, BorderLayout.NORTH);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("圖片載入失敗", SwingConstants.CENTER);
            errorLabel.setForeground(Color.RED);
            errorLabel.setPreferredSize(new Dimension(150, 120));
            card.add(errorLabel, BorderLayout.NORTH);
        }

        // ===== 員工資訊 =====
        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setBackground(Color.WHITE);
        Font font = new Font("微軟正黑體", Font.BOLD, 14);
        JLabel nameLabel = new JLabel("姓名: " + emp.getName(), SwingConstants.CENTER);
        JLabel areaLabel = new JLabel("區域: " + emp.getArea(), SwingConstants.CENTER);
        nameLabel.setFont(font);
        areaLabel.setFont(font);
        info.add(nameLabel);
        info.add(areaLabel);
        card.add(info, BorderLayout.CENTER);

        // ===== 點擊選取效果 =====
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedCard != null)
                    selectedCard.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                selectedCard = card;
                selectedEmployee = emp;
                card.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
            }
        });

        return card;
    }


    private void confirmAssign() {
        if (selectedEmployee == null) {
            JOptionPane.showMessageDialog(this, "請先選擇要指派的外送員！");
            return;
        }

        int option = JOptionPane.showConfirmDialog(
                this,
                "確定要將所有未指派訂單指派給 " + selectedEmployee.getName() + " 嗎？",
                "確認指派",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            try {
                // 取得被指派的訂單 ID 清單
                List<String> assignedOrderIds = orderService.assignEmployeeToPendingOrdersAndReturnIds(selectedEmployee.getEmployeeid());

                for (String orderId : assignedOrderIds) {
                    // 查出該訂單會員的 gmail
                    String recipient = orderService.getMemberGmailByOrderId(orderId);
                    // 寄送該筆訂單明細
                    EmailUtil.sendOrderReportToGmail(recipient, orderId);
                }

                JOptionPane.showMessageDialog(this,
                        "成功指派 " + assignedOrderIds.size() + " 筆訂單！訂單明細已寄送給相關會員。");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "指派失敗：" + ex.getMessage());
            }
        }
    }



}
