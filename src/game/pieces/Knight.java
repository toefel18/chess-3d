package game.pieces;

import java.util.HashSet;
import java.util.Set;

import display.displayobjects.pieces.KnightDisplayObject;

import game.Board;
import game.Piece;
import game.Player;
import game.Square;
import game.commands.Move;

public class Knight extends Piece
{
	private KnightDisplayObject knightDisplayObject;
	
	public Knight(Board board, Player player, Square square, int id)
	{
		super(board, player, square, id);
		
		knightDisplayObject = new KnightDisplayObject(board.getGame().getScene(), this);
	}

	public KnightDisplayObject getDisplayObject()
	{
		return knightDisplayObject;
	}
	

	public Set<Move> getAvailableMoves()
	{
		int x = square.getX();
		int y = square.getY();
				
		Set<Move> moves = new HashSet<Move>();
			
		int [][]locations = {	{x - 1, y + 2},
								{x - 1, y - 2},
								{x - 2, y + 1},
								{x - 2, y - 1},
								{x + 1, y + 2},
								{x + 1, y - 2},
								{x + 2, y + 1},
								{x + 2, y - 1}
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