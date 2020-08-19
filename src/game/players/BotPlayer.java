package game.players;

import display.GraphicalCommand;
import display.displayobjects.PieceDisplayObject;
import display.graphicalcommands.MovePiece;
import game.ChessStrategy;
import game.Player;
import game.commands.Move;

public class BotPlayer extends Player 
{

	ChessStrategy engine;
	
	public BotPlayer(ChessStrategy engine)
	{
		this.engine = engine;
	}
	
	public void setEngine(ChessStrategy engine)
	{
		this.engine = engine;
	}
	
	public ChessStrategy getEngine()
	{
		return this.engine;
	}
	
	@Override
	public void makeMove() 
	{
		System.out.println("BotPlayer's turn");
		
		Move move = engine.getPrefferedMove(this);
		// TODO hier is nullpointer exception
		if(move == null)
		{
			System.err.println("The botplayer wasn't able to make a move! No moves where available for any pieces");
			return;
		}
		
		GraphicalCommand graphicalMove = new MovePiece((PieceDisplayObject) move.getPiece().getDisplayObject(), move.getPiece().getSquare().getDisplayObject(), move.getNewSquare().getDisplayObject());
		
		move.setGraphicalCommand(graphicalMove);
		
		move.execute();
	}

}
