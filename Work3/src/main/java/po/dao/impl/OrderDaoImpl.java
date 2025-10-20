package po.dao.impl;

import po.Order;
import po.OrderItem;
import po.dao.OrderDao;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrderDaoImpl implements OrderDao {

    private Connection conn = DBUtil.getConnection();

    /** 新增訂單（header + 明細）並自動生成 orderid */
    @Override
    public synchronized String addOrder(Order order, List<OrderItem> items) throws Exception {
        // 1) 產生下一個順序 orderId（O001,O002…）
        String orderId = generateOrderId();
        order.setOrderid(orderId);

        try {
            conn.setAutoCommit(false);

            // 插入 header
            String sqlOrder = "INSERT INTO orders(orderid, memberid, employeeid, date, payment_method, total) VALUES (?,?,?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlOrder)) {
                ps.setString(1, order.getOrderid());
                ps.setString(2, order.getMemberid());
                ps.setString(3, order.getEmployeeid());
                ps.setString(4, order.getDate());
                ps.setString(5, order.getPaymentMethod());
                ps.setInt(6, order.getTotal());
                ps.executeUpdate();
            }

            // 插入明細
            String sqlItem = "INSERT INTO order_items(orderid, productid, amount, price, subtotal) VALUES (?,?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlItem)) {
                for (OrderItem item : items) {
                    ps.setString(1, orderId);
                    ps.setString(2, item.getProductid());
                    ps.setInt(3, item.getAmount());
                    ps.setInt(4, item.getPrice());
                    ps.setInt(5, item.getSubtotal());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
            return orderId;
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
    @Override
    public String generateOrderId() throws SQLException {
        String sql = "SELECT orderid FROM orders ORDER BY orderid DESC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String last = rs.getString(1); // O017
                try {
                    int num = Integer.parseInt(last.substring(1)) + 1;
                    return String.format("O%03d", num);
                } catch (NumberFormatException ex) {
                    // 若格式不對 fallback
                    return "O001";
                }
            } else {
                return "O001";
            }
        }
    }

    @Override
    public boolean exists(String orderId) throws SQLException {
        String sql = "SELECT 1 FROM orders WHERE orderid=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<Order> getAllOrders() throws Exception {
        // TODO: 實作查詢所有訂單
        return null;
    }

    @Override
    public void deleteOrder(String orderid) throws Exception {
        String sql = "DELETE FROM orders WHERE orderid=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderid);
            ps.executeUpdate();
        }
    }
    
    @Override
    public List<String> assignEmployeeToPendingOrdersAndReturnIds(String employeeid) throws Exception {
        List<String> orderIds = new ArrayList<>();

        // 先查出所有尚未指派的訂單
        String selectSql = "SELECT orderid FROM orders WHERE employeeid IS NULL";
        try (PreparedStatement ps = conn.prepareStatement(selectSql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                orderIds.add(rs.getString("orderid"));
            }
        }

        // 更新外送員
        String updateSql = "UPDATE orders SET employeeid=? WHERE employeeid IS NULL";
        try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
            ps.setString(1, employeeid);
            ps.executeUpdate();
        }

        return orderIds;
    }
    @Override
    public String getMemberGmailByOrderId(String orderId) throws Exception {
        String sql = "SELECT m.gmail FROM orders o JOIN member m ON o.memberid = m.memberid WHERE o.orderid = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("gmail");
                }
            }
        }
        return null; // 找不到就回 null
    }



}
