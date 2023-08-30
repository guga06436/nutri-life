package entities;

import lombok.Data;

import java.util.Date;
@Data
public class Nutritionist {
	private String name;
	private int age;
	private Date birthdate;
	private String crn;
	
	public Nutritionist(String name, int age, Date birthdate, String crn) {
		this.name = name;
		this.age = age;
		this.birthdate = birthdate;
		this.crn = crn;
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
