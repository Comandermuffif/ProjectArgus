package org.projectargus.factory;

import java.util.ArrayList;

import org.projectargus.data.GameData;
import org.projectargus.objects.Fleet;
import org.projectargus.objects.SolarSystem;
import org.projectargus.objects.SpaceObject;
import org.projectargus.other.Faction;

public class FactionFactory 
{
	GameData data = GameData.getInstance();

	String[] factionNames = {"Death Haven","Legions","Dwarfs","The Raiders","Guardians"};


	public FactionFactory()
	{

	}

	public void setFactions()
	{
		createFactions();
		assignFactions();
	}

	public void createFactions()
	{
		int amount = (int) (Math.random()*4 + 1);
		for(int i = 0;i<amount;i++)
		{
			Faction f;

			if(i == 0)
			{
				f = new Faction(createRandomFactionName(),true);
			}else{
				f = new Faction(createRandomFactionName());
			}

			data.addFaction(f);
		}
	}

	public void assignFactions()
	{		
		//Assigns a faction to each fleet
		for(int i = 0;i<data.getAllFleets().size();i++)
		{
			Fleet fleet = data.getAllFleets().get(i);
			int size = data.getFactions().size();
			Faction faction = data.getFactions().get(i%size);
			
			faction.addFleet(fleet);
		}
		
		for(int i = 0;i<data.getFactions().size();i++)
		{
			System.out.println("Each Faction: " + i);
			Faction f = data.getFactions().get(i);
			int size = getAllUnassignedPlanets().size();
			int factionsLeft = data.getFactions().size() - i;

			int factionsToAssign = (int) Math.ceil((double)size/(double)factionsLeft);

			System.out.println(size + " ; " + factionsLeft + " ; " + factionsToAssign);

			for(int j=0;j<factionsToAssign;j++)
			{
				f.addPlanet(getAllUnassignedPlanets().get(0));
			}			
		}
	}

	public Faction getUserFaction()
	{
		for(int i = 0;i<data.getFactions().size();i++)
		{
			if(data.getFactions().get(i).isUser())
			{
				return data.getFactions().get(i);
			}
		}
		return null;
	}

	public String createRandomFactionName()
	{
		int i;
		do{
			i = (int) (Math.random() *factionNames.length);
		}while(checkForFactionName(factionNames[i]));
		return factionNames[i];
	}

	public ArrayList<SpaceObject> getAllUnassignedPlanets()
	{
		ArrayList<SolarSystem> solarSystemList = data.getAllSolarSystems();
		ArrayList<SpaceObject> planetList = new ArrayList<SpaceObject>();

		for(int i = 0;i<solarSystemList.size();i++)
		{
			for(int j = 0;j<solarSystemList.get(i).getChildren().size();j++)
			{
				SpaceObject p = solarSystemList.get(i).getChildren().get(j);

				if(p.getFaction() == null)
				{
					planetList.add(p);
				}
			}
		}	
		
		return planetList;			
	}

	public boolean checkForFactionName(String name)
	{
		for(int i = 0;i<data.getFactions().size();i++)
		{
			if(data.getFactions().get(i).getName().equals(name))
			{
				return true;
			}
		}
		return false;
	}


}