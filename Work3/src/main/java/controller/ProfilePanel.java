package controller;

import po.Member;
import po.service.MemberService;
import po.service.impl.MemberServiceImpl;
import util.MemberIoUtil;

import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends JPanel {

    private Member currentMember;
    private MemberService memberService = new MemberServiceImpl();

    private JTextField txtName, txtGmail, txtAddress, txtBalance;
    private JPasswordField txtOldPassword, txtNewPassword;

    public ProfilePanel(Member member) {
        this.currentMember = member;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel(" 個人資料管理");
        title.setFont(new Font("微軟正黑體", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(6, 3, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        // === 姓名 ===
        JLabel label = new JLabel("姓名：");
        label.setFont(new Font("新細明體", Font.BOLD, 16));
        formPanel.add(label);
        txtName = new JTextField(currentMember.getName());
        txtName.setFont(new Font("新細明體", Font.BOLD, 16));
        formPanel.add(txtName);
        JButton btnUpdateName = new JButton("修改");
        btnUpdateName.setFont(new Font("新細明體", Font.PLAIN, 16));
        formPanel.add(btnUpdateName);

        // === Gmail ===
        JLabel label_1 = new JLabel("Gmail：");
        label_1.setFont(new Font("新細明體", Font.BOLD, 16));
        formPanel.add(label_1);
        txtGmail = new JTextField(currentMember.getGmail());
        txtGmail.setFont(new Font("新細明體", Font.BOLD, 16));
        formPanel.add(txtGmail);
        JButton btnUpdateGmail = new JButton("修改");
        btnUpdateGmail.setFont(new Font("新細明體", Font.PLAIN, 16));
        formPanel.add(btnUpdateGmail);

        // === 密碼 ===
        JLabel label_2 = new JLabel("舊密碼：");
        label_2.setFont(new Font("新細明體", Font.BOLD, 16));
        formPanel.add(label_2);
        txtOldPassword = new JPasswordField();
        txtOldPassword.setFont(new Font("新細明體", Font.BOLD, 16));
        formPanel.add(txtOldPassword);
        formPanel.add(new JLabel("")); // 空白佔位

        JLabel label_3 = new JLabel("新密碼：");
        label_3.setFont(new Font("新細明體", Font.BOLD, 16));
        formPanel.add(label_3);
        txtNewPassword = new JPasswordField();
        txtNewPassword.setFont(new Font("新細明體", Font.BOLD, 16));
        formPanel.add(txtNewPassword);
        JButton btnUpdatePassword = new JButton("修改");
        btnUpdatePassword.setFont(new Font("新細明體", Font.PLAIN, 16));
        formPanel.add(btnUpdatePassword);

        // === 地址 ===
        JLabel label_4 = new JLabel("地址：");
        label_4.setFont(new Font("新細明體", Font.BOLD, 16));
        formPanel.add(label_4);
        txtAddress = new JTextField(currentMember.getAddress());
        txtAddress.setFont(new Font("新細明體", Font.BOLD, 16));
        formPanel.add(txtAddress);
        JButton btnUpdateAddress = new JButton("修改");
        btnUpdateAddress.setFont(new Font("新細明體", Font.PLAIN, 16));
        formPanel.add(btnUpdateAddress);

        // === 電子錢包 ===
        JLabel label_5 = new JLabel("電子錢包餘額：");
        label_5.setFont(new Font("新細明體", Font.BOLD, 16));
        formPanel.add(label_5);
        txtBalance = new JTextField(String.valueOf(currentMember.getBalance()));
        txtBalance.setFont(new Font("新細明體", Font.PLAIN, 16));
        txtBalance.setEditable(false);
        formPanel.add(txtBalance);
        JButton btnTopUp = new JButton("儲值");
        btnTopUp.setFont(new Font("新細明體", Font.PLAIN, 16));
        formPanel.add(btnTopUp);

        add(formPanel, BorderLayout.CENTER);

        // === 各項功能實作 ===

        // 🔹 修改姓名
        btnUpdateName.addActionListener(e -> {
            try {
                currentMember.setName(txtName.getText().trim());
                memberService.updateMember(currentMember);
                MemberIoUtil.saveMember(currentMember);
                JOptionPane.showMessageDialog(this, "姓名修改成功！");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "修改失敗：" + ex.getMessage());
            }
        });

     // 🔹 修改 Gmail（含驗證碼）
        btnUpdateGmail.addActionListener(e -> {
            try {
                String newGmail = txtGmail.getText().trim();
                if (newGmail.equals(currentMember.getGmail())) {
                    JOptionPane.showMessageDialog(this, "新 Gmail 與舊的相同！");
                    return;
                }

                // 🔸 先檢查 Gmail 是否已存在
                boolean exists = memberService.checkGmailExists(newGmail);
                if (exists) {
                    JOptionPane.showMessageDialog(this, "此 Gmail 已被其他帳號使用，請更換！");
                    return;
                }

                // 🔸 呼叫 Service 寄送驗證碼
                String code = memberService.sendGmailVerification(newGmail);
                String input = JOptionPane.showInputDialog(this, "驗證碼已寄出至 " + newGmail + "\n請輸入驗證碼：");

                // 若使用者按取消或未輸入就直接返回
                if (input == null || input.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "已取消修改 Gmail。");
                    return;
                }

                // 驗證碼檢查
                memberService.verifyCode(input.trim(), code);

                // 🔸 驗證通過後更新 Gmail
                currentMember.setGmail(newGmail);
                memberService.updateMember(currentMember);
                MemberIoUtil.saveMember(currentMember);

                JOptionPane.showMessageDialog(this, "Gmail 修改成功！");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "修改失敗：" + ex.getMessage());
            }
        });


        // 🔹 修改密碼（需要舊密碼）
        btnUpdatePassword.addActionListener(e -> {
            try {
                String oldPwd = new String(txtOldPassword.getPassword());
                String newPwd = new String(txtNewPassword.getPassword());
                memberService.changePassword(currentMember, oldPwd, newPwd);

                txtOldPassword.setText("");
                txtNewPassword.setText("");
                MemberIoUtil.saveMember(currentMember);
                JOptionPane.showMessageDialog(this, "密碼修改成功！");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "修改失敗：" + ex.getMessage());
            }
        });

        // 🔹 修改地址
        btnUpdateAddress.addActionListener(e -> {
            try {
                currentMember.setAddress(txtAddress.getText().trim());
                memberService.updateMember(currentMember);
                MemberIoUtil.saveMember(currentMember);
                JOptionPane.showMessageDialog(this, "地址修改成功！");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "修改失敗：" + ex.getMessage());
            }
        });

        // 🔹 儲值電子錢包
        btnTopUp.addActionListener(e -> {
            String s = JOptionPane.showInputDialog(this, "請輸入儲值金額：");
            try {
                int add = Integer.parseInt(s);
                memberService.topUpBalance(currentMember, add);
                txtBalance.setText(String.valueOf(currentMember.getBalance()));
                MemberIoUtil.saveMember(currentMember);
                JOptionPane.showMessageDialog(this, "儲值成功！目前餘額：" + currentMember.getBalance());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "請輸入有效金額！");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "儲值失敗：" + ex.getMessage());
            }
        });
    }
}
