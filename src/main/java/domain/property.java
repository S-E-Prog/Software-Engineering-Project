package domain;

import java.io.Serializable;

public class property implements Serializable {

	private final String propertyId;
	private String name;
	private String address;
	private double price;
	private int maxParticipants;
	private user owner;

	public property(String propertyId, String name, String address , double price, int maxViewingCapacity, user owner) {
		super();
		this.propertyId = propertyId;
		this.name = name;
		this.address = address;

		this.price = price;
		this.maxParticipants = maxViewingCapacity;
		this.owner = owner;
	}

	public user getOwner() {
		return owner;
	}

	public void setOwner(user owner) {
		this.owner = owner;
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


}