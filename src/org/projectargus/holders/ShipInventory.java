package org.projectargus.holders;

import org.projectargus.objects.Ship;
import org.projectargus.parts.WeaponPart;

public class ShipInventory 
{
	PartsHolder parts;
	ResourceHolder resources;

	Ship ship;

	//Stats
	float maxHealth = 100;
	float health = maxHealth;
	
	float baseArmor = 100;
	float baseCargoCapacity = 20000;
	
	
	public ShipInventory(Ship ship)
	{
		this.ship = ship;

		resources = new ResourceHolder();
		parts = new PartsHolder(ship);
	}

	public PartsHolder getParts()
	{
		return parts;
	}

	public ResourceHolder getResources()
	{
		return resources;
	}

	public String toString()
	{
		String str = "";

		str += parts.toString();
		str += "\n";
		str += resources.toString();

		return str;		
	}

	public void loseHealth(float damage)
	{
		if(isAlive())
		{
			health -= damage;
			if(isAlive() == false)
			{
				ship.shipFight.die();
			}
		}
	}
	
	public void gainHealth(float life)
	{
		health += life;
		if(health > maxHealth)
		{
			health = maxHealth;
		}
	}

	public boolean isAlive()
	{
		return health > 0;
	}

	public boolean isIncapacitated() 
	{
		return (((float)health/(float)maxHealth) < .2);
	}
	
	//Health and shit

	public float getMaxHealth()
	{
		return maxHealth;
	}

	public float getHealth()
	{
		return health;
	}
	
	public float getTotalWeight()
	{
		return resources.getAllWeight() + parts.getAllPartsWeight();
	}

	//TODO Calculate
	public float getWeightLimit()
	{
		return baseCargoCapacity;
	}
	
	//TODO Calculate
	public float getTotalArmor()
	{
		return baseArmor;
	}

	public void hit(WeaponPart weapon) 
	{
		float damage = weapon.getDamage();
		float armorPenetration = weapon.getArmourPenetration();
		
		float armorDiff = getTotalArmor() - armorPenetration;
		
		if(armorDiff < 0)
		{
			armorDiff = 0;
		}
		
		float finalDamage = (float) (damage / (Math.log(armorDiff+1) + 1));
		
		System.out.println(finalDamage);
		
		loseHealth(finalDamage);
	}
}
