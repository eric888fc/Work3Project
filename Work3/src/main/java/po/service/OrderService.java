package po.service;

import po.Order;
import po.OrderItem;

import java.util.List;

public interface OrderService {
    String addOrder(Order order, List<OrderItem> items) throws Exception;

    List<Order> getAllOrders() throws Exception;

    void deleteOrder(String orderid) throws Exception;
    
	List<String> assignEmployeeToPendingOrdersAndReturnIds(String employeeid) throws Exception;

	String getMemberGmailByOrderId(String orderId) throws Exception;
}
