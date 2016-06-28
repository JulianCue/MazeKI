package de.fhaachen.mazenet.Client;

import java.util.List;

import de.fhaachen.mazenet.generated.BoardType;
import de.fhaachen.mazenet.generated.CardType;
import de.fhaachen.mazenet.generated.TreasureType;
import de.fhaachen.mazenet.generated.TreasuresToGoType;

public abstract class SuperLogic {
	protected int playerCol;
	protected int playerRow;
	protected int cardCol;
	protected int cardRow;
	protected CardType shiftCard;
	
	protected abstract void calculateNextMoveFirst(BoardType board,
			List<TreasuresToGoType> treasuresToGo, TreasureType treasure, int playerId, List<TreasureType> foundTreasures);
	
	public int getPlayerCol() {
		return playerCol;
	}
	public int getPlayerRow() {
		return playerRow;
	}
	public int getCardCol() {
		return cardCol;
	}
	public int getCardRow() {
		return cardRow;
	}
	public CardType getShiftCard(){
		return shiftCard;
	}
}
