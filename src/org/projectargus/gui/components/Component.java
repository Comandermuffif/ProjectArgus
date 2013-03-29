package org.projectargus.gui.components;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Component 
{
	Rectangle dims;
	ComponentListener listener;

	public Component(Rectangle dims,ComponentListener listener)
	{
		this.dims = dims;
		this.listener = listener;
	}

	public abstract void render(GameContainer gc, StateBasedGame sbg, Graphics g);
	public abstract void update(GameContainer gc, StateBasedGame sbg, int delta);
	
	public Rectangle getDims()
	{
		return dims;
	}
	
	public ComponentListener getListener()
	{
		return listener;
	}
	
	public int getX()
	{
		return (int)dims.getX();
	}
	public int getY()
	{
		return (int)dims.getY();
	}
	public int getWidth()
	{
		return (int)dims.getWidth();
	}
	public int getHeight()
	{
		return (int)dims.getHeight();
	}
}
