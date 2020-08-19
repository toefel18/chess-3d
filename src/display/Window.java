package display;

import game.Game;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.sun.opengl.util.GLUT;

import display.window.Menu;

public class Window implements ControlListener, Runnable
{
	private Display		display;
	private Shell		shell;
	private GLCanvas	canvas;
	private GLContext	context;
	
	private DisplayManager displayManager;

	public Window()
	{
	}

	public void controlResized(ControlEvent e)
	{
		displayManager.resize();
	}

	public boolean isActive()
	{
		if (shell != null)
			return !shell.isDisposed();
		else
			return true;
	}
	
	public void controlMoved(ControlEvent arg0)
	{
	}
	
	public DisplayManager getDisplayManager()
	{
		return displayManager;
	}

	public void run() 
	{	
		display = new Display();
		{
			shell = new Shell(display);
			{
				shell.setText("Chess3D Second Edition");
				shell.setLayout(new FillLayout());
				
				new Menu(this);
			}
	
			GLData data = new GLData();
			{
				data.doubleBuffer 	= true;
				
				// TODO Has no effect, overridden by display driver?
				data.sampleBuffers 	= 4;
				data.samples 		= 2;
			}
			
			canvas = new GLCanvas(shell, SWT.None, data);
			{
				canvas.setCurrent();		
				
				canvas.addControlListener(this);
				
				context = GLDrawableFactory.getFactory().createExternalGLContext();
				
				displayManager = new DisplayManager(canvas, context, this);
			}
		}
		shell.setVisible(true);
		
		display.asyncExec(displayManager);
		
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();	
	
		display.dispose();
	}
	
	public void close()
	{
		shell.close();
	}
	
	public Shell getShell()
	{
		return shell;
	}
}
