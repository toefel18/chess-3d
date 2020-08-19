package game;

import game.chessstrategies.RandomMove;

import java.util.HashMap;
import java.util.Map;

public class ChessStrategyFactory 
{	
	private static Map<String, ChessStrategy> strategies = new HashMap<String, ChessStrategy>();
	
	static	// gets executed when this class is first accessed!
	{
		
		strategies.put("Random move", new RandomMove());
	}
	
	public static void registerStrategy(String name, ChessStrategy strategy)
	{
		// the name could be fetched via the Class.getName method, but User interfaces names tend to be different 
		strategies.put(name, strategy);
	}
	
	public static String[] getStrategyNames()
	{
		return strategies.keySet().toArray(new String[0]);
	}
	
	public static ChessStrategy getStrategy(String name)
	{
		ChessStrategy strategy = strategies.get(name);
		
		if(strategy == null)
			return null;
		
		try {
			return strategy.getClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
