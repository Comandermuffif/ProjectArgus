package org.projectargus.gui;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.data.GameData;
import org.projectargus.objects.Fleet;
import org.projectargus.objects.SpaceObject;
import org.projectargus.objects.Star;

public class SolarSystemMiniMap
{
	GameData data = GameData.getInstance();

	int x;
	int y;
	int w;
	int h;

	Rectangle screen_box = new Rectangle(x,y,w,h);
	ArrayList<Point4d> planetDots = new ArrayList<Point4d>();
	ArrayList<Point4d> fleetDots = new ArrayList<Point4d>();
	Point4d sunDot = new Point4d(0,0,0,0);
	Point4d centerOfView = new Point4d(0,0,0,0);
	
	public SolarSystemMiniMap(int x,int y,int w,int h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	//Draw everything here
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		//Draw Map Background
		g.setColor(new Color(100,100,100,150));
		g.fillRect(x, y, w, h);
		
		//Draw Viewfinder
		g.setColor(new Color(0,200,200,50));
		g.fill(screen_box);
		
		//Draw All Planets
		g.setColor(new Color(255,0,0));
		
		for(int i = 0;i<planetDots.size();i++)
		{
			Point4d p = planetDots.get(i);
			g.fillOval(p.x, p.y, p.w, p.h);
		}
		
		//Draw all fleets
		g.setColor(new Color(255,0,255));
		
		for(int i = 0;i<fleetDots.size();i++)
		{
			Point4d f = fleetDots.get(i);
			g.fillOval(f.x, f.y, f.w, f.h);
		}
		
		//Draw Sun
		g.setColor(new Color(255,255,0));
		g.fillOval(sunDot.x, sunDot.y, sunDot.w, sunDot.h);
		
		g.setColor(new Color(255,255,255));
		g.fillOval(centerOfView.x-2, centerOfView.y-2, 4,4);
		
		
	}

	//Do all calculations here
	public void update(GameContainer gc, StateBasedGame sbg, int delta, Rectangle screenRect) throws SlickException 
	{
		float tempSSDiameter = data.getCurrentSolarSystem().getDiameter();
		
		//Rectangle Viewfinder Maker
		float tempX = (float)x + (float)w*(screenRect.getX()/tempSSDiameter);
		float tempY = (float)y + (float)h*(screenRect.getY()/tempSSDiameter);
		float tempW = (float)w*(screenRect.getWidth()/tempSSDiameter);
		float tempH = (float)h*(screenRect.getHeight()/tempSSDiameter);
		
		if(tempX < x)
		{
			tempW -= x - tempX;
			tempX = x;
		}
		if(tempY < y)
		{
			tempH -= y - tempY;
			tempY = y;
		}
		if(tempX + tempW > x+w)
		{
			tempW = x+w - tempX;
		}
		if(tempY + tempH > y+h)
		{
			tempH = y+h - tempY;
		}
		
		screen_box = new Rectangle(tempX,tempY,tempW,tempH);	
		
		
		//Planet Dot Maker
		planetDots.clear();
		fleetDots.clear();
		
		for(SpaceObject p:data.getCurrentSolarSystem().getChildren())
		{
			float tX = (float)x + (float)w*(p.getX()/tempSSDiameter);
			float tY = (float)y + (float)h*(p.getY()/tempSSDiameter);
			float tW = (float)w*(p.getWidth()/tempSSDiameter);
			float tH = (float)h*(p.getHeight()/tempSSDiameter);
			
			planetDots.add(new Point4d(tX,tY,tW,tH));
		}		
		
		//Sun dot Maker
		Star s = data.getCurrentSolarSystem().getSun();
		
		float tX = (float)x + (float)w*(s.getX()/tempSSDiameter);
		float tY = (float)y + (float)h*(s.getY()/tempSSDiameter);
		float tW = (float)w*(s.getWidth()/tempSSDiameter);
		float tH = (float)h*(s.getHeight()/tempSSDiameter);
		
		sunDot = new Point4d(tX,tY,tW,tH);
		
		//Fleet dot Maker		
		for(Fleet f:data.getAllFleetsInCurrentSolarSystem())
		{
			tX = (float)x + (float)w*(f.getX()/tempSSDiameter);
			tY = (float)y + (float)h*(f.getY()/tempSSDiameter);
			tW = (float)w*(f.getWidth()/tempSSDiameter);
			tH = (float)h*(f.getHeight()/tempSSDiameter);
			
			fleetDots.add(new Point4d(tX,tY,tW,tH));
		}
		
		
		tX = (float)x + (float)w*(screenRect.getCenterX()/tempSSDiameter);
		tY = (float)y + (float)h*(screenRect.getCenterY()/tempSSDiameter);
		tW = 10;
		tH = 10;		
		
		centerOfView = new Point4d(tX,tY,tW,tH);
	}
	
	
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public int getWidth()
	{
		return w;
	}
	public int getHeight()
	{
		return h;
	}
	
	
	public class Point4d
	{
		float x;
		float y;
		float w;
		float h;
		
		public Point4d(float x,float y, float w,float h)
		{
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
	}


}
