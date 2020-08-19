package display.displayobjects.pieces;

import javax.media.opengl.GL;

import game.Game;
import game.Piece;
import display.displayobjects.PieceDisplayObject;
import display.displayobjects.SquareDisplayObject;
import display.scenes.GameScene;

public class KnightDisplayObject extends PieceDisplayObject
{
	protected static final String GROUP_NAME = "cylinder31";
	
	public KnightDisplayObject(GameScene gameScene, Piece piece)
	{
		super(gameScene, piece);
	}

	protected void setPosition(GL gl)
	{
		float[] plateLevel = this.getPosition();
		
		if(piece.getPlayer().getColor() == Game.COLOR_BLACK)
		{
			gl.glTranslatef(plateLevel[0] - .2f + .4f, plateLevel[1] - .2f, plateLevel[2] + SquareDisplayObject.TRUERADIUS + 0.001f - 0.15f);
	
			gl.glRotatef(90f, 0f, 0f, 1f);
		}
		else
		{
			gl.glTranslatef(plateLevel[0] - .2f, plateLevel[1] - .2f + .4f, plateLevel[2] + SquareDisplayObject.TRUERADIUS + 0.001f - 0.15f);
			
			gl.glRotatef(270f, 0f, 0f, 1f);
		}
	}
	
	protected String getGroupName()
	{
		return GROUP_NAME;
	}
}
