package org.projectargus.gui.components;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.objects.Ship;
import org.projectargus.parts.PartSlot;
import org.projectargus.parts.ShipPart;

public class ShipPartSlotComponent extends Component
{
	//Data
	Ship ship;

	//List of all slot boxes for UI interaction
	ArrayList<ShipSlotBox> shipSlotBoxes = new ArrayList<ShipSlotBox>();

	ShipPart selectedPart = null;
	ShipPart hoveredPart = null;


	public ShipPartSlotComponent(Rectangle dims, ComponentListener listener,Ship ship) {
		super(dims, listener);

		this.ship = ship;

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
	{
		//Ship Image
		Image bigShipImg = ship.getImage();

		//Drawing the Big ship
		g.drawImage(bigShipImg,dims.getX(),dims.getY(),
				dims.getX() + dims.getWidth(),
				dims.getY() + dims.getHeight(),
				0,0,bigShipImg.getWidth(),bigShipImg.getHeight());

		//Loops through all the slot boxes
		//And draws the rectangle
		for(int i = 0;i<shipSlotBoxes.size();i++)
		{
			Rectangle rect = shipSlotBoxes.get(i).getRectangle();
			g.setColor(new Color(255,255,255,75));
			g.fill(rect);
		}	
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) 
	{
		Input input = gc.getInput();

		//Create Slots for the ship slots
		ArrayList<PartSlot> slots = ship.getInventory().getParts().getAllSlots();			

		//Empty the list every update
		shipSlotBoxes.clear();

		for(int i = 0;i<slots.size();i++)
		{
			PartSlot tempSlot = slots.get(i);

			//Gets the ratio of the draw image to the ships actual width
			float ratio = dims.getWidth()/ship.getWidth();

			int slotW;
			int slotH;

			if(tempSlot.isEmpty())
			{
				slotW = (int) (10 * ratio);
				slotH = (int) (10 * ratio);
			}else{
				slotW = (int) (tempSlot.getPart().getWidth() * ratio);
				slotH = (int) (tempSlot.getPart().getHeight() * ratio);	
			}

			int slotX = (int) (dims.getX() + (tempSlot.getRelativeX()) * ratio) - slotW/2;
			int slotY = (int) (dims.getY() +  (tempSlot.getRelativeY()) * ratio) - slotH/2;

			Rectangle rect = new Rectangle(slotX,slotY,slotW,slotH);

			shipSlotBoxes.add(new ShipSlotBox(tempSlot,rect));
		}

		hoveredPart = null;
		
		for(int i = 0;i<shipSlotBoxes.size();i++)
		{
			Rectangle rect = shipSlotBoxes.get(i).getRectangle();
			PartSlot slot = shipSlotBoxes.get(i).getSlot();

			if(rect.contains(input.getMouseX(),input.getMouseY()))
			{
				if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
				{
					//Adds part to inventory
					if(slot.getPart() != null)
					{
						System.out.println(i + " was clicked. It contains: " + slot.getPart().getName());
						ShipPart part = slot.removePart();
						ship.getInventory().getParts().addPartToInventory(part);
						ship.updateImage(gc, sbg);
					}else{
						//Adds part to slot from inventory
						System.out.println(i + " was clicked. It does not contain a part.");
						if(selectedPart != null)
						{
							//Checks if the part is the same type as the slot
							if(slot.getType() == selectedPart.getType())
							{
								slot.addPart(selectedPart);
								ship.getInventory().getParts().removePartFromInventory(selectedPart);
								selectedPart = null;
								ship.updateImage(gc, sbg);
							}else{
								System.out.println("Invalid weapon type");
							}
						}
					}
				}//Mouse Hovering over
				else
				{
					hoveredPart = shipSlotBoxes.get(i).getSlot().getPart();
				}
			}
		}	
	}

	/**
	 * If null then set selected part to null
	 * @return
	 */
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
	
	public class ShipSlotBox
	{
		PartSlot slot;
		Rectangle rect;

		public ShipSlotBox(PartSlot slot,Rectangle rect)
		{
			this.slot = slot;
			this.rect = rect;
		}

		public Rectangle getRectangle() 
		{
			return rect;
		}

		public PartSlot getSlot()
		{
			return slot;
		}
	}
}
