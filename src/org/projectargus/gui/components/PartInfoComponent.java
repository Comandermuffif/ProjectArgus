package org.projectargus.gui.components;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.parts.ShipPart;

public class PartInfoComponent extends Component
{
	ShipPart part = null;

	//GUI
	
	Color bgColor = Color.darkGray;
	Color textColor = Color.white;
	
	public PartInfoComponent(Rectangle dims, ComponentListener listener) {
		super(dims, listener);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
	{			
		int x = (int)dims.getX();
		int y = (int)dims.getY();
		int w = (int)dims.getWidth();
		int h = (int)dims.getHeight();
		
		if(part != null)
		{
			g.setColor(bgColor);
			g.fillRect(x, y, w, h);
			g.setColor(textColor);
			g.drawString(part.getName(), x + 20, y += 10);
			g.drawLine(x, y += 20, x+w, y);
			g.drawString("Type: " + part.getTypeName(), x + 20,y += 3);
			g.drawString(part.getDescription(), x + 20,y += 15);				
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) 
	{

	}
	
	public void setPart(ShipPart part)
	{
		this.part = part;
	}
}
