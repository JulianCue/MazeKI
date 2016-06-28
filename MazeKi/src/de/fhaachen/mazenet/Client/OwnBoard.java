package de.fhaachen.mazenet.Client;

import java.util.ArrayList;
import java.util.List;

import de.fhaachen.mazenet.generated.BoardType;
import de.fhaachen.mazenet.generated.CardType;
import de.fhaachen.mazenet.generated.CardType.Openings;
import de.fhaachen.mazenet.generated.CardType.Pin;
import de.fhaachen.mazenet.generated.MoveMessageType;
import de.fhaachen.mazenet.generated.PositionType;
import de.fhaachen.mazenet.generated.TreasureType;
import de.fhaachen.mazenet.networking.Messages;
import de.fhaachen.mazenet.tools.Debug;
import de.fhaachen.mazenet.tools.DebugLevel;

public class OwnBoard extends BoardType {

	private TreasureType currentTreasure;

	public OwnBoard(BoardType boardType) {
		super();
		PositionType forbiddenPositionType = boardType.getForbidden();
		forbidden = (forbiddenPositionType != null) ? new Position(
				forbiddenPositionType) : null;
		shiftCard = new Card(boardType.getShiftCard());
		this.getRow();
		for (int i = 0; i < 7; i++) {
			this.getRow().add(i, new Row());
			this.getRow().get(i).getCol();
			for (int j = 0; j < 7; j++) {
				this.getRow()
						.get(i)
						.getCol()
						.add(j,
								new Card(boardType.getRow().get(i).getCol()
										.get(j)));
			}
		}
	}

	public void setCard(int row, int col, Card c) {
		// Muss ueberschrieben werden, daher zuerst entfernen und dann...
		this.getRow().get(row).getCol().remove(col);
		// ...hinzufuegen
		this.getRow().get(row).getCol().add(col, c);
	}

	public CardType getCard(int row, int col) {
		return this.getRow().get(row).getCol().get(col);
	}

	// Fuehrt nur das Hereinschieben der Karte aus!!!
	public void proceedShift(MoveMessageType move) {
		Debug.print(
				Messages.getString("Board.proceedShiftFkt"), DebugLevel.DEBUG); //$NON-NLS-1$
		Position sm = new Position(move.getShiftPosition());
		if (sm.getCol() % 6 == 0) { // Col=6 oder 0
			if (sm.getRow() % 2 == 1) {
				// horizontal schieben
				int row = sm.getRow();
				int start = (sm.getCol() + 6) % 12; // Karte die rausgenommen
													// wird
				setShiftCard(getCard(row, start));

				if (start == 6) {
					for (int i = 6; i > 0; --i) {
						setCard(row, i, new Card(getCard(row, i - 1)));
					}
				} else {// Start==0
					for (int i = 0; i < 6; ++i) {
						setCard(row, i, new Card(getCard(row, i + 1)));
					}
				}
			}
		} else if (sm.getRow() % 6 == 0) {
			if (sm.getCol() % 2 == 1) {
				// vertikal schieben
				int col = sm.getCol();
				int start = (sm.getRow() + 6) % 12; // Karte die rausgenommen
													// wird
				setShiftCard(getCard(start, col));
				if (start == 6) {
					for (int i = 6; i > 0; --i) {
						setCard(i, col, new Card(getCard(i - 1, col)));
					}
				} else {// Start==0
					for (int i = 0; i < 6; ++i) {
						setCard(i, col, new Card(getCard(i + 1, col)));
					}
				}

			}
		}
		forbidden = sm.getOpposite();
		Card c = null;
		c = new Card(move.getShiftCard());
		// Wenn Spielfigur auf neuer shiftcard steht,
		// muss dieser wieder aufs Brett gesetzt werden
		// Dazu wird Sie auf die gerade hereingeschoben
		// Karte gesetzt
		if (!shiftCard.getPin().getPlayerID().isEmpty()) {
			// Figur zwischenspeichern
			Pin temp = shiftCard.getPin();
			// Figur auf SchiebeKarte lschen
			shiftCard.setPin(new Pin());
			// Zwischengespeicherte Figut auf
			// neuer Karte plazieren
			c.setPin(temp);
		}
		setCard(sm.getRow(), sm.getCol(), c);
	}

	public void proceedShift(int rowo, int colo, CardType shiftCard) {
		Debug.print(
				Messages.getString("Board.proceedShiftFkt"), DebugLevel.DEBUG); //$NON-NLS-1$
		Position sm = new Position(rowo, colo);
		if (sm.getCol() % 6 == 0) { // Col=6 oder 0
			if (sm.getRow() % 2 == 1) {
				// horizontal schieben
				int row = sm.getRow();
				int start = (sm.getCol() + 6) % 12; // Karte die rausgenommen
													// wird
				setShiftCard(getCard(row, start));

				if (start == 6) {
					for (int i = 6; i > 0; --i) {
						setCard(row, i, new Card(getCard(row, i - 1)));
					}
				} else {// Start==0
					for (int i = 0; i < 6; ++i) {
						setCard(row, i, new Card(getCard(row, i + 1)));
					}
				}
			}
		} else if (sm.getRow() % 6 == 0) {
			if (sm.getCol() % 2 == 1) {
				// vertikal schieben
				int col = sm.getCol();
				int start = (sm.getRow() + 6) % 12; // Karte die rausgenommen
													// wird
				setShiftCard(getCard(start, col));
				if (start == 6) {
					for (int i = 6; i > 0; --i) {
						setCard(i, col, new Card(getCard(i - 1, col)));
					}
				} else {// Start==0
					for (int i = 0; i < 6; ++i) {
						setCard(i, col, new Card(getCard(i + 1, col)));
					}
				}

			}
		}
		forbidden = sm.getOpposite();
		Card c = null;
		c = new Card(shiftCard);
		// Wenn Spielfigur auf neuer shiftcard steht,
		// muss dieser wieder aufs Brett gesetzt werden
		// Dazu wird Sie auf die gerade hereingeschoben
		// Karte gesetzt
		if (!shiftCard.getPin().getPlayerID().isEmpty()) {
			// Figur zwischenspeichern
			Pin temp = shiftCard.getPin();
			// Figur auf SchiebeKarte lschen
			shiftCard.setPin(new Pin());
			// Zwischengespeicherte Figut auf
			// neuer Karte plazieren
			c.setPin(temp);
		}
		setCard(sm.getRow(), sm.getCol(), c);
	}

	/**
	 * gibt zurueck ob mit dem Zug der aktuelle Schatz erreicht wurde
	 * 
	 * @param move
	 * @param currPlayer
	 * @return
	 */
	public boolean proceedTurn(MoveMessageType move, Integer currPlayer) {
		Debug.print(
				Messages.getString("Board.proceedTurnFkt"), DebugLevel.DEBUG); //$NON-NLS-1$

		this.proceedShift(move);
		Position target = new Position(move.getNewPinPos());
		movePlayer(findPlayer(currPlayer), target, currPlayer);
		Card c = new Card(getCard(target.getRow(), target.getCol()));
		return (c.getTreasure() == currentTreasure);

	}

	protected void movePlayer(PositionType oldPos, PositionType newPos,
			Integer playerID) {
		Debug.print(Messages.getString("Board.movePlayerFkt"), DebugLevel.DEBUG); //$NON-NLS-1$
		getCard(oldPos.getRow(), oldPos.getCol()).getPin().getPlayerID()
				.remove(playerID);
		getCard(newPos.getRow(), newPos.getCol()).getPin().getPlayerID()
				.add(playerID);
	}

	@Override
	public Object clone() {
		OwnBoard clone = new OwnBoard(this);
		if (forbidden == null) {
			clone.forbidden = null;
		} else {
			clone.forbidden = new Position(this.forbidden);
		}
		clone.shiftCard = new Card(this.shiftCard);
		clone.currentTreasure = this.currentTreasure;
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				clone.setCard(i, j, new Card(this.getCard(i, j)));
			}
		}
		return clone;
	}

	public boolean pathPossible(PositionType oldPos, PositionType newPos) {
		Debug.print(
				Messages.getString("Board.pathPossibleFkt"), DebugLevel.DEBUG); //$NON-NLS-1$
		if (oldPos == null || newPos == null)
			return false;
		Position oldP = new Position(oldPos);
		Position newP = new Position(newPos);
		return getAllReachablePositions(oldP).contains(newP);
	}

	public List<PositionType> getAllReachablePositions(PositionType position) {
		Debug.print(
				Messages.getString("Board.getAllReachablePositionsFkt"), DebugLevel.DEBUG); //$NON-NLS-1$
		List<PositionType> erreichbarePositionen = new ArrayList<PositionType>();
		int[][] erreichbar = new int[7][7];
		erreichbar[position.getRow()][position.getCol()] = 1;
		erreichbar = getAllReachablePositionsMatrix(position, erreichbar);
		for (int i = 0; i < erreichbar.length; i++) {
			for (int j = 0; j < erreichbar[0].length; j++) {
				if (erreichbar[i][j] == 1) {
					erreichbarePositionen.add(new Position(i, j));
				}
			}
		}
		return erreichbarePositionen;
	}

	int[][] getAllReachablePositionsMatrix(PositionType position,
			int[][] erreichbar) {
		for (PositionType p1 : getDirectReachablePositions(position)) {
			if (erreichbar[p1.getRow()][p1.getCol()] == 0) {
				erreichbar[p1.getRow()][p1.getCol()] = 1;
				getAllReachablePositionsMatrix(p1, erreichbar);
			}
		}
		return erreichbar;
	}

	private List<PositionType> getDirectReachablePositions(PositionType position) {
		List<PositionType> positionen = new ArrayList<PositionType>();
		CardType k = this.getCard(position.getRow(), position.getCol());
		Openings openings = k.getOpenings();
		if (openings.isLeft()) {
			if (position.getCol() - 1 >= 0
					&& getCard(position.getRow(), position.getCol() - 1)
							.getOpenings().isRight()) {
				positionen.add(new Position(position.getRow(), position
						.getCol() - 1));
			}
		}
		if (openings.isTop()) {
			if (position.getRow() - 1 >= 0
					&& getCard(position.getRow() - 1, position.getCol())
							.getOpenings().isBottom()) {
				positionen.add(new Position(position.getRow() - 1, position
						.getCol()));
			}
		}
		if (openings.isRight()) {
			if (position.getCol() + 1 <= 6
					&& getCard(position.getRow(), position.getCol() + 1)
							.getOpenings().isLeft()) {
				positionen.add(new Position(position.getRow(), position
						.getCol() + 1));
			}
		}
		if (openings.isBottom()) {
			if (position.getRow() + 1 <= 6
					&& getCard(position.getRow() + 1, position.getCol())
							.getOpenings().isTop()) {
				positionen.add(new Position(position.getRow() + 1, position
						.getCol()));
			}
		}
		return positionen;
	}

	public PositionType findPlayer(Integer PlayerID) {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				Pin pinsOnCard = getCard(i, j).getPin();
				for (Integer pin : pinsOnCard.getPlayerID()) {
					if (pin == PlayerID) {
						PositionType pos = new PositionType();
						pos.setCol(j);
						pos.setRow(i);
						return pos;
					}
				}
			}
		}
		// Pin nicht gefunden.
		// XXX: Darf eigentlich nicht vorkommen
		return null;
	}

	public PositionType findTreasure(TreasureType treasureType) {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				TreasureType treasure = getCard(i, j).getTreasure();
				if (treasure == treasureType) {
					PositionType pos = new PositionType();
					pos.setCol(j);
					pos.setRow(i);
					return pos;
				}
			}
		}
		// Schatz nicht gefunden, kann nur bedeuten, dass Schatz sich auf
		// Schiebekarte befindet
		PositionType pos = new PositionType();
		pos.setCol(10);
		pos.setRow(10);
		return pos;
	}

	public void setTreasure(TreasureType t) {
		currentTreasure = t;
	}

	public TreasureType getTreasure() {
		return currentTreasure;
	}
}
