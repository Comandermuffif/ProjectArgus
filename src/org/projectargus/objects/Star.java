package org.projectargus.objects;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

public class Star extends SpaceObject
{
	
	SolarSystem solarsystem;
	
	/**
	 * 
	 * @param solarSystem solarSystem The current Solar System the object is in
	 * @param x The x position within the Solar System
	 * @param y The y position within the Solar System
	 * @param size The diameter of the Sun
	 */
	//Star sun = new Star(this, diameter/2, diameter/2, 5000, 10);
	public Star(SolarSystem solarSystem,int x,int y, int size, int kids)
	{
		super(x,y,size,size);
		
		grav = 27;
		
		//Earth radius / sun = .009
		
		Random a = new Random();
		//Because adding in Absolute value was soooooo hard.......
			
		int dist = size;
		
		for(int a1 = 1; a1 <= kids; a1++){
			
			System.out.println(dist + " I'm makin kids " + a1 + " : " + kids);
			
			addChild(new Planet(this,dist,(int)(1000),(int)(Math.abs(a.nextGaussian()*2))));
			
			dist = (int) (dist + (solarSystem.diameter/2/kids));
		}
			
			
		//addChild(new Planet(this,(int)(80000+111111*Math.random()),(int)(2*Math.abs(a.nextGaussian()*1000 + 500)),(int)(Math.abs(a.nextGaussian()*2))));
			
		//addChild(new Planet(this,(int)(111111 + a.nextGaussian() * 40000),(int)(size),1));
		
		try {
			FileInputStream fstream = new FileInputStream("res/suns.txt");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			
			try {
				int temp = (int) (Math.random()*98);
				int temp2 = 0;
				do{
					strLine = br.readLine();
					temp2++;
				}while(temp2 <= temp);
				name = strLine;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try {
			img = new Image("res/img/sun1.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//Draw everything here
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{	
		if(isOnScreen)
		{
			g.setColor(new Color(255,255,0));
			g.drawImage(img, screen_x, screen_y,screen_x+screen_w,screen_y+screen_h,0,0,img.getWidth(),img.getHeight());
		}
		super.render(gc, sbg, g);
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta, Rectangle screenRect) throws SlickException {
		super.update(gc, sbg, delta, screenRect);
	}
	
	public boolean checkClicked(int mouse_x,int mouse_y)
	{		
		if (super.checkClicked(mouse_x, mouse_y))
		{
			System.out.println("Clicked Sun!");
			data.getSelectedFleet().setTarget(this);
			return true;
		}
		return false;
	}
}
