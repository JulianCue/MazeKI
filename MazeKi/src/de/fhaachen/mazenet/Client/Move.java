package de.fhaachen.mazenet.Client;

public class Move {
	/**
	 * to insert at top, set cardRow = 0, col element of {1,3,5} (from left to right)
	 * to insert at bottom, set row at 6, col same as above
	 * to insert at left, set col at 0, row as above
	 * to insert at right, set col at 6, above
	 * playerposition starts counting at left above with 0,0
	 * 
	 */
	public int playerCol;
	public int playerRow;
	public int cardCol;
	public int cardRow;
	public Card shiftCard;
}
