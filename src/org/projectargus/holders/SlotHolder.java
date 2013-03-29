package org.projectargus.holders;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.objects.Ship;
import org.projectargus.parts.PartSlot;
import org.projectargus.parts.ShipPart;
import org.projectargus.parts.WeaponPart;

public class SlotHolder 
{
	Ship ship;
	int type;
	
	ArrayList<PartSlot> slots = new ArrayList<PartSlot>();
	
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
	{
		for(int i = 0;i<slots.size();i++)
		{
			slots.get(i).render(gc, sbg, g);
		}
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta, Rectangle screenRect) 
	{
		for(int i = 0;i<slots.size();i++)
		{
			slots.get(i).update(gc, sbg, delta,screenRect);
		}
	}
	
	public void addSlot(PartSlot s)
	{
		slots.add(s);
	}
	
	public int getType()
	{
		return type;
	}
	
	public int getOpenCount()
	{
		int count = 0;
		for(int i =0;i<slots.size();i++)
		{
			if(slots.get(i).isEmpty())
			{
				count++;
			}
		}
		return count;
	}
	
	public ShipPart getPart(int slot)
	{
		return slots.get(slot).getPart();
	}
	
	public boolean removePart(ShipPart part)
	{
		for(int i = 0;i<slots.size();i++)
		{
			if(slots.get(i).getPart().equals(part))
			{
				return true;
			}
		}
		return false;
	}
	
	public ShipPart removePart(int slot)
	{
		if(slots.size() > slot && slots.get(slot).getPart() != null)
		{
			return slots.get(slot).removePart();
		}
		return null;
	}
	
	public PartSlot getOpenSlot()
	{
		for(int i = 0;i< slots.size();i++)
		{
			if( slots.get(i).isEmpty())
			{
				return slots.get(i);
			}
		}
		return null;
	}
	
	public boolean isOpenSlot()
	{
		for(int i = 0;i<slots.size();i++)
		{
			if(slots.get(i).isEmpty())
			{
				return true;
			}
		}
		return false;
	}
	
	public String toString()
	{
		String str = "";
		
		for(int i = 0;i<slots.size();i++)
		{
			str += ">>> Slot " + i + "\n" + slots.get(i).toString() + "\n";
		}
		
		if(slots.size() == 0)
		{
			str += ">>> No Slots\n";
		}
		
		return str;
	}

	public ArrayList<WeaponPart> getAllWeaponParts() 
	{
		ArrayList<WeaponPart> tempParts = new ArrayList<WeaponPart>();
		
		for(int i = 0;i<slots.size();i++)
		{
			if(slots.get(i).isEmpty() == false)
			{
				tempParts.add((WeaponPart)slots.get(i).getPart());
			}
		}
		return tempParts;
	}
	
	public ArrayList<PartSlot> getSlots()
	{
		return slots;
	}
}
