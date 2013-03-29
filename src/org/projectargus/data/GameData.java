package org.projectargus.data;

import java.util.ArrayList;

import org.projectargus.objects.Fleet;
import org.projectargus.objects.Planet;
import org.projectargus.objects.Ship;
import org.projectargus.objects.SolarSystem;
import org.projectargus.objects.SpaceObject;
import org.projectargus.other.Faction;

public class GameData 
{	

	private ArrayList<SolarSystem> solarSystems = new ArrayList<SolarSystem>(); 
	private SolarSystem currentSolarSystem = null;
	private ArrayList<Faction> factions = new ArrayList<Faction>();  
	private ArrayList<Fleet> fleets = new ArrayList<Fleet>(); 

	public Fleet playerFightFleet;
	public Fleet enemyFightFleet;

	Ship selectedShip = null;
	Fleet selectedFleet = null;
	Planet selectedPlanet = null;
	
	//Fight Screen
	public int fightScreenSize = 100000;

	private static GameData instance = null;

	public static GameData getInstance()
	{
		if(instance == null)
			instance = new GameData();

		return instance;
	}	

	public void addFleet(Fleet f)
	{
		fleets.add(f);
	}

	public void addFaction(Faction f)
	{
		factions.add(f);
	}

	public ArrayList<Faction> getFactions()
	{
		return factions;
	}	

	//Change to all fleets in current solarsystem....
	public ArrayList<Fleet> getAllFleetsInCurrentSolarSystem()
	{
		return fleets;
	}

	public ArrayList<Fleet> getAllFleets() 
	{
		return fleets;
	}

	public void setSelectedFleet(Fleet fleet)
	{
		selectedFleet = fleet;
	}

	public Fleet getSelectedFleet()
	{
		if(selectedFleet == null)
		{
			return getAllFleets().get(0);
		}
		return selectedFleet;
	}

	public SolarSystem getCurrentSolarSystem()
	{
		return currentSolarSystem;
	}

	public void setCurrentSolarSystem(SolarSystem solarSystem) 
	{
		currentSolarSystem = solarSystem;
	}

	public ArrayList<SolarSystem> getAllSolarSystems() 
	{
		return solarSystems;
	}

	public void addSolarSystem(SolarSystem ss) 
	{
		solarSystems.add(ss);		
	}

	public void setFightFleets(Fleet playerFleet, Fleet enemyFleet)
	{
		playerFightFleet = playerFleet;
		enemyFightFleet = enemyFleet;

		playerFightFleet.fleetFight.setFightPos();
		enemyFightFleet.fleetFight.setFightPos();

	}

	public Ship getSelectedShip()
	{
		if(selectedShip == null)
		{
			return fleets.get(0).getShips().get(0);
		}else{
			return selectedShip;
		}
	}

	public void setSelectedShip(Ship ship) 
	{
		selectedShip = ship;
	}

	public Planet getSelectedPlanet() 
	{
		if(selectedPlanet == null)
		{
			return (Planet) currentSolarSystem.getChildren().get(1);
		}else{
			return selectedPlanet;
		}
	}

	public void setSelectedPlanet(Planet planet) 
	{
		selectedPlanet = planet;
	}

	public void setSelectedByType(SpaceObject temp) 
	{
		if(temp instanceof Planet)
		{
			setSelectedPlanet((Planet)temp);
		}
		else if(temp instanceof Fleet)
		{
			setSelectedFleet((Fleet)temp);
		}
		else{	
			
		}
	}
	
	public ArrayList<Fleet> getAllFleetsDockedOnObject(SpaceObject spaceObject)
	{
		ArrayList<Fleet> temp = new ArrayList<Fleet>();
		
		ArrayList<Fleet> allFleets = getAllFleets();
		
		for(int i = 0;i<allFleets.size();i++)
		{
			if(spaceObject.equals(allFleets.get(i).getDockedObject()))
			{
				temp.add(allFleets.get(i));
			}
		}
		
		return temp;
	}
}

