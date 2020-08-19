package com.obj;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
//import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
//import org.lwjgl.opengl.GL11;

import com.obj.TextureReader;

import com.sun.opengl.util.BufferUtil;

public class TextureLoader {

	//private int[] textures = new int[10];

	private int lastTextureId = 0;
	
	private TextureLoader(){}
	private static TextureLoader instance = null;
	private Hashtable<String,BufferedImage> bufferedImageCache = new Hashtable <String,BufferedImage>();

	private static GL gl = null;
	private static GLU glu = null;
	
	public static void setGL(GL gl)
	{
		TextureLoader.gl = gl;
	}
	
	public static TextureLoader instance()
	{
		if (instance == null)
			instance = new TextureLoader();
		
		return instance;
	}

	Texture[] loadAnimation(String path,int cols, int rows, int textWidth, int textHeight) 
	{
		return loadAnimation(path,cols,rows,textWidth,textHeight,0,0);
	}
	
	private  Texture[] loadAnimation(String path,int cols, int rows, int textWidth, int textHeight, int xOffSet, int yOffSet) 
	{
		Texture[] toReturntextures = new Texture[cols*rows];

		System.out.println("LoadAnimtation(..)");
		
		//throw new Exception("LoadAnimation called.");
		
		/*for (int i=0;i< rows ; i++)
			for (int j=0;j< cols ; j++)
			{
				toReturntextures[i*cols+j] = loadTexture(path,j*textWidth+xOffSet,i*textHeight+yOffSet,textWidth,textHeight);
			}*/
		
		return toReturntextures;
	}
	
	public Texture loadTexture(String path)
	{
		GLU glu = new GLU();
		
		TextureReader.Texture texture = null;
        System.out.println(" Loading Texture: " + path + " into: " + lastTextureId);
        try {
            texture = TextureReader.readTexture(path);
        } catch (IOException e) {
            System.err.println(" Texture could not be loaded " + path);
            System.exit(0);
            return null;
        }
        gl.glBindTexture(GL.GL_TEXTURE_2D, lastTextureId);

        makePlainTexture(gl, glu, texture);
       
        /*else {
            try {
                makeTextureMipMap(gl, glu, texture);
            } catch (Exception e) {
                System.err.println("Mipmap not possible!");
                makePlainTexture(gl, glu, texture);
            }
        }*/
        
        System.out.println("TextureId: " +lastTextureId + ", w/h: " + texture.getWidth() + "/" + texture.getHeight());
        
        lastTextureId++;
        
        return new Texture(lastTextureId - 1, texture.getWidth(), texture.getHeight());
    }


	private void loadTexture(String path, int xOffSet, int yOffSet, int textWidth, int textHeight)
	{

		
		
		
		/*Texture toReturn = null;
       
		BufferedImage buffImage = bufferedImageCache.get(path);
		
		if (buffImage == null)
			try
			{
				buffImage = ImageIO.read(getClass().getResourceAsStream(path));
				 //System.out.println("URL loaded:"+path);
			}
			catch (Exception e)
			{
				try
				{
					buffImage =  ImageIO.read(new File(path));
					//System.out.println("F loaded:"+path);
				}
				catch(Exception e2)
				{
					System.out.println("Could not load path '"+path+"'");
					e.printStackTrace();
					e2.printStackTrace();
					return null;
				}
			}
		
		bufferedImageCache.put(path, buffImage);
		
		if (textWidth == 0)
			textWidth = buffImage.getWidth();
		
		if (textHeight == 0)
			textHeight = buffImage.getHeight();
		
		int[] packedPixels = new int[buffImage.getWidth() * buffImage.getHeight()];

        PixelGrabber pixelgrabber = new PixelGrabber(buffImage, 0, 0, buffImage.getWidth(), buffImage.getHeight(), packedPixels, 0, buffImage.getWidth());
        try {
            pixelgrabber.grabPixels();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }

        ByteBuffer unpackedPixels = BufferUtil.newByteBuffer(packedPixels.length * 3);

        for (int row = buffImage.getHeight() - 1; row >= 0; row--)
        {
            for (int col = 0; col < buffImage.getWidth(); col++) 
            {
                int packedPixel = packedPixels[row * buffImage.getWidth() + col];
                unpackedPixels.put((byte) ((packedPixel >> 16) & 0xFF));
                unpackedPixels.put((byte) ((packedPixel >> 8) & 0xFF));
                unpackedPixels.put((byte) ((packedPixel >> 0) & 0xFF));
            }
        }

        unpackedPixels.flip();

        gl.glEnable(GL.GL_TEXTURE_2D);
        */
       /* // Create A IntBuffer For Image Address In Memory
        final int[] tmp = new int[1];
        gl.glGenTextures(1, tmp, 0); // Create Texture In OpenGL

         // Create Nearest Filtered Texture
        gl.glBindTexture(GL.GL_TEXTURE_2D, tmp[0]);
        
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, textWidth, textHeight, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, unpackedPixels);
       
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        
        toReturn = new Texture(tmp[0], textWidth, textHeight);
        
        System.out.println("Texture path '"+path+"-"+xOffSet+"-"+yOffSet+"-"+textWidth+"-"+textHeight+"-" + textWidth + "x" + textHeight + "', is loaded with id="+toReturn.getTextureID());
        
        return toReturn;*/
        
		/*int bytesPerPixel = buffImage.getColorModel().getPixelSize() / 8;
		if (textWidth == 0)
			textWidth = buffImage.getWidth();
		if (textHeight == 0)
			textHeight = buffImage.getHeight();
		
		ByteBuffer scratch = ByteBuffer.allocateDirect(textWidth*textHeight*bytesPerPixel).order(ByteOrder.nativeOrder());	
		DataBufferByte data = ((DataBufferByte) buffImage.getRaster().getDataBuffer());
		
		for (int i = 0 ; i < textHeight ; i++)
			scratch.put(data.getData(),(xOffSet+(yOffSet+i)*buffImage.getWidth())*bytesPerPixel, textWidth * bytesPerPixel);
		
        scratch.rewind();

        gl.glEnable(GL.GL_TEXTURE_2D);
        
        // Create A IntBuffer For Image Address In Memory
        final int[] tmp = new int[1];
        gl.glGenTextures(1, tmp, 0); // Create Texture In OpenGL

         // Create Nearest Filtered Texture
        gl.glBindTexture(GL.GL_TEXTURE_2D, tmp[0]);
        
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, textWidth, textHeight, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, scratch);
       
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        
        toReturn = new Texture(tmp[0], textWidth, textHeight);
        
        System.out.println("Texture path '"+path+"-"+xOffSet+"-"+yOffSet+"-"+textWidth+"-"+textHeight+"', is loaded with id="+toReturn.getTextureID());
        
        return toReturn;*/
    }
	
    private void makePlainTexture(GL gl, GLU glu, TextureReader.Texture texture) {
        boolean wrap = true;
        ByteBuffer buf = texture.getPixels();
        System.out.println("texture: "+texture+"  size:"+buf + " height:" + texture.getHeight());
        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);

        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, wrap ? GL.GL_REPEAT : GL.GL_CLAMP);
        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, wrap ? GL.GL_REPEAT : GL.GL_CLAMP);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, texture.getWidth(), texture.getHeight(),
            0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE,buf);

    }

    public int makeTextureMipMap(GL gl, GLU glu, TextureReader.Texture textureImg) {
        ByteBuffer buf = textureImg.getPixels();
        int ret = glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGBA, textureImg.getWidth(),
                textureImg.getHeight(), GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, buf);
        if (ret != 0) {
            System.out.println(
                "GLApp.makeTextureMipMap(): Error occured while building mip map, ret=" + ret);
        }
        // Assign the mip map levels and texture info
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_NEAREST);
        gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
        return ret;
    }

}
