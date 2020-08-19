package display;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.nio.IntBuffer;
import java.text.DecimalFormat;

import game.Game;
import game.Manager;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import javax.media.opengl.glu.GLU;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.opengl.win32.PIXELFORMATDESCRIPTOR;
import org.eclipse.swt.internal.opengl.win32.WGL;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.opengl.GLCanvas;

import com.sun.management.OperatingSystemMXBean;
import com.sun.opengl.impl.x11.GLX;
import com.sun.opengl.util.GLUT;

public class DisplayManager implements Runnable
{
	public static final int MODE_PICKING	= 1;
	public static final int MODE_SCREEN		= 2;

	private static final int DESIRED_FPS 	= 64;
	private static final boolean DEBUG 		= true;
	
	private Scene			currentScene;
	private GLCanvas		canvas;
	private GLContext		context;
	private GL				gl;
	private GLU				glu 	= new GLU();
	private GLUT			glut 	= new GLUT();
	private Window			window;
	private Rectangle		bounds;
	private ObjectPicker	objectPicker;
	
	private double passedFrames 			= 0;
	private double lastLoadCalculationTime	= System.currentTimeMillis();
	private double currentFPS				= 0;
	
	private double lastCPUTime			= 0;
	private double currentCPUUsage		= 0;
	
	public DisplayManager(GLCanvas canvas, GLContext context, Window window)
	{
		this.canvas 	= canvas;
		this.context 	= context;
		this.gl 		= context.getGL();
		this.window 	= window;
	
		objectPicker = new ObjectPicker(this);
		
		init();
	}

	private void init()
	{
		gl.glEnable(GL.GL_DEPTH_TEST);
		
		gl.glEnable(GL.GL_LINE_SMOOTH);
	    gl.glEnable(GL.GL_BLEND);
	    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
	    
	    IntBuffer sampleBuffers = IntBuffer.allocate(1);
	    
	    gl.glGetIntegerv(GL.GL_SAMPLE_BUFFERS, sampleBuffers);
	    
	    sampleBuffers.rewind();
	    
	    System.out.println("Sample buffers: " + sampleBuffers.get());
	    
	    resize();
	}
	
	public void setFocus(Scene scene)
	{
		if(scene != null)
			scene.unfocussed();
		
		currentScene = scene;
		objectPicker.setScene(currentScene);
		
		scene.focussed(this);
	}

	private void drawCurrentScene(int mode)
	{
		canvas.setCurrent();
		context.makeCurrent();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT | GL.GL_ACCUM_BUFFER_BIT | GL.GL_STENCIL_BUFFER_BIT);
		gl.glClearColor(0.5f, 0.5f, 0.9f, 0.0f);
		gl.glLoadIdentity();
		   
		if(mode == MODE_PICKING)
		{
			objectPicker.startPicking();
		
			currentScene.draw(this, DisplayManager.MODE_PICKING);
		
			objectPicker.endPicking();
		}
		else
		{
			currentScene.draw(this, DisplayManager.MODE_SCREEN);
			
			drawDebug();
			
			canvas.getDisplay().timerExec(1000 / DESIRED_FPS, this);
			
			canvas.swapBuffers();
		}
	}
	
	private void calculateLoad()
	{
		double currentTime = System.currentTimeMillis();
		
		if(lastLoadCalculationTime + 1000 < currentTime)
		{
			OperatingSystemMXBean systemBean = ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean());
			
			long currentCPUTime = systemBean.getProcessCpuTime();
			
			currentFPS 		= passedFrames	/ ((currentTime - lastLoadCalculationTime) / 1000);
			currentCPUUsage = 100 * (currentCPUTime - lastCPUTime) / (1000000 * (currentTime - lastLoadCalculationTime)) / systemBean.getAvailableProcessors();
				
			currentCPUUsage = currentCPUUsage > 100 ? 100 : currentCPUUsage;
			
			lastLoadCalculationTime = currentTime;
			
			passedFrames			= 0;
			lastCPUTime 			= currentCPUTime;
		}
		
		passedFrames++;	
	}

	private void drawDebug()
	{
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glDisable(GL.GL_LIGHTING);
		
		gl.glMatrixMode(GL.GL_PROJECTION);
		
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glOrtho(0, bounds.width, 0, bounds.height, -1, 1);
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		
		gl.glColor3f(1f, 1f, 1f);
		
		// Display CPU usage.
		{
			gl.glRasterPos2i(bounds.width - 50, 36);
			
			glut.glutBitmapString(GLUT.BITMAP_HELVETICA_10, Math.round(currentCPUUsage) + "% CPU");
		}
		
		// Display memory usage.
		{
			Runtime runtime = Runtime.getRuntime();
		
			gl.glRasterPos2i(bounds.width - 40, 23);
			
			glut.glutBitmapString(GLUT.BITMAP_HELVETICA_10, Math.round((runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024) + " MB");
		}
		
		// Display FPS.
		{
			gl.glRasterPos2i(bounds.width - 40, 10);
			
			glut.glutBitmapString(GLUT.BITMAP_HELVETICA_10, Math.round(currentFPS) + " FPS");
		}
		
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPopMatrix();
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_LIGHTING);
		
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
	}
	
	public Scene getCurrentScene()
	{
		return currentScene;
	}

	private void drawScreen()
	{
		drawCurrentScene(MODE_PICKING);
		drawCurrentScene(MODE_SCREEN);
	}
	
	@Override
	public void run()
	{
		calculateLoad();
		
		try
		{			
			Scene newScene 	= Manager.getInstance().getScene();
			
			if(newScene != currentScene)
				setFocus(newScene);
			
			if(currentScene != null && canvas.isDisposed())
				return;

			drawScreen();
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
	}

	public void resize()
	{
		bounds = canvas.getBounds();
		
		if(bounds.width == 0 || bounds.height == 0)
			return;
		
		canvas.setCurrent();
		context.makeCurrent();
		
		gl.glViewport(0, 0, bounds.width, bounds.height);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45.0f, (float) bounds.width / (float) bounds.height, 1.0f, 20.0f);
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		context.release();
	}

	public GL getGL()
	{
		return gl;
	}
	
	public GLCanvas getCanvas()
	{
		return canvas;
	}
	
	public Rectangle getBounds()
	{
		return bounds;
	}
	
	public Window getWindow()
	{
		return window;
	}

	public ObjectPicker getObjectPicker()
	{
		return objectPicker;
	}
}
