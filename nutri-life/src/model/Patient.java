package model;

import lombok.Data;

@Data
public class Patient {
	private String name;
	private int age;
	private String cpf;
	private float height;
	private float weight;
	private String username;
	private String password;
	
	public Patient() {
		
	}
	
	public Patient(String name, int age, String cpf, float height, float weight, String username, String password) {
		this.name = name;
		this.age = age;
		this.cpf = cpf;
		this.height = height;
		this.weight = weight;
		this.username = username;
		this.password = password;
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