package fr.esisar.px504.simulation;

/**
 * Orientation class
 * Used for know the actual orientation (xPlus, xMoins, yPlus, yMoins)
 * Used for manage the orientation after turns
 * @author acadiou
 *
 */
public class Orientation {
	

	/**
	 * Enumeration of different cardinality
	 * xPlus, West
	 * xMoins, East
	 * yPlus, North
	 * yMoins, South
	 * @author acadiou
	 *
	 */
	public enum Cardinality {
		xPlus,
		xMoins,
		yPlus,
		yMoins;
	}


	// Variables
	private Cardinality cardinality;

	
	// Constructors
	
	/**
	 * Create the object without initial cardinality
	 * Default value : xPlus
	 */
	public Orientation() {
		super();
		this.cardinality = Cardinality.xPlus;
	}

	/**
	 * Create the object with an initial cardinality
	 * @param cardinality The initial cardinality
	 */
	public Orientation(Cardinality cardinality) {
		super();
		this.cardinality = cardinality;
	}


	// Getters and setters
	
	/**
	 * Get the actual cardinality (xPlus, xMoins, yPlus, yMoins)
	 * @return the actual cardinality
	 */
	public Cardinality getCardinality() {
		return cardinality;
	}


	/**
	 * Set the cardinality (xPlus, xMoins, yPlus, yMoins)
	 * @param cardinality The new cardinality
	 */
	public void setCardinality(Cardinality cardinality) {
		this.cardinality = cardinality;
	}


	// Methods
	
	/**
	 * Make a left turn
	 */
	public void turnLeft() {
		switch(cardinality) {
		case xPlus:
			cardinality = Cardinality.yPlus;
			break;
		case xMoins:
			cardinality = Cardinality.yMoins;
			break;
		case yPlus:
			cardinality = Cardinality.xMoins;
			break;
		case yMoins:
			cardinality = Cardinality.xPlus;
			break;
		}
	}

	/**
	 * Make a right turn
	 */
	public void turnRight() {
		switch (cardinality) {
		case xPlus:
			cardinality = Cardinality.yMoins;
			break;
		case xMoins:
			cardinality = Cardinality.yPlus;
			break;
		case yPlus:
			cardinality = Cardinality.xPlus;
			break;
		case yMoins:
			cardinality = Cardinality.xMoins;
			break;
		}
	}
	
	
	/**
	 * Make a half-turn
	 */
	public void uTurn() {
		switch (cardinality) {
		case xPlus:
			cardinality = Cardinality.xMoins;
			break;
		case xMoins:
			cardinality = Cardinality.xPlus;
			break;
		case yPlus:
			cardinality = Cardinality.yMoins;
			break;
		case yMoins:
			cardinality = Cardinality.yPlus;
			break;
		}
	}
	
	
	public String toString() {
		switch (cardinality) {
		case xPlus:
			return Cardinality.xPlus.toString();
		case xMoins:
			return Cardinality.xMoins.toString();
		case yPlus:
			return Cardinality.yPlus.toString();
		case yMoins:
			return Cardinality.yMoins.toString();
		}
		return null;
	}



}
