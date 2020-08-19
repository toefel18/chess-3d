package game.pieces;

import java.util.HashSet;
import java.util.Set;

import display.displayobjects.pieces.KingDisplayObject;

import game.Board;
import game.Piece;
import game.Player;
import game.Square;
import game.commands.Move;

public class King extends Piece
{
	private KingDisplayObject kingDisplayObject;
		
	public King(Board board, Player player, Square square, int id)
	{
		super(board, player, square, id);
		
		kingDisplayObject = new KingDisplayObject(board.getGame().getScene(), this);
	}

	public KingDisplayObject getDisplayObject()
	{
		return kingDisplayObject;
	}
	
	public Set<Move> getAvailableMoves()
	{
		int x = square.getX();
		int y = square.getY();
				
		Set<Move> moves = new HashSet<Move>();
			
		int [][]locations = {	{x - 1, y + 1},
								{x - 1, y},
								{x - 1, y - 1},
								{x, y + 1},
								{x, y - 1},
								{x + 1, y + 1},
								{x + 1, y},
								{x + 1, y - 1}
							};
			
		for(int i = 0; i < locations.length; i++)
		{
			Move move = validMoveLocation(locations[i]);
			
			if(move != null)
				moves.add(move);
		}
	
		
		return moves;
	}
	
}