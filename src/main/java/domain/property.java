package domain;

import java.io.Serializable;

public class property implements Serializable {

	private final String propertyId;
	private String address;
	private String description;
	private double price;
	private int maxViewingCapacity;

	public property(String propertyId, String address, String description, double price, int maxViewingCapacity) {

		this.propertyId = propertyId;
		this.address = address;
		this.description = description;
		this.price = price;
		this.maxViewingCapacity = maxViewingCapacity;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getMaxViewingCapacity() {
		return maxViewingCapacity;
	}

	public void setMaxViewingCapacity(int maxViewingCapacity) {
		this.maxViewingCapacity = maxViewingCapacity;
	}

	public String getPropertyId() {
		return propertyId;
	}

	@Override
	public String toString() {
		return "Property{id='" + propertyId + "', address='" + address +
				", price=" + price + "}";
	}

}
