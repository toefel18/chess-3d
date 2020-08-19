package game;

public abstract class Player
{
	private int color = -1;
	
	private Game game;
	
	public void setColor(int color)
	{
		this.color = color;
	}
	
	public void setGame(Game game)
	{
		this.game = game;
	}
	
	public abstract void makeMove();
	
	public int getColor()
	{
		if(color == -1)
			throw new IllegalStateException("Player does not have a color.");
		
		return color;
	}
	
	public Game getGame()
	{
		return game;
	}
}
