package org.projectargus.holders;

import java.util.ArrayList;


//COPPER,IRON,GOLD,FUEL,PARTS

public class ResourceHolder 
{
	public static final int COPPER_ID = 0; //Weighted
	public static final int IRON_ID = 1; //Weighted
	public static final int GOLD_ID = 2; //Weighted
	public static final int FUEL_ID = 3; //Weighted
	public static final int PARTS_ID = 4; //Weighted
	public static final int CASH_ID = 5;
	public static final int CRYSTAL_ID = 6; //Weighted
	public static final int POPULATION_ID = 7;
	
	public static final String[] resourceNames = {"Copper","Iron","Gold",
		"Fuel","Parts","Cash","Crystals","People"};
	
	private int[] wholeNumberResources = {PARTS_ID,CASH_ID,CRYSTAL_ID,POPULATION_ID};
	
	public float[] resources = new float[8];
	public float[] weights = new float[8];
	
	public ResourceHolder()
	{
		resources[COPPER_ID] = (float) (Math.random()*10000);
		resources[IRON_ID] = (float) (Math.random()*10000);
		resources[GOLD_ID] = (float) (Math.random()*100);
		resources[FUEL_ID] = (float) (Math.random()*1000 +10);
		resources[PARTS_ID] = (int) (Math.random()*500 + 125);
		resources[CASH_ID] = (int) (Math.random()*1000) * 100;
		resources[CRYSTAL_ID] = (int) (Math.random()*6)*100;
		resources[POPULATION_ID] = (int) (Math.random()*100 + 20);
		
		//Set Weights for specific resources?
		weights[FUEL_ID] = 10;
		weights[PARTS_ID] = 5;
		weights[CRYSTAL_ID] = 50;
	}
	
	public float getResource(int id)
	{
		return resources[id];
	}
	
	public void setResource(int id, float amount)
	{
		resources[id] = amount;
		if(wholeNumberResources(id))
		{
			resources[id] = Math.round(resources[id]);
		}
	}
	
	public void addResource(int id, float amount)
	{
		resources[id] += amount;
		if(wholeNumberResources(id))
		{
			resources[id] = Math.round(resources[id]);
		}
	}
	
	public float subtractResource(int id, float amount)
	{
		float diff = amount;
		
		if(getResource(id) < amount)
		{
			diff = getResource(id);
		}
		
		addResource(id,-diff);
		
		return diff;
	}
	
	public String toString()
	{
		String str = "";

		for(int i = 0;i<resources.length;i++)
		{
			String end = "";
			String res = "" + getResource(i);
			
			if(i == COPPER_ID ||i == IRON_ID ||i == GOLD_ID)
				end = " Kgs";
			if(i == FUEL_ID)
				end = " gal";
			if(i == POPULATION_ID)
			{
				end = " people";
				res = "" + (int)getResource(i);
			}
			if(i == PARTS_ID)
			{
				end = " parts";
				res = "" + (int)getResource(i);
			}
			if(i == CRYSTAL_ID)
			{
				end = " crystals";
				res = "" + (int)getResource(i);
			}
			if(i == CASH_ID)
			{
				end = " dollars";
				res = "" + (int)getResource(i);
			}
			
			str += resourceNames[i] + " : " + res + end + "\n";
		}
		return str;
	}
	
	public float getWeight(int id)
	{
		switch(id)
		{
		case COPPER_ID:
		case IRON_ID:
		case GOLD_ID:
			return resources[id];
		case FUEL_ID:
		case PARTS_ID:
		case CRYSTAL_ID:
			return weights[id] * resources[id];
		case CASH_ID:
		case POPULATION_ID:
			return 0;
		}
		
		System.err.println("Resource does not have id.");
		
		return -100000000;
	}
	
	public float getAllWeight()
	{
		float sum = 0;
		for(int i = 0;i<resources.length;i++)
		{
			sum += getWeight(i);
		}
		return sum;
	}
	
	public float[] getAllResources() 
	{
		float[] res = new float[resources.length];
		
		for(int i = 0;i<resources.length;i++)
		{
			res[i] = getResource(i);
		}
		
		return res;
	}
	
	public boolean wholeNumberResources(int id)
	{
		for(int i = 0;i<wholeNumberResources.length;i++)
		{
			if(wholeNumberResources[i] == id)
			{
				return true;
			}
		}
		return false;
	}
}
