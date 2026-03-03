package domain;

public class administrator extends user{

	private boolean loggedIn;
	private String password;
	

	public administrator(String id, String name, String email, String phonenumber, String password) {
		super(id, name, email, phonenumber);
		this.loggedIn=false;
		this.password=password;
		// TODO Auto-generated constructor stub
	}


	public void logout() {
        this.loggedIn = false;
    }
	
	public boolean login(String inputPassword) {
		if (password.equals(inputPassword)) {   
			this.loggedIn = true;
	        return true;
	        }
		return false;
		}


	public boolean isLoggedIn() {
		return loggedIn;
	}

	
}
