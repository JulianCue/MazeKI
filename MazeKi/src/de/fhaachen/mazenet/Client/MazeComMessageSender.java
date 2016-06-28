package de.fhaachen.mazenet.Client;

import java.util.List;

import de.fhaachen.mazenet.generated.BoardType;
import de.fhaachen.mazenet.generated.MazeCom;
import de.fhaachen.mazenet.generated.MazeComType;
import de.fhaachen.mazenet.generated.ObjectFactory;
import de.fhaachen.mazenet.generated.PositionType;
import de.fhaachen.mazenet.generated.TreasureType;
import de.fhaachen.mazenet.generated.TreasuresToGoType;
import de.fhaachen.mazenet.networking.MazeComMessageFactory;
import de.fhaachen.mazenet.networking.XmlOutStream;

public class MazeComMessageSender {
	private XmlOutStream out;
	private MazeComMessageFactory of;
	private Logic logic;
	private int id;

	public MazeComMessageSender(XmlOutStream out, MazeComMessageFactory of,
			Logic logic) {
		this.out = out;
		this.of = of;
		this.logic = logic;;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void login(String name) {
		out.write(of.createLoginMessage((name)));
	}

	/**
	 * Retrieves the position from some class, creates the MazeComs and sends it
	 * 
	 * @param ct
	 *            CardType recieved in the AwaitMoveMEssage calling this
	 *            function
	 */
	public void doMove(BoardType board, List<TreasuresToGoType> treasuresToGo,
			TreasureType treasure, List<TreasureType> foundTreasures) {
		logic.calculateNextMove(board, treasuresToGo, treasure, id,
				foundTreasures);
		out.write(createMazeCom());
	}

	public void doSecondMove() {
		logic.getSecondMove();
		out.write(createMazeCom());
	}

	public MazeCom createMazeCom() {
		ObjectFactory asd = new ObjectFactory();
		MazeCom mc = asd.createMazeCom();
		mc.setMcType(MazeComType.MOVE);
		mc.setMoveMessage(asd.createMoveMessageType());
		PositionType playerPosition = new PositionType();
		playerPosition.setCol(logic.getPlayerCol());
		playerPosition.setRow(logic.getPlayerRow());
		PositionType cardPosition = new PositionType();
		cardPosition.setCol(logic.getCardCol());
		cardPosition.setRow(logic.getCardRow());
		mc.getMoveMessage().setShiftPosition(cardPosition);
		mc.getMoveMessage().setNewPinPos(playerPosition);
		mc.getMoveMessage().setShiftCard(logic.getShiftCard());

		return mc;
	}
}
