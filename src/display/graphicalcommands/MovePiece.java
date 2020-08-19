package display.graphicalcommands;

import display.GraphicalCommand;
import display.displayobjects.PieceDisplayObject;
import display.displayobjects.SquareDisplayObject;

public class MovePiece extends GraphicalCommand {

	PieceDisplayObject pieceDisplayObject;
	SquareDisplayObject source;
	SquareDisplayObject dest;
	
	private static final int NUM_TIME_FRAMES = 200;
	private static final int MOVE_TIME = 800;
	
	public MovePiece(PieceDisplayObject pieceDisplayObject, SquareDisplayObject source, SquareDisplayObject dest)
	{
		this.pieceDisplayObject = pieceDisplayObject;
		this.source = source;
		this.dest = dest;
	}
	
	@Override
	public void start() {
		float[] source = this.source.getBasePlane().clone();
		float[] dest = this.dest.getBasePlane();
		
		float[] translation = {	(dest[0] - source[0]) / NUM_TIME_FRAMES, 
								(dest[1] - source[1]) / NUM_TIME_FRAMES, 
								(dest[2] - source[2]) / NUM_TIME_FRAMES};
		
		//always call this method prior to setOverridePosition!
		pieceDisplayObject.setBasePosition(source[0], source[1], source[2]);
		
		pieceDisplayObject.setOverridePosition(true);
		
		double sineValue = 0;
		double sineIncrement = Math.PI / NUM_TIME_FRAMES;
		
		long sleepTime = MOVE_TIME / NUM_TIME_FRAMES; 
			
		for(int i = 0; i < NUM_TIME_FRAMES; i++)
		{
			sineValue += sineIncrement;
			
			source[0] += translation[0];
			source[1] += translation[1];
			//source[2] += translation[2];
			
			pieceDisplayObject.setBasePosition(source[0], source[1], source[2] + ((float)Math.sin(sineValue) / 2));
			
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		pieceDisplayObject.setOverridePosition(false);
				
	}

}
