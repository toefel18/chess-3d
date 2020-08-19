package display;

import game.Manager;

import javax.media.opengl.GL;

import display.displayobjects.BoardDisplayObject;

public abstract class DisplayObject 
{
	protected Scene scene;
	
	private static int nextIdentifier = 1; 
	
	private int id;
	
	protected boolean selected = false;
	
	public DisplayObject(Scene scene)
	{
		this.scene = scene;
		
		id = nextIdentifier;
		
		nextIdentifier++;
		
		scene.registerDisplayObject(this);
	}
	
	protected int getId()
	{
		return id;
	}
	
	protected boolean mouseIsOver()
	{
		//return mouseIsOverAndFirstObject();
		return scene.getDisplayManager().getObjectPicker().mouseIsOver(this);
	}
	
	protected boolean mouseIsOverAndFirstObject()
	{
		return scene.getDisplayManager().getObjectPicker().mouseIsOverAndFirstObject(this);
	}
	
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}
	
	public boolean getSelected()
	{
		return selected;
	}
	
	protected boolean isSelected()
	{
		return selected;
		//return scene.getDisplayManager().getObjectPicker().isSelectedObject(this);
	}
	
	
	
	abstract public void draw(DisplayManager displayManager);
	
	
	public void mouseDoubleClick(MouseInteractionEvent e) 
	{
		//System.out.println("MouseDoubleClick");
	}

	public void mouseDown(MouseInteractionEvent e) 
	{
		/*
		 * SELECTION SHOULD NOT BE CONTROLLED BY THE OBJECTS SELF!
		 * SelectionStates control what's selected!
		 */
		//System.out.println("MouseDown");
		//setSelected(!this.selected);
	}

	//NOTE not yet implemented
	public void mouseEnter(MouseInteractionEvent e) 
	{
		System.out.println("MouseEnter");
	}
	//NOTE not yet implemented
	public void mouseExit(MouseInteractionEvent e) 
	{
		System.out.println("MouseExit");
	}

	public void mouseMove(MouseInteractionEvent e) 
	{
		//System.out.println("MouseMove");
	}

	public void mouseUp(MouseInteractionEvent e) 
	{
		//System.out.println("MouseUp");
	}

}
