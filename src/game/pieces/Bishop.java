package game.pieces;

import java.awt.Point;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import display.displayobjects.pieces.BishopDisplayObject;

import game.Piece;
import game.Board;
import game.Player;
import game.Square;
import game.commands.Move;

public class Bishop extends Piece {

	BishopDisplayObject bishopDisplayObject = null;
	
	public Bishop(Board board, Player player, Square square, int id)
	{
		super(board, player, square, id);
		
		bishopDisplayObject = new BishopDisplayObject(board.getGame().getScene(), this);
	}

	public BishopDisplayObject getDisplayObject()
	{
		return bishopDisplayObject;
	}
	
	public Set<Move> getAvailableMoves()
	{
		int x = square.getX();
		int y = square.getY();
		
		boolean validDirection[] = {true, true, true, true};
		
		Set<Move> moves = new HashSet<Move>();
		
		for(int i = 1; i < Board.SQUARESPERROW; i++)
		{
			int [][]locations = {	{x + i, y + i},
									{x + i, y - i},
									{x - i, y + i},
									{x - i, y - i},
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
