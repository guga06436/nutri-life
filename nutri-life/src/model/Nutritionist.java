<<<<<<< HEAD:nutri-life/src/main/java/entities/Nutritionist.java
package entities;

import lombok.Data;

import java.util.Date;
@Data
public class Nutritionist {
	private String name;
	private int age;
	private Date birthdate;
	private String crn;
	private String login;
	private String password;
	
	public Nutritionist(String name, int age, Date birthdate, String crn, String login, String password) {
		this.name = name;
		this.age = age;
		this.birthdate = birthdate;
		this.crn = crn;
		this.login = login;
		this.password = password;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((crn == null) ? 0 : crn.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Nutritionist other = (Nutritionist) obj;
		if (crn == null) {
			if (other.crn != null)
				return false;
		} else if (!crn.equals(other.crn))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return name + "(" + crn + ")";
	}
}
=======
package model;

import lombok.Data;
@Data
public class Nutritionist {
	private String name;
	private int age;
	private String crn;
	private String username;
	private String password;
	
	public Nutritionist(String name, int age, String crn, String username, String password) {
		this.name = name;
		this.age = age;
		this.crn = crn;
		this.username = username;
		this.password = password;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((crn == null) ? 0 : crn.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Nutritionist other = (Nutritionist) obj;
		if (crn == null) {
			if (other.crn != null)
				return false;
		} else if (!crn.equals(other.crn))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return name + "(" + crn + ")";
	}
}
>>>>>>> 077c0cb370a2fa69a196862a944be826d981f9f1:nutri-life/src/model/Nutritionist.java
