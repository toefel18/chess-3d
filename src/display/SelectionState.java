package display;

public interface SelectionState
{
	/**
	 * Is called whenever an object is selected
	 * @param displayObject
	 */
	public void select(DisplayObject displayObject);
	
	/**
	 * Is called whenever a state should be destroyed because it would otherwise be invalid
	 */
	public void destroyState();
}
