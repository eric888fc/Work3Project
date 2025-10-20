package po.dao;

import po.Order;
import po.OrderItem;

import java.sql.SQLException;
import java.util.List;

public interface OrderDao {
    /**
     * 新增訂單（header + 多筆明細），內部自動生成 orderid
     * @return 
     */
    String addOrder(Order order, List<OrderItem> items) throws Exception;

    // 取得所有訂單
    List<Order> getAllOrders() throws Exception;

    void deleteOrder(String orderid) throws Exception;
    String generateOrderId() throws SQLException;

	boolean exists(String orderId) throws SQLException; 

	List<String> assignEmployeeToPendingOrdersAndReturnIds(String employeeid) throws Exception;

	String getMemberGmailByOrderId(String orderId) throws Exception;

	/** 更新訂單的 wallet_after */
	void updateWalletAfter(String orderId, int walletAfter) throws Exception;
}
