package po.service.impl;

import po.Order;
import po.OrderItem;
import po.dao.OrderDao;
import po.dao.impl.OrderDaoImpl;
import po.service.OrderService;

import java.util.List;

public class OrderServiceImpl implements OrderService {

    private OrderDao orderDao = new OrderDaoImpl();

    @Override
    public String addOrder(Order order, List<OrderItem> items) throws Exception {
        return orderDao.addOrder(order, items);
    }

    @Override
    public List<Order> getAllOrders() throws Exception {
        return orderDao.getAllOrders();
    }

    @Override
    public void deleteOrder(String orderid) throws Exception {
        orderDao.deleteOrder(orderid);
    }

    @Override
    public List<String> assignEmployeeToPendingOrdersAndReturnIds(String employeeid) throws Exception {
        return orderDao.assignEmployeeToPendingOrdersAndReturnIds(employeeid);
    }

    @Override
    public String getMemberGmailByOrderId(String orderId) throws Exception {
        return orderDao.getMemberGmailByOrderId(orderId);
    }

    /** 新增方法：更新訂單付款後餘額 */
    @Override
    public void updateWalletAfter(String orderId, int walletAfter) throws Exception {
        if (orderDao instanceof OrderDaoImpl) {
            ((OrderDaoImpl) orderDao).updateWalletAfter(orderId, walletAfter);
        }
    }
}

