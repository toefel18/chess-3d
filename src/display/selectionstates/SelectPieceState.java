package display.selectionstates;

import game.Game;
import display.DisplayObject;
import display.SelectionState;
import display.ObjectPicker;
import display.displayobjects.PieceDisplayObject;

public class SelectPieceState implements SelectionState {

	private ObjectPicker picker;
	private Game game;
	
	
	public SelectPieceState(ObjectPicker picker, Game game)
	{
		this.picker = picker;
		this.game = game;
	}
	
	public void select(DisplayObject displayObject) 
	{
		if(displayObject instanceof PieceDisplayObject)
		{
			PieceDisplayObject pieceDisplayObject = (PieceDisplayObject)displayObject;
			
			if(pieceDisplayObject.getPiece().getPlayer() != game.getActivePlayer())
				return;
			
			displayObject.setSelected(true);
			
			SelectionState newState = new SelectSquareState(picker, (PieceDisplayObject)displayObject, game);
			
			picker.setSelectionState(newState);
		}
	}

	public void destroyState() 
	{
		// nothing to do
	}
}
