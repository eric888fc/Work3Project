package controller;

import javax.swing.*;
import po.Member;
import util.MemberIoUtil;

import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class MainUi extends JFrame {

    private Member currentMember;
    private JPanel contentPanel;
    private JLabel lblClock;
    private JLabel lblUserName;
    private CartPanel cartPanel;  // 🔹 新增

    public CartPanel getCartPanel() { return cartPanel; }

    public MainUi(Member member) {
        this.currentMember = member;

        setTitle("Uber Cat - 外送平台");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // ===== 上方主容器 =====
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(new Color(0, 128, 0));

        // ========= 第一層：標題 + 使用者資訊 =========
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));

        JLabel lblTitle = new JLabel("Uber Cat 外送平台", SwingConstants.LEFT);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("微軟正黑體", Font.BOLD, 22));

        lblUserName = new JLabel("您好，" + member.getName(), SwingConstants.RIGHT);
        lblUserName.setForeground(Color.WHITE);
        lblUserName.setFont(new Font("微軟正黑體", Font.PLAIN, 16));

        lblClock = new JLabel();
        lblClock.setForeground(Color.WHITE);
        lblClock.setFont(new Font("Consolas", Font.BOLD, 16));

        JPanel rightInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
        rightInfoPanel.setOpaque(false);
        rightInfoPanel.add(lblUserName);
        rightInfoPanel.add(lblClock);

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(rightInfoPanel, BorderLayout.EAST);

     // ========= 第二層：功能選單 =========
        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));

        JButton btnStore = new JButton("商店");
        JButton btnCart = new JButton("購物車");
        JButton btnEmployee = new JButton("外送員");
        JButton btnProfile = new JButton("個人資料");
        JButton btnOrders = new JButton("訂單查詢");
     // ===== 登出按鈕 =====
        JButton btnLogout = new JButton("登出");
        btnLogout.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "確定要登出嗎？", "登出確認", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose(); // 關閉 MainUi
                // 返回登入頁面
                SwingUtilities.invokeLater(() -> new LoginUi().setVisible(true));
            }
        });

        // 加入到 rightInfoPanel
        rightInfoPanel.add(btnLogout);


        Font menuFont = new Font("微軟正黑體", Font.BOLD, 15);
        for (JButton b : new JButton[]{btnStore, btnCart, btnEmployee, btnProfile, btnOrders}) {
            b.setFont(menuFont);
            menuPanel.add(b);
        }

        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(menuPanel, BorderLayout.SOUTH);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        // ===== 主內容區（Panel 切換區）=====
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(Color.WHITE);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

     // ===== 各功能面板 =====
        StorePanel storePanel = new StorePanel();
        cartPanel = new CartPanel(currentMember);  
        EmployeePanel employeePanel = new EmployeePanel(currentMember);
        ProfilePanel profilePanel = new ProfilePanel(currentMember);  

        // 將 orderPanel 改成 OrderPanel，傳入當前會員
        OrderPanel orderPanel = new OrderPanel(currentMember);

        // 加入 contentPanel
        contentPanel.add(storePanel, "STORE");
        contentPanel.add(cartPanel, "CART");
        contentPanel.add(employeePanel, "EMPLOYEE");
        contentPanel.add(profilePanel, "PROFILE");
        contentPanel.add(orderPanel, "ORDERS");

        // ===== 按鈕切換功能 =====
        btnStore.addActionListener(e -> switchPanel("STORE"));
        btnCart.addActionListener(e -> {
            Member latest = MemberIoUtil.readMember();
            if (latest != null) {
                currentMember = latest;
                ((CartPanel) cartPanel).refreshMember(currentMember);
                switchPanel("CART");
            } else {
                JOptionPane.showMessageDialog(this, "尚未登入，請重新登入！");
            }
        });
        btnEmployee.addActionListener(e -> switchPanel("EMPLOYEE"));
        btnProfile.addActionListener(e -> {
            Member latest = MemberIoUtil.readMember();
            if (latest != null) {
                currentMember = latest;
                ProfilePanel refreshedProfile = new ProfilePanel(currentMember);
                contentPanel.add(refreshedProfile, "PROFILE_REFRESH");
                switchPanel("PROFILE_REFRESH");
            } else {
                JOptionPane.showMessageDialog(this, "尚未登入，請重新登入！");
            }
        });
        btnOrders.addActionListener(e -> {
            Member latest = MemberIoUtil.readMember();
            if (latest != null) {
                currentMember = latest;
                if (orderPanel instanceof OrderPanel) {
                    ((OrderPanel) orderPanel).refreshData(); // 重新載入最新資料庫訂單
                }
                switchPanel("ORDERS");
            } else {
                JOptionPane.showMessageDialog(this, "尚未登入，請重新登入！");
            }
        });



        startClock();
        setVisible(true);
    }

    /** 切換中間顯示的面板 */
    public void switchPanel(String name) {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, name);
    }

    /** 時鐘顯示 */
    private void startClock() {
        Timer timer = new Timer();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> lblClock.setText(LocalTime.now().format(formatter)));
            }
        }, 0, 1000);
    }
}