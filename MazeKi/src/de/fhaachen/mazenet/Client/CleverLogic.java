package de.fhaachen.mazenet.Client;

import java.util.LinkedList;
import java.util.List;

import de.fhaachen.mazenet.generated.BoardType;
import de.fhaachen.mazenet.generated.PositionType;
import de.fhaachen.mazenet.generated.TreasureType;
import de.fhaachen.mazenet.generated.TreasuresToGoType;

public class CleverLogic extends Logic {
	private final static int WORTH_FOUND_TREASURE = 1000000;
	private final static int WORTH_FACTOR_TREASURE = 6;
	private final static int WORTH_FACTOR_POSITION = 1;
	private final static int WORTH_FACTOR_DISTANCE = 40;
	private LinkedList<MoveRating> ratings;
	private int playerId;
	private TreasureType treasure;
	@SuppressWarnings("unused")
	private int numberFoundTreasures;
	@SuppressWarnings("unused")
	private List<TreasureType> foundTreasures;
	private List<TreasuresToGoType> toGoList;
	// Rated by danger
	private int[] playerRatings;
	private boolean[] isPlayerAv;
	private int lastRow;
	private int lastCol;
	private int lastlastRow;
	private int lastlastCol;
	private boolean samePosition;

	protected CleverLogic(SuperLogic secondLogic) {
		super(secondLogic);
		ratings = new LinkedList<MoveRating>();
		lastCol = -1;
		lastRow = -1;
		lastlastCol = -1;
		lastlastRow = -1;
		samePosition = true;// false;
	}

	@Override
	protected void calculateNextMoveFirst(BoardType boardO,
			List<TreasuresToGoType> treasuresToGo, TreasureType treasure,
			int playerId, List<TreasureType> foundTreasures) {
		ratings.clear();
		this.playerId = playerId;
		this.numberFoundTreasures = foundTreasures.size();
		this.foundTreasures = foundTreasures;
		this.treasure = treasure;
		this.toGoList = treasuresToGo;
		samePosition = false;
		if (lastRow == lastlastRow && lastCol == lastlastCol) {
			samePosition = true;
		}

		// make player Ratings

		playerRatings = new int[4];
		isPlayerAv = new boolean[4];
		for (int i = 0; i < isPlayerAv.length; i++) {
			isPlayerAv[i] = false;
		}

		for (int i = 0; i < toGoList.size(); i++) {

			int ind = toGoList.get(i).getPlayer() - 1;

			isPlayerAv[ind] = true;
			playerRatings[ind] = (int) Math.pow(((24 / toGoList.size() + 1)
					- toGoList.get(i).getTreasures() + 1), 1.75);

		}

		System.out.println();

		for (Card.Orientation o : Card.Orientation.values()) {
			Card curShiftCard = new Card(
					new Card(boardO.getShiftCard()).getShape(), o, boardO
							.getShiftCard().getTreasure());

			for (int i = 0; i <= 6; i += 6) {
				for (int j = 1; j <= 5; j += 2) {
					Move mov = new Move();
					mov.cardRow = i;
					mov.cardCol = j;
					mov.shiftCard = new Card(curShiftCard);
					evaluateBoard(mov, new OwnBoard(boardO));
					Move movb = new Move();
					movb.cardRow = j;
					movb.cardCol = i;
					movb.shiftCard = new Card(curShiftCard);
					evaluateBoard(movb, new OwnBoard(boardO));

				}
			}
		}

		MoveRating rat = ratings.getFirst();
		cardRow = rat.move.cardRow;
		cardCol = rat.move.cardCol;
		playerCol = rat.move.playerCol;
		playerRow = rat.move.playerRow;
		shiftCard = rat.move.shiftCard;

		lastlastCol = lastCol;
		lastlastRow = lastRow;
		lastCol = playerCol;
		lastRow = playerRow;

	}

	@SuppressWarnings("cast")
	private void evaluateBoard(Move move, OwnBoard boardC) {
		MoveRating rating = new MoveRating();
		rating.rating = 0;
		rating.move = move;
		boolean treasureFound = false;
		if (boardC.getForbidden() != null) {
			if (move.cardRow == boardC.getForbidden().getRow()
					&& move.cardCol == boardC.getForbidden().getCol()) {
				rating.rating = -1;
				return;
			}
		}
		boardC.proceedShift(move.cardRow, move.cardCol, move.shiftCard);
		List<PositionType> positionsToReach = new LinkedList<PositionType>();
		PositionType playerPos = boardC.findPlayer(playerId);
		if (playerPos == null) {
			rating.rating = -1;
			return;
		}
		positionsToReach = boardC.getAllReachablePositions(playerPos);
		int distance = 14;
		PositionType tp = boardC.findTreasure(treasure);
		for (PositionType p : positionsToReach) {
			int d = Math.abs(tp.getCol() - p.getCol())
					+ Math.abs(tp.getRow() - p.getRow());

			if (d < distance) {
				if (samePosition && p.getCol() == lastCol
						&& p.getRow() == lastRow) {
					// do nothing!
				} else {
					distance = d;
					move.playerCol = p.getCol();
					move.playerRow = p.getRow();
				}

			}
			if (tp.getCol() == p.getCol() && tp.getRow() == p.getRow()) {
				rating.rating += WORTH_FOUND_TREASURE;
				move.playerCol = tp.getCol();
				move.playerRow = tp.getRow();
				treasureFound = true;
				break;
			}
		}
		if (!treasureFound) {
			rating.rating += (14 - distance) * WORTH_FACTOR_DISTANCE
					* playerRatings[playerId - 1];

		}

		// rate every players Position
		for (int i = 1; i <= 4; i++) {
			if (i == playerId) {
				continue;
			}

			if (!isPlayerAv[i - 1]) {
				continue;
			}

			int numberOfTreasuresToReach = 0;
			int numberOfPositionsToReach = 0;
			List<PositionType> positions = new LinkedList<PositionType>();
			PositionType opponent = boardC.findPlayer(i);
			if (opponent == null) {
				rating.rating = -1;
				continue;
			}
			positions = boardC.getAllReachablePositions(opponent);
			numberOfPositionsToReach += positions.size();
			for (PositionType p : positions) {
				if (boardC.getCard(p.getRow(), p.getCol()).getTreasure() != null) {
					numberOfTreasuresToReach++;
				}
			}
			rating.rating += (49 - numberOfPositionsToReach)
					* WORTH_FACTOR_POSITION * playerRatings[i - 1];
			rating.rating += (int) ((25 - numberOfTreasuresToReach)
					* WORTH_FACTOR_TREASURE * playerRatings[i - 1]);
		}

		// add in ascending order
		int index = ratings.size();
		for (int i = 0; i < ratings.size(); i++) {

			if (rating.rating > ratings.get(i).rating) {
				index = i;
				break;
			}
		}
		ratings.add(index, rating);
	}

}
