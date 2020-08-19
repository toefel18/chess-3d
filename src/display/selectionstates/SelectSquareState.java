package display.selectionstates;

import java.util.Iterator;
import java.util.Set;

import display.DisplayObject;
import display.ObjectPicker;
import display.SelectionState;
import display.displayobjects.PieceDisplayObject;
import display.displayobjects.SquareDisplayObject;
import display.graphicalcommands.MovePiece;
import game.Game;
import game.Square;
import game.Piece;
import game.commands.Move;

public class SelectSquareState implements SelectionState {

	private ObjectPicker 		picker;
	
	private PieceDisplayObject 	pieceDisplayObject;
	
	private Game 				game;
	
	public SelectSquareState(ObjectPicker picker, PieceDisplayObject pieceDisplayObject, Game game)
	{
		this.picker = picker;
		
		System.out.println("SelectSquare state");
		
		this.pieceDisplayObject = pieceDisplayObject;
		
		this.game = game;
	}

	public void select(DisplayObject displayObject) 
	{
		if(displayObject instanceof PieceDisplayObject)
		{
			
			PieceDisplayObject selectedPiece = (PieceDisplayObject) displayObject;
			
			// if this piece is owned by the competitor, inject the square of that piece in this algorithm.
			if(selectedPiece.getPiece().getPlayer() != pieceDisplayObject.getPiece().getPlayer())
			{
				this.select(selectedPiece.getPiece().getSquare().getDisplayObject());
				
				return;
			}
			
			pieceDisplayObject.setSelected(false);
			
			pieceDisplayObject = (PieceDisplayObject)displayObject;
			
			// if piece is owned by other player, do not select!
			if(pieceDisplayObject.getPiece().getPlayer() != game.getActivePlayer())
			{
				picker.setSelectionState(new SelectPieceState(picker, game));
				
				return;
			}
			
			displayObject.setSelected(true);
			
			return;
		} 
		else if (displayObject instanceof SquareDisplayObject)
		{
			
			//TODO, inject ignore state, and select piece state later!
			Square square = ((SquareDisplayObject)displayObject).getSquare();
			
			Piece piece = pieceDisplayObject.getPiece();
			
			Set<Move> moves = piece.getAvailableMoves();
			
			Iterator<Move> itr = moves.iterator();
			
			while(itr.hasNext())
			{
				Move move = itr.next();
				
				if(move.getNewSquare() == square)
				{
					move.setGraphicalCommand(new MovePiece(pieceDisplayObject, pieceDisplayObject.getPiece().getSquare().getDisplayObject(), square.getDisplayObject()));
					
					move.execute();
					
					pieceDisplayObject.setSelected(false);
					
					picker.setSelectionState(new SelectPieceState(picker, this.game));
					
					try {
						Thread.sleep(100L);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
					break;
				}
			}
		}
		else
		{		
			pieceDisplayObject.setSelected(false);
			
			picker.setSelectionState(new SelectPieceState(picker, this.game));
		}
		
	}

	/**
	 * State is being destroyed so the piece should be deselected.
	 */
	public void destroyState() 
	{
		this.pieceDisplayObject.setSelected(false);
	}

}
