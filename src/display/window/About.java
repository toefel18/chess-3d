package display.window;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class About
{
	private Shell parent;
	
	public About(Shell parent)
	{
		this.parent = parent;
		
		buildGUI();
	}
	
	private void buildGUI()
	{
		final Shell shell = new Shell(parent);
		{
			Display display = shell.getDisplay();
			
			shell.setText("About Chess3D");
			shell.setSize(300, 430);
			shell.setBackgroundImage(new Image(shell.getDisplay(), "data/images/aboutwindowbackground.jpg"));
			shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
			shell.open();
			
			Label versionLabel = new Label(shell, SWT.LEFT);
		    {
		    	versionLabel.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		    	
		    	FontData[] fontData = versionLabel.getFont().getFontData();
		    	{
		    		fontData[0].setHeight(10);
		    	}
		    	versionLabel.setFont(new Font(display, fontData[0]));

		    	versionLabel.setText("Versie 1.0");
		    	
		    	versionLabel.setBounds(new Rectangle(170, 50, 300, 15));
		    }
		    
		    Label infoLabel = new Label(shell, SWT.LEFT);
		    {
		    	infoLabel.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
		    	
		    	FontData[] fontData = infoLabel.getFont().getFontData();
		    	{
		    		fontData[0].setStyle(fontData[0].getStyle());
		    		fontData[0].setHeight(9);
		    	}
		    	infoLabel.setFont(new Font(display, fontData[0]));

		    	String[] allDevelopers = new String[] { "Christophe Hesters", "Jeffrey Krist"};
		    	
		    	String developers = new String();
		    	
		    	if(Math.random() >= .5)
		    		developers = "  " + allDevelopers[0] + "\n  " + allDevelopers[1];
		    	
		    	else
		    		developers = "  " + allDevelopers[1] + "\n  " + allDevelopers[0];
		    	
		    	infoLabel.setText("© 2009-2010 Programmers.\nAll rights reserverd.\n\nProgrammed by: \n" + developers);
		    	
		    	infoLabel.setBounds(new Rectangle(10, 245, 300, 120));
		    }
			
		    final Button button = new Button(shell, SWT.PUSH);
			{
				button.setText("OK");

				button.setBounds(new Rectangle(120, 370, 70, 20));
			}
			button.addListener(SWT.Selection, new Listener()
			{
				public void handleEvent(Event event)
				{
					shell.close();
				}
			});
		}
	}
}