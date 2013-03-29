package org.projectargus.other;

import java.util.ArrayList;

import org.projectargus.objects.Fleet;
import org.projectargus.objects.SpaceObject;

public class Faction 
{
	String name;
	boolean isPlayer;
	
	ArrayList<SpaceObject> planets = new ArrayList<SpaceObject>();
	ArrayList<Fleet> fleets = new ArrayList<Fleet>();
		
	ArrayList<ArrayList<Faction>> factionStances = new ArrayList<ArrayList<Faction>>();
	
	
	public static final int ENEMY = 0;
	public static final int ALLY = 1;
	public static final int NEUTRAL = -1;
	
	public Faction(String name,boolean player)
	{
		this(name);
		isPlayer = player;
	}
	
	public Faction(String name)
	{
		this.name = name;
		
		factionStances.add(new ArrayList<Faction>());
		factionStances.add(new ArrayList<Faction>());
	}
	
	public void addAllyFaction(Faction f)
	{
		addFaction(f,ALLY);
	}
	
	public void addEnemyFaction(Faction f)
	{
		addFaction(f,ENEMY);
	}
		
	public void removeFaction(Faction f)
	{
		for(int i =0;i<factionStances.size();i++)
		{
			if(factionStances.remove(f))
			{
				f.removeFaction(this);
			}
		}
	}
	
	public void addFaction(Faction f,int stance)
	{
		removeFaction(f);
		factionStances.get(stance).add(f);
		f.addFactionSecond(this, stance);
	}
	
	public void addFactionSecond(Faction f,int stance)
	{
		removeFaction(f);
		factionStances.get(stance).add(f);
	}
	
	public int checkStance(Faction f)
	{
		for(int i =0;i<factionStances.size();i++)
		{
			for(int j=0;j<factionStances.get(i).size();j++)
			{
				if(factionStances.get(i).get(j).equals(f))
				{
					return i;
				}
			}
		}
		return -1;
	}
	
	
	public void addPlanet(SpaceObject p)
	{
		if(p.getFaction() != null)
		{
			p.getFaction().removePlanet(p);
		}
		
		p.setFaction(this);
		planets.add(p);
	}
	
	public void removePlanet(SpaceObject p)
	{
		p.setFaction(null);
		planets.remove(p);
	}
	
	public void addFleet(Fleet f)
	{
		if(f.getFaction() != null)
		{
			f.getFaction().removeFleet(f);
		}
		
		f.setFaction(this);
		fleets.add(f);
	}
	
	public void removeFleet(Fleet f)
	{
		f.setFaction(null);
		fleets.remove(f);
	}
	
	public boolean isUser()
	{
		return isPlayer;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void print()
	{
		System.out.println(toString());
	}
	
	public String toString()
	{
		String temp = "-------- Name: " + name + "\nAlliances--------------\nEnemies: ";
		for(int i = 0;i<factionStances.get(ENEMY).size();i++)
		{
			temp += factionStances.get(ENEMY).get(i).getName() + ", ";
		}
		temp += "\nAllies: ";
		for(int i = 0;i<factionStances.get(ALLY).size();i++)
		{
			temp += "," + factionStances.get(ALLY).get(i).getName();
		}
		return temp;
	}	
}
