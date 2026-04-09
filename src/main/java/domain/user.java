package domain;

import java.io.Serializable;

/**
 * Represents a client user who can book an appointment and add properties and appointments for others to book.
 * @author sabreen
 * @version 1.0
 * 
 */
public class user implements Serializable {

	private final String id;
	private String name;
	private String email;
	private String password;

	/**
	 * @param id
	 * @param name
	 * @param email
	 * @param password
	 */
	public user(String id, String name, String email, String password) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
	}

    public user(String number, String ahmad, String id) {
        this.id = id;
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
	@Override
	public String toString() {
		return "| ID: " + id + " |  | Name: " + name + " |  | Email: " + email + " |";
	}
}