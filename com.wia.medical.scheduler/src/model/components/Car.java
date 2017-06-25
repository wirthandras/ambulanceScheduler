package model.components;

public class Car {

	private String identifier;
	private ECarType type;

	public Car(String identifier, ECarType type) {
		this.identifier = identifier;
		this.type = type;
	}

	public String getIdentifier() {
		return identifier;
	}

	public ECarType getCarType() {
		return type;
	}

	@Override
	public String toString() {

		return identifier + "," + type;
	}

}
