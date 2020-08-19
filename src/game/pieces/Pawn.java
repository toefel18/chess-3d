package game.pieces;

import java.awt.Point;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


import display.displayobjects.pieces.PawnDisplayObject;

import game.Board;
import game.Game;
import game.Piece;
import game.Player;
import game.Square;
import game.commands.Move;

public class Pawn extends Piece
{
	private PawnDisplayObject pawnDisplayObject;
	
	public Pawn(Board board, Player player, Square square, int id)
	{
		super(board, player, square, id);
		
		pawnDisplayObject = new PawnDisplayObject(board.getGame().getScene(), this);
	}

	public PawnDisplayObject getDisplayObject()
	{
		return pawnDisplayObject;
	}
	
	/**
	 * TODO revisit this code 
	 * Returns the moves that this piece can make
	 * It also includes the moves that maybe remove an enemy piece.
	 * moves that are not possible because some constraint are not returned
	 */
	public Set<Move> getAvailableMoves()
	{
		int x = square.getX();
		int y = square.getY();
		
		Set<Move> moves = new HashSet<Move>();
		Set<Point> points = new HashSet<Point>();
		
		int ydelta = player.getColor() == Game.COLOR_WHITE ? -1 : +1;
		
		//only valid when it captures an enemy piece
		points.add(new Point(x-1, y+ydelta));
		points.add(new Point(x+1, y+ydelta));
		
		Iterator<Point> itr = points.iterator();
		while(itr.hasNext())
		{
			Point p = itr.next();
			
			if(board.coordsInRange(p.x, p.y))
			{
				Square newPlace = board.getSquare(p.x, p.y);
				Player opponent = board.getGame().getOpponentPlayer(player);
				Piece pieceOnNewPlace = newPlace.getPiece();
				
				if(pieceOnNewPlace != null && pieceOnNewPlace.getPlayer() == opponent)
					moves.add(new Move(board, this, newPlace));
			}
		}
		
		//the only valid way to move if no special conditions arise
		if(board.coordsInRange(x, y+ydelta))
		{
			Square newPlace = board.getSquare(x, y+ydelta);
			
			if(newPlace.getPiece() == null)
			{
				//System.out.println("newPlace location: " + newPlace.getX() + ", " + newPlace.getY());
				moves.add(new Move(board, this, newPlace));
			}
		}
		
		return moves;
	}
}