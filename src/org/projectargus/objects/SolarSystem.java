package org.projectargus.objects;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

public class SolarSystem 
{
	int x = 100;
	int y = 100;
	int diameter = (int) (100000);//*4;
	
	ArrayList<SpaceObject> children = new ArrayList<SpaceObject>(0);
	
	Star sun = new Star(this, diameter/2, diameter/2, 10000, 6); //111111

	public SolarSystem()
	{
		
		children = sun.getAllChildren();
		//Herpa Derpa, each object makes it's own child
	}

	//Draw everything here
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{		
		//Render the sun
		sun.render(gc, sbg, g);
	}

	//Do all calculations here
	public void update(GameContainer gc, StateBasedGame sbg, int delta,Rectangle screenRect) throws SlickException 
	{	
		//Update the sun
		sun.update(gc, sbg, delta,screenRect);
	}
	
	public boolean checkClicked(int mouse_x, int mouse_y) 
	{	
		if(sun.checkClicked(mouse_x, mouse_y))
		{
			return true;
		}
		
		for(SpaceObject a : children){
			if(a.checkClicked(mouse_x, mouse_y)){
				return true;
			}
		}
		return false;
	}
	
	public SpaceObject checkMouse(int mouse_x, int mouse_y) 
	{
		//We now use the child architecture...
		
		if(sun.checkMouse(mouse_x, mouse_y))
		{
			return sun;
		}
		
		for(SpaceObject a : children){
			if(a.checkMouse(mouse_x, mouse_y)){
				return a;
			}
		}
		return null;
	}
	
	public Star getSun()
	{
		return sun;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;	
	}
	
	public int getDiameter()
	{
		return diameter;
	}
	
	public ArrayList<SpaceObject> getChildren(){
		return children;
	}
}
