package display.window;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import display.Window;

public class Menu
{
	private Window window;

	public Menu(Window window)
	{
		this.window = window;
		
		init();
	}

	private void init()
	{
		final Shell shell = window.getShell();
		
		org.eclipse.swt.widgets.Menu menuBar = new org.eclipse.swt.widgets.Menu(shell, SWT.BAR);
		{
			org.eclipse.swt.widgets.Menu fileMenu = new org.eclipse.swt.widgets.Menu(shell, SWT.DROP_DOWN);
			{
				MenuItem actionsItem = new MenuItem(menuBar, SWT.CASCADE);
				{
					actionsItem.setText("&File");
				}
				actionsItem.setMenu(fileMenu);
				
				MenuItem NewGameItem = new MenuItem(fileMenu, SWT.PUSH);
				{
					NewGameItem.setText("&New game");
					
					NewGameItem.addSelectionListener(new SelectionListener()
					{
						public void widgetDefaultSelected(SelectionEvent arg0)
						{
						}

						public void widgetSelected(SelectionEvent arg0)
						{
							NewGame newGame = new NewGame(shell);
							newGame.openBlocking();
						}
					});
				}
				
				MenuItem loadItem = new MenuItem(fileMenu, SWT.PUSH);
				{
					loadItem.setText("&Load game");
					
					loadItem.addSelectionListener(new SelectionListener()
					{
						public void widgetDefaultSelected(SelectionEvent arg0)
						{
						}

						public void widgetSelected(SelectionEvent arg0)
						{
							new LoadGame(shell);
						}
					});
				}
				
				MenuItem saveItem = new MenuItem(fileMenu, SWT.PUSH);
				{
					saveItem.setText("&Save game");
					
					saveItem.addSelectionListener(new SelectionListener()
					{
						public void widgetDefaultSelected(SelectionEvent arg0)
						{
						}

						public void widgetSelected(SelectionEvent arg0)
						{
							new SaveGame(shell);
						}
					});
				}
				
				MenuItem separatorItem = new MenuItem(fileMenu, SWT.SEPARATOR);
				
				MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
				{
					exitItem.setText("&Exit");
					
					exitItem.addSelectionListener(new SelectionListener()
					{
						public void widgetDefaultSelected(SelectionEvent arg0)
						{
						}

						public void widgetSelected(SelectionEvent arg0)
						{
							window.close();
						}
					});
				}
				
				org.eclipse.swt.widgets.Menu helpMenu = new org.eclipse.swt.widgets.Menu(shell, SWT.DROP_DOWN);
				{
					MenuItem infoItem = new MenuItem(menuBar, SWT.CASCADE);
					{
						infoItem.setText("&Help");
					}
					infoItem.setMenu(helpMenu);
					
					MenuItem aboutItem = new MenuItem(helpMenu, SWT.PUSH);
					{
						aboutItem.setText("&About");
						
						aboutItem.addSelectionListener(new SelectionListener()
						{
							public void widgetDefaultSelected(SelectionEvent arg0)
							{
							}
	
							public void widgetSelected(SelectionEvent arg0)
							{
								new About(shell);
							}
						});
					}
				}
			}
			shell.setMenuBar(menuBar);
		}
	}
}
