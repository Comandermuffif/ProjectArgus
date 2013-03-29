package org.projectargus.states;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.Main;
import org.projectargus.data.GameData;
import org.projectargus.data.GameSettings;
import org.projectargus.gui.components.Button;
import org.projectargus.gui.components.ButtonListener;
import org.projectargus.gui.components.Component;


public class MainMenuState extends BasicGameState implements ButtonListener
{
	Sound s;

	Button newGameBtn = null;// = new Button(new Rectangle(settings.getScreenX()/2 - w/2,175,w,h),this,"New Game");
	Button loadGameBtn = null;// = new Button(new Rectangle(settings.getScreenX()/2 - w/2,250,w,h),this,"Load Game");
	Button settingsBtn = null; //new Button(new Rectangle(settings.getScreenX()/2 - w/2,325,w,h),this,"Settings");
	Button creditBtn = null;//new Button(new Rectangle(settings.getScreenX()/2 - w/2,400,w,h),this,"Credits");
	Button exitBtn = null;//new Button(new Rectangle(settings.getScreenX()/2 - w/2,475,w,h),this,"Exit");
		
	ArrayList<Button> buttons = new ArrayList<Button>();
	
	GameSettings settings = GameSettings.getInstance();
	
	Image img;
	
	//Will be assigned actual value on construct from main class
	int stateID = -1;
	
	//A singleton class which stores all data across all classes
	GameData data = null;
	
	public MainMenuState( int stateID ) 
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
		System.out.println("Entering Main Menu");
	}
	
	public void leave(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		System.out.println("Leaving Main Menu");
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		System.out.println("Initializing Main Menu");
		
		try {
			img = new Image("res/img/titleImg.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int w = 200;
		int h = 50;
		int y = 150;
		int t = 75;
		
		newGameBtn = new Button(new Rectangle(settings.getScreenX()/2 - w/2,y+=t,w,h),this,"New Game");
		loadGameBtn = new Button(new Rectangle(settings.getScreenX()/2 - w/2,y+=t,w,h),this,"Load Game");
		settingsBtn = new Button(new Rectangle(settings.getScreenX()/2 - w/2,y+=t,w,h),this,"Settings");
		creditBtn = new Button(new Rectangle(settings.getScreenX()/2 - w/2,y+=t,w,h),this,"Credits");
		exitBtn = new Button(new Rectangle(settings.getScreenX()/2 - w/2,y+=t,w,h),this,"Exit");
		
		buttons.add(newGameBtn);
		buttons.add(loadGameBtn);
		buttons.add(settingsBtn);
		buttons.add(creditBtn);
		buttons.add(exitBtn);
		
	}

	//Draw everything here
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		g.drawImage(img,settings.getScreenX()/2 - img.getWidth()/2,50);
		
		for(Button button:buttons)
		{
			button.render(gc, sbg, g);
		}
	}

	//Do all calculations here
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException 
	{		
		for(Button button:buttons)
		{
			button.update(gc, sbg, delta);
		}
		
		Input input = gc.getInput();
		
		if(input.isKeyPressed(Input.KEY_SPACE))
		{
			sbg.enterState(Main.SOLAR_SYSTEM_MAP_STATE);
		}
	}

	@Override
	public void buttonClicked(Component component) 
	{
		if(component.equals(newGameBtn))
		{
			System.out.println("New Game!");
		}
		else if(component.equals(loadGameBtn))
		{
			System.out.println("Load Game!");
		}
		else if(component.equals(settingsBtn))
		{
			System.out.println("Settings!");
		}
		else if(component.equals(creditBtn))
		{
			System.out.println("Credits!");
		}
		else if(component.equals(exitBtn))
		{
			System.out.println("Exiting!");
			System.exit(0);
		}
	}

}