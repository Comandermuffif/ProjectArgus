package org.projectargus.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.util.*;

public class InfoBubble 
{
	int x = -1;
	int y = -1;
	int w = 200;
	int ww = 200;
	int h = 100;
	int hh = 100;
	int spacing = 20;

	Color bgColor = new Color(120,120,120);
	Color textColor = new Color(200,30,30);
	Color nameColor = new Color(240,240,240);
	
	//Because fuck your arrays
	List<String> rows = new ArrayList<String>(4);
	//String[] rows = new String[4];


	public InfoBubble()
	{
		for(int i = 0;i<rows.size();i++)
		{
			rows.set(i, "");
		}
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		if(x != -1 && y != -1)
		{
			g.setColor(bgColor);	
			g.fillRect(x + w/2, y + h/2-100, ww,20 * rows.size());	
			for(int i = 0;i<rows.size();i++)
			{
				if(i == 0){
					g.setColor(nameColor);
				}else{
					g.setColor(textColor);
				}
				g.drawString(rows.get(i), x + w/2 + 5, y + h/2-100 + spacing*i);
			}
		}
	}

	//TODO
	public void setNextLine(String s)
	{
		
	}

	public void setPos( int x,int y, int w, int h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public void setRow(int row,String str)
	{
		while(rows.size() < row + 1){
			rows.add("");
		}
		//rows.ensureCapacity(row);
		rows.set(row, str);
	}
	
	public void addRow(String str)
	{
		rows.add(str);
	}

}
