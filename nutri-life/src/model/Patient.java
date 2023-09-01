package model;

import lombok.Data;

@Data
public class Patient {
	private String username;
	private String password;
	private String name;
	private int age;
	private String cpf;
	private float height;
	private float weight;
	
	public Patient() {
		
	}

	public Patient(String username , String password, String name, String cpf, int age, float height, float weight){
		this.username = username;
		this.password = password;
		this.name = name;
		this.age = age;
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
