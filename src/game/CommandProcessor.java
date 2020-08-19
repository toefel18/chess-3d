package game;

import game.commands.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.Queue;


import org.w3c.dom.Element;

import display.GraphicalCommand;
import display.displayobjects.PieceDisplayObject;
import display.graphicalcommands.MovePiece;

public class CommandProcessor implements Runnable
{
	private Game game;
	private boolean isAlive = true;
	
	private static Map<String, Command> commands = new HashMap<String, Command>();
	
	private Stack<Command> executed 	= new Stack<Command>();
	private Queue<Command> toExecute 	= new LinkedList<Command>();
	
	private int undoTimes = 0;
	
	private Command isExecuting;
	
	public CommandProcessor(Game game)
	{
		this.game = game;
	}
	
	protected void execute(Command command)
	{
		toExecute.add(command);
	}
	
	private void doNext()
	{
		if(isExecuting != null)
			return;
		
		if(undoTimes > 0)
		{
			if(executed.size() > 0)
			{
				toExecute.clear();
			
				Command command = executed.pop();
				
				isExecuting = command;
				
				command.undo();
				
				undoTimes--;
			}
			else
				undoTimes = 0;
		}
		else if(toExecute.size() > 0)
		{
			Command command = toExecute.poll();
			
			isExecuting = command;
			
			if(command != null)
				command.start();
		}
	}
	/**
	 * TODO WARNING! this can cause an undo on a command that is not the last, if there are still commands in the queue
	 *  
	 */
	public void undo()
	{
		undoTimes++;
	}
	
	/**
	 * 
	 */
	public void undo(Command command)
	{
		if(executed.size() == 0)
			return;
		
		while(true)
		{
			if(executed.size() == 0)
				break;
			
			Command prevCommand = executed.pop();
			
			undo();
			
			if(prevCommand == command)
				break;
		}
	}
	
	public void completed(Command command)
	{
		isExecuting = null;
		
		executed.push(command);
	}

	public Stack<Command> getExecutedCommands()
	{
		return executed;
	}

	public static void registerCommand(Command command)
	{
		commands.put(command.getName(), command);
	}

	public static void restoreCommand(Game game, Element commandElement)
	{
		Command command = commands.get(commandElement.getAttribute("name")).getInstance(game, commandElement);
		//TODO this is not a nice, maybe search for a more flexible solution
		if(command instanceof Move)
		{
			Move move = (Move) command;
			GraphicalCommand graphicalMove = new MovePiece((PieceDisplayObject) move.getPiece().getDisplayObject(), move.getPiece().getSquare().getDisplayObject(), move.getNewSquare().getDisplayObject() );
			command.setGraphicalCommand(graphicalMove);
		}
		command.execute();
	}

	@Override
	public void run()
	{
		while(isAlive)
		{
			doNext();
			
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void setStopFlag(boolean stop)
	{
		isAlive = !stop;
	}
}
