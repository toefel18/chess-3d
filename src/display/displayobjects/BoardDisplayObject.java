package display.displayobjects;

import java.nio.FloatBuffer;

import game.Board;
import game.Manager;

import javax.media.opengl.GL;

import com.obj.Face;
import com.obj.Group;
import com.obj.Vertex;
import com.sun.opengl.util.BufferUtil;

import display.DisplayManager;
import display.DisplayObject;
import display.Scene;
import display.ShapeLibrary;

public class BoardDisplayObject extends DisplayObject
{
	public static final float RADIUS 		= 2f;
	public static final float PLATERADIUS 	= RADIUS * 0.8f;
	public static final float PLATEPADDING 	= PLATERADIUS * 0.05f;
	
	protected static final float[] PLATECOLOR = new float[] { 1f / 255 * 0xAE, 1f / 255 * 0x80, 1f / 255 * 0x33 };
	protected static final float[] BOARDCOLOR = new float[] { 1f / 255 * 0x70, 1f / 255 * 0x2E, 1f / 255 * 0x0D };
	
	protected 	Board 		board;
	
	protected 	Group		group;
	protected 	FloatBuffer vertices;
	protected 	FloatBuffer	normals;
	
	protected int displayListId = -1;
	
	public BoardDisplayObject(Scene scene, Board board)
	{
		super(scene);
		
		this.board = board;
		
		init();
	}
	
	/**
	 * Unlights all squares on the board.
	 * TODO maybe find a better solution for this.
	 */
	public void unlightAllSquares()
	{
		for(int x = 0; x < Board.SQUARESPERROW; x++)
			for(int y = 0; y < Board.SQUARESPERROW; y++)
				board.getSquare(x, y).getDisplayObject().setLightColored(false);				
	}

	private void init()
	{
		group = ShapeLibrary.getGroup("data/models/pionki_3.obj", "cube1"); 
		
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
	
	public void draw(DisplayManager displayManager)
	{
		GL gl = displayManager.getGL();
		
		gl.glLoadName(getId());
		
		// Draw wood.
		{
			gl.glPushMatrix();
			
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
			
			gl.glTranslatef(0f, 0f, 0.05f);
			
			gl.glColor3f(BOARDCOLOR[0], BOARDCOLOR[1], BOARDCOLOR[2]);
  
			gl.glCallList(displayListId);
			
			gl.glPopMatrix();
		}
		
		// Draw plate.
		{
			gl.glColor3f(PLATECOLOR[0], PLATECOLOR[1], PLATECOLOR[2]);
	
			gl.glTranslatef(0f, 0f, RADIUS / 30);
			
			ShapeLibrary.drawCube(gl, PLATERADIUS, PLATERADIUS, 0.001f);
		}
	}

	public float[] getPlateLevel()
	{
		return new float[] { 0f, 0f, RADIUS / 30 };
	}
}