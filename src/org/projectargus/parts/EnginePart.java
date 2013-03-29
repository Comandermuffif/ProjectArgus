package org.projectargus.parts;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class EnginePart extends ShipPart
{
	public EnginePart(String name,String description,float weight)
	{
		super(name, description, weight);
		
		try {
			img = new Image("res/img/engine1.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String toString()
	{
		String str = super.toString();
		
		return str;
	}
}
