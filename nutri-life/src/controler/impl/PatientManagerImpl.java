<<<<<<< HEAD:nutri-life/src/main/java/managers/impl/PatientManagerImpl.java
package managers.impl;

import entities.Patient;
import managers.PatientManager;
import persistence.PatientPersistence;

public class PatientManagerImpl implements PatientManager{

    public PatientManagerImpl() {

    }

    @Override
    public boolean add(Patient n) {
        return PatientPersistence.patientPersistence.add(n);
    }

    @Override
    public void listAll() {
        for(Patient p : PatientPersistence.patientPersistence) {
            System.out.println(p);
        }
    }

    @Override
    public Patient retrieve(String login, String password) {
        return null;
    }
=======
package controler.impl;

import java.util.List;

import controler.PatientManager;
import model.Patient;
import persistence.PatientPersistence;

public class PatientManagerImpl implements PatientManager{
	private PatientPersistence pp;
	
	public PatientManagerImpl() {
		pp = new PatientPersistence();
	}
	
	@Override
	public boolean add(Patient n) {
		return pp.add(n);
	}

	@Override
	public void listAll() {
		List<Patient> patients = pp.listAll();
		
		for(Patient p : patients) {
			System.out.println(p);
		}
	}

	@Override
	public Patient retrieve(String login, String password) {
		return null;
	}
>>>>>>> 077c0cb370a2fa69a196862a944be826d981f9f1:nutri-life/src/controler/impl/PatientManagerImpl.java

}