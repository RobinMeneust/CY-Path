package abstraction;

/**
 * This class represents a 2D point with integer coordinates with a cost value attached to it
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public class PointWithCost extends Point implements Comparable<PointWithCost> {
	/**
	 * Cost associated to this point
	 */
	private int cost;

	/**
	 * Constructor of PointWithCost
	 * 
	 * @param point Point whose coordinates are used to create a PointWithCost 
	 * @param cost Cost associated to this point
	 */
	public PointWithCost(Point point, int cost) {
		super(point);
		this.cost = cost;
	}

	/**
	 * Get the cost value of this point
	 * 
	 * @return The cost value
	 */

	public int getCost() {
		return cost;
	}

	/**
	 * Set the cost value of this point
	 * 
	 * @param cost New cost
	 */

	public void setCost(int cost) {
		this.cost = cost;
	}

	/**
	 * Compare this point cost to the cost of the one given
	 *
	 * @param point Point compared to
	 * @return -1 if this point is lesser than the one given, 0 if it's equal and 1 if it's greater
	 */
	
	@Override
	public int compareTo(PointWithCost point) {
		if(this.getCost() == point.getCost()) {
			return 0;
		} else if(this.getCost() < point.getCost()) {
			return -1;
		} else {
			return 1;
		}
	}
}
