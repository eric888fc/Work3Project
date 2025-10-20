package po;

import java.io.Serializable;

public class Product  implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
    private String productid;
    private String sort;
    private String name;
    private int price;
    private String image;
	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Product(String productid,String sort, String name, int price, String image) {
		super();
		this.productid = productid;
		this.sort = sort;
		this.name = name;
		this.price = price;
		this.image = image;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProductid() {
		return productid;
	}
	public void setProductid(String productid) {
		this.productid = productid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
    
}
