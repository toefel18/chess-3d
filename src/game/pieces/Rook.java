package game.pieces;

import java.util.HashSet;
import java.util.Set;

import display.displayobjects.pieces.RookDisplayObject;

import game.Board;
import game.Piece;
import game.Player;
import game.Square;
import game.commands.Move;

public class Rook extends Piece
{
	private RookDisplayObject rookDisplayObject;
	
	public Rook(Board board, Player player, Square square, int id)
	{
		super(board, player, square, id);
		
		rookDisplayObject = new RookDisplayObject(board.getGame().getScene(), this);
	}

	public RookDisplayObject getDisplayObject()
	{
		return rookDisplayObject;
	}
	public Set<Move> getAvailableMoves()
	{
		int x = square.getX();
		int y = square.getY();
		
		boolean validDirection[] = {true, true, true, true};
		
		Set<Move> moves = new HashSet<Move>();
		
		for(int i = 1; i < Board.SQUARESPERROW; i++)
		{
			int [][]locations = {	{x + i, y},
									{x - i, y},
									{x, y + i},
									{x, y - i},
								};
			
			for(int j = 0; j < 4; j++)
			{
				Move move = validMoveLocationExtended(locations, validDirection, j);
				if(move != null)
					moves.add(move);
			}
		}
		
		return moves;
	}
	
}