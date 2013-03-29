package org.projectargus;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.states.*;
import org.projectargus.data.GameSettings;

public class Main extends StateBasedGame {

	//Make an enum of all states
	public static int MAIN_MENU_STATE = 0;
	public static int SPACE_MAP_STATE = 1;
	public static int SOLAR_SYSTEM_MAP_STATE = 2;
	public static int FIGHT_STATE = 3;
	public static int SHIP_MENU_STATE = 4;
	public static int FLEET_MENU_STATE = 5;
	public static int PLANET_MENU_STATE = 6;
	public static int RESOURCE_TRANSFER_STATE = 7;
	
	
	static GameSettings settings = GameSettings.getInstance();

	public Main()
	{
		//Set window title
		super("Project Argus");
	}

	public static void main(String[] args) throws SlickException
	{
		//Set up app container
		StateBasedGame sbg = new Main();
		AppGameContainer app = new AppGameContainer(sbg);
		
		settings.setGame(sbg,app);
		
		//Set screen dimentions
		settings.setScreenX(1280);
		settings.setScreenY(720);
		
		//Set resolution
		app.setDisplayMode(settings.getScreenX(), settings.getScreenY(), false); //Fix
		
		//Start game
		app.start();
	}

	@Override
	public void initStatesList(GameContainer gameContainer) throws SlickException {
		
		//Add all states here
		this.addState(new MainMenuState(MAIN_MENU_STATE));
		this.addState(new ShipMenuState(SHIP_MENU_STATE));
		this.addState(new SpaceMapState(SPACE_MAP_STATE));
		this.addState(new SolarSystemMapState(SOLAR_SYSTEM_MAP_STATE));
		this.addState(new FightState(FIGHT_STATE));
		this.addState(new FleetMenuState(FLEET_MENU_STATE));
		this.addState(new PlanetMenuState(PLANET_MENU_STATE));
		this.addState(new ResourceTransferState(RESOURCE_TRANSFER_STATE));
		
	}
}