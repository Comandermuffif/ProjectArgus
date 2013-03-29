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
import java.util.Random;

public class Asteroid extends SpaceObject
{
	Image shadow;
	ResourceHolder resources = new ResourceHolder();

	Random random = new Random();

	/**
	 * 
	 * @param solarSystem solarSystem The current Solar System the object is in
	 * @param x The x position within the Solar System
	 * @param y The y position within the Solar System
	 * @param size The diameter of the Planet
	 */
	public Asteroid(SpaceObject parent,int radiousToParent,int size){
		super(parent,0,0,size,size);
	
		radiousParent = radiousToParent;
		angleParent = (float) (Math.random() * 360);
		
		grav = (float) (1.8 * Math.abs(random.nextGaussian()));
		
		float distToSun = (float) Math.pow(Math.pow(x - getSun().getCenterX(), 2) + Math.pow(y - getSun().getCenterY(), 2),.5);
		
		//angleParentSpeed = (float) (random.nextGaussian() / 2000);//*(radiousParent/50000);
		//angleParentSpeed = (float)((444444 - distToSun)/444444/100);
		angleParentSpeed = (float) (distToSun*Math.pow(parent.grav / distToSun, .5)/444444);
		System.out.println(angleParentSpeed + " <- SUN");
		rotationSpeed = .004f;

		resources.addResource(ResourceHolder.POPULATION_ID, (int)(Math.random()*100));

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
			int te = 1;
			img = new Image("res/img/Moons/Moon" + te + ".png");
			shadow = new Image("res/img/Moons/Moon" + te + "_shadow.png");
			
			/*int te = (int) (Math.random()*4 + 1);
			System.out.println("Loading Asteroid: " + te);
			img = new Image("res/img/Planets/Planet" + te + ".png");
			shadow = new Image("res/img/Planets/Planet" + te + "_shadow.png");*/

		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

		/* TODO Planet updating
		 * 
		 * */

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
			System.out.println("Clicked Asteroid!");
			data.getSelectedFleet().setTarget(this);
			return true;
		}
		return false;
	}	
}
