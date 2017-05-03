package fr.esisar.px504.simulation;

import fr.esisar.px504.simulation.Orientation.Cardinality;

/**
 * Coordinate class
 * Used for save x and y
 * Used for manage x and y after moves
 * @author acadiou
 *
 */
public class Coordinate {
	
	// Variables
	private int x;
	private int y;

	// Constructors
	
	public Coordinate() {
		super();
		this.setX(0);
		this.setY(0);
	}

	public Coordinate(int x, int y) {
		super();
		this.setX(x);
		this.setY(y);
	}
	
	public Coordinate(Coordinate another) {
		this.x = another.x;
		this.y = another.y;
	}

	
	//Getters and setters
	
	/**
	 * Get x value
	 * @return x value
	 */
	public int getX() {
		return x;
	}

	/**
	 * Set x value
	 * @param New x value
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Get y value
	 * @return y value
	 */
	public int getY() {
		return y;
	}

	/**
	 * Set y value
	 * @param y New y value
	 */
	public void setY(int y) {
		this.y = y;
	}

	// methods
	
	
	/**
	 * Moving forward on x axis
	 * @return New coordinates
	 */
	public Coordinate xForward() {
		this.x ++;
		return this;
	}

	/**
	 * Moving backward on x axis
	 * @return New coordinates
	 */
	public Coordinate xBackard() {
		this.x --;
		return this;
	}

	/**
	 * Moving forward on y axis
	 * @return New coordinates
	 */
	public Coordinate yForward() {
		this.y ++;
		return this;
	}

	/**
	 * Moving backward on y axis
	 * @return New coordinates
	 */
	public Coordinate yBackard() {
		this.y --;
		return this;
	}


	/**
	 * Moving according to a cardinality
	 * @param cardinality The actual cardinality
	 * @return New coordinates
	 */
	public Coordinate move(Cardinality cardinality) {
		switch(cardinality) {
		case xPlus:
			xForward();
			break;
		case xMoins:
			xBackard();
			break;
		case yPlus:
			yForward();
			break;
		case yMoins:
			yBackard();
			break;
		}
		return this;
	}
	

	public String toString() {
		return "(" + x + "," + y +")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Coordinate other = (Coordinate) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}


}
