package po;

public class OrderItem {
    private int id;
    private String orderid;
    private String productid;
    private int amount;
    private int price;
    private int subtotal;
	public OrderItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OrderItem(int id, String orderid, String productid, int amount, int price, int subtotal) {
		super();
		this.id = id;
		this.orderid = orderid;
		this.productid = productid;
		this.amount = amount;
		this.price = price;
		this.subtotal = subtotal;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getProductid() {
		return productid;
	}
	public void setProductid(String productid) {
		this.productid = productid;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(int subtotal) {
		this.subtotal = subtotal;
	}


}
