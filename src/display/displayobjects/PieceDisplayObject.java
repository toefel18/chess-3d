package display.displayobjects;

import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.Set;

import game.Board;
import game.Game;
import game.Piece;
import game.Square;
import game.commands.Move;

import javax.media.opengl.GL;

import com.obj.Face;
import com.obj.Group;
import com.obj.Vertex;
import com.sun.opengl.util.BufferUtil;

import display.DisplayManager;
import display.DisplayObject;
import display.Scene;
import display.ShapeLibrary;
import display.scenes.GameScene;

public abstract class PieceDisplayObject extends DisplayObject
{
	private static final float PADDING = 0.2f;
	
	private static final float[] COLOR_WHITE 		= new float[] { 1f / 255 * 0xF0, 1f / 255 * 0xDF, 1f / 255 * 0xC2 };
	private static final float[] COLOR_BLACK 		= new float[] { 1f / 255 * 0x23, 1f / 255 * 0x22, 1f / 255 * 0x20 };
	
	private 	GameScene 	gameScene;
	protected	Piece		piece;
	protected 	Group		group;
	protected 	FloatBuffer vertices;
	protected 	FloatBuffer	normals;
	
	protected boolean positionOverridden = false;
	protected float[] basePosition = {0f, 0f, 0f};	
	
	protected int displayListId = -1;
	
	public PieceDisplayObject(GameScene gameScene, Piece piece)
	{
		super(gameScene);

		this.piece 		= piece;
		this.gameScene 	= gameScene;
		
		init();
	}
	
	private void init()
	{
		group = ShapeLibrary.getGroup("data/models/pionki_3.obj", getGroupName()); 
		
		vertices 	= BufferUtil.newFloatBuffer(group.vertices.size() * 3);
		normals		= BufferUtil.newFloatBuffer(group.normals.size() * 3);
		    
		for(Face face : group.getFaces())
		{	
			Vertex[] vertices 	= face.getVertices();
			Vertex[] normals 	= face.getNormals();
				
			for(int i = 0; i < vertices.length; i++)
			{
				float mutation = -1 * 0.2f;
				
				this.vertices.put(vertices[i].getX() * mutation);
				this.vertices.put(vertices[i].getY() * mutation);
				this.vertices.put(vertices[i].getZ() * mutation * -1f);
				
				this.normals.put(normals[i].getX());
				this.normals.put(normals[i].getY());
				this.normals.put(normals[i].getZ());
			}
		}		    
	}
	
	protected void setColor(GL gl)
	{
		float alpha = 1f;
		
		//this block should be removed
		if( selected )
		{		
			alpha = .5f;
		}
					
		if(piece.getPlayer().getColor() == Game.COLOR_BLACK)
			gl.glColor4f(COLOR_BLACK[0], COLOR_BLACK[1], COLOR_BLACK[2], alpha);
		
		else
			gl.glColor4f(COLOR_WHITE[0], COLOR_WHITE[1], COLOR_WHITE[2], alpha);
	}
	
	public Piece getPiece()
	{
		return piece;
	}
	
	public void setSelected(boolean selected)
	{
		this.selected = selected;
		
		System.out.println("Calcing moves " + piece.getClass().getSimpleName());
		
		Set<Move> moves = piece.getAvailableMoves();
		
		if(moves == null)
			return;
	
		System.out.println("NumMoves: " + moves.size());
	
		Iterator<Move> itr = moves.iterator();
		
		while(itr.hasNext())
		{
			Move move = itr.next();
			
			//System.out.println("square light " + move.getNewSquare().getX() + ", " + move.getNewSquare().getY() );
			
			if(this.selected)
				move.getNewSquare().getDisplayObject().setLightColored(true);
			else
				move.getNewSquare().getDisplayObject().setLightColored(false);
		}
	}
	
	public void draw(DisplayManager displayManager)
	{
		if(piece.getSquare() == null) return;
		
		GL gl = displayManager.getGL();

		if(displayListId == -1)
		{
			displayListId = gl.glGenLists(1);
			
			// the following statements cannot be stored in display lists
			gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
		    gl.glEnableClientState(GL.GL_VERTEX_ARRAY);        
		   
		    normals.rewind();
		    vertices.rewind();
		    
		    // these are also invalid in display lists
		    gl.glNormalPointer(GL.GL_FLOAT, 0, normals);
		    gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertices);        
		    
			gl.glNewList(displayListId, GL.GL_COMPILE);
			{
				gl.glLoadName(getId());
															    
			    gl.glDrawArrays(GL.GL_TRIANGLES, 0, group.vertices.size());
			}
			gl.glEndList();
		}
		
		//PUSHMATRIX and POPMATRIX in display list can prohibit the effect of this??
		setPosition(gl);
		
		setColor(gl);
		
		gl.glCallList(displayListId);
	}
	
	/**
	 * Overrides the original position of this piece to the set of coordinates given by the method
	 * SetBasePosition. 
	 * 
	 * @note WARNING call setBasePosition prior to this method call
	 * @param overridePosition
	 */
	public void setOverridePosition(boolean overridePosition)
	{
		this.positionOverridden = overridePosition;
	}
	
	public boolean getOverridePosition()
	{
		return this.positionOverridden;
	}
	
	public void setBasePosition(float x, float y, float z)
	{
		basePosition[0] = x;
		basePosition[1] = y;
		basePosition[2] = z;
	}
	
	public float[] getBasePosition()
	{
		return basePosition;
	}
	
	protected float[] getPosition()
	{
		if(positionOverridden)
			return basePosition;
		
		return piece.getSquare().getDisplayObject().getBasePlane();		
	}
		
	protected abstract String getGroupName();
	
	protected abstract void setPosition(GL gl);
}
