package de.fhaachen.mazenet.Client;

import java.util.List;

import de.fhaachen.mazenet.generated.BoardType;
import de.fhaachen.mazenet.generated.CardType;
import de.fhaachen.mazenet.generated.TreasureType;
import de.fhaachen.mazenet.generated.TreasuresToGoType;

public abstract class Logic extends SuperLogic {
	protected SuperLogic secondLogic;
	protected int playerColB;
	protected int playerRowB;
	protected int cardColB;
	protected int cardRowB;
	protected CardType shiftCardB;

	protected Logic(SuperLogic secondLogic) {
		this.secondLogic = secondLogic;
	}

	public void calculateNextMove(BoardType board,
			List<TreasuresToGoType> treasuresToGo, TreasureType treasure,
			int playerId, List<TreasureType> foundTreasures) {
		calculateSecondMove(board, treasuresToGo, treasure, playerId,
				foundTreasures);
		calculateNextMoveFirst(board, treasuresToGo, treasure, playerId,
				foundTreasures);

	}

	/**
	 * Calculated the next move and sets it to playerCol, playerRow, cardCol and
	 * CardRow
	 * 
	 * @param board
	 * @param treasuresToGo
	 * @param treasure
	 */
	protected void calculateSecondMove(BoardType board,
			List<TreasuresToGoType> treasuresToGo, TreasureType treasure,
			int playerId, List<TreasureType> foundTreasures) {
		secondLogic.calculateNextMoveFirst(board, treasuresToGo, treasure,
				playerId, foundTreasures);
		playerColB = secondLogic.getPlayerCol();
		playerRowB = secondLogic.getPlayerRow();
		cardColB = secondLogic.getCardCol();
		cardRowB = secondLogic.getCardRow();
		shiftCardB = secondLogic.getShiftCard();
	}

	public void getSecondMove() {
		this.playerCol = playerColB;
		this.playerRow = playerRowB;
		this.cardCol = cardColB;
		this.cardRow = cardRowB;
		this.shiftCard = shiftCardB;
	}

}
