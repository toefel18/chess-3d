package display;

import java.util.HashMap;

import javax.media.opengl.GL;

import com.obj.WavefrontObject;
import com.obj.Group;

public class ShapeLibrary
{
	private static HashMap<String, WavefrontObject> models = new HashMap<String, WavefrontObject>();
	
	private ShapeLibrary()
	{
	}
	
	public static Group getGroup(String modelPath, String groupName)
	{
		WavefrontObject model;
		
		if(models.containsKey(modelPath))
			model = models.get(modelPath);
		
		else
		{
			model = new WavefrontObject(modelPath);
			
			models.put(modelPath, model);
		}
		
		for(Group group : model.getGroups())
			if(group.getName().equals(groupName))
					return group;
		
		return null;
	}
	
	public static void drawCube(GL gl, float width, float height, float depth)
	{
		gl.glBegin(GL.GL_QUADS);

		// front
		gl.glVertex3f(-width, -height, depth); // bottom left
		gl.glVertex3f(width, -height, depth); // bottom right
		gl.glVertex3f(width, height, depth); // top right
		gl.glVertex3f(-width, height, depth); // top left

		// back
		gl.glVertex3f(-width, -height, -depth); // bottom left
		gl.glVertex3f(width, -height, -depth); // bottom right
		gl.glVertex3f(width, height, -depth); // top right
		gl.glVertex3f(-width, height, -depth); // top left
		
		// left
		gl.glVertex3f(-width, -height, -depth); // bottom left
		gl.glVertex3f(-width, -height, depth); // bottom right
		gl.glVertex3f(-width, height, depth); // top right
		gl.glVertex3f(-width, height, -depth); // top left
		
		// right
		gl.glVertex3f(width, -height, depth); // bottom left
		gl.glVertex3f(width, -height, -depth); // bottom right
		gl.glVertex3f(width, height, -depth); // top right
		gl.glVertex3f(width, height, depth); // top left
		
		// top
		gl.glVertex3f(width, height, -depth); // back right
		gl.glVertex3f(-width, height, -depth); // back left
		gl.glVertex3f(-width, height, depth); // front left
		gl.glVertex3f(width, height, depth); // front right
		
		// bottom
		gl.glVertex3f(width, -height, depth); // front right
		gl.glVertex3f(-width, -height, depth); // front left
		gl.glVertex3f(-width, -height, -depth); // back left
		gl.glVertex3f(width, -height, -depth); // back right
		
		gl.glEnd();
	}
}
