package domain;

public class user {


	private final String id;
	private String name;
	private String email;
	private String phonenumber;;
	
	
	public user(String id, String name, String email, String phonenumber) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phonenumber = phonenumber;
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


	public String getPhonenumber() {
		return phonenumber;
	}


	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	
	 @Override
	    public String toString() {
	        return "User{id='" + id + "', name='" + name + "'}";
	    }
}
