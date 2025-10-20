package controller;

import po.Member;
import po.service.MemberService;
import po.service.impl.MemberServiceImpl;
import exception.InvalidInputException;

import javax.swing.*;
import java.awt.*;

public class RegisterUi extends JPanel {

    private JTextField txtName, txtGmail, txtAddress;
    private JPasswordField txtPassword;
    private JButton btnSendCode, btnRegister;
    private JTextField txtCode;

    private final MemberService memberService = new MemberServiceImpl();
    private String realCode; // 暫存驗證碼

    public RegisterUi() {
        setLayout(null);
        setBackground(new Color(255, 255, 255)); // 淡藍背景，方便Design模式預覽

        JLabel lblTitle = new JLabel("會員註冊", SwingConstants.CENTER);
        lblTitle.setFont(new Font("微軟正黑體", Font.BOLD, 22));
        lblTitle.setBounds(100, 20, 200, 40);
        add(lblTitle);

        JLabel lblName = new JLabel("姓名：");
        lblName.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        lblName.setBounds(60, 80, 80, 25);
        add(lblName);

        txtName = new JTextField();
        txtName.setBounds(140, 80, 200, 25);
        add(txtName);

        JLabel lblGmail = new JLabel("Gmail：");
        lblGmail.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        lblGmail.setBounds(60, 120, 80, 25);
        add(lblGmail);

        txtGmail = new JTextField();
        txtGmail.setBounds(140, 120, 200, 25);
        add(txtGmail);

        btnSendCode = new JButton("寄送驗證碼");
        btnSendCode.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        btnSendCode.setBounds(140, 155, 200, 25);
        add(btnSendCode);

        JLabel lblCode = new JLabel("驗證碼：");
        lblCode.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        lblCode.setBounds(60, 190, 80, 25);
        add(lblCode);

        txtCode = new JTextField();
        txtCode.setBounds(140, 190, 200, 25);
        add(txtCode);

        JLabel lblPassword = new JLabel("密碼：");
        lblPassword.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        lblPassword.setBounds(60, 230, 80, 25);
        add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(140, 230, 200, 25);
        add(txtPassword);

        JLabel lblAddress = new JLabel("地址：");
        lblAddress.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        lblAddress.setBounds(60, 270, 80, 25);
        add(lblAddress);

        txtAddress = new JTextField();
        txtAddress.setBounds(140, 270, 200, 25);
        add(txtAddress);

        btnRegister = new JButton("完成註冊");
        btnRegister.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        btnRegister.setBounds(140, 320, 200, 35);
        add(btnRegister);

        // === 寄送驗證碼 ===
        btnSendCode.addActionListener(e -> sendVerification());

        // === 完成註冊 ===
        btnRegister.addActionListener(e -> registerMember());
    }

    private void sendVerification() {
        try {
            String gmail = txtGmail.getText().trim();
            realCode = memberService.sendGmailVerification(gmail);
            JOptionPane.showMessageDialog(this, "驗證碼已發送至：" + gmail /*+ "\n(除錯模式顯示：" + realCode + ")"*/);
        } catch (InvalidInputException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "格式錯誤", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "發送驗證碼失敗：" + ex.getMessage());
        }
    }

    private void registerMember() {
        try {
            String name = txtName.getText().trim();
            String gmail = txtGmail.getText().trim();
            String password = new String(txtPassword.getPassword());
            String address = txtAddress.getText().trim();
            String code = txtCode.getText().trim();

            memberService.verifyCode(code, realCode);

            Member m = new Member();
            m.setName(name);
            m.setGmail(gmail);
            m.setPassword(password);
            m.setAddress(address);
            m.setBalance(0);

            memberService.register(m);

            JOptionPane.showMessageDialog(this, "註冊成功！請返回登入頁面登入。");

            // ===== 註冊成功後返回登入畫面 =====
            // 取得目前 JPanel 所屬的 JFrame
            java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose(); // 關閉註冊畫面
            }

            // 開啟登入畫面
            LoginUi loginUi = new LoginUi();
            loginUi.setVisible(true);

        } catch (InvalidInputException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "輸入錯誤", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "註冊失敗：" + ex.getMessage());
        }
    }



}
