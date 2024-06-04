package Model;

import java.io.Serializable;
import java.sql.Date;

public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private int user_id;
	private String username;
	private String password;
	private String email;
	private Date date_of_birth;
	private String nationality;
	private double balance;
	
	public User(int user_id, String username, String password, String email, Date date_of_birth, String nationality,
			double balance) {
		super();
		this.user_id = user_id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.date_of_birth = date_of_birth;
		this.nationality = nationality;
		this.balance = balance;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(Date date_of_birth) {
		this.date_of_birth = date_of_birth;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "User:\nuser_id=" + user_id + "\nusername=" + username + "\npassword=" + password + "\nemail=" + email
				+ "\ndate_of_birth=" + date_of_birth + "\nnationality=" + nationality + "\nbalance=" + balance + "\n";
	}
	
	
	
}
