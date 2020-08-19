package game;

import display.DisplayObject;
import game.commands.Move;

import java.util.Set;
public abstract class Piece
{
	protected Board 	board;
	protected Player 	player;
	protected Square	square;
	protected int 		timesMoved;
	private int 		id;
	
	public Piece(Board board, Player player, Square square, int id)
	{
		if(board == null)
			throw new IllegalArgumentException("Board shouldn't be empty.");
		
		if(player == null)
			throw new IllegalArgumentException("Player shouldn't be empty.");
		
		this.board = board;
		this.player = player;
		this.square = square;
		this.square.setPiece(this);
		this.timesMoved = 0;
		this.id = id;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public Square getSquare()
	{
		return square;
	}
	
	public void setSquare(Square square)
	{
		if(this.square != square)
		{
			this.square = square;
			
			if(square != null)
				square.setPiece(this);
		}
	}
	
	public int getPieceId()
	{
		return id;
	}
	
	public void incrementTimesMoved()
	{
		timesMoved++;
	}
	
	public void decrementTimesMoved()
	{
		if(--timesMoved < 0)
			System.err.println("This piece cannot be moved back more than it has be moved forward!");		
	}
	
	public int getTimesMoved()
	{
		return timesMoved;
	}
		
	public abstract DisplayObject getDisplayObject();
	
	/**
	 * Returns the moves that this piece can make
	 * It also includes the moves that maybe remove an enemy piece.
	 * moves that are not possible because some constraint are not returned
	 */
	public abstract Set<Move> getAvailableMoves();
	
	/**
	 * Returns a move if the given location is a valid move
	 * @param locations 2 integer values with x and y location
	 * @return
	 */
	protected Move validMoveLocation(int []locations)
	{
		if(board.coordsInRange(locations[0], locations[1]))
		{
			Square newPlace = board.getSquare(locations[0], locations[1]);
			
			Piece piece = newPlace.getPiece();
			
			if(piece != null)
			{				
				if(piece.getPlayer() != player)
					// captures opponent player piece
					return new Move(board, this, newPlace);
				
			} else 
				return new Move(board, this, newPlace);
		}
		
		return null;
	}
	
	/**
	 * Calculates if the piece can move to the location
	 * @note -For the ease of use and readability, this function does not use validMoveLocation
	 * @param locations
	 * @param validDirection
	 * @param id
	 * @return
	 */
	protected Move validMoveLocationExtended(int [][]locations,  boolean []validDirection, int id)
	{
		if(validDirection[id] && board.coordsInRange(locations[id][0], locations[id][1]))
		{
			Square newPlace = board.getSquare(locations[id][0], locations[id][1]);
			
			Piece piece = newPlace.getPiece();
			
			if(piece != null)
			{
				validDirection[id] = false;
				
				// this would capture other player object
				if(piece.getPlayer() != player)	
					return new Move(board, this, newPlace);
				
			}else
				// the location is empty, so move can be made
				return new Move(board, this, newPlace);
		}
		
		return null;
	}
}
