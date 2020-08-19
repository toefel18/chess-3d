package display.displayobjects;

import game.Board;
import game.Square;

import javax.media.opengl.GL;

import display.DisplayManager;
import display.DisplayObject;
import display.Scene;
import display.ShapeLibrary;
import display.scenes.GameScene;

public class SquareDisplayObject extends DisplayObject
{
	private static final float PADDING 		= .1f;
	private static final float RADIUS 		= (BoardDisplayObject.PLATERADIUS - BoardDisplayObject.PLATEPADDING) / Board.SQUARESPERROW;
	public static final float TRUERADIUS 	= RADIUS * (1f - PADDING);
	
	private static final float[] COLOR_WHITE 		= new float[] { 1f / 255 * 0xF0, 1f / 255 * 0xDF, 1f / 255 * 0xC2 };
	private static final float[] COLOR_BLACK 		= new float[] { 1f / 255 * 0x45, 1f / 255 * 0x29, 1f / 255 * 0x04 };
	private static final float[] COLOR_HIGHLIGHT	= new float[] { 1f / 255 * 0xAB, 1f / 255 * 0xCF, 1f / 255 * 0xE4 };
	
	private GameScene gameScene;
	private Square	square;
	private boolean lightColored = false;
	
	public SquareDisplayObject(GameScene gameScene, Square square)
	{
		super(gameScene);
		
		this.square 	= square;
		this.gameScene 	= gameScene;
	}
	
	private void setColor(GL gl)
	{
		if(lightColored || mouseIsOverAndFirstObject())
			gl.glColor3f(COLOR_HIGHLIGHT[0], COLOR_HIGHLIGHT[1], COLOR_HIGHLIGHT[2]);

		else if(square.getX() % 2 == 0)
		{
			if(square.getY() % 2 == 0)
				gl.glColor3f(COLOR_WHITE[0], COLOR_WHITE[1], COLOR_WHITE[2]);
			
			else
				gl.glColor3f(COLOR_BLACK[0], COLOR_BLACK[1], COLOR_BLACK[2]);
		}
		else
		{
			if(square.getY() % 2 == 0)
				gl.glColor3f(COLOR_BLACK[0], COLOR_BLACK[1], COLOR_BLACK[2]);
			
			else
				gl.glColor3f(COLOR_WHITE[0], COLOR_WHITE[1], COLOR_WHITE[2]);
		}
	}
	
	public void draw(DisplayManager displayManager)
	{
		GL gl = displayManager.getGL();

		gl.glLoadName(getId());
		
		setColor(gl);
		
		float[] basePlace = getBasePlane();
		
		gl.glTranslatef(basePlace[0], basePlace[1], basePlace[2]);
		gl.glTranslatef(0f, 0f, 0.02f);
		
		ShapeLibrary.drawCube(gl, TRUERADIUS, TRUERADIUS, 0.001f);
	}
	
	/**
	 * makes the square rendering itself lightened, useful when possible moves have to be shown
	 * @param lightColored
	 */
	public void setLightColored(boolean lightColored)
	{
		this.lightColored = lightColored;
	}
	
	public boolean getLightColored()
	{
		return lightColored;
	}
	
	public Square getSquare()
	{
		return square;
	}

	public float[] getBasePlane()
	{
		BoardDisplayObject boardDisplayObject = gameScene.getGame().getBoard().getBoardDisplayObject();
		
		float[] plateLevel = boardDisplayObject.getPlateLevel();
		
		float beginX = plateLevel[0] - BoardDisplayObject.PLATERADIUS + BoardDisplayObject.PLATEPADDING + (RADIUS * (1f - PADDING));
		float beginY = plateLevel[1] - BoardDisplayObject.PLATERADIUS + BoardDisplayObject.PLATEPADDING + (RADIUS * (1f - PADDING));
		
		return new float[]	{
								beginX + ((square.getX() * RADIUS) * 2) + (RADIUS - TRUERADIUS),
								beginY + ((square.getY() * RADIUS) * 2) + (RADIUS - TRUERADIUS),
								plateLevel[2]
							};
	}
}
