package org.projectargus.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.Main;
import org.projectargus.data.GameData;
import org.projectargus.gui.components.ComponentListener;
import org.projectargus.gui.components.ResourceTransferComponent;
import org.projectargus.holders.ResourceHolder;
import org.projectargus.objects.Ship;

public class ResourceTransferState  extends BasicGameState implements ComponentListener 
{
	ResourceTransferComponent resourceComponent;
	
	GameData data = GameData.getInstance();
	
	
	private int stateID;
	
	public ResourceTransferState( int stateID ) 
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
		System.out.println("Entering Resource Transfer State");
		
		Ship ship1 = data.getSelectedFleet().getShips().get(0);
		Ship ship2 = data.getSelectedFleet().getShips().get(1);
		
		ResourceHolder resourceHolder1 = ship1.getInventory().getResources();
		ResourceHolder resourceHolder2 = ship2.getInventory().getResources();
		
		//Create Dynamic Components
		
		int width = 500;
		Rectangle rect = new Rectangle(gc.getWidth()/2 - width/2,100,width,width);
		
		resourceComponent = new ResourceTransferComponent(rect,this,
				resourceHolder1,resourceHolder2); 		
	}

	public void leave(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		System.out.println("Leaving Resource Transfer State");
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		System.out.println("Initializing Resource Transfer State");		
	}

	//Draw everything here
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		g.setColor(Color.white);
		g.drawString("Resource Transfer", 
				gc.getWidth()/2 - g.getFont().getWidth("Resource Transfer")/2, 20);
		
		resourceComponent.render(gc, sbg, g);
	}

	//Do all calculations here
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException 
	{	
		resourceComponent.update(gc, sbg, delta);
		
		Input input = gc.getInput();

		if(input.isKeyPressed(Input.KEY_SPACE))
		{
			sbg.enterState(Main.FIGHT_STATE);
		}
	}
}
