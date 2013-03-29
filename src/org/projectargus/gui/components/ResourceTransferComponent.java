package org.projectargus.gui.components;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.holders.ResourceHolder;

public class ResourceTransferComponent extends Component implements ButtonListener
{
	ResourceHolder resourceLeft;
	ResourceHolder resourceRight;

	ArrayList<Button> buttonsLeft = new ArrayList<Button>();
	ArrayList<Button> buttonsRight = new ArrayList<Button>();

	int verticalSpacing = 55;

	float resourceAmount = -1;

	SwitchGroup valueSwitchGroup = new SwitchGroup();

	Switch hundrethsSwitch;
	Switch tenthsSwitch;
	Switch oneSwitch;
	Switch tenSwitch;
	Switch hundredSwitch;
	Switch thousandSwitch;
	Switch allSwitch;

	public ResourceTransferComponent(Rectangle dims, ComponentListener listener, 
			ResourceHolder resourceHolder1, ResourceHolder resourceHolder2) 
	{
		super(dims, listener);

		resourceLeft = resourceHolder1;
		resourceRight = resourceHolder2;	

		createSwitches();
		createButtons();
		
	}

	private void createButtons()
	{
		int w = 30;
		int spacing = 10;
		int x = (int)(dims.getX() + dims.getWidth()/2) - (w+spacing/2);
		int y = 175;		

		for(int i = 0;i<resourceLeft.getAllResources().length;i++)
		{
			buttonsLeft.add(new Button(new Rectangle(x,y,w,w),this,"<"));
			buttonsRight.add(new Button(new Rectangle(x+w+spacing,y,w,w),this,">"));
			y+= verticalSpacing;
		}
	}
	
	private void createSwitches()
	{
		int amount = 7;
		int w = 40;
		int h = 20;
		int t = 25 + w;
		int x = (int)dims.getX() + (int)((dims.getWidth() - (amount)*t +(t-w))/2);
		int y = (int)dims.getY() + 10;
		

		hundrethsSwitch = new Switch(new Rectangle(x,y,w,h),this,".01",valueSwitchGroup);
		tenthsSwitch = new Switch(new Rectangle(x+=t,y,w,h),this,".1",valueSwitchGroup);
		oneSwitch = new Switch(new Rectangle(x+=t,y,w,h),this,"1",valueSwitchGroup);
		tenSwitch = new Switch(new Rectangle(x+=t,y,w,h),this,"10",valueSwitchGroup);
		hundredSwitch = new Switch(new Rectangle(x+=t,y,w,h),this,"100",valueSwitchGroup);
		thousandSwitch = new Switch(new Rectangle(x+=t,y,w,h),this,"1000",valueSwitchGroup);
		allSwitch = new Switch(new Rectangle(x+=t,y,w,h),this,"All",valueSwitchGroup);

		valueSwitchGroup.setSelected(oneSwitch);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
	{			
		g.setColor(Color.white);
		g.draw(dims);

		int x = (int)(dims.getX() + dims.getWidth()/2);
		int y = 130;
		int t = verticalSpacing;

		/*for(int i = 0;i<resourceLeft.getAllResources().length;i++)
		{
			String temp = ResourceHolder.resourceNames[i];
			g.drawString(temp,x - g.getFont().getWidth(temp)/2,y + t*i);
			String temp2 = "" + resourceLeft.getResource(i);
			String temp3 = "" + resourceRight.getResource(i);
			g.drawString(temp2,x - g.getFont().getWidth(temp2)*3,y+ t*i + t/2);
			g.drawString(temp3,(int)(x + g.getFont().getWidth(temp3)*1.5),y+ t*i + t/2);
		}*/

		for(int i = 0;i<buttonsLeft.size();i++)
		{
			g.setColor(Color.white);
			y = buttonsLeft.get(i).getY() - t/2 + g.getFont().getLineHeight()/3;

			String temp = ResourceHolder.resourceNames[i];
			g.drawString(temp,x - g.getFont().getWidth(temp)/2,y);
			String temp2 = "" + resourceLeft.getResource(i);
			String temp3 = "" + resourceRight.getResource(i);
			g.drawString(temp2,x - g.getFont().getWidth(temp2)*3,y+ t/2);
			g.drawString(temp3,(int)(x + g.getFont().getWidth(temp3)*1.5),y + t/2);

			buttonsLeft.get(i).render(gc, sbg, g);
			buttonsRight.get(i).render(gc, sbg, g);
		}

		hundrethsSwitch.render(gc, sbg, g);
		tenthsSwitch.render(gc, sbg, g);
		oneSwitch.render(gc, sbg, g);
		tenSwitch.render(gc, sbg, g);
		hundredSwitch.render(gc, sbg, g);
		thousandSwitch.render(gc, sbg, g);
		allSwitch.render(gc, sbg, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) 
	{
		for(int i = 0;i<buttonsLeft.size();i++)
		{
			buttonsLeft.get(i).update(gc, sbg, delta);
		}
		for(int i = 0;i<buttonsRight.size();i++)
		{
			buttonsRight.get(i).update(gc, sbg, delta);
		}

		hundrethsSwitch.update(gc, sbg, delta);
		tenthsSwitch.update(gc, sbg, delta);
		oneSwitch.update(gc, sbg, delta);
		tenSwitch.update(gc, sbg, delta);
		hundredSwitch.update(gc, sbg, delta);
		thousandSwitch.update(gc, sbg, delta);
		allSwitch.update(gc, sbg, delta);
	}

	@Override
	public void buttonClicked(Component component)
	{
		for(int i = 0;i<buttonsLeft.size();i++)
		{
			if(component.equals(buttonsLeft.get(i)))
			{
				float temp = resourceRight.subtractResource(i, resourceAmount);
				resourceLeft.addResource(i, temp);
			}
			else if(component.equals(buttonsRight.get(i)))
			{
				float temp = resourceLeft.subtractResource(i, resourceAmount);
				resourceRight.addResource(i, temp);
			}
		}

		if(component.equals(hundrethsSwitch))
		{
			resourceAmount = .01f;
		}
		if(component.equals(tenthsSwitch))
		{
			resourceAmount = .1f;
		}
		if(component.equals(oneSwitch))
		{
			resourceAmount = 1;
		}
		if(component.equals(tenSwitch))
		{
			resourceAmount = 10;
		}
		if(component.equals(hundredSwitch))
		{
			resourceAmount = 100;
		}
		if(component.equals(thousandSwitch))
		{
			resourceAmount = 1000;
		}
		if(component.equals(allSwitch))
		{
			resourceAmount = 10000000;
		}

	}
}
