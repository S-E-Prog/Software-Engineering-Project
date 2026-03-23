package domain;

import java.io.Serializable;

public class property implements Serializable {

	private final String propertyId;
	private String name;
	private String address;
	private String description;
	private double price;
	private int maxParticipants;
	
	public property(String propertyId, String name, String address, String description, double price, int maxViewingCapacity) {
		super();
		this.propertyId = propertyId;
		this.name=name;
		this.address = address;
		this.description = description;
		this.price = price;
		this.maxParticipants = maxViewingCapacity;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		return maxParticipants;
	}

	public void setMaxViewingCapacity(int maxParticipants) {
		this.maxParticipants = maxParticipants;
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
