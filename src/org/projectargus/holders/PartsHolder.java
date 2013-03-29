package org.projectargus.holders;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.objects.Ship;
import org.projectargus.parts.EnginePart;
import org.projectargus.parts.HullPart;
import org.projectargus.parts.MissileLauncher;
import org.projectargus.parts.OtherPart;
import org.projectargus.parts.PartSlot;
import org.projectargus.parts.ShipPart;
import org.projectargus.parts.*;

public class PartsHolder 
{
	Ship ship;

	ArrayList<SlotHolder> slots = new ArrayList<SlotHolder>();

	ArrayList<ShipPart> inventoryParts = new ArrayList<ShipPart>();



	public final static int WEAPON_ID = 0;
	public final static int ENGINE_ID = 1;
	public final static int HULL_ID = 2;
	public final static int OTHER_ID = 3;

	public final static String[] partTypeNames = {"Weapon","Engine","Hull","Other"};

	//TODO IMPORTANT, do all the checks for most of the functions

	public PartsHolder(Ship ship) 
	{
		this.ship = ship;

		//Sets all the part type arrayLists
		slots.add(new SlotHolder());
		slots.add(new SlotHolder());
		slots.add(new SlotHolder());
		slots.add(new SlotHolder());


		testPopulateParts();
	}

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

	public ArrayList<ShipPart> getAllParts()
	{
		ArrayList<ShipPart> tempParts = new ArrayList<ShipPart>();

		for(int i = 0;i<slots.size();i++)
		{
			for(int j = 0;j<slots.get(i).slots.size();j++)
			{
				if(slots.get(i).slots.get(j).isEmpty() == false)
				{
					tempParts.add(slots.get(i).slots.get(j).getPart());
				}
			}
		}

		return tempParts;
	}

	/**
	 * Temporary way to test parts and parts holder :p
	 */
	public void testPopulateParts()
	{
		addSlot(WEAPON_ID,250,100);
		addSlot(WEAPON_ID,250,400);
		addSlot(HULL_ID,200,250);
		addSlot(ENGINE_ID,40,250);
		addSlot(OTHER_ID,400,250);

		addPart(new MissileLauncher());
		addPart(new LaserCannon());

		addPart(new HullPart("Steel Hull", "A decently strong hull", 100));

		addPart(new EnginePart("400 HP Engine", "A decently strong engine", 20));

		addPart(new OtherPart("Command Bridge","See the enemy better",25));

		
		addPartToInventory(new MissileLauncher());
		addPartToInventory(new MissileLauncher());
		addPartToInventory(new HullPart("Steel Hull", "A decently strong hull", 100));
		addPartToInventory(new LaserCannon());
		addPartToInventory(new HullPart("Steel Hull", "A decently strong hull", 100));
		addPartToInventory(new LaserCannon());
		addPartToInventory(new MissileLauncher());
		addPartToInventory(new HullPart("Steel Hull", "A decently strong hull", 100));
		addPartToInventory(new LaserCannon());
		addPartToInventory(new MissileLauncher());
		addPartToInventory(new HullPart("Steel Hull", "A decently strong hull", 100));
		addPartToInventory(new LaserCannon());
		addPartToInventory(new EnginePart("400 HP Engine", "A decently strong engine", 20));
		addPartToInventory(new OtherPart("Command Bridge","See the enemy better",25));
		addPartToInventory(new MissileLauncher());
		addPartToInventory(new HullPart("Steel Hull", "A decently strong hull", 100));
		addPartToInventory(new LaserCannon());
		addPartToInventory(new EnginePart("400 HP Engine", "A decently strong engine", 20));
		addPartToInventory(new OtherPart("Command Bridge","See the enemy better",25));
		
		
		
		System.out.println(toString());
	}

	public void addSlot(int type,int x,int y)
	{		
		Image img = ship.shipFight.getBaseImage();

		int tempX = (int) ((float)x/(float)img.getWidth() * (float)(ship.shipFight.getWidth()));
		int tempY = (int) ((float)y/(float)img.getHeight() * (float)(ship.shipFight.getHeight()));

		System.out.println(tempX + " : " + tempY + " : " + x + " : " + y);

		slots.get(type).addSlot(new PartSlot(tempX,tempY,this));
	}


	/**
	 * Checks whether or not a part can be added
	 * @param part the part
	 * @return <b>true</b> if the part could be added
	 */
	public boolean canAddPart(ShipPart part)
	{
		return getAvailableSlotCount(getPartType(part)) > 0;	
	}

	/**
	 * Add a part to the holder
	 * @param part the part you want to add
	 * @param id the type of the part
	 * @return <b>true</b> if 
	 */
	public boolean addPart(ShipPart part,int id)
	{
		if(canAddPart(part))
		{
			slots.get(id).getOpenSlot().addPart(part);
			return true;
		}
		return false;
	}

	/**
	 * Add a part to the holder
	 * @param part the part
	 * @return <b>true</b> if the part was added successfully
	 */
	public boolean addPart(ShipPart part)
	{
		int type = getPartType(part);
		if(type != -1)
		{
			return addPart(part,type);
		}
		return false;
	}

	/**
	 * 
	 * @param id the part id
	 * @param slot the slot position
	 * @return the ShipPart that was removed or <b>null</b> if it doesn't exist
	 */
	public ShipPart removePart(int id,int slot)
	{
		ShipPart part = null;
		part = slots.get(id).removePart(slot);
		return part;
	}

	/**
	 * Removes the given part
	 * @param part the part you want to remove
	 * @return <b>true</b> if the part was removed, <b>false</b> if it was never present
	 */
	public boolean removePart(ShipPart part)
	{
		int type = getPartType(part);
		return slots.get(type).removePart(part);
	}

	public int getSlotType(PartSlot slot)
	{
		for(int i = 0;i<slots.size();i++)
		{
			if(slots.get(i).getSlots().contains(slot))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * Gets the type of the part
	 * @param part the ShipPart
	 * @return the id of the part type or -1 if it is not a know type
	 */
	public static int getPartType(ShipPart part)
	{
		if(part instanceof WeaponPart)
		{
			return WEAPON_ID;
		}
		else if(part instanceof EnginePart)
		{
			return ENGINE_ID;
		}
		else if(part instanceof HullPart)
		{
			return HULL_ID;
		}
		else if(part instanceof OtherPart)
		{
			return OTHER_ID;
		}
		return -1;
	}


	/**
	 * Gets all of the unused parts in the inventory
	 * @return <b>ArrayList&lt;ShipPart&gt;</b> of all the unused parts
	 */
	public ArrayList<ShipPart> getInventoryParts()
	{
		return inventoryParts;
	}


	public void addPartToInventory(ShipPart part)
	{
		inventoryParts.add(part);
		if(part.getSlot() != null)
		{
			part.getSlot().removePart();
		}
	}
	
	public void removePartFromInventory(ShipPart part)
	{
		inventoryParts.remove(part);
	}
	
	
	/**
	 * Get the total weight of all the parts
	 * @return the total weight of all the parts
	 */
	public float getAllPartsWeight()
	{
		float sum = 0;
		for(int i = 0;i<inventoryParts.size();i++)
		{
			sum += inventoryParts.get(i).getWeight();
		}
		return sum;
	}

	/**
	 * Get the part in the given id and slot
	 * @param id the part type
	 * @param slot the slot
	 * @return the ShipPart in the slot of the given part type, <b>null</b> if no part
	 */
	public ShipPart getPart(int id,int slot)
	{
		return slots.get(id).getPart(slot);
	}


	/**
	 * Get the available amount of slots in a given part type
	 * @param id the part type
	 * @return the available amount of slots in the given type
	 */
	public int getAvailableSlotCount(int id)
	{
		return slots.get(id).getOpenCount();
	}


	/**
	 * Get the amount of parts in a given part type
	 * @param id the part type
	 * @return the amount of parts in the given type
	 */
	/*public int getPartCount(int id)
	{
		return parts.get(id).size();
	}*/


	/**
	 * 
	 * @param id the part id
	 * @return the total amount of a certain part type the holder can have
	 */
	/*public int getPartLimit(int id)
	{
		return partsLimit[id];
	}*/


	/**
	 * Get all the parts, shouldn't really be used
	 * @return an arraylist of arraylists of all the parts
	 */
	/*public ArrayList<ArrayList<ShipPart>> getAllParts()
	{
		return parts;
	}*/

	public Ship getShip()
	{
		return ship;
	}

	/**
	 * @return a string that represents the partHolder
	 */
	public String toString()
	{
		String str = "";

		str += "Ship Part Holder\n";

		for(int i = 0;i<slots.size();i++)
		{
			str += ">> " + partTypeNames[i] + "\n" + slots.get(i).toString();
		}

		return str;
	}

	public SlotHolder getAllParts(int weaponId) 
	{
		return slots.get(weaponId);
	}

	public ArrayList<String> getAllWeaponAttacks()
	{
		SlotHolder holder = slots.get(WEAPON_ID);
		ArrayList<WeaponPart> tempParts = holder.getAllWeaponParts();
		ArrayList<String> tempAttackNames = new ArrayList<String>();


		for(int i = 0;i<tempParts.size();i++)
		{
			tempAttackNames.add(tempParts.get(i).getAttackName());
		}

		return tempAttackNames;
	}

	public ArrayList<PartSlot> getAllFilledSlots() 
	{

		ArrayList<PartSlot> tempParts = new ArrayList<PartSlot>();

		for(int i = 0;i<slots.size();i++)
		{
			for(int j = 0;j<slots.get(i).slots.size();j++)
			{
				if(slots.get(i).slots.get(j).isEmpty() == false)
				{
					tempParts.add(slots.get(i).slots.get(j));
				}
			}
		}

		return tempParts;
	}

	public ArrayList<PartSlot> getAllSlots() 
	{
		ArrayList<PartSlot> tempParts = new ArrayList<PartSlot>();

		for(int i = 0;i<slots.size();i++)
		{
			for(int j = 0;j<slots.get(i).slots.size();j++)
			{
				tempParts.add(slots.get(i).slots.get(j));
			}
		}

		return tempParts;
	}	
}


