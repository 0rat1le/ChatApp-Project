/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package whatsapp;


import java.util.Scanner;
import java.util.regex.Pattern;

public class WhatsApp {

	
	private String registeredUsername;
	private String registeredPassword;
	private String registeredCellPhone;
	private String firstName;
	private String lastName;

	
	public WhatsApp() {
	}

	public WhatsApp(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter First Name: ");
		String firstName = scanner.nextLine();

		System.out.print("Enter Last Name: ");
		String lastName = scanner.nextLine();

		WhatsApp userLogin = new WhatsApp(firstName, lastName);

		System.out.print("Enter Username: ");
		String username = scanner.nextLine();

		System.out.print("Enter Password: ");
		String password = scanner.nextLine();

		System.out.print("Enter Cell Phone Number: ");
		String cellPhone = scanner.nextLine();

		String registrationMessage = userLogin.registerUser(username, password, cellPhone);
		System.out.println("\n" + registrationMessage);

		if (registrationMessage.contains("incorrectly formatted") || registrationMessage.contains("not correctly formatted") || registrationMessage.contains("not correctly formatted;")) {
			scanner.close();
			return;
		}

		System.out.println("\n=== LOGIN ===");
		System.out.print("Enter Username: ");
		String loginUsername = scanner.nextLine();

		System.out.print("Enter Password: ");
		String loginPassword = scanner.nextLine();

		boolean loginSuccess = userLogin.loginUser(loginUsername, loginPassword);
		System.out.println("\n" + userLogin.returnLoginStatus(loginSuccess));

		scanner.close();
	}

	
	public boolean checkUserName(String username) {
		if (username == null) return false;
		if (!username.contains("_") || username.length() > 5) return false;
		return username.matches("^[A-Za-z0-9_]+$");
	}

	public boolean checkPasswordComplexity(String password) {
		if (password == null || password.length() < 8) {
			return false;
		}
		boolean hasUppercase = false;
		boolean hasDigit = false;
		boolean hasSpecialChar = false;

		for (char c : password.toCharArray()) {
			if (Character.isUpperCase(c)) hasUppercase = true;
			else if (Character.isDigit(c)) hasDigit = true;
			else if (!Character.isLetterOrDigit(c)) hasSpecialChar = true;
		}
		return hasUppercase && hasDigit && hasSpecialChar;
	}

	public boolean checkCellPhoneNumber(String cellPhoneNumber) {
		if (cellPhoneNumber == null) return false;
		
		String cellRegex = "^\\+\\d{10,13}$";
		return Pattern.matches(cellRegex, cellPhoneNumber);
	}

	public String registerUser(String username, String password, String cellPhone) {
		if (!checkUserName(username)) {
			return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
		}
		if (!checkPasswordComplexity(password)) {
			return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
		}
		if (!checkCellPhoneNumber(cellPhone)) {
			return "Cell phone number is not correctly formatted; include the international code as '+' followed by 10-13 digits.";
		}

		this.registeredUsername = username;
		this.registeredPassword = password;
		this.registeredCellPhone = cellPhone;

		return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.";
	}

	public boolean loginUser(String username, String password) {
		if (username == null || password == null) return false;
		return username.equals(this.registeredUsername) && password.equals(this.registeredPassword);
	}

	public String returnLoginStatus(boolean isLoggedIn) {
		if (isLoggedIn) {
			return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
		} else {
			return "Username or password incorrect, please try again.";
		}
	}
} 


