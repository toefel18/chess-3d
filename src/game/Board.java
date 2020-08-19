package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import game.pieces.Bishop;
import game.pieces.King;
import game.pieces.Knight;
import game.pieces.Pawn;
import game.pieces.Queen;
import game.pieces.Rook;
import display.DisplayObject;
import display.displayobjects.BoardDisplayObject;
import display.displayobjects.pieces.KingDisplayObject;

public class Board
{
	public static final int SQUARESPERROW 	= 8;
	public static final int PIECESPERPLAYER = 16; 
	
	private Game game;
	private BoardDisplayObject boardDisplayObject;
	private Square[][] squares 	= new Square[SQUARESPERROW][SQUARESPERROW];
	private Piece[][] pieces 	= new Piece[2][PIECESPERPLAYER];
	//TODO compiler says this is not possible:
	//private ArrayList<Piece> []piecesRemoved = new ArrayList<Piece>[2]; 
	
	private ArrayList<Piece> piecesRemovedWhite = new ArrayList<Piece>();
	private ArrayList<Piece> piecesRemovedBlack = new ArrayList<Piece>();
	
	public Board(Game game)
	{
		this.game = game;
		
		initSquares();
		
		initPieces();
		
		boardDisplayObject = new BoardDisplayObject(game.getScene(), this);
	}
	
	private void initSquares()
	{
		for(int x = 0; x < SQUARESPERROW; x++)
			for(int y = 0; y < SQUARESPERROW; y++)
				squares[x][y] = new Square(this, x, y);
	}
	
	private void initPieces()
	{
		initPieceByPlayer(game.getPlayer(Game.COLOR_BLACK));
		initPieceByPlayer(game.getPlayer(Game.COLOR_WHITE));
	}
	
	private void initPieceByPlayer(Player player)
	{
		int color = player.getColor();
		
		int y = color == Game.COLOR_BLACK ? 0 : SQUARESPERROW - 1;
		
		for(int i = 0, x = 0; i < PIECESPERPLAYER; i++, x++)
		{
			if(x == SQUARESPERROW)
			{
				x  = 0;
				y += color == Game.COLOR_BLACK ? 1 : -1;
			}
			
			Piece piece;
			
			Square square = squares[x][y];
			
			if(y == 1 || y == SQUARESPERROW - 2)
				piece = new Pawn(this, player, square, i);
			
			else if((color == Game.COLOR_BLACK && x == 3) || (color == Game.COLOR_WHITE && x == 4))
				piece = new King(this, player, square, i);
			
			else if((color == Game.COLOR_BLACK && x == 4) || (color == Game.COLOR_WHITE && x == 3))
				piece = new Queen(this, player, square, i);
			
			else if(x == 2 || x == 5)
				piece = new Bishop(this, player, square, i);
			
			else if(x == 1 || x == 6)
				piece = new Knight(this, player, square, i);
			
			else
				piece = new Rook(this, player, square, i);
			
			pieces[color - 1][i] = piece;
		}
	}

	public Game getGame()
	{
		return game;
	}
	
	/**
	 * Moves the piece to the given location, and returns the piece that was on that location, prior to this movement
	 * @param movePiece
	 * @param location
	 * @return piece removed from board
	 */
	public Piece movePieceTo(Piece movePiece, Square location)
	{
		Piece oldPiece = location.getPiece();
		if(oldPiece != null)
		{
			if(oldPiece.getPlayer().getColor() == Game.COLOR_BLACK)
				piecesRemovedBlack.add(oldPiece);
			else
				piecesRemovedWhite.add(oldPiece);
			
			oldPiece.setSquare(null);
		}
		
		Square oldLocation = movePiece.getSquare();
		
		location.setPiece(movePiece);
		
		oldLocation.setPiece(null);
		
		return oldPiece;
	}
	
	public boolean coordsInRange(int x, int y)
	{
		if(x > Board.SQUARESPERROW - 1 || y > Board.SQUARESPERROW - 1 || x < 0 || y < 0)
			return false;
		
		return true;
	}
	
	public Square getSquare(int x, int y)
	{
		if(!coordsInRange(x, y))
		{
			//throw new ArrayIndexOutOfBoundsException("x and y vary between 0 and 7");
			
			return null;
		}
		
		return squares[x][y];		
	}
	
	/**
	 * Returns the piece of the player with the corresponding id
	 * @return
	 */
	public Piece getPiece(Player player, int id)
	{
		return this.getPiece(player.getColor(), id);
	}
	
	public Piece getPiece(int playerColor, int id)
	{
		for(int i = 0; i < Board.PIECESPERPLAYER; i++)
		{
			Piece piece = pieces[playerColor - 1][i];
			
			if(piece.getPieceId() == id)
				return piece;
		}
		
		//piece not found on the board, now look in the removed container
		
		List<Piece> removedPieces = playerColor == Game.COLOR_BLACK ? piecesRemovedBlack : piecesRemovedWhite;
		
		Iterator<Piece> itr = removedPieces.iterator();
		
		while(itr.hasNext())
		{
			Piece removedPiece = itr.next();
			
			if(removedPiece.getPieceId() == id)
				return removedPiece;
		}
		
		return null;
	}
	
	/**
	 * Returns the pieces form the player which can be used for playing, removed pieces will not be included
	 * @param player
	 * @return
	 */
	public Set<Piece> getPiecesFromPlayer(Player player)
	{
		return getPiecesFromPlayer(player.getColor());
	}
	
	/**
	 * Returns the pieces form the player which can be used for playing, removed pieces will not be included
	 * @param playerColor
	 * @return
	 */
	public Set<Piece> getPiecesFromPlayer(int playerColor)
	{
		Set<Piece> playerPieces = new HashSet<Piece>(); 
		
		for(Piece piece : pieces[playerColor - 1])
		{
			playerPieces.add(piece);
		}
		
		playerPieces.removeAll(getRemovedPieces(playerColor));
		
		return playerPieces;
	}
	
	
	public List<Piece> getRemovedPieces(Player player)
	{
		return getRemovedPieces(player.getColor());
	}
	
	public List<Piece> getRemovedPieces(int playerColor)
	{
		return Collections.unmodifiableList(playerColor == Game.COLOR_BLACK ? piecesRemovedBlack : piecesRemovedWhite);
	}
	
	public BoardDisplayObject getBoardDisplayObject()
	{
		return boardDisplayObject;
	}
}