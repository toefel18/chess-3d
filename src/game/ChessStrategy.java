package game;

import java.util.HashMap;
import java.util.Map;

import game.commands.Move;

public abstract class ChessStrategy 
{

	public ChessStrategy()
	{

	}
	
	public abstract Move getPrefferedMove(Player player);
}
