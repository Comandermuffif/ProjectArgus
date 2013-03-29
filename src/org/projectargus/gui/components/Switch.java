package org.projectargus.gui.components;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

public class Switch extends Component
{
	boolean selected = false;
	String text;
	SwitchGroup switchGroup;

	//Gui
	int cornerRadius = 5;

	Color selectedColor = new Color(255,140,0);
	Color unSelectedColor = new Color(105,105,105);

	Color textColor = new Color(0,0,0);
	
	public Switch(Rectangle dims, ButtonListener listener,String text) 
	{
		super(dims, listener);
		this.text = text;
	}

	public Switch(Rectangle dims,ButtonListener listener, String text,
			SwitchGroup group) {
		this(dims,listener,text);
		
		addToGroup(group);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
	{
		int textWidth = g.getFont().getWidth(text);
		int textHeight = g.getFont().getLineHeight();

		int tempXOffset = (int)(dims.getX() + (dims.getWidth() - textWidth)/2);
		int tempYOffset = (int)(dims.getY() + (dims.getHeight() - textHeight)/2);

		if(selected)
		{
			g.setColor(selectedColor);
		}else{
			g.setColor(unSelectedColor);
		}

		g.fillRoundRect(dims.getX(), dims.getY(), dims.getWidth(), dims.getHeight(), cornerRadius);

		g.setColor(textColor);
		g.drawString(text, tempXOffset, tempYOffset);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) 
	{
		Input input = gc.getInput();
		if(dims.contains(input.getMouseX(),input.getMouseY()))
		{
			if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
			{
				if(switchGroup != null)
				{
					switchGroup.setSelected(this);
				}
			}
		}
	}

	public void setSelected(boolean selected)
	{
		((ButtonListener)listener).buttonClicked(this);
		this.selected = selected;
	}

	public void addToGroup(SwitchGroup sw)
	{
		switchGroup = sw;
		switchGroup.addSwitch(this);
	}

	public SwitchGroup getGroup() 
	{
		return switchGroup;
	}

}
