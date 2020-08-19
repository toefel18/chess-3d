package display.window;

import game.Game;
import game.Manager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class LoadGame
{
	private Shell shell;
	
	private FileDialog fileDialog;
	
	public LoadGame(Shell shell)
	{
		this.shell = shell;
		
		init();
	}
	
	private void init()
	{
		try
		{
			fileDialog = new FileDialog(shell, SWT.OPEN);
			{
	        	fileDialog.setText("Load Game");
	        
	        	fileDialog.setFilterExtensions(new String[] { "*.xml" });
	        }
	        new game.SaveGame(Manager.getInstance().getGame(), fileDialog.open()).load();
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
	}
}