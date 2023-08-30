package entities;

import lombok.Data;

import java.util.Date;
@Data
public class Patient {
	private String login;
	private String password;
	private String name;
	private int age;
	private String birthdate;
	private String cpf;
	private float height;
	private float weight;
	
<<<<<<< HEAD:nutri-life/src/entities/Patient.java
	public Patient(String name, int age, Date birthdate, String cpf, float height, float weight) {
=======
	public Patient(String login , String password,String name, int age, String birthdate, float height, float weight) {
		this.login = login;
		this.password = password;
>>>>>>> 97e4376c8af78984b6de5fe9107fbd7ad96bb135:nutri-life/src/main/java/entities/Patient.java
		this.name = name;
		this.age = age;
		this.birthdate = birthdate;
		this.cpf = cpf;
		this.height = height;
		this.weight = weight;

<<<<<<< HEAD:nutri-life/src/entities/Patient.java
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
=======
>>>>>>> 97e4376c8af78984b6de5fe9107fbd7ad96bb135:nutri-life/src/main/java/entities/Patient.java
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
