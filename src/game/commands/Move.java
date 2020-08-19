package game.commands;

import java.util.Timer;
import java.util.TimerTask;

import org.w3c.dom.Element;

import game.Board;
import game.Command;
import game.CommandProcessor;
import game.Game;
import game.Piece;
import game.Player;
import game.Square;
import game.pieces.King;

/**
 * Controls the actual moving on the board. It also supports rewinding the operation. 
 * 
 * Be careful, this move class is only valid when there is no other move executed.
 * When another move is executed, not executed instances of this class should be
 * considered invalid!
 * @author Christophe
 *
 */
public class Move extends Command
{
	Piece piece;
	Piece capturedPiece;
	Board board;
	//Square oldPlace;
	Square newPlace;
	boolean executed;
	
	static 
	{
		//TODO THIS ONLY GETS CALLED WHEN Move IS ACCESSED FOR THE FIRST TIME!! THIS IS PROBABLY NOT THE IDEAL SITUATION
		CommandProcessor.registerCommand(new Move());
	}
	
	public Move(Board board, Piece piece, Square newPlace)
	{
		super(board.getGame());
		
		this.board = board;
		this.piece = piece;
		//this.oldPlace = oldPlace;
		this.newPlace = newPlace;
		this.executed = false;
	}
		
	public Move()
	{
		
	}
			
	public Square getNewSquare()
	{
		return newPlace;
	}
	
	public Piece getPiece()
	{
		return this.piece;
	}
	
	/*
	public Square getOldSquare()
	{
		return oldPlace;
	}*/
		
	/**
	 * Rewinds the move, moves the piece back to its old location and places possibly removed pieces back.
	 * @return
	 */
	public boolean rewind()
	{
		if(executed == false)
		{
			return false;
		}
		
		//rewind code here
		
		executed = false;
		
		return true;
	}
	
	protected void start()
	{
		if(game.getActivePlayer() != piece.getPlayer())
			throw new IllegalStateException();
		
		//first execute graphical command, if not null
		
		if(graphicalCommand != null)
			graphicalCommand.start();
		
		capturedPiece = board.movePieceTo(piece, newPlace);
		
		if(capturedPiece instanceof King)
		{
			game.kingCaputred(capturedPiece);
		}
		
		piece.incrementTimesMoved();
		
		game.switchActivePlayer();
		
		completed();
	}
	
	public void saveState(Element commandElement)
	{
		super.saveState(commandElement);
		
		commandElement.setAttribute("player", 			String.valueOf(piece.getPlayer().getColor()));
		commandElement.setAttribute("pieceid", 			String.valueOf(piece.getPieceId()));
	//	commandElement.setAttribute("capturedpieceid", 	String.valueOf(capturedPiece == null ? -1 : capturedPiece.getPieceId()));
		//commandElement.setAttribute("oldx", 			String.valueOf(oldPlace.getX()));
		//commandElement.setAttribute("oldy", 			String.valueOf(oldPlace.getY()));
		commandElement.setAttribute("newx", 			String.valueOf(newPlace.getX()));
		commandElement.setAttribute("newy", 			String.valueOf(newPlace.getY()));
	//	commandElement.setAttribute("executed", 		String.valueOf(executed));
	}

	public Command getInstance(Game game, Element commandElement) {
		Board board = game.getBoard();
		Player player = game.getPlayer(Integer.parseInt(commandElement.getAttribute("player")));
		Piece piece = board.getPiece(player, Integer.parseInt(commandElement.getAttribute("pieceid")));
		//Piece capturedPiece = board.getPiece(player, Integer.parseInt(commandElement.getAttribute("capturedpieceid")));
		//Square oldPlace = board.getSquare(Integer.parseInt(commandElement.getAttribute("oldx")), Integer.parseInt(commandElement.getAttribute("oldy")));
		Square newPlace = board.getSquare(Integer.parseInt(commandElement.getAttribute("newx")), Integer.parseInt(commandElement.getAttribute("newy")));
		//boolean executed = Boolean.parseBoolean(commandElement.getAttribute("executed"));
		//TODO, this doesn't wokr
		return new Move(board, piece, newPlace);
	}
}
