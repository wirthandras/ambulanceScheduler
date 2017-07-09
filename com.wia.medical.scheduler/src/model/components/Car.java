package model.components;

public class Car {

	private String identifier;
	private ECarType type;
	private String typeText;

	public Car(String identifier, ECarType type) {
		this.identifier = identifier;
		this.type = type;
		this.typeText = this.type.toString();
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

	public String getTypeText() {
		return typeText;
	}

}
