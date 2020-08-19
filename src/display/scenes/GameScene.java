package display.scenes;

import java.nio.ByteBuffer;

import game.Game;
import game.Square;

import javax.media.opengl.GL;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Rectangle;

import display.DisplayManager;
import display.Scene;
import display.ShapeLibrary;
import display.Texture;

public class GameScene extends Scene implements MouseMoveListener, MouseListener
{
	private int mouseX			= -1;
	private float lastRotation	= 180f;
	
	private float rotation = 0f;
	
	private boolean resetCamera = false;

	private Game game;

	public GameScene(Game game)
	{
		this.game = game;
	}

	public void draw(DisplayManager displayManager, int mode)
	{
		GL gl = displayManager.getGL();	
		
		gl.glPushMatrix();
		
		setupLightning(gl);		  
		
		// @debug
		//gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
		
		gl.glTranslatef(0.0f, 0.0f, -5.0f);
		
		if(resetCamera)
			resetCamera();
		
		rotate();
		
		gl.glRotatef(-50f, 		1f, 0f, 0f);
		gl.glRotatef(0f, 		0f, 1f, 0f);
		gl.glRotatef(rotation, 	0f, 0f, 1f);
		
		if(mode == DisplayManager.MODE_SCREEN)
			drawBackground(displayManager);
		
		super.draw(displayManager, mode);
		
		gl.glPopMatrix();
	}
	
	private void drawBackground(DisplayManager displayManager)
	{
		GL gl = displayManager.getGL();
		
		Rectangle canvasBounds = displayManager.getBounds();
		
		Texture texture = Texture.get("data/images/gamescenebackground.jpg");
		
		texture.bind(gl);

		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glDisable(GL.GL_LIGHTING);
		
		gl.glMatrixMode(GL.GL_PROJECTION);
		
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glOrtho(0, canvasBounds.width, 0, canvasBounds.height, -1, 1);
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		
		gl.glBegin(GL.GL_QUADS);
		{
			gl.glTexCoord2f(0f, 0f);
			gl.glVertex2f(0, 0);
			
			gl.glTexCoord2f(1f, 0f);
			gl.glVertex2f(canvasBounds.width, 0);
			
			gl.glTexCoord2f(1f, 1f);
			gl.glVertex2f(canvasBounds.width, canvasBounds.height);
			
			gl.glTexCoord2f(0f, 1f);
			gl.glVertex2f(0, canvasBounds.height);
		}
		gl.glEnd();
		
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPopMatrix();
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_LIGHTING);
		
		gl.glDisable(GL.GL_TEXTURE_2D);
		
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
	}
	
	private void setupLightning(GL gl)
	{
		gl.glEnable(GL.GL_LIGHTING);

		float diffuseLight[] 	= { 1f, 1f, 1f };
		float light_position[] 	= { 1.0f, 1.2f, 3.0f, .15f };
		
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, 	diffuseLight, 	0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, 	light_position, 0);
		  
		gl.glEnable(GL.GL_LIGHT0);

		gl.glEnable(GL.GL_COLOR_MATERIAL);
		gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
		gl.glMateriali(GL.GL_FRONT, 	GL.GL_SHININESS, 128);
	}

	private void rotate()
	{
		float mutation = lastRotation * 0.02f;
		
		if(lastRotation > .1f)
		{
			if(mutation > 2f)
				mutation = 2f;
			
			rotation += mutation;
			
			lastRotation -= mutation;
			
			if(lastRotation < 0.1f)
				lastRotation = 0;
		}
		else if(lastRotation < -.1f)
		{
			if(mutation < -2f)
				mutation = -2f;
			
			rotation += mutation;
			
			lastRotation -= mutation;
			
			if(lastRotation > -0.1f)
				lastRotation = 0;
		}
	}
	
	private void resetCamera()
	{
		rotation = rotation % 360f;
		
		if(rotation > 180f)
			rotation = (360f - rotation) * -1;
		
		else if(rotation < -180f)
			rotation = 360f - rotation * - 1;
		
		rotation /= 1.1f;
		
		if((rotation > 0 && rotation < 0.1f) || (rotation < 0 && rotation > -0.1f))
			resetCamera = false;
	}

	public void mouseMove(MouseEvent mouseEvent)
	{
		super.mouseMove(mouseEvent);
		
		if((mouseState & MOUSE_DOWN) > 0)
		{
			resetCamera = false;
			
			int mutation = 0;
			
			if(mouseX != -1)
			{
				if(mouseX > mouseEvent.x)
					mutation -= (mouseX - mouseEvent.x) * 0.1f;
				
				else if(mouseX < mouseEvent.x)
					mutation += (mouseEvent.x - mouseX) * 0.1f;
			}
			
			lastRotation = mutation;
			
			rotation += mutation;
		}
		
		mouseX = mouseEvent.x;
	}

	public void mouseDoubleClick(MouseEvent mouseEvent)
	{	
		super.mouseDoubleClick(mouseEvent);
		
		resetCamera = true;
	}
	
	public Game getGame()
	{
		return game;
	}
}
