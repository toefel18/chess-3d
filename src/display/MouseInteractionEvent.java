package display;

/**
 * Structure that represents a mouse event
 * @author Christophe
 *
 */
public class MouseInteractionEvent 
{
	public int x;
	public int y;
	public int clickCount;
	public int time;
	public int button;
	
	public MouseInteractionEvent(int x, int y, int button, int clickCount, int time)
	{
		this.x = x;
		this.y = y;
		this.clickCount = clickCount;
		this.time = time;
		this.button = button;
	}
	
	public MouseInteractionEvent()
	{
		
	}
}
