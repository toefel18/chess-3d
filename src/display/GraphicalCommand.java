package display;

import display.displayobjects.PieceDisplayObject;
import display.displayobjects.SquareDisplayObject;

public abstract class GraphicalCommand 
{
	PieceDisplayObject pieceDisplayObject;
	SquareDisplayObject source;
	SquareDisplayObject dest;
	
	
	public abstract void start();
	
}
