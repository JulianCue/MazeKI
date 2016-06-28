package de.fhaachen.mazenet.Client;

import de.fhaachen.mazenet.generated.AcceptMessageType;
import de.fhaachen.mazenet.generated.AwaitMoveMessageType;
import de.fhaachen.mazenet.generated.DisconnectMessageType;
import de.fhaachen.mazenet.generated.ErrorType;
import de.fhaachen.mazenet.generated.MazeCom;
import de.fhaachen.mazenet.generated.MazeComType;
import de.fhaachen.mazenet.generated.WinMessageType;

public class MazeComHandler {
	private MazeComMessageSender sender;
	private int id;
	private int counter;

	public MazeComHandler(MazeComMessageSender sender) {
		this.sender = sender;
		counter = 0;
	}

	public void processMazeCom(MazeCom mc) {
		if (mc.getMcType() == MazeComType.AWAITMOVE) {
			AwaitMoveMessageType awm = mc.getAwaitMoveMessage();
			sender.doMove(awm.getBoard(), awm.getTreasuresToGo(),
					awm.getTreasure(), awm.getFoundTreasures());
			counter++;

		} else if (mc.getMcType() == MazeComType.LOGINREPLY) {
			this.id = mc.getId();
			sender.setId(id);
			System.out.println(id);
		} else if (mc.getMcType() == MazeComType.ACCEPT) {
			AcceptMessageType amt = mc.getAcceptMessage();
			if (amt.isAccept() == false) {
				if (amt.getErrorCode() == ErrorType.AWAIT_MOVE) {
					System.out.println("Second Move"); //$NON-NLS-1$
					sender.doSecondMove();
				} else if (amt.getErrorCode() == ErrorType.ILLEGAL_MOVE) {
					System.out.println("Second Move - illegal"); //$NON-NLS-1$
					sender.doSecondMove();
				} else if (amt.getErrorCode() == ErrorType.AWAIT_LOGIN) {
					sender.login("hehe xd"); //$NON-NLS-1$
				}
			}
		} else if (mc.getMcType() == MazeComType.WIN) {
			WinMessageType wmt = mc.getWinMessage();
			System.out.println("Moves: " + counter); //$NON-NLS-1$
			if (wmt.getWinner().getId() == id) {
				System.out.println("gg easy"); //$NON-NLS-1$
			} else {
				System.out
						.println("The winner is: " + wmt.getWinner().getValue()); //$NON-NLS-1$
				System.out.println("gg easy"); //$NON-NLS-1$
			}

		} else if (mc.getMcType() == MazeComType.DISCONNECT) {
			DisconnectMessageType dmt = mc.getDisconnectMessage();
			if (dmt.getErrorCode() == ErrorType.TOO_MANY_TRIES) {
				System.out.println("Disconnected, too many tries."); //$NON-NLS-1$
			}

		}

	}
}
