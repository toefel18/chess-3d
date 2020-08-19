package display.window;

import game.Game;
import game.Manager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class SaveGame
{
	private Shell shell;
	
	private FileDialog fileDialog;
	
	public SaveGame(Shell shell)
	{
		this.shell = shell;
		
		init();
	}
	
	private void init()
	{
		try
		{
			fileDialog = new FileDialog(shell, SWT.SAVE);
			{
	        	fileDialog.setText("Save Game");
	        
	        	fileDialog.setFilterExtensions(new String[] { "*.xml" });
	        }
	        new game.SaveGame(Manager.getInstance().getGame(), fileDialog.open()).save();
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
	}
}