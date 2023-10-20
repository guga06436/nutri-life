package model;

import java.util.List;

import lombok.Data;
@Data
public class Nutritionist {
	private String name;
	private int age;
	private String crn;
	private String username;
	private String password;
	private List<Patient> patients;

	public Nutritionist() {

	}
	
	public Nutritionist(String name, int age, String crn, String username, String password, List<Patient> patients) {
		this.name = name;
		this.age = age;
		this.crn = crn;
		this.username = username;
		this.password = password;
		this.patients = patients;
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
