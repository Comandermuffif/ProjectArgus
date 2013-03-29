package org.projectargus.states;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.Main;
import org.projectargus.data.GameData;
import org.projectargus.data.GameSettings;
import org.projectargus.factory.FactionFactory;
import org.projectargus.gui.SolarSystemMiniMap;
import org.projectargus.objects.Fleet;
import org.projectargus.objects.SolarSystem;
import org.projectargus.objects.SpaceObject;

public class SolarSystemMapState extends BasicGameState 
{
	//Will be assigned actual value on construct from main class
	int stateID = -1;

	//A singleton class which stores all data across all classes
	GameData data = GameData.getInstance();
	GameSettings settings = GameSettings.getInstance();

	SolarSystemMiniMap map = new SolarSystemMiniMap(settings.getScreenX() - 160 ,10,150,150);

	double zoom = 100;
	float maxZoom = 200;

	float startMouseX = -1;
	float startMouseY = -1;

	int x;
	int y;


	SpaceObject selected = null;

	public SolarSystemMapState( int stateID ) 
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
		System.out.println("Entering Solar System");
	}

	public void leave(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		System.out.println("Leaving Solar System");
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		//Todo Move all of this to a different state/method
		
		System.out.println("Initializing Solar System");
		SolarSystem ss = new SolarSystem();
		data.addSolarSystem(ss);
		data.setCurrentSolarSystem(ss);

		int tempDiameter = data.getCurrentSolarSystem().getDiameter();

		for(int i = 0;i<4;i++)
		{
			data.addFleet(new Fleet(data.getCurrentSolarSystem(), (int)(Math.random() * (tempDiameter-10000) + 10000), (int)(Math.random() * (tempDiameter-10000) + 10000), 300, 300));
		}
		
		x = data.getCurrentSolarSystem().getDiameter()/2;
		y = data.getCurrentSolarSystem().getDiameter()/2;

		(new FactionFactory()).setFactions();

		data.setFightFleets(data.getAllFleetsInCurrentSolarSystem().get(0),data.getAllFleetsInCurrentSolarSystem().get(1));
	}

	//Draw everything here
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		data.getCurrentSolarSystem().render(gc, sbg, g);

		for(int i = 0;i<data.getAllFleetsInCurrentSolarSystem().size();i++)
		{
			Fleet f = data.getAllFleetsInCurrentSolarSystem().get(i);
			f.render(gc, sbg, g);
		}

		map.render(gc, sbg, g);
	}

	//Do all calculations here
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException 
	{	
		//Set camera position based on selected objects position
		if(selected != null)
		{
			selected.showInfo();//TODO it does nothing.... damn you mouse check
			
			x = (int) selected.getCenterX();
			y = (int) selected.getCenterY();	
		}
		
		//Get The current SolarSystems size. May need to be changed to width and height
		int ssDiameter = data.getCurrentSolarSystem().getDiameter();

		//Find the rectangle of solarSystem to display 
		int tempX = x - (int)((float)ssDiameter/2 * getZoomRatio());
		int tempY = y - (int)((float)ssDiameter/2 * getZoomRatio()) * gc.getHeight()/gc.getWidth();
		int tempW = (int)((float)ssDiameter * getZoomRatio());
		int tempH = ((int)((float)ssDiameter * getZoomRatio())) * gc.getHeight()/gc.getWidth(); //Ratio creates rectangle rather than square

		//Create the SolarSystem rectabgle that the user can see		
		Rectangle screenRect = new Rectangle(tempX,tempY,tempW,tempH);

		//Update all of the SolarSystem Components
		data.getCurrentSolarSystem().update(gc, sbg, delta, screenRect);

		//Update all of the miniMaps componets
		map.update(gc, sbg, delta, screenRect);

		//Update all of the Fleets Components
		for(int i = 0;i<data.getAllFleetsInCurrentSolarSystem().size();i++)
		{
			Fleet f = data.getAllFleetsInCurrentSolarSystem().get(i);
			f.update(gc, sbg, delta, screenRect);
		}

		//Start of Input		

		//Key and mouse input control
		Input input = gc.getInput();
		int mouse_x = input.getMouseX();
		int mouse_y = input.getMouseY();

		//Controls Mouse clicking

		//Checks if click is on minimap
		if(input.isMouseButtonDown(0))
		{
			if(mouse_x >= map.getX() && mouse_x <= map.getX()+map.getWidth()
					&& mouse_y >= map.getY() && mouse_y <= map.getY()+map.getHeight())
			{
				x = (int)((float)(mouse_x - map.getX())/(float)map.getWidth() * data.getCurrentSolarSystem().getDiameter());
				y = (int)((float)(mouse_y - map.getY())/(float)map.getHeight() * data.getCurrentSolarSystem().getDiameter());				
			}
		}

		if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
		{
			if(startMouseX != -1)
			{
				float diffX = startMouseX - mouse_x;
				float diffY = startMouseY - mouse_y;

				x += diffX/(float)gc.getWidth() * data.getCurrentSolarSystem().getDiameter() * getZoomRatio();
				y += (diffY/(float)gc.getHeight() * data.getCurrentSolarSystem().getDiameter() * getZoomRatio())*gc.getHeight()/gc.getWidth();

				if(x > data.getCurrentSolarSystem().getDiameter())
				{
					x = data.getCurrentSolarSystem().getDiameter();
				}
				if(y > data.getCurrentSolarSystem().getDiameter())
				{
					y = data.getCurrentSolarSystem().getDiameter();
				}
				if(x < 0)
				{
					x = 0;
				}
				if(y < 0)
				{
					y = 0;
				}

			}

			startMouseX = mouse_x;
			startMouseY = mouse_y;		
		}
		else
		{
			startMouseX = -1;
			startMouseY = -1;
		}

		//Set selected/ camera locked object
		if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
		{
			SpaceObject temp = checkClickSelected(mouse_x,mouse_y);
			data.setSelectedByType(temp);
			selected = temp;
		}
		
		if(input.isMousePressed(Input.MOUSE_RIGHT_BUTTON))
		{			
			if(checkClicked(mouse_x,mouse_y))
			{

			}else{

				int tempXpos = (int) (screenRect.getX() + screenRect.getWidth() * mouse_x/gc.getWidth());
				int tempYpos = (int) (screenRect.getY() + screenRect.getHeight() * mouse_y/gc.getHeight()); 

				data.getSelectedFleet().setTarget(new Point(tempXpos,tempYpos));				
			}
		}	


		if(input.isMousePressed(Input.MOUSE_RIGHT_BUTTON) == false)
		{
			SpaceObject temp = checkClickSelected(mouse_x,mouse_y);

			if(temp != null)
			{
				temp.showInfo();
			}
		}

		
		//Temporary - Changes to fight state
		if(input.isKeyPressed(Input.KEY_SPACE))
		{
			sbg.enterState(Main.PLANET_MENU_STATE);
		}		

		//When zoom = 0, it is fullscreen
		//When zoom = maxZoom, the screen is practically invisible
		//As zoom is greater, x needs to move slower 

		float moveDist = 10/(float)((float)10 / (Math.pow((maxZoom-zoom) + 1,1.4)));

		//Moves the "camera" around the map
		if(input.isKeyDown(Input.KEY_RIGHT))
		{
			if(x + moveDist < ssDiameter)
			{
				x += moveDist;
			}
		}
		if(input.isKeyDown(Input.KEY_LEFT))
		{
			if(x - moveDist > 0)
			{
				x -= moveDist;
			}
		}
		if(input.isKeyDown(Input.KEY_UP))
		{
			if(y - moveDist > 0)
			{
				y -= moveDist;
			}
		}
		if(input.isKeyDown(Input.KEY_DOWN))
		{
			if(y + moveDist < ssDiameter)
			{
				y += moveDist;
			}
		}
		if(input.isKeyDown(Input.KEY_G)){
		}
	}

	//Controls zoom in and out
	public void mouseWheelMoved(int change) 
	{				
		zoom += (float)(change)/(float)30;

		if(zoom > getMinimumZoom())
		{
			zoom = getMinimumZoom();
		}
		if(zoom < getMaximumZoom())
		{
			zoom = getMaximumZoom();
		}
	}

	public boolean checkClicked(int mouse_x,int mouse_y)
	{
		ArrayList<Fleet> fleets = data.getAllFleetsInCurrentSolarSystem();

		for(int i = 0;i<fleets.size();i++)
		{
			if(fleets.get(i).checkClicked(mouse_x, mouse_y))
			{
				return true;
			}
		}	

		if(data.getCurrentSolarSystem().checkClicked(mouse_x,mouse_y))
		{
			return true;
		}
		return false;
	}

	public SpaceObject checkClickSelected(int mouse_x,int mouse_y)
	{
		ArrayList<Fleet> fleets = data.getAllFleetsInCurrentSolarSystem();

		for(int i = 0;i<fleets.size();i++)
		{
			if(fleets.get(i).checkMouse(mouse_x, mouse_y))
			{
				return fleets.get(i);
			}
		}	

		if(data.getCurrentSolarSystem().checkMouse(mouse_x,mouse_y) != null)
		{
			return data.getCurrentSolarSystem().checkMouse(mouse_x,mouse_y);
		}
		return null;
	}

	public float getZoomRatio()
	{
		return (1 - (float)zoom/maxZoom);
	}

	public double getMinimumZoom()
	{
		return ((float)1-((float)settings.getScreenX()/(float)data.getCurrentSolarSystem().getDiameter()))*maxZoom;
	}
	
	public double getMaximumZoom()
	{
		return -100;
	}


	/**
	 * Gets the screen position and dimention of a given rectangle in space
	 * @param dims the x,y position and the width and height
	 * @param screenRect The part of the solarsystem currently visible
	 * @param gc the GameContainer passed from the update method
	 * @return the screen position and dimention or null if off screen
	 */
	public static Rectangle getScreenDims(GameContainer gc,Rectangle dims,Rectangle screenRect)
	{
		//System.out.println(dims.getWidth());
		float viewWidth = ((float)screenRect.getWidth());
		float viewHeight = ((float)screenRect.getHeight());

		float screen_w = dims.getWidth()/viewWidth * gc.getWidth();
		float screen_h = dims.getHeight()/viewHeight * gc.getHeight();
		
		float screen_x = (gc.getWidth() * ((dims.getX() - screenRect.getX()) / screenRect.getWidth()));
		float screen_y = (gc.getHeight() * ((dims.getY() - screenRect.getY()) / screenRect.getHeight()));
		
		
		return new Rectangle(screen_x,screen_y,screen_w,screen_h);
	}
}