package org.projectargus.gui.components;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.parts.ShipPart;


public class PartInventoryComponent extends Component
{
	ArrayList<ShipPart> parts;
	ArrayList<ShipPartBox> inventoryBoxes = new ArrayList<ShipPartBox>();
	
	//Gui Vars
	
	//Size of part images
	int partSize = 30;
	//Sets how far to the left the top label will be from the middle
	int labelOffset = 50;
	//Amount of space each part has
	int partInterval = 50;
	//Offset up top
	int offsetY = 25;
	
	//Parts vars
	ShipPart selectedPart = null;
	ShipPart hoveredPart = null;
	
	public PartInventoryComponent(Rectangle dims, ComponentListener listener, ArrayList<ShipPart> parts) {
		super(dims, listener);
		this.parts = parts;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
	{
		g.draw(dims);
		
		int x1 = (int) dims.getX();
		int y1 = (int) dims.getY();

		g.setColor(Color.white);
		g.drawString("Inventory", x1 + dims.getWidth()/2 - labelOffset, y1);
		g.setColor(Color.gray);

		for(int i = 0;i<inventoryBoxes.size();i++)
		{	
			ShipPartBox box = inventoryBoxes.get(i);
			ShipPart tempPart = box.getPart();
			Rectangle rect = box.getRectangle();
						
			//Make special if its selected :D
			if(tempPart.equals(selectedPart))
			{
				g.setColor(new Color(255,165,0,150));
				int tempXOffset = (int)(partInterval - rect.getWidth()); 
				int tempYOffset = (int)(partInterval - rect.getHeight()); 
				Rectangle tempRect = new Rectangle(rect.getX() - tempXOffset/2,rect.getY() - tempYOffset/2,50,50);
				g.fill(tempRect);
			}
			
			g.drawImage(tempPart.getImage(), rect.getX(),rect.getY(),rect.getX()+rect.getWidth(),rect.getY() + rect.getHeight(),(float)0,(float)0,(float)tempPart.getImage().getWidth(),(float)tempPart.getImage().getHeight());
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) 
	{
		inventoryBoxes.clear();
		
		int numColumns = (int)(dims.getWidth() / partInterval);
		
		int startX = (int)(dims.getX() + (dims.getWidth() % partInterval)/2);
		int startY = (int)dims.getY() + offsetY;
		
		int x = startX;
		int y = startY;
		
		for(int i = 0;i<parts.size();i++)
		{	
			ShipPart tempPart = parts.get(i);
			Rectangle rect = new Rectangle(x + (partInterval - partSize)/2,
					y + (partInterval - partSize)/2,partSize,partSize);
			inventoryBoxes.add(new ShipPartBox(tempPart, rect));
			if(i % numColumns  == numColumns-1)
			{
				x = startX;
				y += partInterval;
			}else{
				x+= partInterval;
			}
		}
		
		Input input = gc.getInput();
		
		hoveredPart = null;
		
		for(int i = 0;i<inventoryBoxes.size();i++)
		{
			if(inventoryBoxes.get(i).getRectangle().contains(input.getMouseX(),input.getMouseY()))
			{
				if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
				{					
					if(inventoryBoxes.get(i).getPart().equals(selectedPart))
					{
						//Deselects if already selected
						selectedPart = null;
					}else
					{
						//Selects if not already selected
						selectedPart = inventoryBoxes.get(i).getPart();
					}
				}
				else
				{
					hoveredPart = inventoryBoxes.get(i).getPart();
				}
				break;
			}
		}	
	}
	
	public ShipPart getSelectedPart()
	{
		return selectedPart;
	}
	
	public ShipPart getHoveredPart()
	{
		return hoveredPart;
	}
	
	public void setSelectedPart(ShipPart part)
	{
		selectedPart = part;
	}
	
	

	public class ShipPartBox
	{
		ShipPart part;
		Rectangle rect;

		public ShipPartBox(ShipPart part,Rectangle rect)
		{
			this.part = part;
			this.rect = rect;
		}

		public Rectangle getRectangle() 
		{
			return rect;
		}

		public ShipPart getPart()
		{
			return part;
		}
	}
}
