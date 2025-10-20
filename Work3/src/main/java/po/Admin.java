package po;

public class Admin {
	private String adminid;
	private String account;
	private String password;

	public Admin() {
	}

	public Admin(String adminid, String account, String password) {
		this.adminid = adminid;
		this.account = account;
		this.password = password;
	}

	// getter / setter
	public String getAdminid() {
		return adminid;
	}

	public void setAdminid(String adminid) {
		this.adminid = adminid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
