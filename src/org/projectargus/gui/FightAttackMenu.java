package org.projectargus.gui;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.data.GameSettings;
import org.projectargus.objects.Ship;

public class FightAttackMenu 
{
	GameSettings settings = GameSettings.getInstance();
	
	ArrayList<String> attacks = new ArrayList<String>();
	
	
	int w = 200;
	int h = 100;
	int x = settings.getScreenX()/2 - w/2;
	int y = 0;
	
	int margin_x = 10;
	int margin_y = 10;
	
	int spacing = 20;
	
	Color bgColor = new Color(120,120,120);
	Color textColor = new Color(200,30,30);
	Color nameColor = new Color(240,240,240);
	
	public FightAttackMenu()
	{
		
	}	
	
	public void setBox(Ship ship)
	{
		attacks.clear();
		attacks = ship.getInventory().getParts().getAllWeaponAttacks();
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		if(x != -1 && y != -1)
		{
			g.setColor(bgColor);	
			g.fillRect(x, y, w,margin_y + spacing * attacks.size());	
			for(int i = 0;i<attacks.size();i++)
			{
				g.setColor(textColor);
				g.drawString(attacks.get(i), x + margin_x, y + margin_y + i * spacing);
			}
		}
	}
	
	public int checkClick(int mouse_x,int mouse_y)
	{
		for(int i = 0;i<attacks.size();i++)
		{
			if(mouse_x >= x && mouse_x <= x+w
					&& mouse_y >= y+margin_y+spacing*i && mouse_y < y+margin_y+spacing*(i+1))
			{
				System.out.println("Attack - " + attacks.get(i));
				return i;
			}
		}
		
		return -1;
	}

	public void setPosition(int x, int y) 
	{
		this.x = x;
		this.y = y;
	}
}
