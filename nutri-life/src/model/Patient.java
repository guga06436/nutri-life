package model;

import lombok.Data;

import java.util.Date;

@Data
public class Patient {
	private String name;
	private int age;
	private Date birthdate;
	private String cpf;
	private float height;
	private float weight;
	
	public Patient(String name, int age, Date birthdate, String cpf, float height, float weight) {
		this.name = name;
		this.age = age;
		this.birthdate = birthdate;
		this.cpf = cpf;
		this.height = height;
		this.weight = weight;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
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
		Patient other = (Patient) obj;
		if (cpf == null) {
			if (other.cpf != null)
				return false;
		} else if (!cpf.equals(other.cpf))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return name + " (" + cpf + ")";
	}
}