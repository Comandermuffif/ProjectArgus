package org.projectargus.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.Main;
import org.projectargus.data.*;

public class SpaceMapState extends BasicGameState 
{
	//Will be assigned actual value on construct from main class
	int stateID = -1;

	//A singleton class which stores all data across all classes
	GameData data = null;

	public SpaceMapState( int stateID ) 
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
		System.out.println("Entering Space Map");
	}

	public void leave(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		System.out.println("Leaving Space Map");
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		System.out.println("Initializing Space Map");
	}

	//Draw everything here
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		g.drawString("Space Map", 350, 275);
	}

	//Do all calculations here
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException 
	{
		Input input = gc.getInput();

		if(input.isKeyPressed(Input.KEY_SPACE))
		{
			sbg.enterState(Main.SOLAR_SYSTEM_MAP_STATE);
		}
	}

}