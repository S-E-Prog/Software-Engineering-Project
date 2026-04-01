package domain;

import java.io.Serializable;

public class user implements Serializable {

	private final String id;
	private String name;
	private String email;
	private String password;
	private boolean isSeller;

	public user(String id, String name, String email, String password, boolean isSeller) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.isSeller = isSeller;	
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isSeller() {
		return isSeller;
	}
	public void setSeller(boolean isSeller) {
		this.isSeller = isSeller;
	}

	

	@Override
	public String toString() {
		if (this.isSeller()) {
            return "| ID: " + id + " |  | Name: " + name + " |  | Email: " + email + " |  | [SELLER]";
			
        }
		return "| ID: " + id + " |  | Name: " + name + " |  | Email: " + email + " |  | [CLIENT]";
	}
}
