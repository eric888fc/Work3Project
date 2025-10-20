package po;

import java.io.Serializable;

public class Member   implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
    private String memberId;
    private String name;
    private String gmail;
    private String password;
    private String address;
    private int balance;
	public Member() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Member(String memberid, String name, String gmail, String password, String address, int balance) {
		super();
		this.memberId = memberid;
		this.name = name;
		this.gmail = gmail;
		this.password = password;
		this.address = address;
		this.balance = balance;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberid) {
		this.memberId = memberid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGmail() {
		return gmail;
	}
	public void setGmail(String gmail) {
		this.gmail = gmail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
    
    
}
