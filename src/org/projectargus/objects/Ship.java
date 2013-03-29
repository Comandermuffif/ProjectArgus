package org.projectargus.objects;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.holders.PartsHolder;
import org.projectargus.holders.ShipInventory;
import org.projectargus.parts.PartSlot;
import org.projectargus.parts.WeaponPart;
import org.projectargus.parts.attack.Attack;
import org.projectargus.parts.attack.Explosion;
import org.projectargus.states.FightState;

public class Ship extends SpaceObject
{
	public Fleet fleet;

	public float fleet_x = 0;
	public float fleet_y = 0;

	public ShipFight shipFight;

	ShipInventory inventory;

	Image baseImg;
	Image lastImg;



	/**
	 * @param solarSystem The SolarSystem the ship is in... Pointless?
	 * @param x The x position in the fleet
	 * @param y The y position in the fleet
	 * @param w The width of the ship
	 * @param h The height of the ship
	 */

	public Ship(SolarSystem solarSystem, int x, int y, int w, int h) 
	{
		super(0, 0, w, h);

		try {
			img = new Image("res/img/ship1.png");
			lastImg = new Image(500,500);
			baseImg = img.copy();

		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		shipFight = new ShipFight(this);
		inventory = new ShipInventory(this);

		fleet_x = x;
		fleet_y = y;

		name = "Ship " + ((int)(Math.random()*40) + 1);
	}

	//Draw everything here
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		//If the ship is actually on screen, draw the image
		if(isOnScreen)
		{
			//Set the rotation to the proper angle
			img.setCenterOfRotation(screen_w/2,screen_h/2);
			img.setRotation((float) (fleet.getAngle() * 180 / Math.PI));

			g.drawImage(img, screen_x, screen_y, screen_x + screen_w, screen_y + screen_h, 0, 0, img.getWidth(), img.getHeight());		
		}
	}

	//Do all calculations here
	public void update(GameContainer gc, StateBasedGame sbg, int delta, Rectangle screenRect) throws SlickException 
	{				
		//Gets the hypotenous of the x and y values
		float fleet_d =  (float) Math.sqrt(Math.pow(fleet_x,2) + Math.pow(fleet_y,2));

		//Gets the angle the two legs create
		float fleet_a = (float) Math.atan2(fleet_y, fleet_x);

		//Set the actual position of the ship in the solarsystem
		x = (int) (fleet.getX() + fleet_d * Math.cos(fleet.getAngle() + fleet_a));
		y = (int) (fleet.getY() + fleet_d * Math.sin(fleet.getAngle() + fleet_a));

		super.update(gc, sbg, delta, screenRect);
	}	


	/**
	 * This is voiding the typical checkClick so ships cannot be clicked/hovered over
	 */
	public boolean checkClicked(int mouse_x, int mouse_y)
	{
		//Put nothing here because we dont want it to be clicked.
		return false;
	}


	/**
	 * Assigns a parent fleet to this ship. Use from fleet.addShip()
	 * @param f the fleet you want to be the parent
	 */
	public void setFleet(Fleet f)
	{
		fleet = f;
	}

	/**
	 * Removes the fleet parent from this ship by setting fleet to null. Use from fleet.removeShip()
	 */
	public void removeFleet()
	{
		fleet = null;
	}	




	public void updateImage(GameContainer gc, StateBasedGame sbg)
	{	
		System.out.println(lastImg.getWidth() + " : " + getWidth());
		baseImg.setRotation(0);
		
		Graphics lastG = null;
		try{
			lastG = lastImg.getGraphics();
		}
		catch(Exception e)
		{

		}

		lastG.clear();
		lastG.drawImage(baseImg,lastImg.getWidth()/2-baseImg.getWidth()/2,lastImg.getHeight()/2-baseImg.getHeight()/2);

		for(PartSlot p:getInventory().getParts().getAllFilledSlots())
		{
			//The ratio changes the position and size from game size
			//To the actual image size to draw on.
			int x = (int) ((float)p.getRelativeX() * ((float)baseImg.getWidth()/getWidth()));
			int y = (int) ((float)p.getRelativeY() * ((float)baseImg.getHeight()/getHeight()));
			int w = (int) ((float)p.getPart().getWidth() * ((float)baseImg.getWidth()/getWidth()));
			int h = (int) ((float)p.getPart().getHeight() * (float)baseImg.getHeight()/getHeight());

			p.getPart().updateImage(gc, sbg);
			Image temp = p.getPart().getImage();
			//temp.setRotation(0);

			lastG.drawImage(temp, x-w/2, y-h/2, x+w/2, y+h/2, 0,0,temp.getWidth(),temp.getHeight());
		}

		lastG.flush(); 
	}

	//Need to call updateImage first!
	public Image getImage()
	{
		return lastImg;
	}


	//Getter Methods
	public ShipInventory getInventory()
	{
		return inventory;
	}


	//FIGHT Stuff

	public void fightRender(GameContainer gc, StateBasedGame sbg, Graphics g) 
	{
		shipFight.render(gc, sbg, g);
	}	

	public void fightUpdate(GameContainer gc, StateBasedGame sbg, int delta, Rectangle screenRect) 
	{
		shipFight.update(gc, sbg, delta,screenRect);
	}			

	public class ShipFight 
	{
		float x;
		float y;

		float screen_x = 0;
		float screen_y = 0;
		float screen_w = 0;
		float screen_h = 0;
		double rotation = 0;
		double angle;
		double angleSpeed;
		float radius = 100;
		Ship parent;

		boolean isSelected = false;

		WeaponPart selectedWeapon;

		Explosion explosion;

		Rectangle incapRect = null;


		public ShipFight(Ship s)
		{
			parent = s;
			angle = Math.random()* Math.PI*2;
			angleSpeed = Math.random() * .0004 + .0004;
		}

		public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
		{
			if(parent.getInventory().isAlive())
			{
				if(screen_x != -1 || screen_y != -1)
				{
					g.drawImage(img, screen_x, screen_y, screen_x+screen_w, screen_y+screen_h, 0, 0, img.getWidth(), img.getHeight());			
				}

				inventory.getParts().render(gc, sbg, g);

				if(isIncapacitated())
				{
					if(incapRect != null)
					{
						g.setColor(new Color(255,255,255,100));
						g.fill(incapRect);
					}
				}
			}

			if(explosion != null)
			{
				explosion.render(gc, sbg, g);
			}
		}	

		public void update(GameContainer gc, StateBasedGame sbg, int delta, Rectangle screenRect) 
		{			
			if(parent.fleet.equals(data.playerFightFleet))
			{
				img.setCenterOfRotation(screen_w/2,screen_h/2);
				rotation = 0;
			}else{
				img.setCenterOfRotation(screen_w/2,screen_h/2);
				rotation = 180;
			}

			//rotation += .1 * delta;

			img.setCenterOfRotation(screen_w/2,screen_h/2);
			img.setRotation((float) rotation);
			angle += angleSpeed * delta;

			Rectangle temp = FightState.getScreenDims(gc, new Rectangle(getX(),(float)(getY()),getWidth(),getHeight()), screenRect);

			screen_x = temp.getX();
			screen_y = temp.getY();
			screen_w = temp.getWidth();
			screen_h = temp.getHeight();

			//Update all of the parts
			inventory.getParts().update(gc, sbg, delta,screenRect);

			if(isIncapacitated())
			{
				incapRect = FightState.getScreenDims(gc, new Rectangle(getX(),getY(),getWidth(),getHeight()), screenRect);
			}


			if(explosion != null)
			{
				explosion.update(gc, sbg, delta, screenRect);

				if(explosion.getAnimation().isStopped() &&
						parent.getInventory().isAlive() == false)
				{
					System.out.println("Remove From fleet!!!");
					fleet.getShips().remove(parent);
				}
			}
		}	

		public void setBaseX(float x)
		{
			this.x = x;
		}

		public void setBaseY(float y)
		{
			this.y = y;
		}

		public float getBaseX()
		{
			return x;
		}

		public float getBaseY()
		{
			return y;
		}

		public float getX()
		{
			return (float) (x + radius*Math.cos(angle));
		}

		public float getY()
		{
			return (float) (y + radius*Math.sin(angle));
		}

		public float getRotation()
		{
			return (float) rotation;
		}


		public float getCenterX()
		{
			return (float) getX() + getWidth()/2;
		}

		public float getCenterY()
		{
			return (float) getY() + getHeight()/2;
		}

		public float getScreenX()
		{
			return screen_x;
		}

		public float getScreenY()
		{
			return screen_y;
		}

		public float getWidth()
		{
			return parent.getWidth();
		}

		public float getHeight()
		{
			return parent.getHeight();
		}

		public Image getBaseImage()
		{
			System.out.println("is null - " + img + " : " + img == null);
			return img;
		}

		/**
		 * Checks to see if the mouse clicked within the object
		 * @param mouse_x the x position of the mouse
		 * @param mouse_y the y position of the mouse
		 * @return <b>true</b> if the object was clicked
		 */
		public boolean checkClicked(int mouse_x,int mouse_y)
		{		
			if (mouse_x <= screen_x + screen_w && mouse_x >= screen_x
					&& mouse_y <= screen_y + screen_h && mouse_y >= screen_y)
			{
				System.out.println("Mouse clicked!");
				return true;
			}
			return false;
		}	

		public void selectAttack(int weaponPos)
		{
			selectedWeapon = inventory.getParts().getAllParts(PartsHolder.WEAPON_ID).getAllWeaponParts().get(weaponPos);
		}

		public void fire(ArrayList<Ship> targets)
		{
			selectedWeapon.fireWeapon(targets);
		}

		public Attack getAttack()
		{
			return selectedWeapon.getAttack();
		}

		public boolean isDoneFiring()
		{
			if(explosion == null)
			{
				if(selectedWeapon != null)
				{
					return  selectedWeapon.isDoneFiring();
				}else{
					return true;
				}
			}
			else{
				if(selectedWeapon != null)
				{
					return  selectedWeapon.isDoneFiring() && explosion.getAnimation().isStopped();
				}else{
					return true;
				}
			}
		}

		public int getMaxTargets()
		{
			return selectedWeapon.getMaxTargets();
		}

		public void hit(Attack attack)
		{
			WeaponPart weapon = attack.getWeapon();
			parent.getInventory().hit(weapon);
		}

		public void die() 
		{
			System.out.println("Ship Died!");
			explosion = new Explosion(getCenterX(),getCenterY(),400,400);
		}

		public boolean isDead()
		{
			return inventory.isAlive() == false;
		}

		public boolean isIncapacitated()
		{
			return inventory.isIncapacitated();
		}
	}

	public String getConditionString() 
	{
		String[] conditions = {"Dead...","Practically Dead","Barely Moving","Seen Better","Meh","Okay","Decent","Well","Good","Near Perfect","Not A Scratch"};
		int index = (int) ((conditions.length-1) * getInventory().getHealth()/getInventory().getMaxHealth());
		return conditions[index];
	}

	/*public void removeFromFleetAfterDeath()
	{
		fleet.getShips().remove(parent);
	}*/
}
