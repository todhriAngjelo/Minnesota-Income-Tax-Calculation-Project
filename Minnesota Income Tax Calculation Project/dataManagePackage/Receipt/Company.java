package dataManagePackage.Receipt;

public class Company {
	private String name;
	private String country;
	private String city;
	private String street;
	private String number;
	
	public Company(String name, String country, String city, String street, String number){
		this.name = name;
		this.country = country;
		this.city = city;
		this.street = street;
		this.number = number;
	}
	
	public String getName(){
		return name;
	}
	
	public String getCountry(){
		return country;
	}
	
	public String getCity(){
		return city;
	}
	
	public String getStreet(){
		return street;
	}
	
	public String getNumber(){
		return number;
	}
}