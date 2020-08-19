package game;

import game.pieces.King;
import display.Scene;
import display.scenes.GameScene;

public class Game
{
	public static final int	COLOR_BLACK	= 1;
	public static final int	COLOR_WHITE	= 2;
	
	private Board 				board;
	private GameScene 			scene;
	private Player				playerWhite;
	private Player 				playerBlack;
	private CommandProcessor 	commandProcessor;
	
	private Player 				currentPlayer;
	/*
	public Game(Player playerWhite, Player playerBlack)
	{
		this.playerWhite = playerWhite;
		this.playerBlack = playerBlack;
		
		playerWhite.setColor(COLOR_WHITE);
		playerBlack.setColor(COLOR_BLACK);
		
		playerWhite.setGame(this);
		playerBlack.setGame(this);
		
		currentPlayer = playerBlack;
			
		init();
	}
	*/
	
	public Game(Player playerWhite, Player playerBlack)
	{
		this.playerWhite = playerWhite;
		this.playerBlack = playerBlack;
		
		playerWhite.setColor(COLOR_WHITE);
		playerBlack.setColor(COLOR_BLACK);
		
		playerWhite.setGame(this);
		playerBlack.setGame(this);
		
		currentPlayer = null;
			
		init();
	}
	
	public void kingCaputred(Piece king)
	{
		if(king instanceof King)
		{
			if(king.getPlayer() == playerWhite)
			{
				System.out.println("Player white wins");
			}
			else if(king.getPlayer() == playerBlack)
			{
				System.out.println("Player black wins");
			}
			
			this.stopGame();
		}
	}
		
	public void setBlackPlayer(Player player)
	{
		if(playerBlack != null)
			throw new IllegalStateException("You cannot set a player in a game, once a player is already set. This would invalidate the internal configuration");
		
		playerBlack = player;
	}
	
	public void setWhitePlayer(Player player)
	{
		if(playerWhite != null)
			throw new IllegalStateException("You cannot set a player in a game, once a player is already set. This would invalidate the internal configuration");
		
		playerWhite = player;
	}
	
	private void init()
	{
		scene 				= new GameScene(this);
		
		board 				= new Board(this);
		
		commandProcessor 	= new CommandProcessor(this);
		
		new Thread(commandProcessor).start();
	}
	
	public void startGame()
	{
		currentPlayer = playerBlack;
				
		currentPlayer.makeMove();
	}
		
	public GameScene getScene()
	{
		return scene;
	}
	
	public Board getBoard()
	{
		return board;
	}
	
	public void switchActivePlayer()
	{
		currentPlayer = getOpponentPlayer(currentPlayer);
		//TODO  if this player is a computer, the selection state should be set to SelectStateIgnore
		currentPlayer.makeMove();
	}
	
	public Player getActivePlayer()
	{
		return currentPlayer;
	}
	
	public void stopGame()
	{
		commandProcessor.setStopFlag(true);
	}
	
	public CommandProcessor getCommandProcessor()
	{
		return commandProcessor;
	}
	
	public Player getPlayer(int playerColor)
	{
		if(playerColor == COLOR_BLACK)
			return playerBlack;
		
		else if(playerColor == COLOR_WHITE)
			return playerWhite;
		
		else
			throw new IllegalArgumentException("Given playerColor is unsupported.");
	}
	
	/**
	 * Returns the opponent of the given player
	 * @param player
	 * @return
	 */
	public Player getOpponentPlayer(Player player)
	{
		if(player == playerBlack)
			return playerWhite;
		else if (player == playerWhite)
			return playerBlack;
		else
			return null;
			
	}
}
