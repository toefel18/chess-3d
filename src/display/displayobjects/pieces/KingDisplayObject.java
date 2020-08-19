package display.displayobjects.pieces;

import javax.media.opengl.GL;

import game.Piece;
import display.displayobjects.PieceDisplayObject;
import display.displayobjects.SquareDisplayObject;
import display.scenes.GameScene;

public class KingDisplayObject extends PieceDisplayObject
{
	protected static final String GROUP_NAME = "cylinder59";
	
	public KingDisplayObject(GameScene gameScene, Piece piece)
	{
		super(gameScene, piece);
	}

	protected String getGroupName()
	{
		return GROUP_NAME;
	}
	
	protected void setPosition(GL gl)
	{
		float[] plateLevel = this.getPosition();
		
		gl.glTranslatef(plateLevel[0] + 1f, plateLevel[1] - .2f, plateLevel[2] + SquareDisplayObject.TRUERADIUS + 0.001f - 0.15f);
	}
}
