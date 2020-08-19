package display.selectionstates;
import display.DisplayObject;
import display.SelectionState;

public class SelectIgnoreState implements SelectionState
{
	public void select(DisplayObject displayObject) 
	{
		// just ignore the selection!
	}

	public void destroyState() 
	{
		// just ignore
	}
}
