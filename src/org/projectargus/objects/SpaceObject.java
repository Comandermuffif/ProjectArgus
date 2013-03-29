package org.projectargus.objects;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.data.GameData;
import org.projectargus.gui.InfoBubble;
import org.projectargus.other.Faction;
import org.projectargus.states.SolarSystemMapState;

public class SpaceObject 
{
	private Faction faction;
	
	float x; //X position
	float y; //Y position
	float w; //Width
	float h; //Height
	
	SpaceObject parent;
	
	ArrayList<SpaceObject> children = new ArrayList<SpaceObject>(0);
	
	//String name;
	float radiousParent; //Radius to parent
	float angleParent; //Angle to parent (inDegrees)
	float angleParentSpeed; //The speed it rotates around the parent
	
	float rotationSpeed; //Change in sprite angle
	float angleRotation; //The sprite angle
	
	float grav;
	
	//Info bubble
	boolean showInfo;
	InfoBubble infoBubble = new InfoBubble();
	
	//Screen stuff
	boolean isOnScreen;
	float screen_x;
	float screen_y;

	float screen_w;
	float screen_h;

	float viewWidth;
	float viewHeight;

	GameData data = GameData.getInstance();

	//Important attributes of the object
	String name;
	Image img;
	Image shadow;
	
	/**
	 * A generic space object - anything that moves around in a solarsystem
	 * 
	 * @param solarSystem The current Solar System the object is in
	 * @param x The x position within the Solar System
	 * @param y The y position within the Solar System
	 * @param w The width of the object
	 * @param h The height of the object
	 * @param ID The unique identifier
	 */
	public SpaceObject(SpaceObject parent,int x,int y,int w,int h)
	{
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		showInfo = false;
		name = "Temp Name";
	}
	
	public SpaceObject(int x,int y,int w,int h)
	{
		//We'll try suns having the solar system as a parent, as a test
		//Just kidding, not possible......
		
		//And fleets and ships will do this too, ah well
		this.parent = this;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		showInfo = false;
		name = "Temp Name";
	}
	
	/**
	 * @return 
	 * @return The parent object
	 */
	public SpaceObject getParent(){
		return parent;
	}
	
	/**
	 * @return The name of the object
	 */
	
	public String getName()
	{
		return name;
	}
	
	/**
	 * @return the x position of the object
	 */
	public float getX()
	{
		return x - getWidth()/2;
	}

	/**
	 * @return the middle x position of the object
	 */
	public float getCenterX()
	{
		return x;
	}

	/**
	 * @return the y position of the object
	 */
	public float getY()
	{
		return y - getHeight()/2;
	}

	/**
	 * @return the middle of the y position of the object
	 */
	public float getCenterY()
	{
		return y;
	}

	/**
	 * @return the width of the object
	 */
	public float getWidth()
	{
		return w;
	}

	/**
	 * @return the height of the object
	 */
	public float getHeight()
	{
		return h;
	}

	//Draw everything here
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		for(SpaceObject a : children){
			a.render(gc, sbg, g);
		}
		//Show the info bubble if hovered over
		if(showInfo)
		{
			infoBubble.render(gc, sbg, g);
		}
		
		g.setColor(new Color(255,0,0));

		infoBubble.setRow(5,"Temp Size: " + screen_w);
		infoBubble.setRow(6,"x: " + screen_x + " y: " + screen_y);
		
		if(isOnScreen && parent.isOnScreen){
			//I make better
			g.drawLine((screen_x  + screen_w/2), (screen_y  + screen_h/2), (parent.screen_x  + parent.screen_w/2), (parent.screen_y  + parent.screen_h/2));
		}
	}

	//Do all calculations here
	public void update(GameContainer gc, StateBasedGame sbg, int delta, Rectangle screenRect) throws SlickException 
	{
		for(SpaceObject a : children){
			a.update(gc, sbg, delta, screenRect);
		}
		//Vincent fixed :)
		Rectangle dims = SolarSystemMapState.getScreenDims(gc, new Rectangle(getX(),getY(),getWidth(),getHeight()),screenRect);
		
		screen_x = dims.getX();
		screen_y = dims.getY();
		screen_w = dims.getWidth();
		screen_h = dims.getHeight();
		
		//New on screen check : Take it Mullens. Love you Vinny <3 - TEHE
		if(screen_x + screen_w < 0 || screen_x > gc.getWidth() || screen_y + screen_h < 0 || screen_y  > gc.getHeight()){
			isOnScreen = false;
		}
		else{
			isOnScreen = true;
		}
		
		//Setting up the info bubble
		if(showInfo)
		{
			infoBubble.setPos((int)screen_x,(int)screen_y,(int)screen_w,(int)screen_h);
			
			infoBubble.setRow(0, getName());
			infoBubble.setRow(1, "("+ getX() + "," + getY() + ")");	
			
		}
	}
	
	/**
	 * Checks to see if the mouse clicked within the object
	 * @param mouse_x the x position of the mouse
	 * @param mouse_y the y position of the mouse
	 * @return <b>true</b> if the object was clicked
	 */
	public boolean checkClicked(int mouse_x,int mouse_y)
	{		
		if (isOnScreen && mouse_x <= screen_x + screen_w && mouse_x >= screen_x
				&& mouse_y <= screen_y + screen_h && mouse_y >= screen_y)
		{
			return true;
		}
		return false;
	}

	public boolean checkMouse2(int mouse_x,int mouse_y){ //Fuck you mullens
		if (isOnScreen && mouse_x <= screen_x + screen_w && mouse_x >= screen_x
				&& mouse_y <= screen_y + screen_h && mouse_y >= screen_y){
			return true;
		}
		showInfo = false;
		return false;
	}
	
	public boolean checkMouse(int mouse_x,int mouse_y){ //Rewrote this so it actually works; CIRCLES
		float dist = (float) Math.pow(Math.pow((screen_x  + screen_w/2) - mouse_x, 2) + Math.pow((screen_y+ screen_h/2) - mouse_y, 2), .5 );
		if (isOnScreen && dist <= screen_w/2){
			return true;
		}
		showInfo = false;
		return false;
	}
	
	public void showInfo()
	{
		showInfo = true;
	}
	
	public void addChild(SpaceObject newChild){
		children.add(newChild);
	}
	
	public int getChildCount(){
		return children.size();
	}
	
	public SpaceObject getChild(int index){
		return children.get(index);
	}
	public ArrayList<SpaceObject> getChildren(){
		return children;
	}
	public ArrayList<SpaceObject> getAllChildren(){
		//Returns all of the objects children and itself
		ArrayList<SpaceObject> temp = new ArrayList<SpaceObject>(0);
		temp.add(this);
		for(SpaceObject a : children){
			temp.addAll(a.getAllChildren());
		}
		return temp;
	}
	public void setFaction(Faction f)
	{
		faction = f;
	}
	
	public Faction getFaction()
	{
		return faction;
	}
	
	public SpaceObject getSun(){
		if(this == parent){
			return this;
		}else{
			return parent.getSun();
		}
	}
	
	public Image getImage()
	{
		return img;
	}
}
