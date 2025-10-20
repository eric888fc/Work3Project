package vo;

public class OrderReportVo {
    private String orderid;
    private String memberName;
    private String gmail;
    private String productsDetail;
    private int total;
    private String employeeName;
    private String date;
    private String paymentMethod;
    private Integer walletBalance;

	public OrderReportVo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderReportVo(String orderid, String memberName, String gmail, String productsDetail, int total,
			String employeeName, String date, String paymentMethod, Integer walletBalance) {
		super();
		this.orderid = orderid;
		this.memberName = memberName;
		this.gmail = gmail;
		this.productsDetail = productsDetail;
		this.total = total;
		this.employeeName = employeeName;
		this.date = date;
		this.paymentMethod = paymentMethod;
		this.walletBalance = walletBalance;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getGmail() {
		return gmail;
	}

	public void setGmail(String gmail) {
		this.gmail = gmail;
	}

	public String getProductsDetail() {
		return productsDetail;
	}

	public void setProductsDetail(String productsDetail) {
		this.productsDetail = productsDetail;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
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

	public Integer getWalletBalance() {
		return walletBalance;
	}

	public void setWalletBalance(Integer walletBalance) {
		this.walletBalance = walletBalance;
	}

    
}
