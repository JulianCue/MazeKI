package de.fhaachen.mazenet.Client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.fhaachen.mazenet.generated.BoardType;
import de.fhaachen.mazenet.generated.PositionType;
import de.fhaachen.mazenet.generated.TreasureType;
import de.fhaachen.mazenet.generated.TreasuresToGoType;

public class RandomLogic extends SuperLogic {

	@SuppressWarnings("unused")
	private HashMap<Move, Integer> evaluatedMoves;
	private OwnBoard board;
	@SuppressWarnings("unused")
	private int playerId;

	@Override
	public void calculateNextMoveFirst(BoardType board,
			List<TreasuresToGoType> treasuresToGo, TreasureType treasure,
			int playerId, List<TreasureType> foundTreasures) {
		evaluatedMoves = new HashMap<Move, Integer>();
		this.playerId = playerId;
		this.board = new OwnBoard(board);
		this.shiftCard = new Card(board.getShiftCard());
		while (true) {
			generateCard();
			if (board.getForbidden() != null) {
				if (cardCol != board.getForbidden().getCol()
						&& cardRow != board.getForbidden().getRow())
					break;
			} else {
				break;
			}
		}
		// if treasure not reachable
		PositionType old = this.board.findPlayer(playerId);
		playerCol = old.getCol();
		playerRow = old.getRow();

		this.board.proceedShift(cardRow, cardCol, this.board.getShiftCard());
		List<PositionType> pos = new LinkedList<PositionType>();
		pos = this.board.getAllReachablePositions(this.board
				.findPlayer(playerId));
		for (PositionType p : pos) {
			PositionType tp = this.board.findTreasure(treasure);
			if (tp.getCol() == p.getCol() && tp.getRow() == p.getRow()) {
				playerCol = tp.getCol();
				playerRow = tp.getRow();
			}
		}

	}

	@SuppressWarnings("unused")
	private int evaluateBoardAndSetPlayerMove(Move move) {

		return 1;
	}

	private int genereate1to5() {
		int c = ((int) (Math.random() * 3)) * 2 + 1;
		return c;
	}

	private void generateCard() {
		if (Math.random() > 0.5) {
			if (Math.random() > 0.5) {
				cardCol = 0;
				cardRow = genereate1to5();
			} else {
				cardCol = 6;
				cardRow = genereate1to5();
			}

		} else {
			if (Math.random() > 0.5) {
				cardRow = 0;
				cardCol = genereate1to5();
			} else {
				cardRow = 6;
				cardCol = genereate1to5();
			}
		}
	}

}
