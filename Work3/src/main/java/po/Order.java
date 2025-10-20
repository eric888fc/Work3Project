package po;

import java.time.LocalDateTime;
import java.util.Date;

public class Order {
    private String orderid;
    private String memberid;
    private String employeeid;
    private String date;
    private String paymentMethod;
    private int total;
    private int walletAfter;
	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Order(String orderid, String memberid, String employeeid, String date, String paymentMethod, int total,
			int walletAfter) {
		super();
		this.orderid = orderid;
		this.memberid = memberid;
		this.employeeid = employeeid;
		this.date = date;
		this.paymentMethod = paymentMethod;
		this.total = total;
		this.walletAfter = walletAfter;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getMemberid() {
		return memberid;
	}
	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}
	public String getEmployeeid() {
		return employeeid;
	}
	public void setEmployeeid(String employeeid) {
		this.employeeid = employeeid;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getWalletAfter() {
		return walletAfter;
	}
	public void setWalletAfter(int walletAfter) {
		this.walletAfter = walletAfter;
	}
	
    
}
