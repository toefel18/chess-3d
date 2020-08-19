package display.window;

import game.ChessStrategy;
import game.ChessStrategyFactory;
import game.Player;
import game.chessstrategies.RandomMove;
import game.players.BotPlayer;
import game.players.HumanPlayer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

//TODO preload the models in another thread while the user configures the player settings!

public class NewGame 
{
	private static final int MARGIN = 10;
	
	private Shell parent;
	private Display display;
	protected Shell shell;
	private Button buttonUser1;
	private Button buttonComp1;
	private Button buttonUser2;
	private Button buttonComp2;
	private Combo comboComp1;
	private Combo comboComp2;
	private Label lblAlgorithm1;
	private Label lblAlgorithm2;
	private Button startButton;
	
	private boolean isValid = false;
	
	private Player player1 = null;
	private Player player2 = null;
	
	private class PlayerSelectionManager implements Listener
	{
		private Button userButton = null;
		private Button botButton = null;
		private Combo strategy = null;
		private Label selectLbl = null;
		NewGame newGame = null;
		
		/**
		 * 
		 * @param userButton The user botton to toggle on/off
		 * @param botButton the bot button to toggle on/off
		 * @param strategy the strategy to disable/enable
		 * @param selectLbl the label to disable/enable
		 * @param firstSelection the boolean to set to true on first selection on either of the 2 toggle buttons
		 */
		public PlayerSelectionManager(Button userButton, Button botButton, Combo strategy, Label selectLbl, NewGame newGame)
		{
			this.userButton = userButton;
			this.botButton = botButton;
			this.strategy = strategy;
			this.selectLbl = selectLbl;
			this.newGame = newGame;
		}

		@Override
		public void handleEvent(Event event) 
		{
			if(event.widget == userButton)
			{
				userButton.setSelection(true);	
				botButton.setSelection(false);
				strategy.setEnabled(false);
				selectLbl.setEnabled(false);
			}
			else
			{
				userButton.setSelection(false);	
				botButton.setSelection(true);				
				strategy.setEnabled(true);
				selectLbl.setEnabled(true);
			}
			
			newGame.updateButtons();
		} 
	}
	
	private class StartGame implements Listener
	{
		private Shell shell;
		private NewGame newGame;
		
		public StartGame(Shell shell, NewGame newGame)
		{
			this.shell = shell;
			this.newGame = newGame;
		}
		
		public void handleEvent(Event event) 
		{
			newGame.configurePlayers();
			shell.close();
		}
	}
	
	public NewGame(Shell parent)
	{
		if(parent == null) 
			parent = new Shell();
		
		this.parent = parent;
		
		this.display = parent.getDisplay();
				
		buildGUI();
			
		shell.pack();
		
		shell.layout();		
	}
	
	/**
	 * Debug!
	 * @param args
	 */
	public static void main(String[] args)
	{
		NewGame newGame = new NewGame(new Shell());
		newGame.openBlocking();
	}
	
	protected void updateButtons()
	{
		if((buttonUser1.getSelection() || buttonComp1.getSelection()) && (buttonUser2.getSelection() || buttonComp2.getSelection()))
			startButton.setEnabled(true);
		else
			startButton.setEnabled(false);
	}
	
	/**
	 * This method will block till the window is disposed. After this method 
	 */
	public void openBlocking()
	{
		shell.open();
		
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();	
	}
	
	public Player getPlayer1()
	{
		return player1;
	}
	
	public Player getPlayer2()
	{
		return player2;
	}
	
	/**
	 * If this returns true, it means the player has selected start game and configured 2 players correcty
	 * @return
	 */
	public boolean isValid()
	{
		return isValid;
	}
	
	private void buildGUI()
	{
		this.shell = new Shell(parent.getDisplay(), SWT.PRIMARY_MODAL | SWT.BORDER | SWT.CLOSE);
		{			
			shell.setText("Chess3D Second Edition");

			GridLayout shellGridLayout = new GridLayout(1, true);
			shellGridLayout.marginTop = 0;
			shellGridLayout.marginBottom = 15;
			shellGridLayout.marginLeft = 0;
			shellGridLayout.marginRight = 0;
			shellGridLayout.verticalSpacing = 0;
			shellGridLayout.horizontalSpacing = 0;
			shellGridLayout.marginHeight = 0;
			shellGridLayout.marginWidth = 0;
			
			shell.setLayout(shellGridLayout);
			
			Image header = new Image(shell.getDisplay(), "data/images/newgameheader.png");
			Label lblHeader = new Label(shell, SWT.NONE);
			lblHeader.setImage(header);
			
			Composite contents = new Composite(shell, SWT.NONE);
			{
				GridLayout gridLayout = new GridLayout(1, true);
				gridLayout.marginTop = MARGIN;
				gridLayout.marginBottom = MARGIN;
				gridLayout.marginLeft = MARGIN;
				gridLayout.marginRight = MARGIN;
				
				contents.setLayout(gridLayout);
				
				Image imageUser = new Image(shell.getDisplay(), "data/images/user.png");
				Image imageComp = new Image(shell.getDisplay(), "data/images/computer.png");
								
				Label lblPickPlayer1 = new Label(contents, SWT.NONE);
				lblPickPlayer1.setText("Pick player white");
				FontData fd1 = new FontData();
				fd1.setStyle(SWT.BOLD | SWT.ITALIC);
				fd1.setName(lblPickPlayer1.getFont().getFontData()[0].getName());
				lblPickPlayer1.setFont(new Font(display, fd1));
				lblPickPlayer1.setForeground(new Color(display, 60, 60, 60));
				
				
				Composite pickPlayer1Composite = new Composite(contents, SWT.NONE);
				{					
					pickPlayer1Composite.setLayoutData(layoutCenter());
					pickPlayer1Composite.setLayout(new RowLayout(SWT.HORIZONTAL));
					
					buttonUser1 = new Button(pickPlayer1Composite, SWT.TOGGLE);
					buttonUser1.setText("");
					buttonUser1.setImage(imageUser);
					
					Composite picker1 = new Composite(pickPlayer1Composite, SWT.NONE);
					{
						RowLayout picker1Layout = new RowLayout(SWT.VERTICAL);
						picker1Layout.fill = true;
						picker1.setLayout(picker1Layout);
						
						buttonComp1 = new Button(picker1, SWT.TOGGLE);
						buttonComp1.setImage(imageComp);
						buttonComp1.setText("");
						
						lblAlgorithm1 = new Label(picker1, SWT.NONE);
						lblAlgorithm1.setText("Pick strategy:");
						lblAlgorithm1.setEnabled(false);
						
						comboComp1 = new Combo(picker1, SWT.SINGLE | SWT.READ_ONLY);
						comboComp1.setEnabled(false);
						comboComp1.setItems(ChessStrategyFactory.getStrategyNames());
						comboComp1.select(0);
					}					
					PlayerSelectionManager manager1 = new PlayerSelectionManager(buttonUser1, buttonComp1, comboComp1, lblAlgorithm1, this);
					buttonUser1.addListener(SWT.Selection, manager1);
					buttonComp1.addListener(SWT.Selection, manager1);
				}
				
				Label lblPickPlayer2 = new Label(contents, SWT.NONE);
				lblPickPlayer2.setText("Pick player black");
				FontData fd2 = new FontData();
				fd2.setStyle(SWT.BOLD | SWT.ITALIC);
				fd2.setName(lblPickPlayer2.getFont().getFontData()[0].getName());
				lblPickPlayer2.setFont(new Font(display, fd2));
				lblPickPlayer2.setForeground(new Color(display, 60, 60, 60));
				
				Composite pickPlayer2Composite = new Composite(contents, SWT.NONE);
				{	
					pickPlayer2Composite.setLayoutData(layoutCenter());
					pickPlayer2Composite.setLayout(new RowLayout(SWT.HORIZONTAL));	
					
					buttonUser2 = new Button(pickPlayer2Composite, SWT.TOGGLE);
					buttonUser2.setText("");
					buttonUser2.setImage(imageUser);
					
					Composite picker2 = new Composite(pickPlayer2Composite, SWT.NONE);
					{
						RowLayout picker2Layout = new RowLayout(SWT.VERTICAL);
						picker2Layout.fill = true;
						picker2.setLayout(picker2Layout);
						
						buttonComp2 = new Button(picker2, SWT.TOGGLE);
						buttonComp2.setImage(imageComp);
						buttonComp2.setText("");
						
						lblAlgorithm2 = new Label(picker2, SWT.NONE);
						lblAlgorithm2.setText("Pick strategy:");
						lblAlgorithm2.setEnabled(false);
						
						comboComp2 = new Combo(picker2, SWT.SINGLE | SWT.READ_ONLY);
						comboComp2.setEnabled(false);
						comboComp2.setItems(ChessStrategyFactory.getStrategyNames());
						comboComp2.select(0);
					}
					PlayerSelectionManager manager2 = new PlayerSelectionManager(buttonUser2, buttonComp2, comboComp2, lblAlgorithm2, this);
					buttonUser2.addListener(SWT.Selection, manager2);
					buttonComp2.addListener(SWT.Selection, manager2);
				}
				
				Composite buttons = new Composite(shell, SWT.NONE);
				{
					GridData buttonsData = new GridData();
					buttonsData.horizontalAlignment = SWT.CENTER;
					buttons.setLayoutData(buttonsData);
					
					RowLayout rowLayout = new RowLayout(SWT.HORIZONTAL);
					rowLayout.spacing = 15;
					rowLayout.justify = true;
					buttons.setLayout(rowLayout);
					
					startButton = new Button(buttons, SWT.PUSH);
					startButton.setText("Start game!");
					startButton.setLayoutData(increaseHeight());
					startButton.setEnabled(false);
					startButton.addListener(SWT.Selection, new StartGame(shell, this));
					
					Button cancelButton = new Button(buttons, SWT.PUSH);
					cancelButton.setText("Cancel");
					cancelButton.setLayoutData(increaseHeight());
					cancelButton.addListener(SWT.Selection, new Listener(){
						public void handleEvent(Event event) {
							Button btn = (Button) event.widget;
							btn.getShell().close();
						}
					});
				}	
			}								
		}
	}
	
	private void configurePlayers()
	{
		if(buttonUser1.getSelection())
			player1 = new HumanPlayer();
		else
			player1 = new BotPlayer(ChessStrategyFactory.getStrategy(comboComp1.getText()));
		
		if(buttonUser2.getSelection())
			player2 = new HumanPlayer();
		else
			player2 = new BotPlayer(ChessStrategyFactory.getStrategy(comboComp2.getText()));
		
		this.isValid = true;		
	}
	
	private RowData increaseHeight()
	{
		RowData rd = new RowData();
		rd.height = 32;
		return rd;
	}
	
	private GridData layoutCenter()
	{
		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.CENTER;
		data.horizontalIndent = 7;
		return data;
	}		
}
