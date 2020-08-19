package display.displayobjects.pieces;

import javax.media.opengl.GL;

import display.scenes.GameScene;
import game.Piece;
import display.displayobjects.*;

public class BishopDisplayObject extends PieceDisplayObject 
{
	protected static final String GROUP_NAME = "cylinder471";
	
	public BishopDisplayObject(GameScene gameScene, Piece piece)
	{
		super(gameScene, piece);
	}

	protected void setPosition(GL gl)
	{
		float[] plateLevel = this.getPosition();
		
		gl.glTranslatef(plateLevel[0] + .2f, plateLevel[1] - .2f, plateLevel[2] + SquareDisplayObject.TRUERADIUS + 0.001f - 0.15f);
	}
	
	protected String getGroupName()
	{
		return GROUP_NAME;
	}
}
