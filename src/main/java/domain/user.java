package domain;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Represents a client user who can book an appointment and add properties and appointments for others to book.
 * @author sabreen
 * @version 1.0
 * 
 */
public class user implements Serializable {

	private static final Pattern EMAIL_PATTERN = Pattern.compile(
		"^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"
	);

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

	/**
	 * Validates whether the given email address is in a correct format.
	 * @param email the email string to validate
	 * @return true if the email is valid, false otherwise
	 */
	public static boolean isValidEmail(String email) {
		if (email == null || email.trim().isEmpty()) return false;
		return EMAIL_PATTERN.matcher(email.trim()).matches();
	}

	/**
	 * Returns an error message if the email is invalid, or null if valid.
	 * @param email the email string to validate
	 * @return error message string or null
	 */
	public static String validateEmail(String email) {
		if (email == null || email.trim().isEmpty())
			return "Email cannot be empty.";
		if (!email.contains("@"))
			return "Email must contain '@'.";
		if (!email.contains("."))
			return "Email must contain a domain (e.g. '.com').";
		if (!EMAIL_PATTERN.matcher(email.trim()).matches())
			return "Invalid email format. Example: name@example.com";
		return null; 
	}
	@Override
	public String toString() {
		return "| ID: " + id + " |  | Name: " + name + " |  | Email: " + email + " |";
	}
}