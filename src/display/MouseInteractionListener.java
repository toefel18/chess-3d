package display;

/**
 * This interfaces is created to separate SWT from the the display objects.
 * @author Christophe
 * @deprecated INTEGRATED INTO DISPLAY OBJECT, BECAUSE EACH DISPLAY OBJECT
 * SHOULD BE ABLE TO HANDLE THESE EVENDS, AND NO NEED TO SPLIT THIS UP BECAUSE
 * ALL THESE OBJECTS RESIDE IN THE SCENE AS DISPLAY OBJECTS ANYWAY
 *
 */
public interface MouseInteractionListener{

	public void mouseDoubleClick(MouseInteractionEvent e); 
	public void mouseDown(MouseInteractionEvent e) ;
	public void mouseUp(MouseInteractionEvent e);
	public void mouseMove(MouseInteractionEvent e);
	public void mouseEnter(MouseInteractionEvent e);
	public void mouseExit(MouseInteractionEvent e);
	//public void mouseHover(MouseInteractionEvent e);
	
}
