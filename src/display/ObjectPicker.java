package display;

import game.Game;

import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;

import com.sun.opengl.util.BufferUtil;

import display.scenes.GameScene;

import display.selectionstates.*;

/**
 * Also manages the mouse events
 *
 */
public class ObjectPicker implements MouseMoveListener, MouseListener
{
	private GLU glu = new GLU();
	private DisplayManager displayManager;
	private Point mousePoint;
	private Scene scene;
	//private Game game;
	
	private int bufSize;
	private IntBuffer selectBuffer;
	private boolean inPickingMode = false;
	private int[]	selectedDisplayObjects = new int[] {};
	private int closestHoverObject = -1; 
	private int selectedObjectId = -1;
	private boolean mouseDown = false;
	private boolean singleSelection = true;
	private SelectionState selectionState;
	
	
	public ObjectPicker(DisplayManager displayManager)
	{
		this.displayManager = displayManager;
		this.selectionState = new SelectIgnoreState();
		Canvas canvas = displayManager.getCanvas();
		
		canvas.addMouseMoveListener(this);
		canvas.addMouseListener(this);
	}
	
	/**
	 * Focusses the picking and selection mechanism to the new scene 
	 * @param scene
	 */
	public void setScene(Scene scene)
	{
		if(this.scene != scene)
		{
			this.scene = scene; 
			
			selectionState.destroyState();
			
			if(this.scene instanceof GameScene)
			{
				Game game = ((GameScene)scene).getGame();
				
				setSelectionState(new SelectPieceState(this, game));
			}
		}
	}
	
	/**
	 * Returns the currently active scene
	 * @return
	 */
	public Scene getScene()
	{
		return scene;
	}
	
	public void startPicking()
	{
		if(mousePoint == null)
			return;
		
		inPickingMode = true;
		
		GL gl = displayManager.getGL();
		
		bufSize = displayManager.getCurrentScene().getDisplayObjectCount() + 1;
				
		selectBuffer = BufferUtil.newIntBuffer(bufSize);
		 
		int viewport[] = new int[4];
		 
		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
		
		gl.glSelectBuffer(bufSize, selectBuffer);
		gl.glRenderMode(GL.GL_SELECT);
		
		gl.glInitNames();
		gl.glPushName(-1);
		
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		
		glu.gluPickMatrix((double) mousePoint.x, (double) (viewport[3] - mousePoint.y), 1f, 1f, viewport, 0);
	
		Point canvasSize =  displayManager.getCanvas().getSize();
		
		glu.gluPerspective(45f, (float) canvasSize.x / (float) canvasSize.y, 1, 100);
	}

	public void endPicking()
	{
		if(!inPickingMode)
			return;
		
		GL gl = displayManager.getGL();
		
		gl.glPopMatrix();
	    gl.glFlush();
	
	    int hits = gl.glRenderMode(GL.GL_RENDER);
	   
	    int[] selectBuf = new int[bufSize];
		selectBuffer.get(selectBuf);
	    
	    processHits(hits, selectBuf);
	    
	    gl.glMatrixMode(GL.GL_MODELVIEW);
	}
	
	private void processHits(int hits, int buffer[])
	{
		if(hits < 0)
		{
			selectedDisplayObjects  = new int[0];
			
			closestHoverObject = -1; 
			
			return;
		}
		
		selectedDisplayObjects = new int[hits];
		
		int closestToViewpoint = 0;
			
		for (int y = 0, i = 3; i < (hits * 3 + 1); i += 4, y++)
		{		
			// the name precedes 2 depth values. The one with the smallest 
			// min-depth is the object closest to the screen
			if(buffer[i - 2] < closestToViewpoint)
			{
				closestToViewpoint = buffer[i - 2];
				
				closestHoverObject = buffer[i];
			}
			
			selectedDisplayObjects[y] = buffer[i];
		}
		
		if(mouseDown)
		{
			selectedObjectId = closestHoverObject;
			
			DisplayObject displayObject = scene.getDisplayObject(selectedObjectId);
			
			if(displayObject != null)
				selectionState.select(displayObject);
		}
	}
	
	/**
	 * TODO: Also implement mouseEnter en mouseExit with this!
	 */
	public void mouseMove(MouseEvent mouseEvent)
	{
		mousePoint = new Point(mouseEvent.x, mouseEvent.y);
		
		if(scene != null)
		{
			DisplayObject obj = scene.getDisplayObject(closestHoverObject);
			if(obj != null)
			{
				MouseInteractionEvent e = new MouseInteractionEvent(mouseEvent.x, mouseEvent.y, mouseEvent.button, mouseEvent.count, mouseEvent.time);
				obj.mouseMove(e);
			}
		}
	}

	public void mouseDoubleClick(MouseEvent mouseEvent)
	{
		if(scene != null)
		{
			DisplayObject obj = scene.getDisplayObject(closestHoverObject);
			if(obj != null)
			{
				MouseInteractionEvent e = new MouseInteractionEvent(mouseEvent.x, mouseEvent.y, mouseEvent.button, mouseEvent.count, mouseEvent.time);
				obj.mouseDoubleClick(e);
			}
		}
		
	}

	public void mouseDown(MouseEvent mouseEvent)
	{
		mouseDown = true;
		
		if(scene != null)
		{
			DisplayObject obj = scene.getDisplayObject(closestHoverObject);
			if(obj != null)
			{
				MouseInteractionEvent e = new MouseInteractionEvent(mouseEvent.x, mouseEvent.y, mouseEvent.button, mouseEvent.count, mouseEvent.time);
				obj.mouseDown(e);
			}
		}
		
	}

	public void mouseUp(MouseEvent mouseEvent)
	{
		mouseDown = false;
		
		if(scene != null)
		{
			DisplayObject obj = scene.getDisplayObject(closestHoverObject);
			if(obj != null)
			{
				MouseInteractionEvent e = new MouseInteractionEvent(mouseEvent.x, mouseEvent.y, mouseEvent.button, mouseEvent.count, mouseEvent.time);
				obj.mouseUp(e);
			}
		}
	}
	
	public boolean mouseIsOver(DisplayObject displayObject)
	{
		int id = displayObject.getId();
		
		for(int i = 0; i < selectedDisplayObjects.length; i++)
			if(selectedDisplayObjects[i] == id)
				return true;
		
		return false;
	}
	
	/**
	 * Returns true if the display object is the front most selected object. 
	 * @param displayObject
	 * @return 
	 */
	public boolean mouseIsOverAndFirstObject(DisplayObject displayObject)
	{
		int id = displayObject.getId();
				
		if(closestHoverObject == id) 
			return true;
		
		return false;
	}
	
	public void select(int objectId)
	{
		DisplayObject obj = scene.getDisplayObject(objectId);
		if(obj != null)
		{
			obj.setSelected(true);
		}
	}
	
	/**
	 * Unselects all objects
	 */
	public void unselectAllObjects()
	{
		for(DisplayObject obj: scene.getDisplayObjects().values())
		{
			if(obj.getSelected())
				obj.setSelected(false);
		}
	}
	
	/**
	 * Unselects all objects except the one marked as currently selected, 
	 * NOTE, this method DOES NOT select the selected!
	 */
	public void unselectAllObjectsExceptSelected()
	{
		for(DisplayObject obj: scene.getDisplayObjects().values())
		{
			if(obj.getId() != selectedObjectId && obj.getSelected())
				obj.setSelected(false);
		}
	}
	
	public void setSelectionState(SelectionState state)
	{
		selectionState = state;
	}
	
	public SelectionState getSelectionState()
	{
		return selectionState;
	}
	
	/**
	 * Returns the ID of the selected object
	 * @return
	 */
	public int getSelectedObjectId()
	{
		return selectedObjectId;
	}
	
	/**
	 * Configures the selection algorithm to allow or disallow multiple selection
	 * @param multipleSelect
	 */
	public void setMultipleSelect(boolean multipleSelect)
	{
		singleSelection = !multipleSelect;
	}
	
	/**
	 * Returns true if multiple graphical objects are allowed to be selected in-game
	 * @return
	 */
	public boolean getMultipleSelect()
	{
		return singleSelection;
	}
	
	/**
	 * Returns true if the given object is selected in the game
	 * @param displayObject
	 * @return
	 */
	public boolean isSelectedObject(DisplayObject displayObject)
	{
		return selectedObjectId == displayObject.getId();
	}
}
