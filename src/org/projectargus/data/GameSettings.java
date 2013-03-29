package org.projectargus.data;

import java.awt.Font;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;

@SuppressWarnings("deprecation")
public class GameSettings 
{	
	
	private int screen_x;
	private int screen_y;
	
	private static GameSettings instance = null;
	
	Font baseFont1 = null;
	TrueTypeFont baseFont = null;
	
	private StateBasedGame sbg = null;
	private GameContainer gc = null;
	
	public static GameSettings getInstance()
	{
		if(instance == null)
			instance = new GameSettings();

		return instance;
	}
	
	public int getScreenX(){
		return screen_x;
	}
	public int getScreenY(){
		return screen_y;
	}
	public void setScreenX(int tempX){
		screen_x = tempX;
	}
	public void setScreenY(int tempY){
		screen_y = tempY;
	}
	
	public TrueTypeFont getBaseFont()
	{
		if(baseFont == null)
		{
			baseFont1 = new Font("Times New Roman", Font.PLAIN, 16);
			baseFont = new TrueTypeFont(baseFont1, true);
		}
		return baseFont;
	}

	public void setGame(StateBasedGame sbg, GameContainer gc) 
	{
		this.gc = gc;
		this.sbg = sbg;
	}
	
	public StateBasedGame getSBG()
	{
		return sbg;
	}
	
	public GameContainer getGC()
	{
		return gc;
	}
	
}

