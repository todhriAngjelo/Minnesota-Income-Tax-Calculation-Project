package dataManagePackage.Receipt;

public class OtherReceipt extends Receipt{
	private final String kind = "Other";

	public OtherReceipt(String id, String date, String amount, String name, String country, String city, String street, String number){
		super(id, date, amount, name, country, city, street, number);
		super.kind = kind;
	}
}
