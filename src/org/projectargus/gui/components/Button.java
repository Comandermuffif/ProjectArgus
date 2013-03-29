package org.projectargus.gui.components;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

public class Button extends Component
{
	String text;
	
	int cornerRadius = 10;
	int outerBgSize = 5;
	
	Color bgOutColor = new Color(50,50,50);
	Color bgColor = new Color(100,100,100);
	
	Color bgHoverColor = new Color(75,75,75);
	Color bgDownColor = new Color(25,25,25);
	
	Color textColor = new Color(0,0,00);
	
	State state = State.NONE;
	
	private enum State{
		NONE,HOVER,DOWN,CLICK
	}
	
	ButtonListener listener;
	
	public Button(Rectangle dims, ButtonListener listener, String text) {
		super(dims, listener);

		this.listener = listener;
		this.text = text;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
	{
		int textWidth = g.getFont().getWidth(text);
		int textHeight = g.getFont().getLineHeight();
		
		int tempXOffset = (int)(dims.getX() + (dims.getWidth() - textWidth)/2);
		int tempYOffset = (int)(dims.getY() + (dims.getHeight() - textHeight)/2);
		
		g.setColor(bgOutColor);
		g.fillRoundRect(dims.getX(), dims.getY(), dims.getWidth(), dims.getHeight(), cornerRadius);
		
		switch(state)
		{
		case NONE:
			g.setColor(bgColor);
			break;
		case HOVER:
			g.setColor(bgHoverColor);
			break;
		case DOWN:
		case CLICK:
			g.setColor(bgDownColor);
			break;
		}		
		
		g.fillRoundRect(dims.getX() + outerBgSize, dims.getY() + outerBgSize, dims.getWidth() - outerBgSize*2, dims.getHeight() - outerBgSize*2, cornerRadius-2);
		g.setColor(textColor);
		g.drawString(text, tempXOffset,tempYOffset);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) 
	{
		Input input = gc.getInput();
		
		if(dims.contains(input.getMouseX(),input.getMouseY()))
		{
			if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
			{
				//System.out.println("Click");
				listener.buttonClicked(this);
				state = State.CLICK;
			}
			else if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
			{
				//System.out.println("Down");
				state = State.DOWN;
			}
			else{
				state = State.HOVER;
			}
		}else{
			state = State.NONE;
		}
	}
	
	public Rectangle getDims()
	{
		return dims;
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
