package game.pieces;

import java.util.HashSet;
import java.util.Set;

import display.displayobjects.pieces.QueenDisplayObject;

import game.Board;
import game.Piece;
import game.Player;
import game.Square;
import game.commands.Move;

public class Queen extends Piece
{
	private QueenDisplayObject queenDisplayObject;
	
	public Queen(Board board, Player player, Square square, int id)
	{
		super(board, player, square, id);
		
		queenDisplayObject = new QueenDisplayObject(board.getGame().getScene(), this);
	}

	public QueenDisplayObject getDisplayObject()
	{
		return queenDisplayObject;
	}
	
	public Set<Move> getAvailableMoves()
	{
		int x = square.getX();
		int y = square.getY();
		
		System.out.println("Calculating Moves of " + this.getClass().getSimpleName());
		
		boolean validDirection[] = {true, true, true, true, true, true, true, true};
		
		Set<Move> moves = new HashSet<Move>();
		
		for(int i = 1; i < Board.SQUARESPERROW; i++)
		{
			int [][]locations = {	//horizontal and vertical moves
									{x + i, y},
									{x - i, y},
									{x, y + i},
									{x, y - i},
									
									//diagonal moves
									{x + i, y + i},
									{x + i, y - i},
									{x - i, y + i},
									{x - i, y - i}
								};
			
			for(int j = 0; j < 8; j++)
			{
				Move move = validMoveLocationExtended(locations, validDirection, j);
				if(move != null)
					moves.add(move);
			}
		}
		
		return moves;
	}
}