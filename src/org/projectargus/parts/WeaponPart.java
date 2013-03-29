package org.projectargus.parts;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.objects.Ship;
import org.projectargus.parts.attack.Attack;


public class WeaponPart extends ShipPart
{
	float damage;
	float armorPenetration;
	int maxTargets;
	
	Attack attack;	
	
	/**
	 * 
	 * @param name Name of the weapon
	 * @param description Descritpion of the weapon
	 * @param damage The damage done by the weapon
	 * @param armorPenetration The armorPenetration of the weapon
	 * @param weight The weight of the weapon
	 */
	public WeaponPart(String name,String description,float damage,float armorPenetration, float weight)
	{
		super(name, description, weight);
		
		this.damage = damage;
		this.armorPenetration = armorPenetration;
	}
	
	public void fireWeapon(ArrayList<Ship> targets)
	{
		System.out.println("Weapon Fire Called");
		attack.fire(slot,targets);
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
	{		
		super.render(gc, sbg, g);
		
		if(attack != null)
		{
			attack.render(gc, sbg, g);
		}		
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta, Rectangle screenRect) 
	{
		super.update(gc, sbg, delta, screenRect);
		
		if(attack != null)
		{
			attack.update(gc, sbg, delta, screenRect);
		}
	}
	
	
	public String toString()
	{
		String str = super.toString();
		
		return str;
	}

	public String getAttackName() 
	{
		return name;
	}

	public boolean isDoneFiring() 
	{
		return attack.isDone();
	}

	public Attack getAttack() 
	{
		return attack;
	}
	
	//Stats
	public float getDamage()
	{
		return damage;
	}
	
	public float getArmourPenetration()
	{
		return armorPenetration;
	}

	public int getMaxTargets() 
	{
		return maxTargets;
	}
}
