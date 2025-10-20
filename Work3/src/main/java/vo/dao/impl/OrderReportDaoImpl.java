package vo.dao.impl;

import vo.OrderReportVo;
import vo.dao.OrderReportDao;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderReportDaoImpl implements OrderReportDao {

	private Connection conn = DBUtil.getConnection();

	@Override
	public List<OrderReportVo> getAllOrderReports() throws Exception {
		List<OrderReportVo> list = new ArrayList<>();

		String sql = "SELECT " +
	             "o.orderid, " +
	             "m.name AS memberName, " +
	             "m.gmail, " +
	             "GROUP_CONCAT(CONCAT(p.name, ' x', oi.amount, ' (', p.price*oi.amount, '元)') SEPARATOR '\\n') AS productsDetail, " +
	             "SUM(p.price*oi.amount) AS total, " +
	             "e.name AS employeeName, " +
	             "o.date, " +
	             "o.payment_method AS paymentMethod, " +
	             "o.wallet_after AS walletBalance " +
	             "FROM orders o " +
	             "JOIN order_items oi ON o.orderid = oi.orderid " +
	             "JOIN member m ON o.memberid = m.memberid " +
	             "JOIN product p ON oi.productid = p.productid " +
	             "LEFT JOIN employee e ON o.employeeid = e.employeeid " +
	             "GROUP BY o.orderid " +
	             "ORDER BY o.date DESC";


		try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				OrderReportVo r = new OrderReportVo(rs.getString("orderid"), rs.getString("memberName"),
						rs.getString("gmail"), rs.getString("productsDetail"), rs.getInt("total"),
						rs.getString("employeeName"), rs.getString("date"), rs.getString("paymentMethod"),
						rs.getInt("walletBalance"));
				list.add(r);
			}
		}
		return list;
	}

	@Override
	public List<OrderReportVo> getReportsByOrderId(String orderId) throws Exception {
		List<OrderReportVo> list = new ArrayList<>();
		String sql = "SELECT " +
	             "o.orderid, " +
	             "m.name AS memberName, " +
	             "m.gmail, " +
	             "GROUP_CONCAT(CONCAT(p.name, ' x', oi.amount, ' (', p.price*oi.amount, '元)') SEPARATOR '\\n') AS productsDetail, " +
	             "SUM(p.price*oi.amount) AS total, " +
	             "e.name AS employeeName, " +
	             "o.date, " +
	             "o.payment_method AS paymentMethod, " +
	             "o.wallet_after AS walletBalance " +
	             "FROM orders o " +
	             "JOIN order_items oi ON o.orderid = oi.orderid " +
	             "JOIN member m ON o.memberid = m.memberid " +
	             "JOIN product p ON oi.productid = p.productid " +
	             "LEFT JOIN employee e ON o.employeeid = e.employeeid " +
	             "WHERE o.orderid = ? " + 
	             "GROUP BY o.orderid " +
	             "ORDER BY o.date DESC";



		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, orderId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(new OrderReportVo(rs.getString("orderid"), rs.getString("memberName"),
							rs.getString("gmail"), rs.getString("productsDetail"), rs.getInt("total"),
							rs.getString("employeeName"), rs.getString("date"), rs.getString("paymentMethod"),
							rs.getInt("walletBalance")));
				}
			}
		}
		return list;
	}
}
