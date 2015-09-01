package pt.uc.dei.aor.proj.web;

public class Car {

	private int id;
	private String brand;
	private String color;
	private int year;
	private String license;
	private boolean flagvalue;
	
	public Car(){		
	}
	public Car(String license, String brand, int year, String color, int id, boolean flagvalue){
		this.license=license;
		this.brand=brand;
		this.year=year;
		this.color=color;
		this.id=id;
		this.flagvalue=flagvalue;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public boolean isFlagvalue() {
		return flagvalue;
	}
	public void setFlagvalue(boolean flagvalue) {
		this.flagvalue = flagvalue;
	}
	
	//new Car("Y25YIH5", "Fiat", 2014, "Black", 10000, true)
	
	
}
