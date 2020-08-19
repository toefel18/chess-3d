package game;

import org.w3c.dom.Element;

import display.GraphicalCommand;

public abstract class Command
{
	protected Game game;
	
	protected boolean executionIsRequested = false;
	protected boolean executed = false;
	
	private CommandProcessor commandProcessor;
	protected GraphicalCommand graphicalCommand;
	
	
	public Command(Game game)
	{
		this.game 				= game;
		this.commandProcessor	= game.getCommandProcessor();
	}
	
	public Command()
	{
	}
	
	//TODO made this final so it cannot bypass the commandProcessor, review and discuss
	public final void execute()
	{
		if(executionIsRequested)
			throw new IllegalStateException("Command already executed.");
		
		executionIsRequested = true;
		
		commandProcessor.execute(this);
	}
	
	public void setGraphicalCommand(GraphicalCommand graphicalCommand)
	{
		this.graphicalCommand = graphicalCommand;
	}
	
	public GraphicalCommand getGraphicalCommand()
	{
		return this.graphicalCommand;
	}
	
	
	/**
	 * Returns true if this move command is executed
	 * @return
	 */
	public boolean isExecuted()
	{
		return !executed;
	}
	
	public void undo()
	{
		if(!executionIsRequested)
			throw new IllegalStateException("Command isn't executed (or requested to be executed).");
		
		commandProcessor.undo(this);
	}

	protected void completed()
	{
		commandProcessor.completed(this);
	}
	
	//TODO make abstract
	protected abstract void start();

	public String getName()
	{
		return this.getClass().getSimpleName();
	}

	public void saveState(Element commandElement)
	{
		commandElement.setAttribute("name", 	getName());
	}
	
	abstract public Command getInstance(Game game, Element commandElement);
}
