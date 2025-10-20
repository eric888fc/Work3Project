package util;

import vo.OrderReportVo;
import vo.service.OrderReportService;
import vo.service.impl.OrderReportServiceImpl;


import java.util.List;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil {

    public static void sendOrderReportToGmail(String recipient, String orderId) throws Exception {
        OrderReportService reportService = new OrderReportServiceImpl();
        List<OrderReportVo> reports = reportService.getReportsByOrderId(orderId);

        // 只處理寄給 recipient 的那一筆（或多筆）
        for (OrderReportVo r : reports) {
            if (!recipient.equalsIgnoreCase(r.getGmail())) continue;

            orderId = r.getOrderid();  // 直接賦值，不要加 String
            OrderTempStore.PaymentInfo payInfo = OrderTempStore.get(orderId);

            StringBuilder sb = new StringBuilder();
            sb.append("===== 訂單明細 =====\n");
            sb.append("訂單編號: ").append(r.getOrderid()).append("\n");
            sb.append("會員: ").append(r.getMemberName()).append(" (").append(r.getGmail()).append(")\n");
            sb.append("外送員: ").append(r.getEmployeeName() == null ? "尚未指派" : r.getEmployeeName()).append("\n");
            sb.append("日期: ").append(r.getDate()).append("\n\n");

            sb.append("商品明細:\n");
            // productsDetail 用換行符號分隔（view 中是 GROUP_CONCAT 用 \n 作 separator）
            String[] lines = r.getProductsDetail().split("\n");
            int subtotalSum = 0;
            for (String line : lines) {
                sb.append("  ").append(line).append("\n");
            }
            sb.append("\n");
            sb.append("總金額: ").append(r.getTotal()).append(" 元\n");

            // 根據付款方式顯示 wallet 或 change
            if (payInfo != null) {
                if ("電子錢包".equals(payInfo.paymentMethod)) {
                    sb.append("付款方式: 電子錢包\n");
                    sb.append("儲值前餘額: ").append(payInfo.walletBefore == null ? "N/A" : payInfo.walletBefore).append(" 元\n");
                    sb.append("儲值後餘額: ").append(payInfo.walletAfter == null ? "N/A" : payInfo.walletAfter).append(" 元\n");
                } else { // 現金
                    sb.append("付款方式: 現金\n");
                    sb.append("付現: ").append(payInfo.cashGiven == null ? "N/A" : payInfo.cashGiven).append(" 元\n");
                    sb.append("找零: ").append(payInfo.change == null ? "N/A" : payInfo.change).append(" 元\n");
                }
                // 使用完可選擇移除暫存資料（避免重覆寄出或記憶體累積）
                // OrderTempStore.remove(orderId);
            } else {
                sb.append("付款方式: ").append(r.getPaymentMethod() == null ? "N/A" : r.getPaymentMethod()).append("\n");
            }

            sb.append("\n===== 感謝您的訂購 =====\n");

            // 寄信（這裡呼叫內建的 private helper）
            sendGmail(recipient, "您的訂單明細 - " + r.getOrderid(), sb.toString());
        }
    }

    private static void sendGmail(String to, String subject, String body) throws Exception {
        String username = "service0000test@gmail.com";
        String password = "wvzf mlse qzym zhrj";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
}
