package po;

public class Employee {
    private int id;
    private String employeeid;
    private String name;
    private String image;
    private String area;
	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Employee(String employeeid, String name, String image, String area) {
		super();
		this.employeeid = employeeid;
		this.name = name;
		this.image = image;
		this.area = area;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmployeeid() {
		return employeeid;
	}
	public void setEmployeeid(String employeeid) {
		this.employeeid = employeeid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	
    
}
