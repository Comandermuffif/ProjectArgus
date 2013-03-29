package org.projectargus.states;

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
import org.projectargus.objects.Fleet;
import org.projectargus.objects.Ship;


public class FleetMenuState extends BasicGameState 
{
	GameData data = GameData.getInstance();
	GameSettings settings = GameSettings.getInstance();

	//Will be assigned actual value on construct from main class
	int stateID = -1;

	//Data
	Fleet fleet;

	//Gui
	//Temporary selecting method
	Rectangle[] shipThumbnails = new Rectangle[0];


	public FleetMenuState( int stateID ) 
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
		fleet = data.getSelectedFleet();

		for(Ship s:fleet.getShips())
		{
			s.updateImage(gc, sbg);
		}

		System.out.println("Entering Fleet Menu");
	}

	public void leave(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		System.out.println("Leaving Fleet Menu");
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		System.out.println("Initializing Fleet Menu");		
	}

	//Draw everything here
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		g.setColor(Color.white);
		g.drawString("Fleet Menu", 600, 20);

		int x = 100;
		int y = 100;
		int t = 20;

		g.drawString(fleet.getName(), x, y+=t);
		g.drawString("Faction: " + fleet.getFaction().getName(), x, y+=t);
		g.drawString("Ships: " + fleet.getShips().size(), x, y+=t);
		g.drawString("Speed: " + fleet.getSpeed(), x, y+=t);

		y += 150;

		//Temporary selecting method
		shipThumbnails = new Rectangle[fleet.getShips().size()];

		for(int i = 0;i<fleet.getShips().size();i++)
		{
			Ship s = fleet.getShips().get(i);

			Image tempShip = s.getImage();
			g.drawRect(x-2,y-2, 104, 104);
			
			//Temporary selecting method
			shipThumbnails[i] = new Rectangle(x-2,y-2,104,104);

			g.drawImage(tempShip, x, y,x+100 ,y+100, 0, 0, tempShip.getWidth(), tempShip.getHeight());
			g.drawString(s.getName(),x,y+120);
			g.drawString(s.getConditionString(),x,y+140);
			x += 150;			
		}		
	}

	//Do all calculations here
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException 
	{					
		Input input = gc.getInput();

		if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
		{
			//Temporary selecting method
			for(int i =0;i<shipThumbnails.length;i++)
			{
				if(shipThumbnails[i].contains(input.getMouseX(), input.getMouseY()))
				{
					System.out.println("Ship rectangle " + i + " clicked.");
					data.setSelectedShip(fleet.getShips().get(i));
					sbg.enterState(Main.SHIP_MENU_STATE);
				}
			}
		}

		if(input.isKeyPressed(Input.KEY_SPACE))
		{
			//If the fleet has 2+ ships, goto resource transfer. 
			if(fleet.getShips().size() > 1)
			{
				sbg.enterState(Main.RESOURCE_TRANSFER_STATE);
			}else{
				sbg.enterState(Main.FIGHT_STATE);
			}
		}
	}

}