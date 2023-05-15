public enum Side {
	LEFT, RIGHT, TOP, BOTTOM;

	public Side getOpposite() {
		if(this == Side.BOTTOM)
			return Side.TOP;
			
		 else if(this == Side.TOP)
			return Side.BOTTOM;

		else if(this == Side.LEFT)
			return Side.RIGHT;

		else if(this == Side.RIGHT)
			return Side.LEFT;

		return null;
	}
}
