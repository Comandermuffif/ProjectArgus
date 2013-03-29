package org.projectargus.objects;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.holders.ResourceHolder;
//import org.projectargus.other.Faction;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Planet extends SpaceObject
{
	Image shadow;
	Image baseImg;
	
	ResourceHolder resources = new ResourceHolder();
	
	ArrayList<Ship> dockedShips = new ArrayList<Ship>();
	
	
	Float grav;

	Random random = new Random();

	/**
	 * 
	 * @param solarSystem solarSystem The current Solar System the object is in
	 * @param x The x position within the Solar System
	 * @param y The y position within the Solar System
	 * @param size The diameter of the Planet
	 */
	public Planet(SpaceObject parent,int radiousToParent,int size,int kids){
		super(parent,0,0,size,size);
	
		radiousParent = radiousToParent;
		angleParent = (float) (Math.random() * 360);
		
		grav = (float) (9.8 * Math.abs(random.nextGaussian()));
		
		// Did - TODO Rework so that it factors distance to sun
		//Rework again, got to factor in location
		
		//Sun = 111,111
		//Earth = 1000
		//Jupiter = 11,111
		
		float distToSun = (float) Math.pow(Math.pow(x - getSun().getCenterX(), 2) + Math.pow(y - getSun().getCenterY(), 2),.5);
		
		//angleParentSpeed = (float) (random.nextGaussian() / 2000);//*(radiousParent/50000);
		//angleParentSpeed = (float)((444444 - distToSun)/444444/100);
		angleParentSpeed = (float) (distToSun*Math.pow(parent.grav / distToSun, .5)/444444)/2;
		System.out.println(angleParentSpeed + " <- SUN");
		rotationSpeed = .004f;

		resources.addResource(ResourceHolder.POPULATION_ID, (int) (Math.random()*7000));

		for(int i = 0; i < kids; i += 1){
			//SpaceObject temp = new Planet(this, 10000, Math.abs(size*(3/4)), (int)(Math.abs(random.nextGaussian()*1)));
			SpaceObject temp;
			if(parent.getChildCount() > 1){
				temp = new Asteroid(this,10000,Math.abs(750));
				this.addChild(temp);
			}
			
		}

		try {
			FileInputStream fstream = new FileInputStream("res/planets.txt");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int temp = (int) (Math.random()*16727);
			int temp2 = 0;
			
			do{
				strLine = br.readLine();
				temp2++;
			}while(temp2 <= temp);
			
			System.out.println (strLine);
			name = strLine;
			in.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		try {
			// TODO Images 1 and 2 need to be not interlaced BRO. OKAY BRAH!
			int te = (int) (Math.random()*4 + 1);
			System.out.println("Loading planet: " + te);
			img = new Image("res/img/Planets/Planet" + te + ".png");
			baseImg = img.copy();
			shadow = new Image("res/img/Planets/Planet" + te + "_shadow.png");

		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Todo remove, was just for testing of docked ships attribute.
		/*for(int i = 0;i<3;i++)
		{
			dockedShips.add(new Ship(null, 0,0,200,200));
		}*/
	}


	public void update(GameContainer gc, StateBasedGame sbg, int delta, Rectangle screenRect) throws SlickException 
	{
		super.update(gc, sbg, delta, screenRect);

		//Faction stuff
		String tempFactionName;
		if(getFaction() == null)
		{
			tempFactionName = "None";
		}
		else{
			tempFactionName = getFaction().getName();
		}

		infoBubble.setRow(2, "Faction: " + tempFactionName);
		infoBubble.setRow(3, "Speed: " + angleParentSpeed);
		infoBubble.setRow(4, "Pop: " + resources.getResource(ResourceHolder.POPULATION_ID) + " million");

		//This had better be frame independent
		angleParent += delta*angleParentSpeed;

		x = (int) (parent.getCenterX() + radiousParent*Math.cos(Math.toRadians(angleParent)));
		y = (int) (parent.getCenterY() + radiousParent*Math.sin(Math.toRadians(angleParent)));

		angleRotation += delta*rotationSpeed;

		img.setCenterOfRotation(screen_w/2,screen_h/2);
		img.setRotation((float)(angleRotation));

		float shadowAngle = (float) (Math.toDegrees(Math.atan((getCenterX() - getSun().getCenterX())/(getCenterY() - getSun().getCenterY())) + Math.toRadians(90)));
		
		
		shadow.setCenterOfRotation(screen_w/2,screen_h/2);
		
		if(getCenterY() - getSun().getCenterY() > 0){
			shadow.setRotation((float) (90 - shadowAngle));
		}else{
			shadow.setRotation((float) (270 - shadowAngle));
		}
	}


	//Draw everything here
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{	
		if(isOnScreen)
		{
			g.setColor(new Color(255,0,0));
			g.drawImage(img, screen_x, screen_y,screen_x+screen_w,screen_y+screen_h,0,0,img.getWidth(),img.getHeight());
			g.drawImage(shadow, screen_x, screen_y,screen_x+screen_w,screen_y+screen_h,0,0,shadow.getWidth(),shadow.getHeight());
		}	
		super.render(gc, sbg, g);
	}

	public boolean checkClicked(int mouse_x,int mouse_y)
	{		
		if (super.checkClicked(mouse_x, mouse_y))
		{
			System.out.println("Clicked Planet!");
			data.getSelectedFleet().setTarget(this);
			return true;
		}
		return false;
	}	
	
	public ResourceHolder getResources()
	{
		return resources;
	}
	

	public ArrayList<Ship> getAllFleetlessShips() 
	{
		return dockedShips;
	}
	
	@Override
	/**
	 * Returns the base image instead of the image used in the solarsystem
	 */
	public Image getImage()
	{
		return baseImg;
	}
}
