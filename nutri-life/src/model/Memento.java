package model;

public class Memento {
	private Object state;
	
	public Memento() {
		
	}
	
	public void setState(Object state) {
		this.state = state;
	}
	
	public Object getState() {
		return state;
	}
}
