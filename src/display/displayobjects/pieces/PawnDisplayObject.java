package display.displayobjects.pieces;

import javax.media.opengl.GL;

import com.obj.Group;

import game.Piece;
import display.displayobjects.PieceDisplayObject;
import display.displayobjects.SquareDisplayObject;
import display.scenes.GameScene;

public class PawnDisplayObject extends PieceDisplayObject
{
	protected static final String GROUP_NAME = "cylinder45";
	
	private Group group;
	
	public PawnDisplayObject(GameScene gameScene, Piece piece)
	{
		super(gameScene, piece);
	}
	
	protected void setPosition(GL gl)
	{
		float[] plateLevel = this.getPosition();
		
		gl.glTranslatef(plateLevel[0] - 1f, plateLevel[1] - .2f, plateLevel[2] + SquareDisplayObject.TRUERADIUS + 0.001f - 0.15f);
	}
	
	protected String getGroupName()
	{
		return GROUP_NAME;
	}
}
