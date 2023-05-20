package abstraction;

/**
 * This enum represents the different sides of the board
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public enum Side {
	/**
	 * abstrac.Side where y is at its maximum value
	 */
	BOTTOM,

	/**
	 * abstrac.Side where y = 0
	 */
	TOP,

	/**
	 * abstrac.Side where x = 0
	 */
	LEFT,

	/**
	 * abstrac.Side where x is at its maximum value
	 */
	RIGHT;

	/**
	 * Get the opposite side on the board
	 * 
	 * @return Opposite side
	 */

	public Side getOpposite() {
		switch(this) {
			case BOTTOM: return TOP;
			case TOP: return BOTTOM;
			case LEFT: return RIGHT;
			case RIGHT: return LEFT;
		}
		return null;
	}
}
