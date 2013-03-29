package org.projectargus.objects;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.other.Faction;

public class Fleet extends SpaceObject
{	
	private Faction faction;

	ArrayList<Ship> ships = new ArrayList<Ship>();

	float speed = 3;
	double angle = 0;

	double orbitAngle =0;
	double orbitRadius = 1000;

	SpaceObject targetObject;
	Point targetPoint = new Point(50000,50000);		
	Point targetOrbitPoint = new Point(50000,50000);

	private State state = State.NONE;
	private Target target = Target.NONE;

	public enum State
	{
		SET_TARGET,TARGET_FLIGHT,POINT_FLIGHT,ORBITING,START_ORBITING,FIGHT,NONE
	}

	public enum Target
	{
		PLANET,FLEET,SUN,POINT,NONE,SPACEOBJECT
	}

	public FleetFight fleetFight = new FleetFight(this);

	//TODO Delete these, just for graphical display of triangle
	private Rectangle targetPointScreen = new Rectangle(0,0,0,0);
	private Rectangle screenPoint = new Rectangle(0,0,0,0);
	private Rectangle screenTargetPoint = new Rectangle(0,0,0,0);


	/**
	 *  @param solarSystem The current Solar System the object is in
		@param x The x position within the Solar System
		@param y The y position within the Solar System
		@param w The width of the object
		@param h The height of the object
	 */
	public Fleet(SolarSystem solarSystem, int x, int y, int w, int h) 
	{
		this(solarSystem,null,x,y,w,h);
	}

	/**
	 *  @param solarSystem The current Solar System the object is in
	 *  @param f The faction to which the fleet belongs to
		@param x The x position within the Solar System
		@param y The y position within the Solar System
		@param w The width of the object
		@param h The height of the object
	 */
	public Fleet(SolarSystem solarSystem,Faction f, int x, int y, int w, int h) 
	{
		super( x, y, w, h);
  
		name = "Fleet " + (int)(Math.random()*100);
		
		faction = f;
		
		Star temp = solarSystem.getSun();
		
		setTarget(temp);

		System.out.println(temp.getCenterX() + " : " + temp.getCenterY());

		for(int i = 0;i<3;i++)
		{
			if(i == 0)
			{
				ships.add(new Ship(solarSystem, 50, 0, 100, 100));
			}
			else if(i % 2 == 1)
			{
				ships.add(new Ship(solarSystem, -100*i, -100*i, 100, 100));
			}
			else{
				ships.add(new Ship(solarSystem, -100*(i-1), 100*(i-1), 100, 100));
			}		
		}

		for(int i =0;i<ships.size();i++)
		{
			ships.get(i).setFleet(this);
		}		
	}



	//Draw everything here
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{	
		//Render all of the ships
		for(int i =0;i<ships.size();i++)
		{
			ships.get(i).render(gc, sbg, g);
		}

		//Draw rectangle around fleet
		if(isOnScreen)
		{
			g.setColor(new Color(255,255,0));	
			g.drawRect(screen_x, screen_y, screen_w,screen_h);
			// ADDED BY VINCENT
			g.fillOval(screen_x, screen_y, 10,10);
		}
		

		super.render(gc, sbg, g);
	}

	//Do all calculations here
	public void update(GameContainer gc, StateBasedGame sbg, int delta, Rectangle screenRect) throws SlickException 
	{				
		//State gameplay, different types of moving
		switch(state)
		{
		//Setting the orbit point based off of the destination point
		case SET_TARGET:

			setOrbitTargetPoint(targetPoint);
			state = State.POINT_FLIGHT;
			break;


			//Setting the target point based off of the target object
		case TARGET_FLIGHT:

			if(targetObject != null)
			{
				targetPoint = new Point((int)targetObject.getCenterX(),(int)targetObject.getCenterY());
			}

			state = State.SET_TARGET;
			break;

			//Moving towards the point
		case POINT_FLIGHT:
			
			Point tempOrbitPoint = targetOrbitPoint;

			double tempDistAwayX = tempOrbitPoint.getX() - getX();
			double tempDistAwayY = tempOrbitPoint.getY() - getY();
			
			angle = Math.atan2(tempDistAwayY, tempDistAwayX);
			

			float tempMoveDistX = (float) ((float)delta * getSpeed() * Math.cos(angle));
			float tempMoveDistY = (float) ((float)delta * getSpeed() * Math.sin(angle)); 

			//Once the point is reached
			if(Math.abs(tempDistAwayX) <= Math.abs(tempMoveDistX)
					&& Math.abs(tempDistAwayY) <= Math.abs(tempMoveDistY))
			{
				x = (int) tempOrbitPoint.getX();
				y = (int) tempOrbitPoint.getY();

				state = State.START_ORBITING;
				angle = Math.atan2(getY() - targetPoint.getY(), getX() - targetPoint.getX()) - Math.PI/2;
				System.out.println("You Have Arrived!");
			}
			else{
				x += tempMoveDistX;
				y += tempMoveDistY;
				
				// ADDED BY VINCENT
				//Commented out by vincent
				//state = State.TARGET_FLIGHT;
			}
			break;

			//Preparing for orbiting
		case START_ORBITING:
			orbitAngle = Math.atan2(getY() - targetPoint.getY(), getX() - targetPoint.getX());
			angle = orbitAngle - Math.PI/2;
			state = State.ORBITING;

			//While orbiting a point
		case ORBITING:

			orbitAngle -= getOrbitAngleSpeed() * (float)delta;

			angle = (orbitAngle - Math.PI/2);


			if(targetObject == null){
				x = (int) (targetPoint.getX() + getOrbitRadius() * Math.cos(orbitAngle));
				y = (int) (targetPoint.getY() + getOrbitRadius() * Math.sin(orbitAngle));
			}else{
				x = (int) (targetObject.getCenterX() + getOrbitRadius() * Math.cos(orbitAngle));
				y = (int) (targetObject.getCenterY() + getOrbitRadius() * Math.sin(orbitAngle));
			}


			break;
		case FIGHT:

			break;
		}	

		//Sets the screen position of the fleet
		setScreenPosition(gc,screenRect);

		//Calls update for every ship in the fleet
		for(int i =0;i<ships.size();i++)
		{
			ships.get(i).update(gc, sbg, delta, screenRect);
		}

		//If cursor is over fleet, show the info bubble
		if(showInfo)
		{
			infoBubble.setPos((int)screen_x,(int)screen_y,(int)screen_w,(int)screen_h);

			infoBubble.setRow(0, getName());
			infoBubble.setRow(1, "("+ getX() + "," + getY() + ")");	
			//Faction stuff
			String tempFactionName;
			if(faction == null)
			{
				tempFactionName = "None";
			}
			else{
				tempFactionName = faction.getName();
			}

			infoBubble.setRow(2, "Faction: " + tempFactionName);
		}

	}	


	/**
	 * Sets the screen position and size of the fleet 
	 * @param gc The GameContainer passed through the update method
	 * @param screenRect The visible coordinates
	 */
	public void setScreenPosition(GameContainer gc, Rectangle screenRect)
	{
		viewWidth = ((float)screenRect.getWidth());
		viewHeight = ((float)screenRect.getHeight());

		screen_w = getWidth()/viewWidth * gc.getWidth();
		screen_h = getHeight()/viewHeight * gc.getHeight();

		if(screenRect.contains(getX(),getY()) || screenRect.contains(getX(),getY() + getHeight())
				|| screenRect.contains(getX()+getWidth(),getY())
				|| screenRect.contains(getX()+getWidth(),getY() + getHeight()))
		{
			isOnScreen = true;
			screen_x = gc.getWidth() * ((getFleetLeft() - screenRect.getX()) / screenRect.getWidth());
			screen_y = gc.getHeight() * ((getFleetTop() - screenRect.getY()) / screenRect.getHeight());
		}else
		{
			isOnScreen = false;
			screen_x = -1;
			screen_y = -1;
		}		
	}


	/**
	 * Adds a ship to the fleet
	 * @param s The Ship instance you want to add to this fleet
	 */
	public void addShip(Ship s)
	{
		s.setFleet(this);
		ships.add(s);
	}

	/**
	 * 
	 * @param s The Ship instance you want to remove from this fleet
	 */
	public void removeShip(Ship s)
	{
		s.removeFleet();
		ships.remove(s);
	}

	/**
	 * 
	 * @param p The target point you want to orbit around
	 * @return The point you want to fly to so you can orbit
	 */
	public Point getOrbitPoint(Point p)
	{
		Point tempPoint = null;
		double angleToPoint;
		angleToPoint = Math.atan2(getY() - p.getY(),getX() - p.getX());
		angleToPoint -= Math.PI/2;

		int tempX = (int) (p.getX() + Math.cos(angleToPoint) * getOrbitRadius());
		int tempY = (int) (p.getY() + Math.sin(angleToPoint) * getOrbitRadius());

		tempPoint = new Point(tempX,tempY);

		return tempPoint;
	}

	/**
	 * Set the orbiting point
	 * @param p The target point
	 */
	public void setOrbitTargetPoint(Point p)
	{
		targetOrbitPoint = getOrbitPoint(p);
	}	

	/**
	 * Set the object to orbit around
	 * @param sun The sun you want to orbit
	 */
	public void setTarget(Star sun)
	{
		target = Target.SUN;
		targetObject = sun;
		state = State.TARGET_FLIGHT;
	}

	/**
	 * Set the object to orbit around
	 * @param planet The planet you want to orbit
	 */
	public void setTarget(Planet planet)
	{
		target = Target.PLANET;
		targetObject = planet;
		state = State.TARGET_FLIGHT;
	}
	
	public void setTarget(SpaceObject obj){
		target = Target.SPACEOBJECT;
		targetObject = obj;
		state = State.TARGET_FLIGHT;
	}

	/**
	 * Set the object to orbit around
	 * @param fleet The fleet you want to orbit
	 */
	public void setTarget(Fleet fleet)
	{
		target = Target.FLEET;
		targetObject = fleet;
		state = State.TARGET_FLIGHT;
	}

	/**
	 * Set the point to orbit around
	 * @param point The point you want to orbit
	 */
	public void setTarget(Point point)
	{
		target = Target.POINT;
		targetObject = null;
		targetPoint = point;
		state = State.TARGET_FLIGHT;
	}

	public boolean checkClicked(int mouse_x,int mouse_y)
	{		
		if (super.checkClicked(mouse_x, mouse_y))
		{
			System.out.println("Clicked Fleet!");
			return true;
		}
		return false;
	}	


	//Setter Methods

	public void setFaction(Faction f)
	{
		faction = f;
	}

	//Getter Methods

	public Faction getFaction()
	{
		return faction;
	}

	//TODO Fix the orbiting speed problem
	/**
	 * Gets the angular speed to orbit with so that the fleets speed is the same as linear
	 * @return
	 */
	public double getOrbitAngleSpeed()
	{
		double tempSpeed = 0;

		tempSpeed = getSpeed()/(Math.PI * getOrbitRadius());

		return tempSpeed;
	}

	/**
	 * Get the orbit radius, changes from orbiting around an object or point
	 * @return The orbit radius for the current situation
	 */
	public int getOrbitRadius()
	{
		if(targetObject == null)
		{
			return (int) orbitRadius;
		}else{
			return (int) (targetObject.getWidth()/2 + orbitRadius);
		}
	}

	public int getFleetCenterX()
	{
		return (int) (getX() - getWidth()*Math.cos(angle)/2);
	}

	public int getFleetCenterY()
	{
		return (int) (getY() - getHeight()*Math.sin(angle)/2);
	}

	public float getFleetLeft()
	{
		return getFleetCenterX()-getWidth()/2;
	}

	public float getFleetTop()
	{
		return getFleetCenterY()-getHeight()/2;
	}

	public float getSpeed()
	{
		return speed;
	}

	public double getAngle()
	{
		return angle;
	}

	//TODO change to dynamic
	public float getWidth()
	{
		return 1500;
	}

	//TODO change to dynamic
	public float getHeight()
	{
		return 1500;
	}

	/**
	 * If the fleet is orbiting around an object, it will return that object
	 * @return The SpaceObject it is orbiting or null if it isn't
	 */
	public SpaceObject getDockedObject()
	{
		if(state == State.ORBITING)
		{
			return targetObject;
		}else{
			return null;
		}
	}
	
	
	
	
	
	
	
	
	//FIGHT Methods
	public void fightRender(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{	
		//Render all of the ships
		for(int i =0;i<ships.size();i++)
		{
			ships.get(i).fightRender(gc, sbg, g);
		}
	}

	//Do all calculations here
	public void fightUpdate(GameContainer gc, StateBasedGame sbg, int delta, Rectangle screenRect) throws SlickException 
	{
		//Calls update for every ship in the fleet
		for(int i =0;i<ships.size();i++)
		{
			ships.get(i).fightUpdate(gc, sbg, delta,screenRect);
		}
	}	

	public class FleetFight
	{
		Fleet parent;

		public FleetFight(Fleet fleet)
		{
			parent = fleet;
		}

		public Ship checkClicked(int mouse_x,int mouse_y)
		{
			for(int i=0;i<parent.ships.size();i++)
			{
				if(parent.ships.get(i).shipFight.checkClicked(mouse_x, mouse_y) == true)
				{
					return parent.ships.get(i);
				}
			}
			return null;
		}

		public void setFightPos()
		{
			int pos = 0;
			int mult = 500;

			for(int i = 0;i<ships.size();i++)
			{
				if(data.playerFightFleet.equals(parent))
				{
					ships.get(i).shipFight.x = 0;
				}else{
					ships.get(i).shipFight.x = 1500;
				}

				ships.get(i).shipFight.y = pos + i*mult;
			}
		}

		public boolean isDone() 
		{
			for(int i = 0;i<parent.getShips().size();i++)
			{
				if(parent.getShips().get(i).shipFight.isDoneFiring() == false)
				{
					return false;
				}
			}
			return true;
		}

	}
	
	public ArrayList<Ship> getShips() 
	{
		return ships;
	}
	
	public ArrayList<Ship> getLivingShips() 
	{
		ArrayList<Ship> tempList = new ArrayList<Ship>();
		for(int i = 0;i<ships.size();i++)
		{
			if(ships.get(i).shipFight.isDead() == false)
			{
				tempList.add(ships.get(i));
			}
		}
		
		return tempList;
	}
	
	public ArrayList<Ship> getCapableShips() 
	{
		ArrayList<Ship> tempList = new ArrayList<Ship>();
		for(int i = 0;i<ships.size();i++)
		{
			if(ships.get(i).shipFight.isIncapacitated() == false)
			{
				tempList.add(ships.get(i));
			}
		}
		
		return tempList;
	}
}
