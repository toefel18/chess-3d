package display;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.Vector;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;

import com.sun.opengl.util.BufferUtil;

import game.Board;

public class Scene implements MouseMoveListener, MouseListener
{
	protected static final int MOUSE_DOWN = 1;
	
	protected int mouseState = 0;
	
	protected Map<Integer, DisplayObject> displayObjects = new HashMap<Integer, DisplayObject>();
	
	protected DisplayManager displayManager;

	//protected MouseInteractionManager interactionManager = new MouseInteractionManager(this);
	
	private Point mousePoint;
    
	public void registerDisplayObject(DisplayObject displayObject)
	{
		displayObjects.put(displayObject.getId(), displayObject);
	}
	
	public void unregisterDisplayObject(DisplayObject displayObject)
	{
		displayObjects.remove(displayObject);
	}
	
	public void focussed(DisplayManager displayManager)
	{
		this.displayManager = displayManager;
		
		displayManager.getCanvas().addMouseListener(this);
		displayManager.getCanvas().addMouseMoveListener(this);
	}
	
	public void unfocussed()
	{
		this.displayManager = null;
	}
	
	public void draw(DisplayManager displayManager, int mode)
	{
		GL gl = displayManager.getGL();
	        
		drawObjects(gl, mode);
	}
	
	private void drawObjects(GL gl, int mode)
	{
		for(DisplayObject displayObject : displayObjects.values())
		{
			gl.glPushMatrix();
			
			displayObject.draw(displayManager);
			
			gl.glPopMatrix();
		}
	}
	
	public DisplayObject getDisplayObject(int id)
	{
		return displayObjects.get(id);
	}
	
	/**
	 * Returns all the display objects in an unmodifiable map (for safety reasons)
	 * @return
	 */
	public Map<Integer, DisplayObject> getDisplayObjects()
	{
		return Collections.unmodifiableMap(displayObjects);
	}
	
	public void mouseMove(MouseEvent mouseEvent)
	{
		mousePoint = new Point(mouseEvent.x, mouseEvent.y);
	}

	public void mouseDoubleClick(MouseEvent arg0)
	{
		
	}
	
	public void mouseDown(MouseEvent arg0)
	{
		mouseState |= MOUSE_DOWN;
	}

	public void mouseUp(MouseEvent arg0)
	{
		mouseState &= ~MOUSE_DOWN;
	}
	
	public DisplayManager getDisplayManager()
	{
		return displayManager;
	}
	
	public int getDisplayObjectCount()
	{
		return displayObjects.size();
	}
}
