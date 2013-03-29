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
import org.projectargus.gui.components.Button;
import org.projectargus.gui.components.ButtonListener;
import org.projectargus.gui.components.Component;
import org.projectargus.gui.components.PartInfoComponent;
import org.projectargus.gui.components.PartInventoryComponent;
import org.projectargus.gui.components.ShipPartSlotComponent;
import org.projectargus.objects.Ship;
import org.projectargus.parts.ShipPart;


public class ShipMenuState extends BasicGameState implements ButtonListener
{
	GameData data = GameData.getInstance();
	GameSettings settings = GameSettings.getInstance();

	//Will be assigned actual value on construct from main class
	int stateID = -1;

	//Data
	Ship ship;	

	//Menu states
	enum State{
		MAIN,PARTS
	}

	State state = State.MAIN;

	//GUI
	//Rectangle partsBtn = new Rectangle(800,50,150,50);
	//Rectangle backBtn = new Rectangle(25,50,150,50);

	//Current Ship Part that is selected from inventory
	ShipPart selectedPart = null;
	
	//Current Part hovered over
	ShipPart hoveredPart = null;
	
	PartInventoryComponent inventoryComponent;
	ShipPartSlotComponent shipComponent;
	PartInfoComponent partInfoComponent;
	
	Button backBtn;
	Button partsBtn;
	
	
	public ShipMenuState( int stateID ) 
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
		//Gets the currently selected ship every time the menu is brought up
		ship = data.getSelectedShip();

		
		//Updates the ship image
		if(ship != null)
		{
			inventoryComponent = new PartInventoryComponent(new Rectangle(700,150,300,300),this,ship.getInventory().getParts().getInventoryParts());
			shipComponent = new ShipPartSlotComponent(new Rectangle(150,150,300,300),this,ship);
			partInfoComponent = new PartInfoComponent(new Rectangle(400,500,400,125),this);
			ship.updateImage(gc, sbg);
		}

		//Starts the menu in the Main state when entered.
		state = State.MAIN;

		System.out.println("Entering Ship Menu");
	}

	public void leave(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		System.out.println("Leaving Ship Menu");
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		System.out.println("Initializing Ship Menu");	
		
		partsBtn = new Button(new Rectangle(800,50,150,50), this, "Go To Parts");
		backBtn = new Button(new Rectangle(25,50,150,50), this, "Back");
	}

	//Draw everything here
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		//g.setFont(settings.getBaseFont());
		
		//Sets the sub-state - May delete later
		g.setColor(Color.green);
		g.drawString("State: " + state, 100, 10);
		g.setColor(Color.white);

		switch(state)
		{
		case MAIN:
			int y = 50;
			int x = 100;
			int t = 25;

			g.drawString("Ship Menu", 500, 25);

			g.drawString("----Parts-----", x+500, y + t);
			g.drawString("" + ship.getInventory().getParts().toString(), x+500, y+2*t);

			g.drawString("----Resources-----", x+250, y + t);
			g.drawString("" + ship.getInventory().getResources().toString(), x+250, y+2*t);

			y+= 250;

			g.drawString("----General-----", x, y+=t);
			g.drawString("Name: " + ship.getName(), x, y+=t);
			g.drawString("Faction: " + ship.fleet.getFaction().getName(), x, y+=t);
			g.drawString("Status: " + ship.getConditionString(), x, y+=t);
			g.drawString("X: " + ship.getX(), x, y+=t);
			g.drawString("Y: " + ship.getY(), x, y+=t);
			g.drawString("Width: " + ship.getWidth(), x, y+=t);
			g.drawString("Height: " + ship.getHeight(), x, y+=t);
			g.drawString("Weight: " + ship.getInventory().getTotalWeight(),x,y+=t);

			Image img = ship.getImage();

			g.drawImage(img,100,100,250,250,0,0,img.getWidth(),img.getHeight());

			partsBtn.render(gc, sbg, g);

			break;
		case PARTS:

			inventoryComponent.render(gc, sbg, g);
			shipComponent.render(gc, sbg, g);
			partInfoComponent.render(gc, sbg, g);
			
			backBtn.render(gc, sbg, g);

			break;
		}
	}

	//Do all calculations here
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException 
	{	
		Input input = gc.getInput();

		switch(state)
		{
		case MAIN:
			//Check if the parts button was pressed
			partsBtn.update(gc, sbg, delta);
			
			break;
		case PARTS:

			
			inventoryComponent.update(gc, sbg, delta);
			selectedPart = inventoryComponent.getSelectedPart();
			hoveredPart = inventoryComponent.getHoveredPart();
			
			shipComponent.setSelectedPart(selectedPart);
			
			shipComponent.update(gc, sbg, delta);
			
			
			if(hoveredPart == null)
			{
				hoveredPart = shipComponent.getHoveredPart();
			}

			//Allows shipComponent to change selected part if it is equipped
			selectedPart = shipComponent.getSelectedPart();
			inventoryComponent.setSelectedPart(selectedPart);
			
			//Sets info for the hovered item
			partInfoComponent.setPart(hoveredPart);
			
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
		if(component.equals(backBtn))
		{
			state = State.MAIN;
		}else if(component.equals(partsBtn))
		{
			state = State.PARTS;
		}else{
			System.out.println("ERROR: Unhandled button click!");
		}
	}
}