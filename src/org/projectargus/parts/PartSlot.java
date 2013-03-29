package org.projectargus.parts;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.holders.PartsHolder;

public class PartSlot 
{
	int x;
	int y;

	ShipPart part;
	
	public PartsHolder holder;

	public final static int WEAPON_ID = 0;
	public final static int ENGINE_ID = 1;
	public final static int HULL_ID = 2;
	public final static int OTHER_ID = 3;
	
	public PartSlot(int x,int y, PartsHolder partsHolder)
	{
		this.x = x;
		this.y = y;
		this.holder = partsHolder;
	}	

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
	{
		if(isEmpty() == false)
		{
			part.render(gc, sbg, g);
		}
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta, Rectangle screenRect) 
	{
		if(isEmpty() == false)
		{
			part.update(gc, sbg, delta,screenRect);
		}
	}

	public void addPart(ShipPart part)
	{
		this.part = part;
		part.setSlot(this);
	}

	public ShipPart removePart()
	{
		part.removeFromSlot();
		ShipPart temp = part;
		part = null;
		return temp;
	}

	public ShipPart getPart()
	{
		return part;
	}
	
	public int getType()
	{
		return holder.getSlotType(this);
	}

	public boolean isEmpty()
	{
		return part == null;
	}

	public int getRelativeX()
	{
		int height = (int) holder.getShip().shipFight.getHeight()/2;
		int width = (int) holder.getShip().shipFight.getWidth()/2;
		return (int) (width + ((Math.sqrt(Math.pow(y-height,2) + Math.pow(x-width,2))*Math.cos(Math.toRadians(holder.getShip().shipFight.getRotation()) + Math.atan2(y - height, x - width)))));
	}

	public int getRelativeY()
	{
		int height = (int) holder.getShip().shipFight.getHeight()/2;
		int width = (int) holder.getShip().shipFight.getWidth()/2;
		return (int) (height + ((Math.sqrt(Math.pow(y-height,2) + Math.pow(x-width,2))*Math.sin(Math.toRadians(holder.getShip().shipFight.getRotation()) + Math.atan2(y - height, x - width)))));
	}
	
	public float getX()
	{
		return holder.getShip().shipFight.getX() + getRelativeX();
	}

	public float getY()
	{
		return holder.getShip().shipFight.getY() + getRelativeY();
	}	
	

	public String toString()
	{
		String str = "";
		if(isEmpty())
		{
			str = "Empty Slot";
		}else{
			str = part.toString();
		}
		return ">>>> " + str;		
	}

	public PartsHolder getHolder()
	{
		return holder;
	}
}
