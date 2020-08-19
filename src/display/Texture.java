package display;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;

public class Texture
{
	private static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	
	private BufferedImage 	bufferedImage;
	private ByteBuffer 		unpackedPixels;
	
	private GL gl;
	
	private int id;
	
	private Texture(String path)
	{
		try
		{
			bufferedImage = ImageIO.read(new URL("file", "localhost", path));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		int[] packedPixels = new int[bufferedImage.getWidth() * bufferedImage.getHeight()];

		PixelGrabber pixelgrabber = new PixelGrabber(bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), packedPixels, 0, bufferedImage.getWidth());
	    
		try
		{
			pixelgrabber.grabPixels();
	    } 
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		unpackedPixels = ByteBuffer.allocateDirect(packedPixels.length * 4);

        for (int row = bufferedImage.getHeight() - 1; row >= 0; row--)
        {
            for (int col = 0; col < bufferedImage.getWidth(); col++)
            {
                int packedPixel = packedPixels[(row * bufferedImage.getWidth()) + col];

                unpackedPixels.put((byte) ((packedPixel >> 16) & 0xFF));
                unpackedPixels.put((byte) ((packedPixel >> 8) & 0xFF));
                unpackedPixels.put((byte) ((packedPixel >> 0) & 0xFF));
                unpackedPixels.put((byte) ((packedPixel >> 24) & 0xFF));
            }
        }
	      
        unpackedPixels.position(0);
        
        // Store instance.
        textures.put(path, this);
	}
	
	public static Texture get(String path)
	{
		if(textures.containsKey(path))
			return textures.get(path);
		
		return new Texture(path);
	}

	/**
	 * Binds the texture to OpenGL. This means that next drawings will have this texture.
	 */
	public void bind(GL gl)
	{
		gl.glEnable(GL.GL_TEXTURE_2D);
		
		if(gl != this.gl)
			register(gl);
		
		gl.glEnable(GL.GL_TEXTURE_2D);
		
		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_DECAL);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, id);
	}
	
	private void register(GL gl)
	{
		this.gl = gl;
		
		final int[] tmp = new int[1];
		
        gl.glGenTextures(1, tmp, 0);

        gl.glBindTexture(GL.GL_TEXTURE_2D, tmp[0]);
        
        id = tmp[0];
        
        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);

        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, bufferedImage.getWidth(), bufferedImage.getHeight(), 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, unpackedPixels);
	}
}
