package org.projectargus.parts;

import javax.xml.ws.Holder;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.holders.PartsHolder;
import org.projectargus.states.FightState;

public class ShipPart 
{
	String name;
	String description;
	
	PartSlot slot;

	float rotation = 0;
	float rotateSpeed = .05f;

	Image img;

	float weight;

	float w = 15;
	float h = 15;

	float screen_x;
	float screen_y;
	float screen_w;
	float screen_h;

	
	
	public ShipPart(String name,String description,float weight)
	{
		this(name,description,weight,null);
	}

	public ShipPart(String name,String description,float weight,PartsHolder holder)
	{
		this.name = name;
		this.description = description;
		this.weight = weight;
		
		try {
			this.img = new Image("res/img/defaultPart.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
	{		
		if(screen_x != -1 || screen_y != -1)
		{
			g.drawImage(img, screen_x,screen_y, screen_x+screen_w, screen_y+screen_h, 0, 0, img.getWidth(), img.getHeight());	
		}	
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta, Rectangle screenRect) 
	{
		rotation += rotateSpeed*delta;
		img.setCenterOfRotation(screen_w/2, screen_h/2);
		img.setRotation(rotation);

		Rectangle dims = FightState.getScreenDims(gc, new Rectangle(getX(),getY(),w,h), screenRect);

		screen_w = dims.getWidth();
		screen_h = dims.getHeight();
		screen_x = dims.getX();
		screen_y = dims.getY();
	}

	public void updateImage(GameContainer gc, StateBasedGame sbg)
	{
		//rotation += rotateSpeed*delta;
		//img.setCenterOfRotation(100,100);
		//20.
		//30,70
		//40,100
		
		//img.setRotation(rotation);
		img.setRotation(0);
	}
	
	public Image getImage()
	{
		return img;
	}

	public float getWeight()
	{
		return weight;
	}

	public String toString()
	{
		String str = name + " - " + description;

		return str;
	}

	public void setSlot(PartSlot s)
	{
		slot = s;
	}

	public PartSlot getSlot()
	{
		return slot;
	}
	
	public void removeFromSlot()
	{
		slot = null;
	}

	public String getTypeName()
	{
		return PartsHolder.partTypeNames[PartsHolder.getPartType(this)];
	}
	
	public int getType()
	{
		return PartsHolder.getPartType(this);
	}	
	
	public float getX()
	{
		return slot.getHolder().getShip().shipFight.getX() + slot.getRelativeX() - w/2;
	}

	public float getY()
	{
		return slot.getHolder().getShip().shipFight.getY() + slot.getRelativeY() - h/2;
	}
	
	public float getRelativeX()
	{
		return slot.getRelativeX() - w/2;
	}
	
	public float getRelativeY()
	{
		return slot.getRelativeY() - h/2;
	}

	public int getHeight() 
	{
		return (int)h;
	}
	
	public int getWidth() 
	{
		return (int)w;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDescription()
	{
		return description;
	}

}
