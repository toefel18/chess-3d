package game;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.sun.opengl.cg.CgGL;

import display.Window;
import display.scenes.GameScene;
import display.window.NewGame;

import game.Game;
import game.chessstrategies.RandomMove;
import game.commands.Delay;
import game.players.BotPlayer;
import game.players.HumanPlayer;

public class Manager
{
	private Game game;
	private Window window;
	
	private static Manager instance = new Manager();
	
	public void start()
	{
		NewGame newGame = new NewGame(null);
		
		newGame.openBlocking();
		
		if(!newGame.isValid())
		{
			System.exit(0);
		}
		
		game = new Game(newGame.getPlayer1(), newGame.getPlayer2());
		
		game.startGame();
		
		window 	= new Window();
		
		new Thread(window).start();
		
		// @debug
		/*
		 * 
		 * 
		new Delay(game.getPlayer(Game.COLOR_BLACK), 1000).execute();
		new Delay(game.getPlayer(Game.COLOR_BLACK), 1000).execute();
		new Delay(game.getPlayer(Game.COLOR_BLACK), 1000).execute();
		new Delay(game.getPlayer(Game.COLOR_BLACK), 1000).execute();
		
		new Timer().schedule(new TimerTask()
		{
			public void run()
			{
				try
				{
					new Delay(game.getPlayer(Game.COLOR_BLACK), 1000).execute();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}, 1000);
		
		new Timer().schedule(new TimerTask()
		{
			public void run()
			{
				try
				{
					new Delay(game.getPlayer(Game.COLOR_BLACK), 1000).execute();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}, 2500);
		
		new Timer().schedule(new TimerTask()
		{
			public void run()
			{
				try
				{
					new SaveGame(game, "data/savegames/test.xml").save();
					new SaveGame(game, "data/savegames/test.xml").load();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}, 5000);
		// @debug end
		
		*
		*
		*/
		
		
		while(window.isActive())
		{
			try
			{
				Thread.sleep(100L);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		game.stopGame();
	}
		
	public static Manager getInstance()
	{
		if(instance == null)
			new Manager();
		
		return instance;
	}
		
	public Game getGame()
	{
		return game;
	}

	public GameScene getScene()
	{
		return game.getScene();
	}
	
	public Window getWindow()
	{
		return window;
	}
}