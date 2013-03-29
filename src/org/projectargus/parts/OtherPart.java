package org.projectargus.parts;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class OtherPart extends ShipPart
{
	public OtherPart(String name,String description,float weight)
	{
		super(name,description,weight);
		
		try {
			img = new Image("res/img/other1.png");
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
