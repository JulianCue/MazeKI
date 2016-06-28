package de.fhaachen.mazenet.Client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.fhaachen.mazenet.generated.BoardType;
import de.fhaachen.mazenet.generated.PositionType;
import de.fhaachen.mazenet.generated.TreasureType;
import de.fhaachen.mazenet.generated.TreasuresToGoType;

public class AdvancedLogic extends Logic {
	protected AdvancedLogic(SuperLogic secondLogic) {
		super(secondLogic);

	}

	@SuppressWarnings("unused")
	private HashMap<Move, Integer> evaluatedMoves;
	private OwnBoard board;
	private int playerId;
	private TreasureType treasure;

	@Override
	public void calculateNextMoveFirst(BoardType boardO,
			List<TreasuresToGoType> treasuresToGo, TreasureType treasure,
			int playerId, List<TreasureType> foundTreasures) {
		this.evaluatedMoves = new HashMap<Move, Integer>();
		this.playerId = playerId;
		this.board = new OwnBoard(boardO);
		this.treasure = treasure;
		this.shiftCard = new Card(boardO.getShiftCard());
		// if treasure not reachable
		PositionType old = this.board.findPlayer(playerId);
		playerCol = old.getCol();
		playerRow = old.getRow();
		cardRow = 0;
		cardCol = 3;
		// if none is reachable
		outer: for (int i = 0; i <= 6; i += 6) {
			for (int j = 1; j <= 5; j += 2) {
				if (shiftToThisAndMoveToDat(i, j, new OwnBoard(boardO))) {
					cardRow = i;
					cardCol = j;
					break outer;
				} else if (shiftToThisAndMoveToDat(j, i, new OwnBoard(boardO))) {
					cardRow = j;
					cardCol = i;
					break outer;
				}
			}
		}

		outer: for (int i = 0; i <= 6; i += 6) {
			for (int j = 1; j <= 5; j += 2) {
				if (canReachTreasure(i, j, new OwnBoard(boardO))) {
					cardRow = i;
					cardCol = j;
					break outer;
				} else if (canReachTreasure(j, i, new OwnBoard(boardO))) {
					cardRow = j;
					cardCol = i;
					break outer;
				}
			}
		}

	}

	private boolean canReachTreasure(int row, int col, OwnBoard boardC) {
		if (boardC.getForbidden() != null) {
			if (row == boardC.getForbidden().getRow()
					&& col == boardC.getForbidden().getCol())
				return false;
		}
		boardC.proceedShift(row, col, boardC.getShiftCard());
		List<PositionType> positionsToReach = new LinkedList<PositionType>();
		PositionType playerPos = boardC.findPlayer(playerId);
		if (playerPos == null)
			return false;
		positionsToReach = boardC.getAllReachablePositions(playerPos);
		for (PositionType p : positionsToReach) {
			PositionType tp = boardC.findTreasure(treasure);
			if (tp.getCol() == p.getCol() && tp.getRow() == p.getRow()) {
				playerCol = tp.getCol();
				playerRow = tp.getRow();
				return true;
			}
		}
		return false;
	}

	private boolean shiftToThisAndMoveToDat(int row, int col, OwnBoard boardC) {
		if (boardC.getForbidden() != null) {
			if (row == boardC.getForbidden().getRow()
					&& col == boardC.getForbidden().getCol())
				return false;
		}
		boardC.proceedShift(row, col, boardC.getShiftCard());
		PositionType playerPos = boardC.findPlayer(playerId);
		if (playerPos == null)
			return false;
		playerRow = playerPos.getRow();
		playerCol = playerPos.getCol();
		return true;
	}

}
