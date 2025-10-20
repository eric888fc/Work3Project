package controller;

import po.Admin;
import po.Member;
import po.service.AdminService;
import po.service.MemberService;
import po.service.impl.AdminServiceImpl;
import po.service.impl.MemberServiceImpl;
import exception.DataNotFoundException;

import javax.swing.*;
import java.awt.*;

public class LoginUi extends JFrame {

    private JTextField txtGmail;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;
    private final MemberService memberService = new MemberServiceImpl();
    private final AdminService adminService = new AdminServiceImpl();
    public LoginUi() {
        setTitle("外送平台登入");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("外送平台登入系統", SwingConstants.CENTER);
        lblTitle.setFont(new Font("微軟正黑體", Font.BOLD, 20));
        lblTitle.setBounds(60, 20, 280, 30);
        getContentPane().add(lblTitle);

        JLabel lblGmail = new JLabel("Gmail：");
        lblGmail.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        lblGmail.setBounds(60, 80, 80, 25);
        getContentPane().add(lblGmail);

        txtGmail = new JTextField();
        txtGmail.setBounds(140, 80, 200, 25);
        getContentPane().add(txtGmail);

        JLabel lblPassword = new JLabel("密碼：");
        lblPassword.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        lblPassword.setBounds(60, 120, 80, 25);
        getContentPane().add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(140, 120, 200, 25);
        getContentPane().add(txtPassword);

        btnLogin = new JButton("登入");
        btnLogin.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        btnLogin.setBounds(60, 180, 120, 35);
        getContentPane().add(btnLogin);

        btnRegister = new JButton("註冊帳號");
        btnRegister.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        btnRegister.setBounds(220, 180, 120, 35);
        getContentPane().add(btnRegister);

        // === 登入事件 ===
        btnLogin.addActionListener(e -> login());

        // === 點擊註冊跳出 RegisterUi ===
        btnRegister.addActionListener(e -> openRegister());
    }

    private void login() {
        String gmail = txtGmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        try {
            // 先查會員
            Member member = memberService.login(gmail, password);
            if (member != null) {
                new MainUi(member).setVisible(true);
                dispose();
                return;
            }
        } catch (DataNotFoundException ex) {
            // 會員查不到，不用理會，改去查管理員
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "發生錯誤：" + ex.getMessage());
            return;
        }
        try {
            // 查管理員
            Admin admin = adminService.login(gmail, password);
            if (admin != null) {
                new AdminUi(admin).setVisible(true);
                dispose();
                return;
            }
        } catch (DataNotFoundException ex) {
            // 管理員查不到
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "發生錯誤：" + ex.getMessage());
            return;
        }

        // 如果會員和管理員都找不到
        JOptionPane.showMessageDialog(this, "帳號或密碼錯誤", "登入失敗", JOptionPane.ERROR_MESSAGE);
    }


    private void openRegister() {
        // 新建一個註冊視窗
        JFrame registerFrame = new JFrame("註冊新會員");
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registerFrame.setSize(420, 420);
        registerFrame.setLocationRelativeTo(this);
        registerFrame.add(new RegisterUi()); // 加入 JPanel
        registerFrame.setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUi().setVisible(true));
    }
}
