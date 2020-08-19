package game;

import display.DisplayObject;
import display.displayobjects.SquareDisplayObject;

public class Square
{
	private Board board;
	private int x;
	private int y;
	
	private SquareDisplayObject displayObject;
	private Piece piece;
	
	public Square(Board board, int x, int y)
	{
		this.board = board;
		this.x = x;
		this.y = y;
		
		displayObject = new SquareDisplayObject(board.getGame().getScene(), this);
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}

	public SquareDisplayObject getDisplayObject()
	{
		return displayObject;
	}
	
	public Piece getPiece()
	{
		return piece;
	}
	
	public void setPiece(Piece piece)
	{
		if(this.piece != piece)
		{
			this.piece = piece;
			if(piece != null)
				piece.setSquare(this);
		}
	}
}
