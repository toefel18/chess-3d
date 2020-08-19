package com.obj;

import com.obj.Texture;
import com.obj.Vertex;

public class Material {

	private Texture texture;
	private Vertex Ka;
	private Vertex Kd;
	private Vertex Ks;
	private String name;
	public String texName;
	
	public Material(String name)
	{
		this.name = name;
	}

	public void setName( String name ) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public Vertex getKa() {
		return Ka;
	}
	
	public Vertex getKd() {
		return Kd;
	}

	public Vertex getKs() {
		return Ks;
	}

	public void setKa(Vertex ka) {
		Ka = ka;
	}
	
	public void setKd(Vertex kd) {
		Kd = kd;
	}
	
	public void setKs(Vertex ks) {
		Ks = ks;
	}
	
	
}
