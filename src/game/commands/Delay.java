package game.commands;

import java.util.Timer;
import java.util.TimerTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import game.Command;
import game.CommandProcessor;
import game.Game;
import game.Player;

// @note Just a test class.
public class Delay extends Command
{
	private Player 	player;
	private int 	delay;
	
	static 
	{
		//TODO THIS ONLY GETS CALLED WHEN Delay IS ACCESSED FOR THE FIRST TIME!! THIS IS PROBABLY NOT THE IDEAL SITUATION
		CommandProcessor.registerCommand(new Delay());
	}
	
	public Delay(Player player, int delay)
	{
		super(player.getGame());
		
		this.player = player;
		this.delay 	= delay;
	}

	public Delay()
	{
		super();
	}
	
	protected void start()
	{
		System.out.println("Execute command " + getName());
		
		//super.start();
		
		final Delay instance = this;
		
		new Timer().schedule(new TimerTask()
		{
			public void run()
			{
				instance.completed();
			}
		}, delay);
	}

	//TODO maybe the visitor or memento pattern can come in handy!
	public void saveState(Element commandElement)
	{
		super.saveState(commandElement);
		
		commandElement.setAttribute("player", 	String.valueOf(player.getColor()));
		commandElement.setAttribute("delay", 	String.valueOf(delay));
	}
	
	public Command getInstance(Game game, Element commandElement)
	{
		return new Delay(game.getPlayer(Integer.valueOf(commandElement.getAttribute("player"))), Integer.valueOf(commandElement.getAttribute("delay")));
	}
}
