public enum Side {
	BOTTOM, TOP, LEFT, RIGHT;

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
