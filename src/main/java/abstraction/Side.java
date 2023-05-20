package abstraction;

/**
 * This enum represents the different sides of the board
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public enum Side {
	/**
	 * Side where y is at its maximum value
	 */
	BOTTOM,

	/**
	 * Side where y = 0
	 */
	TOP,

	/**
	 * Side where x = 0
	 */
	LEFT,

	/**
	 * Side where x is at its maximum value
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
