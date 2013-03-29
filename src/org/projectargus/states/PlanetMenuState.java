package org.projectargus.states;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.Main;
import org.projectargus.data.GameData;
import org.projectargus.data.GameSettings;
import org.projectargus.gui.components.Button;
import org.projectargus.gui.components.ButtonListener;
import org.projectargus.gui.components.Component;
import org.projectargus.holders.ResourceHolder;
import org.projectargus.objects.Fleet;
import org.projectargus.objects.Planet;
import org.projectargus.objects.Ship;


public class PlanetMenuState extends BasicGameState implements ButtonListener
{
	GameData data = GameData.getInstance();
	GameSettings settings = GameSettings.getInstance();

	//Will be assigned actual value on construct from main class
	int stateID = -1;

	Button storeBtn;
	Button resourceBtn;
	Button backBtn;

	//Current Menu State
	State state = State.MAIN;

	enum State
	{
		MAIN,STORE,RESOURCES
	}

	//Data
	Planet planet;

	public PlanetMenuState( int stateID ) 
	{
		this.stateID = stateID;
	}

	@Override
	public int getID() 
	{
		return stateID;
	}

	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		planet = data.getSelectedPlanet();

		System.out.println("Entering Planet Menu");
	}

	public void leave(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		System.out.println("Leaving Planet Menu");
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		System.out.println("Initializing Planet Menu");	

		//Inits all buttons
		storeBtn = new Button(new Rectangle(650,100,150,50),this,"Go To Store");
		resourceBtn = new Button(new Rectangle(650,175,150,50),this,"Get Resources");
		backBtn = new Button(new Rectangle(25,50,150,50),this,"Go Back");
	}

	//Draw everything here
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		g.setColor(Color.green);
		g.drawString("Menu State: " + state, 100, 10);

		g.setColor(Color.white);
		g.drawString("Planet Menu", 600, 20);

		switch(state)
		{
		case MAIN:
			int x = 100;
			int y = 100;
			int t = 20;

			Image planetImg = planet.getImage();

			g.setColor(new Color(25,25,25));
			g.fillRect(x, y, 150,150);
			g.setColor(new Color(255,255,255));
			g.drawImage(planetImg, x, y, x+150, y+150, 0, 0, planetImg.getWidth(), planetImg.getHeight());
			y += 150;

			g.drawString(planet.getName(), x+25, y);	
			g.drawString("Faction: " + planet.getFaction().getName(),x,y+=50);
			g.drawString("SS: " + planet.getSun().getName(), x, y+=t);
			g.drawString("X: " + planet.getX(), x, y+=t);
			g.drawString("Y: " + planet.getY(), x, y+=t);
			g.drawString("Size: " + planet.getWidth(), x, y+=t);

			g.drawString("Population: " + planet.getResources().getResource(ResourceHolder.POPULATION_ID), x, y+=t);

			ArrayList<Fleet> temp = data.getAllFleetsDockedOnObject(planet);

			y = 100;
			x+= 250;

			g.drawString("Fleets docked:", x, y);

			for(int i = 0;i<temp.size();i++)
			{
				g.drawString("--" + temp.get(i).getName(), x, y+=t);
			}

			g.drawString("Fleetless ships docked:", x, y+=50);

			ArrayList<Ship> tempShips = planet.getAllFleetlessShips();
			for(int i = 0;i<tempShips.size();i++)
			{
				g.drawString("--" + tempShips.get(i).getName(), x, y+=t);
			}

			storeBtn.render(gc, sbg, g);
			resourceBtn.render(gc, sbg, g);

			break;
		case STORE:
			backBtn.render(gc, sbg, g);
		case RESOURCES:
			backBtn.render(gc, sbg, g);
		}


		//TODO Draw all basic info, faction, size, resources, store everything.
	}

	//Do all calculations here
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException 
	{					
		Input input = gc.getInput();

		switch(state)
		{
		case MAIN:
			storeBtn.update(gc, sbg, delta);
			resourceBtn.update(gc, sbg, delta);
			break;
		case STORE:
			backBtn.update(gc, sbg, delta);
			break;
		case RESOURCES:
			backBtn.update(gc, sbg, delta);
			break;
		}

		if(input.isKeyPressed(Input.KEY_SPACE))
		{
			sbg.enterState(Main.FLEET_MENU_STATE);
		}
	}

	@Override
	public void buttonClicked(Component component) 
	{
		if(component.equals(storeBtn))
		{
			System.out.println("Store Button Clicked");
			state = State.STORE;
		}
		else if(component.equals(resourceBtn))
		{
			System.out.println("Resource Button Clicked!");
			state = State.RESOURCES;
		}
		else if(component.equals(backBtn))
		{
			System.out.println("Back Button Clicked!");
			state = State.MAIN;
		}
		else{
			System.out.println("Err:No Button action!");
		}
	}

}